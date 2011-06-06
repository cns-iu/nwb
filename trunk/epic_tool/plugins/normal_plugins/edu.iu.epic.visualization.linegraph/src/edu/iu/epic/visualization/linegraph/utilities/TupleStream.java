package edu.iu.epic.visualization.linegraph.utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;

import stencil.tuple.Tuple;

// TODO: TupleStream probably shouldn't actually extend Iterator, but rather
// just use it internally.
// TODO: Also, reset is redundant with the source methodology.  One of them
// should go.
public interface TupleStream extends Iterator<Tuple> {
	long UNKNOWN_STREAM_SIZE = -1;
	
	Tuple nextTuple() throws NoSuchElementException;
	void reset();
	long streamSize();
}