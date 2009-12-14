package edu.iu.cns.shared.utilities;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;

public class DictionaryIterator<KeyType, ValueType> implements
		Iterator<GenericPair<KeyType, ValueType> >, Iterable<GenericPair<KeyType, ValueType> > {
	private Dictionary<KeyType, ValueType> dictionary;
	Enumeration<KeyType> keys;

	public DictionaryIterator(Dictionary<KeyType, ValueType> dictionary) {
		this.dictionary = dictionary;
		this.keys = dictionary.keys();
	}

	public boolean hasNext() {
		return this.keys.hasMoreElements();
	}

	public GenericPair<KeyType, ValueType> next() {
		KeyType nextKey = this.keys.nextElement();
		ValueType nextValue = this.dictionary.get(nextKey);

		return new GenericPair<KeyType, ValueType>(nextKey, nextValue);
	}

	public void remove() {
		String exceptionMessage = "remove() cannot be called on a DictionaryIterator.";

		throw new UnsupportedOperationException(exceptionMessage);
	}

	public Iterator<GenericPair<KeyType, ValueType> > iterator() {
		return this;
	}
}