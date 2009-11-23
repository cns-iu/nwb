/* Copyright (c) 2006-2008 Indiana University Research and Technology Corporation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *
 * - Neither the Indiana University nor the names of its contributors may be used
 *  to endorse or promote products derived from this software without specific
 *  prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package stencil.util;


import java.util.*;

import stencil.streams.InvalidNameException;
import stencil.adapters.general.Fills;
import stencil.streams.MutableTuple;
import stencil.streams.Tuple;
import stencil.types.SigilType;
import stencil.types.Converter;
import stencil.types.TypesCache;

/**Utility methods for working with tuples.*/
//final because it is a collection of utility methods and is not to be instantiated or overridden
public final class Tuples {
	private Tuples() {/*Utility class. Not instantiable.*/}

	/**Tuple with no fields.  Should be used instead of null wherever a tuple 
	 * is required but cannot be supplied.*/
	public static final Tuple EMPTY_TUPLE = new Tuple() {
		public Object get(String name) throws InvalidNameException {throw new InvalidNameException(name);}
		public Object get(String name, Class<?> type)
				throws IllegalArgumentException, InvalidNameException {throw new InvalidNameException(name);}
		public List<String> getFields() {return new ArrayList<String>();}
		public boolean hasField(String name) {return false;}
		public boolean isDefault(String name, Object value) {throw new InvalidNameException(name);}	
	};

	
	/**Tuple backed by a map.  The tuple is immutable, but the backing
	 * map may not be.  Updates to the map WILL be represented in the tuple.
	 * @author jcottam
	 *
	 */
	public static final class MapTuple implements Tuple {
		private Map<String, ?> map;
		
		public MapTuple(Map<String, ?> source) {this.map = source;}
		
		public Object get(String name) throws InvalidNameException {return map.get(name);}

		public Object get(String name, Class<?> type)
				throws IllegalArgumentException, InvalidNameException {
			return Converter.convert(get(name), type);
		}

		public List<String> getFields() {return new ArrayList(map.keySet());}

		public boolean hasField(String name) {return map.containsKey(name);}

		public boolean isDefault(String name, Object value) {return false;}
	};

	/**Given a source tuple, returns a new tuple with only those fields that were
	 * prefixed with the given prefix.  Fields no longer have the prefix.
	 */
	public static final Tuple sift(String prefix, Tuple source) {
		List<String> fields = new ArrayList();
		List<Object> values = new ArrayList();
		
		if (source == null) {return EMPTY_TUPLE;}
		
		for (String field:source.getFields()) {
			if (field.startsWith(prefix)) {
				values.add(source.get(field));
				fields.add(field.substring(prefix.length()));
			}
		}
		
		return new BasicTuple(fields, values);
	}

	
	/**Creates a read-only copy of the given tuple.  Values
	 * are copied per the rules defined in Transfer.
	 *
	 * @param source
	 * @return
	 */
	public static Tuple copy(Tuple source) {
		List<String> attributes =  new ArrayList<String>();
		attributes.addAll(source.getFields());

		Object[] values = new Object[attributes.size()];
		for (int i=0; i< attributes.size(); i++) {
			Object v = source.get(attributes.get(i));
			if (v != null) {values[i] = v;}
		}

		return new BasicTuple(attributes, Arrays.asList(values));
	}

	/**Copies all of the field of the source to the target.
	 * Only value that appear in the 'getFields' list of the source
	 * will be copied (so implicit fields on concrete tuples, like glyphs,
	 * must be reported in getFields for this to work).
	 * This is a shallow copy, as not all objects can be cloned.
	 * 
	 * Errors in transfer are ignored and not reported.  The attribute is simply skipped.
	 * This is probably not the right way to do it...but it is my way...
	 *
	 * @param source Source of values.
	 * @param target Target for values.  Existing values may be overwritten.
	 * @param defaults Should default values be transfered (false will skip any value currently set to the default on the source)
	 * @return The target passed in (after the transfer).
	 * 
	 * TODO: Make it so it can optionally report errors
	 */
	public static final MutableTuple transfer(Tuple source, MutableTuple target, boolean defaults) {
		for (String field:source.getFields()) {
			try {
				Object sourceValue = source.get(field);
				Object targetValue = target.get(field);
				if (sourceValue == targetValue || sourceValue.equals(targetValue)) {continue;}	//Skip values that are equal.
				if (defaults || !source.isDefault(field, sourceValue)) {target.set(field, sourceValue);}
			} catch (Throwable e) {/*HACK: Errors are ignored, so transfer may not be successful but execution does not know that!*/}
		}
		return target;
	}

	/**Compares two tuples.  Tuples are considered transferNeutral if
	 * the target tuple would be functionally equivalent after a transfer from the source.
	 * This is done by testing the transferable values with .equals
	 * (which is supposed to be symmetric, but may not be, so the target's value's
	 * .equals is invoked on the source's value).
	 *
	 * If the source has any field not included in the target, it will
	 * automatically not be transferNeutral.
	 *
	 * @return
	 */
	public static boolean transferNeutral(Tuple source, Tuple target) {
		for (String field: source.getFields()) {
			if (!target.hasField(field)) {return false;}
			Object sourceValue = source.get(field);
			Object targetValue = target.get(field);
			if (targetValue == sourceValue ||
				(targetValue != null && !targetValue.equals(sourceValue))) {return false;}
		}
		return true;
	}

