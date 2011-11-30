package edu.iu.sci2.database.scopus.load;

import java.util.Date;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

public class ForwardingTuple implements Tuple {
	private Tuple delegate;
	public ForwardingTuple(Tuple delegate) {
		this.delegate = delegate;
	}

	public boolean canGet(String arg0, Class arg1) {
		return delegate.canGet(arg0, arg1);
	}

	public boolean canGetBoolean(String arg0) {
		return delegate.canGetBoolean(arg0);
	}

	public boolean canGetDate(String arg0) {
		return delegate.canGetDate(arg0);
	}

	public boolean canGetDouble(String arg0) {
		return delegate.canGetDouble(arg0);
	}

	public boolean canGetFloat(String arg0) {
		return delegate.canGetFloat(arg0);
	}

	public boolean canGetInt(String arg0) {
		return delegate.canGetInt(arg0);
	}

	public boolean canGetLong(String arg0) {
		return delegate.canGetLong(arg0);
	}

	public boolean canGetString(String arg0) {
		return delegate.canGetString(arg0);
	}

	public boolean canSet(String arg0, Class arg1) {
		return delegate.canSet(arg0, arg1);
	}

	public boolean canSetBoolean(String arg0) {
		return delegate.canSetBoolean(arg0);
	}

	public boolean canSetDate(String arg0) {
		return delegate.canSetDate(arg0);
	}

	public boolean canSetDouble(String arg0) {
		return delegate.canSetDouble(arg0);
	}

	public boolean canSetFloat(String arg0) {
		return delegate.canSetFloat(arg0);
	}

	public boolean canSetInt(String arg0) {
		return delegate.canSetInt(arg0);
	}

	public boolean canSetLong(String arg0) {
		return delegate.canSetLong(arg0);
	}

	public boolean canSetString(String arg0) {
		return delegate.canSetString(arg0);
	}

	public Object get(int arg0) {
		return delegate.get(arg0);
	}

	public Object get(String arg0) {
		return delegate.get(arg0);
	}

	public boolean getBoolean(int arg0) {
		return delegate.getBoolean(arg0);
	}

	public boolean getBoolean(String arg0) {
		return delegate.getBoolean(arg0);
	}

	public int getColumnCount() {
		return delegate.getColumnCount();
	}

	public int getColumnIndex(String arg0) {
		return delegate.getColumnIndex(arg0);
	}

	public String getColumnName(int arg0) {
		return delegate.getColumnName(arg0);
	}

	public Class getColumnType(int arg0) {
		return delegate.getColumnType(arg0);
	}

	public Class getColumnType(String arg0) {
		return delegate.getColumnType(arg0);
	}

	public Date getDate(int arg0) {
		return delegate.getDate(arg0);
	}

	public Date getDate(String arg0) {
		return delegate.getDate(arg0);
	}

	public Object getDefault(String arg0) {
		return delegate.getDefault(arg0);
	}

	public double getDouble(int arg0) {
		return delegate.getDouble(arg0);
	}

	public double getDouble(String arg0) {
		return delegate.getDouble(arg0);
	}

	public float getFloat(int arg0) {
		return delegate.getFloat(arg0);
	}

	public float getFloat(String arg0) {
		return delegate.getFloat(arg0);
	}

	public int getInt(int arg0) {
		return delegate.getInt(arg0);
	}

	public int getInt(String arg0) {
		return delegate.getInt(arg0);
	}

	public long getLong(int arg0) {
		return delegate.getLong(arg0);
	}

	public long getLong(String arg0) {
		return delegate.getLong(arg0);
	}

	public int getRow() {
		return delegate.getRow();
	}

	public Schema getSchema() {
		return delegate.getSchema();
	}

	public String getString(int arg0) {
		return delegate.getString(arg0);
	}

	public String getString(String arg0) {
		return delegate.getString(arg0);
	}

	public Table getTable() {
		return delegate.getTable();
	}

	public boolean isValid() {
		return delegate.isValid();
	}

	public void revertToDefault(String arg0) {
		delegate.revertToDefault(arg0);
	}

	public void set(int arg0, Object arg1) {
		delegate.set(arg0, arg1);
	}

	public void set(String arg0, Object arg1) {
		delegate.set(arg0, arg1);
	}

	public void setBoolean(int arg0, boolean arg1) {
		delegate.setBoolean(arg0, arg1);
	}

	public void setBoolean(String arg0, boolean arg1) {
		delegate.setBoolean(arg0, arg1);
	}

	public void setDate(int arg0, Date arg1) {
		delegate.setDate(arg0, arg1);
	}

	public void setDate(String arg0, Date arg1) {
		delegate.setDate(arg0, arg1);
	}

	public void setDouble(int arg0, double arg1) {
		delegate.setDouble(arg0, arg1);
	}

	public void setDouble(String arg0, double arg1) {
		delegate.setDouble(arg0, arg1);
	}

	public void setFloat(int arg0, float arg1) {
		delegate.setFloat(arg0, arg1);
	}

	public void setFloat(String arg0, float arg1) {
		delegate.setFloat(arg0, arg1);
	}

	public void setInt(int arg0, int arg1) {
		delegate.setInt(arg0, arg1);
	}

	public void setInt(String arg0, int arg1) {
		delegate.setInt(arg0, arg1);
	}

	public void setLong(int arg0, long arg1) {
		delegate.setLong(arg0, arg1);
	}

	public void setLong(String arg0, long arg1) {
		delegate.setLong(arg0, arg1);
	}

	public void setString(int arg0, String arg1) {
		delegate.setString(arg0, arg1);
	}

	public void setString(String arg0, String arg1) {
		delegate.setString(arg0, arg1);
	}


}
