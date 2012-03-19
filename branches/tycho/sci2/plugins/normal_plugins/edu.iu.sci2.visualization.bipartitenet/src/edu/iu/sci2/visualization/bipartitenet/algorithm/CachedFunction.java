package edu.iu.sci2.visualization.bipartitenet.algorithm;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

/*
 * .consume(key)
 * .applyAndCache(key)
 * .clear()
 * 
 * Null keys and values not supported!
 */

public abstract class CachedFunction<I,O> implements Function<I,O> {
	private I currentKey;
	private O currentValue;
	
	private void invalidate() {
		currentKey = null;
	}
	
	private boolean isValid() {
		return currentKey != null;
	}
	
	private boolean isCached(I key) {
		Preconditions.checkNotNull(key);
		return isValid() && key.equals(currentKey);
	}
	
	public O applyAndCache(I input) {
		Preconditions.checkNotNull(input);
		currentKey = input;
		currentValue = apply(input);
		return currentValue;
	}
	
	public O getAndInvalidate(I input) {
		Preconditions.checkNotNull(input);
		O output = null;
		if (isCached(input)) {
			output = currentValue;
		} else {
			output = apply(input);
		}
		
		invalidate();
		return output;
	}
}
