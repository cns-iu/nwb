package org.cishell.service.prefadmin.internal;

import org.cishell.service.prefadmin.PrefPage;
import org.cishell.service.prefadmin.PreferenceOCD;
import org.osgi.service.cm.Configuration;
import org.osgi.service.metatype.ObjectClassDefinition;

public class PrefPageImpl implements PrefPage {
	private Configuration prefConf;
	private PreferenceOCD prefOCD;
	
	public PrefPageImpl(Configuration prefConf, PreferenceOCD prefOCD) {
		this.prefConf = prefConf;
		this.prefOCD = prefOCD;
	}
	
	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.internal.PrefPage#getPrefConf()
	 */
	public Configuration getPrefConf() {
		return this.prefConf;
	}
	
	/* (non-Javadoc)
	 * @see org.cishell.service.prefadmin.internal.PrefPage#getPrefOCD()
	 */
	public PreferenceOCD getPrefOCD() {
		return this.prefOCD;
	}
}
