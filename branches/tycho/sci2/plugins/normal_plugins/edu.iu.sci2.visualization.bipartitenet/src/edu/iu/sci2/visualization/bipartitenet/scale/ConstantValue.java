package edu.iu.sci2.visualization.bipartitenet.scale;

import com.google.common.collect.ImmutableList;

public class ConstantValue<I,O> implements Scale<I, O> {
	private O value;

	public ConstantValue(O value) {
		this.value = value;
	}

	@Override
	public O apply(I arg0) {
		return value;
	}

	@Override
	public void train(Iterable<I> trainingData) {
		// Do nothing
	}

	@Override
	public void doneTraining() {
		// Do nothing
	}

	@Override
	public ImmutableList<I> getExtrema() {
		throw new UnsupportedOperationException();
	}
}
