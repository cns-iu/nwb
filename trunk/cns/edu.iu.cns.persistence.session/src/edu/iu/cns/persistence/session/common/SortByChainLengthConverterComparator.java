package edu.iu.cns.persistence.session.common;

import java.util.Comparator;

import org.cishell.service.conversion.Converter;

public class SortByChainLengthConverterComparator implements Comparator<Converter> {
	public int compare(Converter converter1, Converter converter2) {
		int chainLength1 = converter1.getConverterChain().length;
		int chainLength2 = converter2.getConverterChain().length;

		if (chainLength1 > chainLength2) {
			return 1;
		} else if (chainLength1 < chainLength2) {
			return -1;
		} else {
			return 0;
		}
	}
}