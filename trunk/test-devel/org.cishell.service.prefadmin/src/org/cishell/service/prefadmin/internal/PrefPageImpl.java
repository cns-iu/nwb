package org.cishell.service.prefadmin.internal;

import org.cishell.service.prefadmin.PrefPage;
import org.osgi.service.cm.Configuration;
import org.osgi.service.metatype.ObjectClassDefinition;

public class PrefPageImpl implements PrefPage {
	private Configuration prefConf;
	private ObjectClassDefinition prefOCD;
	
	public PrefPageImpl(Configuration prefConf, ObjectClassDefinition prefOCD) {
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
	public ObjectClassDefinition getPrefOCD() {
		return this.prefOCD;
	}
}
