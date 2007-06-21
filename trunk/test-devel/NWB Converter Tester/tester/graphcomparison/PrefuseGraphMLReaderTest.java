package tester.graphcomparison;

import java.io.File;
import java.io.FileInputStream;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.io.GraphMLReader;
import prefuse.util.collections.IntIterator;

public class PrefuseGraphMLReaderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File fileHandler = new File("/home/mwlinnem/nwb-to-graphml-checkids.xml");
		try {
		Graph graph= (new GraphMLReader()).readGraph(new FileInputStream(fileHandler));
		
		Table edgeTable = graph.getEdgeTable();
		
		for (int ii = 0; ii < edgeTable.getRowCount(); ii++) {
			System.out.println(edgeTable.getTuple(ii));
		}
//		String keyField = graph.getNodeKeyField();
//		Table nodeTable = graph.getNodeTable();
//		for (IntIterator ii = nodeTable.rows(); ii.hasNext();) {
//			int rowNum = ii.nextInt();
//			System.out.println(rowNum);
//			//System.out.println(nodeTable.getTuple(rowNum).getColumnCount());
//			System.out.println(graph.getNode(rowNum));
//			System.out.println(keyField);
//		}
//		System.out.println(graph.getNodeCount());
		//System.out.println(graph.getNode(1).getString(keyField));
		} catch (Exception e) {
			System.out.println("oops!");
			System.out.println(e.toString());
		}
	}

}