	/**Strings of tuples look like name/value lists where
	 * the name and value are separated with a colon and the next
	 * values are terminated with a semicolon and a space.
	 * This static method can be used with any tuple.
	 *
	 * This method goes through extra work to ensure a consistent tuple
	 * representation.  As such, fields are always printed out in
	 * alphabetical order when using this method.
	 *
	 * **/
	public static String toString(Tuple t) {return toString(t, t.getFields());}
	
	/**Provide a string representation of the tuple, 
	 * but only include the fields in the passed list.
	 */
	public static String toString(Tuple t, List<String> fieldNames) {
		StringBuilder rv = new StringBuilder();
		rv.append("(");


		String[] fields = fieldNames.toArray(new String[]{});
		Arrays.sort(fields);
		for (String name: fields){
			Object value =t.get(name);
			if (value instanceof Number && ((Number) value).doubleValue() ==-0) {value =0.0d;}//Prevent negative zeros
			 
			//Skip values currently set to the default
			if (t.isDefault(name, value)) {continue;}
			
			if (value == null) {
				value = "[null]";
			} else if (TypesCache.hasTypeFor(value)) {
				SigilType type = TypesCache.getType(value);
				value = type.toString(value);
			} else if (value instanceof java.awt.TexturePaint) {
				value = Fills.fillString((java.awt.TexturePaint) value);
			}else if (value.getClass().isArray()) {
				value = Arrays.deepToString((Object[]) value);
			}
			rv.append(String.format("%1$s:%2$s; ", name, value));
		}

		rv.deleteCharAt(rv.length()-1);
		if (rv.length() == 0) {rv.append("()");}
		else {rv.append(")");}
		return rv.toString();
	}

	/**Create a new tuple with fields representing a union of the
	 * fields of the two source tuples.  Values will be taken from
	 * source1 first, then source2 (so last-write wins on shared fields).
	 *
	 * If source1 is null or source2 is null, a new tuple is still
	 * returned, but containing a duplicate of the non-null tuple.
	 * If both are null, an illegal argument exception is thrown.
	 *
	 * @param sourceName Where should the resulting tuple indicate it is from?
	 * @param source1
	 * @param source2
	 * @return
	 */
	public static Tuple merge(Tuple source1, Tuple source2) throws IllegalArgumentException {
		class IncrimentalTuple implements Tuple {
			private java.util.Map<String, Object> values;

			public IncrimentalTuple() {
				values = new HashMap<String, Object>();
			}

			public Object get(String name) throws InvalidNameException {
				if (!values.containsKey(name)) {throw new InvalidNameException(name);}
				return values.get(name);
			}

			public Object get(String name, Class<?> target) throws IllegalArgumentException {
				return Converter.convert(values.get(name), target);
			}

			public List<String> getFields() {
				String[] names = values.keySet().toArray(new String[]{});
				Arrays.sort(names);
				return Arrays.asList(names);
			}

			public boolean hasField(String name) {return values.keySet().contains(name);}

			public boolean isDefault(String name, Object value) {return false;}

			public void addField(String name, Object value) {
				values.put(name, value);
			}

			public String toString() {return Tuples.toString(this);}
		}

		if (source1 == null && source2 ==null) {throw new IllegalArgumentException("At least one source to merge must not be null.");}

		if (source1 == null) {return Tuples.copy(source2);}
		if (source2 == null) {return Tuples.copy(source1);}

		IncrimentalTuple result = new IncrimentalTuple();
		for (String name: source1.getFields()) {
			Object value = source1.get(name);
			result.addField(name, value);
		}
		for (String name: source2.getFields()) {
			Object value = source2.get(name);
			result.addField(name, value);
		}
		return result;
	}


	
	/**Produces an array version of a tuple.  Value are in the same order as the original tuple fields.**/
	public static Object[] toArray(Tuple t) {
		Object[] values = new Object[t.getFields().size()];
		int i=0;
		for (String field:t.getFields()) {values[i++] = t.get(field);}
		return values;
	}
	
	/**Create a list of default names.  Default names are derived
	 * from the prefix in a consistent manner.  If the prefix is null,
	 * the global tuple-default-key is used instead.
	 * 
	 * @param count
	 * @param prefix
	 * @return
	 */
	public static String[] defaultNames(int count, String prefix) {
		if (prefix == null) {prefix = Tuple.DEFAULT_KEY;}
		String[] names= new String[count];
		names[0]=prefix;
		for (int i=1; i< count; i++) {
			names[i] = prefix + count;
		}
		return names;
	}
	
	
	/**Remove quotes from around a value (if present).*/
	public static final String stripQuotes(String s) {
        if (s.startsWith("\"")) {s = s.substring(1);}
        if (s.endsWith("\"")) {s = s.substring(0,s.length()-1);}
        return s;
    }
}