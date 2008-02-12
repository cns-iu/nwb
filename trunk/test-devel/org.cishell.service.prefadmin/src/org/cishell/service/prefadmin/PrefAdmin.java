package org.cishell.service.prefadmin;

import org.osgi.service.cm.ConfigurationPlugin;

public interface PrefAdmin extends ConfigurationPlugin {
	public PrefPage[] getLocalPrefPages();
	public PrefPage[] getGlobalPrefPages();
	public PrefPage[] getParamPrefPages();
	
	public PrefsByService[] getPrefsByService();
}
