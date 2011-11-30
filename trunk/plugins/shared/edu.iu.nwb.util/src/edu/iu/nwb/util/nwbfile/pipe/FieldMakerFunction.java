package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Map;

public interface FieldMakerFunction {
	Object compute(String field, Map<String, Object> attributes);
}