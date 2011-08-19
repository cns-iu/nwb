package edu.iu.epic.visualization.linegraph.model.tuple;

import java.util.Iterator;
import java.util.NoSuchElementException;

import stencil.tuple.SourcedTuple;
import stencil.tuple.Tuple;

// TODO: TupleStream probably shouldn't actually extend Iterator, but rather
// just use it internally.
// TODO: Also, reset is redundant with the source methodology.  One of them
// should go.
public interface TupleStream extends Iterator<Tuple> {
	long UNKNOWN_STREAM_SIZE = -1;
	
	SourcedTuple nextTuple() throws NoSuchElementException;
	void reset();
	long streamSize();
}