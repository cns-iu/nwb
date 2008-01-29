package org.cishell.service.prefadmin;

import org.osgi.service.cm.Configuration;
import org.osgi.service.metatype.ObjectClassDefinition;

public interface PrefPage {

	public abstract Configuration getPrefConf();

	public abstract PreferenceOCD getPrefOCD();

}