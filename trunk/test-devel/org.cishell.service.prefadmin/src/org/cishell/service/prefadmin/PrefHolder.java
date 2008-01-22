package org.cishell.service.prefadmin;


public interface PrefHolder {

	public abstract String getServicePID();

	public abstract PrefPage getLocalPrefPage();

	public abstract PrefPage[] getGlobalPrefPages();

}