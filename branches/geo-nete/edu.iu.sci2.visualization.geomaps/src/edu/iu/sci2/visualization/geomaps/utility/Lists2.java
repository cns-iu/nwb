package edu.iu.sci2.visualization.geomaps.utility;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Lists2 {
	private Lists2() {}

	/* TODO Unit test */
	/**
	 * @see Iterables#indexOf(Iterable, Predicate)
	 * @see List#lastIndexOf(Object)
	 */
	public static <T> int lastIndexOf(List<? extends T> list, Predicate<? super T> predicate) {
		int firstIndexInReversedList = Iterables.indexOf(Lists.reverse(list), predicate);
		
		if (firstIndexInReversedList == -1) {
			return -1;
		}
		
		return (list.size() - 1) - firstIndexInReversedList;
	}	
}
