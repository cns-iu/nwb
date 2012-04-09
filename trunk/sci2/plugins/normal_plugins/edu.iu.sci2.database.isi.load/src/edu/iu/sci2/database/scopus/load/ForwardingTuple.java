package edu.iu.sci2.database.scopus.load;

import java.util.Date;

import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;

// Unfortunately, the interface is not generic, so we're stuck.
@SuppressWarnings("rawtypes")
public class ForwardingTuple implements Tuple {
	private Tuple delegate;
	public ForwardingTuple(Tuple delegate) {
		this.delegate = delegate;
	}

	@Override
	public boolean canGet(String arg0, Class arg1) {
		return this.delegate.canGet(arg0, arg1);
	}

	@Override
	public boolean canGetBoolean(String arg0) {
		return this.delegate.canGetBoolean(arg0);
	}

	@Override
	public boolean canGetDate(String arg0) {
		return this.delegate.canGetDate(arg0);
	}

	@Override
	public boolean canGetDouble(String arg0) {
		return this.delegate.canGetDouble(arg0);
	}

	@Override
	public boolean canGetFloat(String arg0) {
		return this.delegate.canGetFloat(arg0);
	}

	@Override
	public boolean canGetInt(String arg0) {
		return this.delegate.canGetInt(arg0);
	}

	@Override
	public boolean canGetLong(String arg0) {
		return this.delegate.canGetLong(arg0);
	}

	@Override
	public boolean canGetString(String arg0) {
		return this.delegate.canGetString(arg0);
	}

	@Override
	public boolean canSet(String arg0, Class arg1) {
		return this.delegate.canSet(arg0, arg1);
	}

	@Override
	public boolean canSetBoolean(String arg0) {
		return this.delegate.canSetBoolean(arg0);
	}

	@Override
	public boolean canSetDate(String arg0) {
		return this.delegate.canSetDate(arg0);
	}

	@Override
	public boolean canSetDouble(String arg0) {
		return this.delegate.canSetDouble(arg0);
	}

	@Override
	public boolean canSetFloat(String arg0) {
		return this.delegate.canSetFloat(arg0);
	}

	@Override
	public boolean canSetInt(String arg0) {
		return this.delegate.canSetInt(arg0);
	}

	@Override
	public boolean canSetLong(String arg0) {
		return this.delegate.canSetLong(arg0);
	}

	@Override
	public boolean canSetString(String arg0) {
		return this.delegate.canSetString(arg0);
	}

	@Override
	public Object get(int arg0) {
		return this.delegate.get(arg0);
	}

	@Override
	public Object get(String arg0) {
		return this.delegate.get(arg0);
	}

	@Override
	public boolean getBoolean(int arg0) {
		return this.delegate.getBoolean(arg0);
	}

	@Override
	public boolean getBoolean(String arg0) {
		return this.delegate.getBoolean(arg0);
	}

	@Override
	public int getColumnCount() {
		return this.delegate.getColumnCount();
	}

	@Override
	public int getColumnIndex(String arg0) {
		return this.delegate.getColumnIndex(arg0);
	}

	@Override
	public String getColumnName(int arg0) {
		return this.delegate.getColumnName(arg0);
	}

	@Override
	public Class getColumnType(int arg0) {
		return this.delegate.getColumnType(arg0);
	}

	@Override
	public Class getColumnType(String arg0) {
		return this.delegate.getColumnType(arg0);
	}

	@Override
	public Date getDate(int arg0) {
		return this.delegate.getDate(arg0);
	}

	@Override
	public Date getDate(String arg0) {
		return this.delegate.getDate(arg0);
	}

	@Override
	public Object getDefault(String arg0) {
		return this.delegate.getDefault(arg0);
	}

	@Override
	public double getDouble(int arg0) {
		return this.delegate.getDouble(arg0);
	}

	@Override
	public double getDouble(String arg0) {
		return this.delegate.getDouble(arg0);
	}

	@Override
	public float getFloat(int arg0) {
		return this.delegate.getFloat(arg0);
	}

	@Override
	public float getFloat(String arg0) {
		return this.delegate.getFloat(arg0);
	}

	@Override
	public int getInt(int arg0) {
		return this.delegate.getInt(arg0);
	}

	@Override
	public int getInt(String arg0) {
		return this.delegate.getInt(arg0);
	}

	@Override
	public long getLong(int arg0) {
		return this.delegate.getLong(arg0);
	}

	@Override
	public long getLong(String arg0) {
		return this.delegate.getLong(arg0);
	}

	@Override
	public int getRow() {
		return this.delegate.getRow();
	}

	@Override
	public Schema getSchema() {
		return this.delegate.getSchema();
	}

	@Override
	public String getString(int arg0) {
		return this.delegate.getString(arg0);
	}

	@Override
	public String getString(String arg0) {
		return this.delegate.getString(arg0);
	}

	@Override
	public Table getTable() {
		return this.delegate.getTable();
	}

	@Override
	public boolean isValid() {
		return this.delegate.isValid();
	}

	@Override
	public void revertToDefault(String arg0) {
		this.delegate.revertToDefault(arg0);
	}

	@Override
	public void set(int arg0, Object arg1) {
		this.delegate.set(arg0, arg1);
	}

	@Override
	public void set(String arg0, Object arg1) {
		this.delegate.set(arg0, arg1);
	}

	@Override
	public void setBoolean(int arg0, boolean arg1) {
		this.delegate.setBoolean(arg0, arg1);
	}

	@Override
	public void setBoolean(String arg0, boolean arg1) {
		this.delegate.setBoolean(arg0, arg1);
	}

	@Override
	public void setDate(int arg0, Date arg1) {
		this.delegate.setDate(arg0, arg1);
	}

	@Override
	public void setDate(String arg0, Date arg1) {
		this.delegate.setDate(arg0, arg1);
	}

	@Override
	public void setDouble(int arg0, double arg1) {
		this.delegate.setDouble(arg0, arg1);
	}

	@Override
	public void setDouble(String arg0, double arg1) {
		this.delegate.setDouble(arg0, arg1);
	}

	@Override
	public void setFloat(int arg0, float arg1) {
		this.delegate.setFloat(arg0, arg1);
	}

	@Override
	public void setFloat(String arg0, float arg1) {
		this.delegate.setFloat(arg0, arg1);
	}

	@Override
	public void setInt(int arg0, int arg1) {
		this.delegate.setInt(arg0, arg1);
	}

	@Override
	public void setInt(String arg0, int arg1) {
		this.delegate.setInt(arg0, arg1);
	}

	@Override
	public void setLong(int arg0, long arg1) {
		this.delegate.setLong(arg0, arg1);
	}

	@Override
	public void setLong(String arg0, long arg1) {
		this.delegate.setLong(arg0, arg1);
	}

	@Override
	public void setString(int arg0, String arg1) {
		this.delegate.setString(arg0, arg1);
	}

	@Override
	public void setString(String arg0, String arg1) {
		this.delegate.setString(arg0, arg1);
	}


}
