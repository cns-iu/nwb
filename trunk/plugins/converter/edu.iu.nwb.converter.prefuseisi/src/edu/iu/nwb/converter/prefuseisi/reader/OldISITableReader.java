package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prefuse.data.Schema;
import prefuse.data.Table;

public class OldISITableReader {
	
	private static Schema schema = new Schema();
	private static Map separators = new HashMap();
	private static List multivalueColumns = new ArrayList();
	
	static {
		schema.addColumn("AB", String.class);
		schema.addColumn("AF", List.class);
		schema.addColumn("AR", String.class);
		schema.addColumn("AU", List.class);
		schema.addColumn("BP", String.class);
		schema.addColumn("C1", List.class);
		schema.addColumn("CR", List.class);
		schema.addColumn("DE", List.class);
		schema.addColumn("DI", String.class);
		schema.addColumn("DT", String.class);
		schema.addColumn("EP", String.class);
		schema.addColumn("GA", String.class);
		schema.addColumn("ID", List.class);
		schema.addColumn("IS", String.class);
		schema.addColumn("J9", String.class);
		schema.addColumn("JI", String.class);
		schema.addColumn("LA", String.class);
		schema.addColumn("NR", int.class);
		schema.addColumn("PD", String.class);
		schema.addColumn("PG", int.class);
		schema.addColumn("PI", String.class);
		schema.addColumn("PN", String.class);
		schema.addColumn("PT", String.class);
		schema.addColumn("PU", String.class);
		schema.addColumn("PY", int.class);
		schema.addColumn("RP", String.class);
		schema.addColumn("SC", List.class);
		schema.addColumn("SE", String.class);
		schema.addColumn("SI", String.class);
		schema.addColumn("SN", String.class);
		schema.addColumn("SO", String.class);
		schema.addColumn("SU", String.class);
		schema.addColumn("TC", int.class);
		schema.addColumn("TI", String.class);
		schema.addColumn("UT", String.class);
		schema.addColumn("VL", String.class);
		schema.addColumn("WP", String.class);
	}
	
	static {
		//any separator that's not a newline
		separators.put("C1", ".");
		separators.put("ID", ";");
		separators.put("DE", ";"); //guessing, since the format does not specify, but the other keyword field is ; separated
		separators.put("SC", ";");
	}
	
	static {
		multivalueColumns.add("AF");
		multivalueColumns.add("AU");
		multivalueColumns.add("C1");
		multivalueColumns.add("CR");
		multivalueColumns.add("DE");
		multivalueColumns.add("ID");
		multivalueColumns.add("SC");
		multivalueColumns.add("AF");
		multivalueColumns.add("AF");
	}
	
	public Table readTable(FileInputStream stream) throws IOException {
		boolean foldNewlines = true;
		boolean finishedRecord = true; //this means the first time we run into some stuff for starting a record, we'll do so.
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		
		Table table = schema.instantiate();
		
		int currentRow = 0; //does not correspond to a row, but needs to be initialized to something
		String line;
		String tag = "";
		
		StringBuffer value = new StringBuffer();
		List items = new ArrayList();
		
		while((line = reader.readLine()) != null) {
			
			if(line.startsWith("EF")) { //end of the file, stop now
				break;
			} else if(line.startsWith("FN") || line.startsWith("VR")) {
				//for now, we care not about exact file types and versions
			} else if("".equals(line.trim())) { //ignore blank lines
				//do nothing
			} else if(line.startsWith(" ")) { //we're in a record's tag, but not where the tag starts
				if(foldNewlines) {
					value.append(" " + line.trim()); //we need either a space or a newline. The wrapping looks pretty artificial, so a space.
				} else {
					items.add(line.trim());
				}
			} else { //a tag starts here
				
				//since there's always an ER tag, this'll always get called, even on the last tag in a record
				if(!"".equals(tag)) {
					if(multivalueColumns.contains(tag)) {
						if(foldNewlines) {
							String[] values = value.toString().split((String) separators.get(tag));
							for(int ii = 0; ii < values.length; ii++) {
								items.add(values[ii]);
							}
						} //we've already added all the items for newline-separated lists
						table.set(currentRow, tag, items);
					} else if(String.class.equals(table.getColumnType(tag))) {
						table.setString(currentRow, tag, value.toString());
					} 
				}
				
				//we happened upon a new record after the ER tag; we have to wait until we see a new record before we add a row, otherwise we'll have an empty row
				if(finishedRecord) {
					currentRow = table.addRow();
					finishedRecord = false;
				}
				//reset this each time we come across a new tag
				foldNewlines = true;
				String[] parts = line.split(" ", 2);
				tag = parts[0]; //we'll have at least this, always
				if("ER".equals(tag)) {
					finishedRecord = true;
				} else if(int.class.equals(table.getColumnType(tag))) {
					table.setInt(currentRow, tag, Integer.parseInt(parts[1]));
				} else if(String.class.equals(table.getColumnType(tag))) { //in the schema, type string
					value = new StringBuffer(parts[1]); //stick the stuff into the stringbuffer; there might be 
				} else if(List.class.equals(table.getColumnType(tag))) {
					items = new ArrayList();
					if(separators.get(tag) == null) {
						items.add(parts[1]);
						foldNewlines = false;
					} else {
						//we'll stick stuff in it later. For now, Tag Discovery Is Initialization
						value = new StringBuffer(parts[1]);
					}	
				} else { //we don't recognize the tag, so we ignore it.
					System.err.println("Unrecognized tag in ISI file: " + tag);
				}
			}
		}
		return table;
	}
}
