package edu.iu.epic.visualization.linegraph.utilities;

import java.util.Iterator;
import java.util.NoSuchElementException;

import stencil.streams.Tuple;

public interface TupleStream extends Iterator {
	public Tuple nextTuple() throws NoSuchElementException;
	public void reset();
}