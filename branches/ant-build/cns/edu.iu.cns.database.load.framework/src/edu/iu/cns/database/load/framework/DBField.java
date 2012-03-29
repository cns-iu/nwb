package edu.iu.cns.database.load.framework;

import edu.iu.cns.database.load.framework.DerbyFieldType;

public interface DBField {

	public abstract String name();

	public abstract DerbyFieldType type();

}