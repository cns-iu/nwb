package edu.iu.nwb.analysis.multipartitejoining;

import java.util.HashMap;
import java.util.Map;

import prefuse.data.Tuple;
import cern.colt.function.ObjectFunction;
import cern.colt.function.ObjectObjectFunction;
import cern.colt.matrix.ObjectMatrix1D;

public class Util {

	static int number(ObjectMatrix1D joins) {
		return ((Number) joins.aggregate(new LongAdder(), new ObjectFunction() {
			public Object apply(Object current) {
				if(current == null) {
					return new Integer(0);
				} else {
					return new Integer(1);
				}
			}
		})).intValue();
	}

	static double doubleSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new DoubleAdder(), new DoubleMaker(field))).doubleValue();
	}

	static float floatSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new DoubleAdder(), new FloatMaker(field))).floatValue();
	}

	static int integerSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new LongAdder(), new IntegerMaker(field))).intValue();
	}

	static long longSum(ObjectMatrix1D joins, final String field) {
		return ((Number) joins.aggregate(new LongAdder(), new LongMaker(field))).longValue();
	}

	static double doubleMax(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new DoubleMax(), new DoubleMaker(field))).doubleValue();
	}

	static float floatMax(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new DoubleMax(), new FloatMaker(field))).floatValue();
	}

	static long longMax(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new LongMax(), new LongMaker(field))).longValue();
	}

	static int integerMax(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new LongMax(), new IntegerMaker(field))).intValue();
	}

	static double doubleMin(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new NullDoubleMin(), new NullDoubleMaker(field))).doubleValue();
	}

	static float floatMin(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new NullDoubleMin(), new NullFloatMaker(field))).floatValue();
	}

	static long longMin(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new NullLongMin(), new NullLongMaker(field))).longValue();
	}

	static int integerMin(ObjectMatrix1D joins, String field) {
		return ((Number) joins.aggregate(new NullLongMin(), new NullIntegerMaker(field))).intValue();
	}

}

class NumberMapper implements ObjectObjectFunction {

	private Map map = new HashMap();

	public Object apply(Object aggregate, Object current) {
		if(current != null) {
			if(map.containsKey(current)) {
				map.put(current, new Integer(((Integer) map.get(current)).intValue() + 1));
			} else {
				map.put(current, new Integer(1));
			}
		}
		return map;
	}
}

class FieldFetcher implements ObjectFunction {

	private String field;

	public FieldFetcher(String field) {
		this.field = field;
	}

	public Object apply(Object object) {
		if(object == null) {
			return null;
		} else {
			return ((Tuple) object).get(field);
		}
	}
}

class LongAdder implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		long totalSoFar = ((Number) aggregate).longValue();
		long soFar = totalSoFar + ((Number) current).longValue();
		return new Long(soFar);
	}
}


class DoubleAdder implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		return new Double(((Number) aggregate).doubleValue() + ((Number) current).doubleValue());
	}
}

class LongMin implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		return new Long(Math.min(((Number) aggregate).longValue(), ((Number) current).longValue()));
	}
}

class DoubleMin implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		return new Double(Math.min(((Number) aggregate).doubleValue(), ((Number) current).doubleValue()));
	}
}

class NullLongMin implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		if(current == null && aggregate != null) {
			return aggregate;
		} else if(aggregate == null && current != null) {
			return current;
		} else if(aggregate == null && current == null) {
			return null;
		} else {
			return new Long(Math.min(((Number) aggregate).longValue(), ((Number) current).longValue()));
		}
	}
}

class NullDoubleMin implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		if(current == null && aggregate != null) {
			return aggregate;
		} else if(aggregate == null && current != null) {
			return current;
		} else if(aggregate == null && current == null) {
			return null;
		} else {
			return new Double(Math.min(((Number) aggregate).doubleValue(), ((Number) current).doubleValue()));
		}
	}
}

class LongMax implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		return new Long(Math.max(((Number) aggregate).longValue(), ((Number) current).longValue()));
	}
}

class DoubleMax implements ObjectObjectFunction {
	public Object apply(Object aggregate, Object current) {
		return new Double(Math.max(((Number) aggregate).doubleValue(), ((Number) current).doubleValue()));
	}
}

class LongMaker implements ObjectFunction {

	private String field;

	public LongMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		Long result;
		if(current == null) {
			result = new Long(0);
		} else {
			result = new Long(((Tuple) current).getLong(field));
		}
		return result;
	}
}

class IntegerMaker implements ObjectFunction {

	private String field;

	public IntegerMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		if(current == null) {
			return new Integer(0);
		} else {
			return new Integer(((Tuple) current).getInt(field));
		}
	}
}

class DoubleMaker implements ObjectFunction {

	private String field;

	public DoubleMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		if(current == null) {
			return new Double(0);
		} else {
			return new Double(((Tuple) current).getDouble(field));
		}
	}
}

class FloatMaker implements ObjectFunction {

	private String field;

	public FloatMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		if(current == null) {
			return new Float(0);
		} else {
			return new Float(((Tuple) current).getFloat(field));
		}
	}
}

class NullLongMaker implements ObjectFunction {

	private String field;

	public NullLongMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		Long result;
		if(current == null) {
			result = null;
		} else {
			result = new Long(((Tuple) current).getLong(field));
		}
		return result;
	}
}

class NullIntegerMaker implements ObjectFunction {

	private String field;

	public NullIntegerMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		if(current == null) {
			return null;
		} else {
			return new Integer(((Tuple) current).getInt(field));
		}
	}
}

class NullDoubleMaker implements ObjectFunction {

	private String field;

	public NullDoubleMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		if(current == null) {
			return null;
		} else {
			return new Double(((Tuple) current).getDouble(field));
		}
	}
}

class NullFloatMaker implements ObjectFunction {

	private String field;

	public NullFloatMaker(String field) {
		this.field = field;
	}

	public Object apply(Object current) {
		if(current == null) {
			return null;
		} else {
			return new Float(((Tuple) current).getFloat(field));
		}
	}
}