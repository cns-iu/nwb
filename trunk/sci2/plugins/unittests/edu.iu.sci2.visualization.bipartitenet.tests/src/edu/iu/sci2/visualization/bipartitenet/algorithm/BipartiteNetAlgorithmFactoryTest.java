package edu.iu.sci2.visualization.bipartitenet.algorithm;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.sci2.visualization.bipartitenet.PageDirector.Layout;

public class BipartiteNetAlgorithmFactoryTest {

	private String getTestNWB() {
		StringBuilder s = new StringBuilder();

		s.append("*Nodes 2\n");
		s.append("id*int	label*string	bipartitetype*string\n");

		s.append("1		\"a\"	\"a\"\n");
		s.append("2		\"b\"	\"b\"\n");

		// Normally, we would also omit edges with missing endpoints.  That's now
		// tested separately in OrphanEdgeRemoverTest.
		s.append("*DirectedEdges 1\n");
		s.append("source*int	target*int	weight*float\n");
		s.append("1	2	1.0\n");

		return s.toString();
	}
	
	@Test
	public void testLayoutIsPrint() throws Exception {
		Dictionary<String, Object> params = new Hashtable<String, Object>();
        params.put("leftSideType", "a");
        params.put("subtitle", "");
        params.put("nodeSizeColumn", BipartiteNetAlgorithmFactory.NO_NODE_WEIGHT_OPTION);
        params.put("edgeWeightColumn", BipartiteNetAlgorithmFactory.NO_EDGE_WEIGHT_OPTION);
        params.put("leftColumnTitle", "Left title");
        params.put("rightColumnTitle", "Right title");
        params.put("leftColumnOrdering", NodeOrderingOption.LABEL_ASC.getIdentifier());
        params.put("rightColumnOrdering", NodeOrderingOption.LABEL_ASC.getIdentifier());
        params.put("layoutType", Layout.PRINT.name());

        File tempFile = File.createTempFile("nwb-file", ".nwb");
        Files.append(getTestNWB(), tempFile, Charsets.US_ASCII);
        Data inData = DataFactory.forFile(tempFile, "file:text/nwb", DataProperty.NETWORK_TYPE,
        		null, "test nwb file");
        
        BipartiteNetAlgorithmFactory factory = new BipartiteNetAlgorithmFactory();
        Algorithm rawAlgorithm = factory.createAlgorithm(
        		new Data[] { inData }, params, new LogOnlyCIShellContext());
        
        BipartiteNetAlgorithm algorithm = (BipartiteNetAlgorithm) rawAlgorithm;
        Layout layout = algorithm.getLayout();
        assertEquals(layout, Layout.PRINT);
        Data[] out = algorithm.execute();
        File f = (File) out[0].getData();
//        System.out.println(Files.getChecksum(f, new CRC32()));
	}
	
	@Test
	public void testLayoutIsWeb() throws Exception {
		Dictionary<String, Object> params = new Hashtable<String, Object>();
        params.put("leftSideType", "a");
        params.put("subtitle", "");
        params.put("nodeSizeColumn", BipartiteNetAlgorithmFactory.NO_NODE_WEIGHT_OPTION);
        params.put("edgeWeightColumn", BipartiteNetAlgorithmFactory.NO_EDGE_WEIGHT_OPTION);
        params.put("leftColumnTitle", "Left title");
        params.put("rightColumnTitle", "Right title");
        params.put("leftColumnOrdering", NodeOrderingOption.LABEL_ASC.getIdentifier());
        params.put("rightColumnOrdering", NodeOrderingOption.LABEL_ASC.getIdentifier());
        params.put("layoutType", Layout.WEB.name());

        File tempFile = File.createTempFile("nwb-file", ".nwb");
        Files.append(getTestNWB(), tempFile, Charsets.US_ASCII);
        Data inData = DataFactory.forFile(tempFile, "file:text/nwb", DataProperty.NETWORK_TYPE,
        		null, "test nwb file");
        
        BipartiteNetAlgorithmFactory factory = new BipartiteNetAlgorithmFactory();
        Algorithm rawAlgorithm = factory.createAlgorithm(
        		new Data[] { inData }, params, new LogOnlyCIShellContext());
        
        BipartiteNetAlgorithm algorithm = (BipartiteNetAlgorithm) rawAlgorithm;
        Layout layout = algorithm.getLayout();
        assertEquals(layout, Layout.WEB);
        Data[] out = algorithm.execute();
        File f = (File) out[0].getData();
//        System.out.println(Files.getChecksum(f, new CRC32()));
	}


}
