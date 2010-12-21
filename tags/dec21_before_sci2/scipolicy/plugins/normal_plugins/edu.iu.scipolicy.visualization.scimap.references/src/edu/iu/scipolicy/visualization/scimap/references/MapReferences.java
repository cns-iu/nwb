package edu.iu.scipolicy.visualization.scimap.references;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class MapReferences implements Algorithm {
	private Data[] data;
	private Dictionary<String, String> parameters;
	private CIShellContext context;
	private LogService logger;
	private String directory;
	private double scalingFactor;
	
	public static StringTemplateGroup group = loadTemplates();
	

	public MapReferences(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.context = context;
		this.logger = (LogService) context.getService(LogService.class.getName());
		
		this.directory = (String) parameters.get("directory");
		this.scalingFactor = (Double) parameters.get("scalingFactor");
	}

	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(new InputStreamReader(MapReferences.class.getResourceAsStream("/edu/iu/scipolicy/visualization/scimap/references/templates/group.st")));
	}

	public Data[] execute() throws AlgorithmExecutionException {
		Overview overview = generatePostscriptVisualization(directory);

		return new Data[] {wrapPostscript(writePostscriptFile(overview), directory), wrapCsv(writeFoundFile(overview), "Found journals for proposals in " + directory), wrapCsv(writeUnfoundFile(overview), "Unlocated references for proposals in " + directory)};
	}

	private Data wrapCsv(File csvFile, String title) {
		BasicData csvData = new BasicData(csvFile, "file:text/csv");
		Dictionary<String, Object> metadata = csvData.getMetadata();
		
		metadata.put(DataProperty.LABEL, title);
		metadata.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		
		return csvData;
	}

	private File writeFoundFile(Overview overview) throws AlgorithmExecutionException {
		try {
			File csv = File.createTempFile("pdf-references-found", ".csv");
			BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
			overview.renderFound(writer);
			writer.close();
			return csv;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to create output file.", e);
		}
	}

	private File writeUnfoundFile(Overview overview) throws AlgorithmExecutionException {
		try {
			File csv = File.createTempFile("pdf-references-unfound", ".csv");
			BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
			overview.renderUnfound(writer);
			writer.close();
			return csv;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to create output file.", e);
		}
	}

	private BasicData wrapPostscript(File postscript, String directory) {
		BasicData postscriptData = new BasicData(postscript, "file:text/ps");
		Dictionary<String, Object> metadata = postscriptData.getMetadata();
		
		metadata.put(DataProperty.LABEL, "Postscript of Proposals in " + directory);
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		
		return postscriptData;
	}

	public static void main(String[] args) {
		try {
			Dictionary<String, Object> parameters = new Hashtable<String, Object>();
			parameters.put("scalingFactor", 1.0);
			
			MapReferences mapReferences = new MapReferences(null, parameters, new CIShellContext(){

				public Object getService(String service) {
					// TODO Auto-generated method stub
					return new LogService() {

						public void log(int level, String message) {
							System.err.println(message);

						}

						public void log(int level, String message,
								Throwable exception) {
							System.err.println(message);

						}

						public void log(ServiceReference sr, int level,
								String message) {
							System.err.println(message);

						}

						public void log(ServiceReference sr, int level,
								String message, Throwable exception) {
							System.err.println(message);

						}

					};
				}});
			File ps = mapReferences.writePostscriptFile(mapReferences.generatePostscriptVisualization("C:\\Documents and Settings\\jrbibers\\Desktop\\nih demo\\scienceMap\\refmapper test pdfs"));//"/home/rduhon/Documents/references/pdfs/"));
			System.err.println(ps.getAbsolutePath());
		} catch (AlgorithmExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Overview generatePostscriptVisualization(String directoryName)
	throws AlgorithmExecutionException {
		File directory = new File(directoryName);
		File[] pdfs = listPDFs(directory);
		this.logger.log(LogService.LOG_INFO, "PDFs in directory: " + pdfs.length);

		Overview overview = initializeOverview(directory);
		//this.logger.log(LogService.LOG_WARNING, "Overivew loaded");

		for(File pdf : pdfs) {
			//this.logger.log(LogService.LOG_WARNING, "  . . . starting PDF");
			try {
				Analysis analysis = new Analysis(pdf, scalingFactor);
				//this.logger.log(LogService.LOG_WARNING, "  . . . PDF analyzed");
				overview.update(analysis);
				//this.logger.log(LogService.LOG_WARNING, "  . . . analysis assimilated");
			} catch(Exception e) {
				e.printStackTrace();
				this.logger.log(LogService.LOG_WARNING, "There was a very unexpected error handling the file " + pdf.getAbsolutePath());
			}
		}
		
		//overview.writeRows(new File("/home/rduhon/Documents/pdfs.csv"));
		
		this.logger.log(LogService.LOG_INFO, "Done with PDFs, writing postscript");
		File postscript = writePostscriptFile(overview);
		//this.logger.log(LogService.LOG_WARNING, "Postscript file at " + postscript.getAbsolutePath());
		return overview;
	}

	private File[] listPDFs(File directory) {
		File[] pdfs = directory.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".pdf");
			}
		});

		Arrays.sort(pdfs);
		return pdfs;
	}

	private Overview initializeOverview(File directory) {
		//this.logger.log(LogService.LOG_WARNING, "Loading Overview");
		return new Overview(directory.getAbsolutePath(), scalingFactor);

	}

	private File writePostscriptFile(Overview overview)
	throws AlgorithmExecutionException {
		try {
			File postscript = File.createTempFile("pdf-references", ".ps");
			BufferedWriter postscriptWriter = new BufferedWriter(new FileWriter(postscript));
			overview.renderPostscript(postscriptWriter);
			postscriptWriter.close();
			return postscript;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to create output file.", e);
		}
	}
}