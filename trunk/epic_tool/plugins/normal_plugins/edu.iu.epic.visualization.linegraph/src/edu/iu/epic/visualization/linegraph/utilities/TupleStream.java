package edu.iu.epic.visualization.linegraph.utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;

import stencil.streams.Tuple;

public interface TupleStream extends Iterator {
	public static final long UNKNOWN_STREAM_SIZE = -1;
	
	public Tuple nextTuple() throws NoSuchElementException;
	public void reset();
	public long streamSize();
}