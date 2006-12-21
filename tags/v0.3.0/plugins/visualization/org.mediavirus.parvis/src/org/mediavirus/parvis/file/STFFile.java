/*

 Copyright (c) 2001, 2002, 2003 Flo Ledermann <flo@subnet.at>

 This file is part of parvis - a parallel coordiante based data visualisation
 tool written in java. You find parvis and additional information on its
 website at http://www.mediavirus.org/parvis.

 parvis is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 parvis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with parvis (in the file LICENSE.txt); if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package org.mediavirus.parvis.file;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.mediavirus.parvis.gui.ProgressEvent;
import org.mediavirus.parvis.gui.ProgressListener;
import org.mediavirus.parvis.model.SimpleParallelSpaceModel;

/**
 * A Simple file parser for reading STF (Simple Table Fomrat) files from URLs.
 * 
 * The STF File format is defined as follows:
 * 
 * <pre>
 *   # test.stf
 *   # Comments have # in the first column.   
 *   # Type the number of fields, on a line by itself. 3                                        
 *   # Then type field names and types. Field names must not contain
 *   # spaces. 
 *   # 
 *   PersonName     String                   
 *   Age            Integer                 
 *   HourlyWage     Real                      
 *   # 
 *   # Data type is case-insensitive.
 *   # Default data delimiters are tabs and spaces. 
 *   # Here's the data, tab-delimited. Notice that the data columns are 
 *   # in the order they are listed above.
 *   # 
 *   Joe            23      5.75              
 *   Mary           18      4.75              
 *   Fred           54      100.00             
 *   Ginger         48      100.00            
 *   # 
 *   # Nothing special is required to end the file.
 *  
 * </pre>
 * 
 * Once the file is read and parsed, the data can be accessed with the methods
 * defined in the ParallelSpaceModel interface.
 * 
 * @author Flo Ledermann flo@subnet.at
 * @version 0.1
 * @author Ketan ... modified
 */
public class STFFile extends SimpleParallelSpaceModel {

	/** The url of the file. */
	URL url;

	private int tempNumDimensions;

	private int bytesRead = 0;

	private int filesize = 0;

	private Vector stringLabels = new Vector();

	private boolean isStringLabel[];

	private String name = "";

	/**
	 * Creates a new STFFile with the given url. The content is not read until
	 * readContents() is called.
	 * 
	 * @param url
	 *            The url of the file to read.
	 */
	public STFFile(URL url) {
		this.url = url;
		name = url.getFile();
		name = name.substring(name.lastIndexOf('/') + 1);
	}

	/**
	 * Returns the filename (without path).
	 * 
	 * @return The filename.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Reads the contents of the file and exposes them vis the
	 * ParallelSpaceModel interface of the class. String values are stripped out
	 * of the model and set as record labels.
	 */
	public void readContents() throws IOException {

		fireProgressEvent(new ProgressEvent(this, ProgressEvent.PROGRESS_START,
				0.0f, "loading file"));

		URLConnection conn = url.openConnection();
		conn.connect();

		filesize = conn.getContentLength();
		System.out.println("filesize: " + filesize);

		InputStreamReader in = new InputStreamReader(conn.getInputStream());

		readFirstLine(in);
		readHeaderLines(in);
		readData(in);

		fireProgressEvent(new ProgressEvent(this,
				ProgressEvent.PROGRESS_FINISH, 1.0f, "loading file"));

	}

	/**
	 * Reads the first data line of the file and sets up the number of
	 * dimensions.
	 */
	protected void readFirstLine(Reader in) throws IOException {
		String line = readLine(in);
		bytesRead += line.length();

		if (line.indexOf(' ') != -1)
			tempNumDimensions = Integer.parseInt(line.substring(0, line
					.indexOf(' ')));
		else
			tempNumDimensions = Integer.parseInt(line);

		// System.out.println("num dimensions: " + tempNumDimensions);

		isStringLabel = new boolean[tempNumDimensions];
		for (int i = 0; i < tempNumDimensions; i++) {
			isStringLabel[i] = false;
		}
	}

	/**
	 * Reads the header lines and sets up the variable types.
	 */
	protected void readHeaderLines(Reader in) throws IOException {
		int i;
		int j = 0;
		String line;

		int numDimensions = tempNumDimensions;
		Vector labels = new Vector();
		Map nominalAxisLabelsHash = null;

		for (i = 0; i < tempNumDimensions; i++) {
			line = readLine(in);
			bytesRead += line.length();

			StringTokenizer str = new StringTokenizer(line, "#");
			String label = str.nextToken();
			String type = str.nextToken();

			if (type.equalsIgnoreCase("string")) { // for display of type
													// string -- always added to
													// tooltip
				isStringLabel[i] = true;
				stringLabels.addElement(label);
				numDimensions--;
				// System.out.println("Recordlabel " + label);
			} else if (type.equalsIgnoreCase("nominal")) { // for nominal data
															// types
				if (nominalAxisLabelsHash == null)
					nominalAxisLabelsHash = new HashMap();
				if (!str.hasMoreElements()) {
					throw new RuntimeException("Runtime Exception .......");
				}
				String[] nominalLabels = new String[str.countTokens()];
				for (int labelCount = 0; labelCount < nominalLabels.length; ++labelCount) {
					nominalLabels[labelCount] = str.nextToken();
				}
				nominalAxisLabelsHash.put(label, nominalLabels);
				labels.addElement(label);
			} else {
				labels.addElement(label);
			}
//			System.out.println("Axis " + j++ + " label " + label + " type "
//					+ type + ".");
		}

		this.initNumDimensions(numDimensions);
		String tempLabels[] = (String[]) labels
				.toArray(new String[numDimensions]);
		this.setAxisLabels(tempLabels);
		if (nominalAxisLabelsHash != null)
			this.setNominalAxisLabelsHash(nominalAxisLabelsHash);
	}

