package edu.iu.cns.shared.utilities;

public class GenericPair<FirstType, SecondType> {
	FirstType firstObject;
	SecondType secondObject;

	public GenericPair(FirstType firstObject, SecondType secondObject) {
		this.firstObject = firstObject;
		this.secondObject = secondObject;
	}

	public FirstType getFirstObject() {
		return this.firstObject;
	}

	public SecondType getSecondObject() {
		return this.secondObject;
	}
}