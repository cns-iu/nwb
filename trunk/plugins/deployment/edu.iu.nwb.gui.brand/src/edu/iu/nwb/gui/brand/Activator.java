package edu.iu.nwb.gui.brand;

//import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup{

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.iu.nwb.gui.brand";
	// The shared instance
	private static Activator plugin;	
	private BundleContext bContext;
	private static final String nwb_greeting=
		"Welcome to use Network Workbench Tool "+
		"-- A large-scale network analysis, modeling and visualization "+
		"cyberinfrastructure for network scientists.\n"+
		"The NWB Tool is funded by an NSF IIS-0513650 award to Katy Börner, "+
		"Albert-Laszlo Barabasi, Santiago Schnell, Alessandro Vespignani, "+
		"Stanley Wasserman, and Eric Wernert, see  http://nwb.slis.indiana.edu.\n"+
		"It builds on the Cyberinfrastructure Shell  (http://cishell.org) developed "+
		"at the InfoVis Lab and the CI for Network Science Center at Indiana University.\n\n"+
        "Please acknowledge this effort by citing:\n"+
        "Bruce Herr, Weixia Huang, Shashikant Penumarthy, and Katy Börner. (in press). "+
        "Designing Highly Flexible and Usable Cyberinfrastructures for Convergence. "+
        "William S. Bainbridge (Ed.) Progress in Convergence. Annals of the New York Academy of Sciences.\n"+
        "http://ella.slis.indiana.edu/~katy/paper/06-cishell.pdf";
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.bContext = context;
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public void earlyStartup(){
		//		TODO: Get log and print initial log blurb
		ServiceReference ref = bContext.getServiceReference(LogService.class.getName());
        LogService logger = (LogService)bContext.getService(ref);
        logger.log(LogService.LOG_INFO, nwb_greeting);

		
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
/*	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
*/	
}