	/**
	 * Reads the data lines.
	 */
	protected void readData(Reader in) throws IOException {
		String line, value;
		int i, j, s;

		String label;

		float curVal[];

		while ((line = readLine(in)) != null) {
			bytesRead += line.length();

			float progress = (float) bytesRead / filesize;
			fireProgressEvent(new ProgressEvent(this,
					ProgressEvent.PROGRESS_UPDATE, progress, "loading file"));

			StringTokenizer str = new StringTokenizer(line);
			curVal = new float[numDimensions];

			j = 0;
			s = 0;
			label = null;

			for (i = 0; i < tempNumDimensions; i++) {
				value = str.nextToken();
				// System.out.println("value " + i + ": " + value);

				if (!isStringLabel[i]) {
					try {
						float val = Float.parseFloat(value);
						curVal[j++] = val;
					} catch (NumberFormatException nfe) {
						System.out.println("Invalid Number Format: "
								+ nfe.getMessage()
								+ " -> dicarding & setting 0.0f");
						curVal[j++] = 0.0f;
					}
				} else {
					value = value.replace('_', ' ');
					value = value.substring(0, 1).toUpperCase()
							+ value.substring(1);

					int spcidx = 0;
					while ((spcidx = value.indexOf(' ', spcidx + 1)) != -1) {
						value = value.substring(0, spcidx + 1)
								+ value.substring(spcidx + 1, spcidx + 2)
										.toUpperCase()
								+ value.substring(spcidx + 2);
					}

					if (label == null) {
						label = stringLabels.elementAt(s++) + ": " + value;
					} else {
						label += "\n" + stringLabels.elementAt(s++) + ": "
								+ value;
					}
				}
			}
			addRecord(curVal, label);
		}
	}

	/**
	 * Reads on line, skipping comments and empty lines.
	 */
	protected String readLine(Reader in) throws IOException {
		char buf[] = new char[128];
		int offset = 0;
		int ch;

		boolean skip = false;

		for (;;) {
			ch = in.read();

			if (ch == -1) {
				break;
			}

			if (ch == '\n' || ch == '\r') {
				if (offset == 0 && !skip) {
					// skip empty line -> do nothing
					skip = true;
					// System.out.println("skipping line: empty");
				}

				if (skip) {
					// next line reached -> stop skipping
					skip = false;
				} else {
					// line finished -> break and return
					break;
				}
			} else if (ch == '#' && offset == 0) {
				// skip this line
				skip = true;
				// System.out.println("skipping line: comment");
			} else if (!skip) {
				if (offset == buf.length) {
					char tmpbuf[] = buf;
					buf = new char[tmpbuf.length * 2];
					System.arraycopy(tmpbuf, 0, buf, 0, offset);
				}
				buf[offset++] = (char) ch;
			}

		}

		if ((offset == 0) || skip) { // eof
			return null;
		}
		return String.copyValueOf(buf, 0, offset);
	}

	private Vector progressListeners = new Vector();

	/**
	 * Method to add a ProgressListener to get notified of the loading progress.
	 */
	public void addProgressListener(ProgressListener l) {
		progressListeners.add(l);
	}

	/**
	 * Remove a ProgressListener.
	 */
	public void removeProgressListener(ProgressListener l) {
		progressListeners.remove(l);
	}

	/**
	 * Dispatches a ProgressEvent to all listeners.
	 * 
	 * @param e
	 *            The ProgressEvent to send.
	 */
	protected void fireProgressEvent(ProgressEvent e) {
		Vector list = (Vector) progressListeners.clone();
		for (int i = 0; i < list.size(); i++) {
			ProgressListener l = (ProgressListener) list.elementAt(i);
			l.processProgressEvent(e);
		}
	}

	/**
	 * Main method for testing purposes.
	 */
	public static void main(String args[]) {
		try {
			// STFFile f = new STFFile(new
			// URL("file:///d:/uni/visualisierung/datasets/table1.stf"));

			STFFile f = new STFFile(new URL(
					"file:///C:/Documents and Settings/Ketan/Desktop/cars.stf"));
			f.readContents();
		} catch (MalformedURLException e) {
			System.out.println("malformed url!");
		} catch (IOException ex) {
			System.out.println("IOException: " + ex.getMessage());
		}

	}

}
