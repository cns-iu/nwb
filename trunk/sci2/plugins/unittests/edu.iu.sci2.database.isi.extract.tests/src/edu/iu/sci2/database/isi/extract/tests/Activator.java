package edu.iu.sci2.database.isi.extract.tests;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.LocalCIShellContext;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static CIShellContext ciContext;

	static BundleContext getContext() {
		return context;
	}
	
	static CIShellContext getCiContext() {
		return ciContext;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.ciContext = new LocalCIShellContext(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		Activator.ciContext = null;
	}

}
