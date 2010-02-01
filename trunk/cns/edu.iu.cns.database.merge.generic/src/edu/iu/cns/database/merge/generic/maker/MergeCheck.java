package edu.iu.cns.database.merge.generic.maker;

import prefuse.data.Tuple;

public interface MergeCheck {

	boolean shouldMerge(Tuple first, Tuple second);

}
