package edu.iu.sci2.visualization.bipartitenet.algorithm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import com.google.common.base.Functions;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.PageDirector.Layout;
import edu.iu.sci2.visualization.bipartitenet.model.NodeType;

public class BipartiteNetAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	// These strings must match with the parameter *values* in METADATA.XML
	public static final String NO_EDGE_WEIGHT_OPTION = "No edge weight";
	public static final String NO_NODE_WEIGHT_OPTION = "No node weight";
	
	// These strings must match with the parameter *names* in METADATA.XML
	private static final String LEFT_SIDE_TYPE_ID = "leftSideType";
	private static final String NODE_SIZE_COLUMN_ID = "nodeSizeColumn";
	private static final String EDGE_WEIGHT_COLUMN_ID = "edgeWeightColumn";
	private static final String LEFT_COLUMN_TITLE_ID = "leftColumnTitle";
	private static final String LEFT_COLUMN_ORDERING_ID = "leftColumnOrdering";
	private static final String RIGHT_COLUMN_TITLE_ID = "rightColumnTitle";
	private static final String RIGHT_COLUMN_ORDERING_ID = "rightColumnOrdering";
	private static final String LAYOUT_TYPE_ID = "layoutType";
	private static final String SUBTITLE_ID = "subtitle";
	
	private final CachedFunction<File, BipartiteNWBFileExaminer> examinerMaker =
			new CachedFunction<File, BipartiteNWBFileExaminer>() {
				@Override
				public BipartiteNWBFileExaminer apply(File nwbFile) {
					BipartiteNWBFileExaminer it 
						= new BipartiteNWBFileExaminer();
					try {
						new NWBFileParser(nwbFile).parse(it);
					} catch (ParsingException e) {
						throw new AlgorithmCreationFailedException(e);
					} catch (IOException e) {
						throw new AlgorithmCreationFailedException(e);
					}
					return it;
				}
			};
	
	@Override
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		BipartiteNWBFileExaminer exam = examinerMaker.getAndInvalidate(getNWBFile(data));
		
		// we get the list of values for the bipartitetype column, which must be exactly two elements.
		// We remove the one that's for the left side, and that leaves the one for the right side.
		List<String> types = new ArrayList<String>(exam.getBipartiteTypes());
		String leftSideType = (String) parameters.get(LEFT_SIDE_TYPE_ID);
		types.remove(leftSideType);
		String rightSideType = Iterables.getOnlyElement(types);

		String edgeWeightColumn = (String) parameters.get(EDGE_WEIGHT_COLUMN_ID);
		if (NO_EDGE_WEIGHT_OPTION.equals(edgeWeightColumn)) {
			edgeWeightColumn = null;
		}
		
		Layout layout = Layout.valueOf((String) parameters.get(LAYOUT_TYPE_ID));
		
		String subtitle = (String) parameters.get(SUBTITLE_ID);
		
		NodeType leftType = configureNodeType(parameters, leftSideType, LEFT_COLUMN_TITLE_ID,
				LEFT_COLUMN_ORDERING_ID, NODE_SIZE_COLUMN_ID);
		
		NodeType rightType = configureNodeType(parameters, rightSideType, RIGHT_COLUMN_TITLE_ID,
				RIGHT_COLUMN_ORDERING_ID, NODE_SIZE_COLUMN_ID);
		
		return new BipartiteNetAlgorithm(data[0], getNWBFile(data),
				layout, subtitle,
				edgeWeightColumn,
				leftType, rightType);
	}
	
	private NodeType configureNodeType(Dictionary<String, Object> params, String typeValue,
			String titleKey, String orderingKey, String weightKey) {
		String title = (String) params.get(titleKey);
		NodeOrderingOption ordering = NodeOrderingOption.getOption((String) params.get(orderingKey));
		String weightColumn = (String) params.get(weightKey);
		
		if (NO_NODE_WEIGHT_OPTION.equals(weightColumn)) {
			weightColumn = null;
		}
		
		if (title.trim().isEmpty()) {
			return NodeType.createWithDefaultDisplayName(typeValue, ordering, weightColumn);
		}
		return NodeType.create(typeValue, title, ordering, weightColumn);
	}

	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition oldParameters) {
		BipartiteNWBFileExaminer examiner = examinerMaker.applyAndCache(getNWBFile(data));

		LinkedHashMap<String, String> nodeSchema = examiner.getNodeSchema();
		List<String> nodeNumericColumns = Lists.newArrayList(
				NWBFileUtilities.findNumericAttributes(
						NWBFileUtilities.removeRequiredNodeProps(nodeSchema)));
		nodeNumericColumns.add(0, NO_NODE_WEIGHT_OPTION);

		List<String> edgeNumericColumns = Lists.newArrayList(
				NWBFileUtilities.findNumericAttributes(
						NWBFileUtilities.removeRequiredEdgeProps(
								NWBFileUtilities.getConsistentEdgeAttributes(examiner))));
		edgeNumericColumns.add(0, NO_EDGE_WEIGHT_OPTION);
		
		DropdownMutator mutator = new DropdownMutator();

		mutator.add(LEFT_SIDE_TYPE_ID, examiner.getBipartiteTypes());
		mutator.add(NODE_SIZE_COLUMN_ID, nodeNumericColumns);
		mutator.add(EDGE_WEIGHT_COLUMN_ID, edgeNumericColumns);
		
		mutator.add(LAYOUT_TYPE_ID, Collections2.transform(EnumSet.allOf(Layout.class), 
				Functions.toStringFunction()));
		
		mutator.add(LEFT_COLUMN_ORDERING_ID, NodeOrderingOption.getIdentifiers());
		mutator.add(RIGHT_COLUMN_ORDERING_ID, NodeOrderingOption.getIdentifiers());
		
		return mutator.mutate(oldParameters);
	}

	private File getNWBFile(Data[] data) {
		return (File) data[0].getData();
	}

}
