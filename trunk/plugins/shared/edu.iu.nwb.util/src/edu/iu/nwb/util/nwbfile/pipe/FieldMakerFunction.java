package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;

import edu.iu.nwb.util.nwbfile.model.NWBGraphPart;

/**
 * A function that computes new attributes for an {@link NWBGraphPart} from its existing
 * attributes.
 */
public interface FieldMakerFunction {
	Map<String, Object> compute(Map<String, Object> attributes);
}