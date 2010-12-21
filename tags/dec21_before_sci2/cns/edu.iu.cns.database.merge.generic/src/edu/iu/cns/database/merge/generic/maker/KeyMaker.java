package edu.iu.cns.database.merge.generic.maker;

import prefuse.data.Tuple;

public interface KeyMaker {

	Object makeKey(Tuple tuple);

}
