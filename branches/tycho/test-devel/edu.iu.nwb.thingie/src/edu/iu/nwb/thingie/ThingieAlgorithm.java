package edu.iu.nwb.thingie;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

public class ThingieAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    URL mrT;
    
    public ThingieAlgorithm(Data[] data, Dictionary parameters, CIShellContext context, URL mrT) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.mrT = mrT;
    }

    public Data[] execute() {
    	Shell parentShell = getParentShell();
    	Runnable runnable = new MessWithLogView(parentShell);
		Thread messWithLogViewThread = new Thread(runnable);
    
		//We must tell SWT to run the preference dialog, instead of running it directly ourselves
		parentShell.getDisplay().asyncExec(messWithLogViewThread);
        return null;
    }
    
    private Shell getParentShell() {
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();

    	//possibly a better, less seamingly arbitrary way to do this
    	IWorkbenchWindow window = windows[0];
    	Shell parentShell = window.getShell();
    	return parentShell;
    }
    
    private class MessWithLogView implements Runnable {

    	private Shell parentShell;
    	
    	public MessWithLogView(Shell parentShell) {
    		this.parentShell = parentShell;
    	}
    	
		public void run() {
			try {
			String pathToImage = (String) parameters.get("1");
			System.out.println("Path to image:" + pathToImage);
			ViewPart logView = (ViewPart) getView("org.cishell.reference.gui.log.LogView");
			Class logClazz = logView.getClass();
			Class subclazz = logView.getClass().getClassLoader().loadClass("org.cishell.reference.gui.log.LogView");
		     Field verbotenField =  subclazz.getDeclaredField("text");
		     verbotenField.setAccessible(true);
		     StyledText result = (StyledText) verbotenField.get(new String());
		    // result.setBackground(new Color(result.getDisplay(), 20 , 20, 220));
		 //    result.append("THIISSS ISS SPAAARRRTTAAAAAA!!!!!!!!!!!!!");
//		     URL mrTURL = this.getClass().getResource("/MrT.jpg");
		    /// Image image = new Image(result.getDisplay(), "/edu.iu.nwb.thingie/MrT.jpg");
		     Transform transform = new Transform(result.getDisplay());
		   
		     Image image = ImageDescriptor.createFromURL(mrT).createImage();
		     GC gc = new GC(result);
//		     gc.drawText("I pity the fool", 200, 400);
		    // gc.setBackground(new Color(result.getDisplay(), (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))); 
		     gc.setBackground(new Color(result.getDisplay(), 255, 20, 255 ));
		     for (int i = 0; i < (108 * 2); i++) {
		    	 if (i % 18 == 0 && i != 18) {
		    		 gc.setBackground(new Color(result.getDisplay(), (int) (Math.random() * 200) + 55, (int) (Math.random() * 200) + 55, (int) (Math.random() * 200) + 55)); 
		 	    	 gc.fillRectangle(0, 0, 1500, 1500);
		    	 }

		    	 transform.translate(250 + 500, 250 + 50);
		    	 transform.rotate(22);
		    	 transform.translate(-750, -300);
		    	 gc.setTransform(transform);
		    	
		    		    	 gc.drawImage(image, 500, 50);
		    	 try {
		    	 } catch (Exception e) {}
		    

		     }
		     

		     
//		     gc.setBackground(new Color(result.getDisplay(), 1, 2, 233));
			//	logClazz.for
//			superClazz = logClazz.asSubclass(Class.forName("package org.cishell.reference.gui.log.LogView"));
//			field.setAccessible(true);
//			System.out.println(field.get(logView));
			} catch (NoSuchFieldException e1) {
				System.out.println("WHOOPS!");
			} catch (IllegalAccessException e2) {
				System.out.println("WHOOPS 2!");
			} catch (ClassNotFoundException e3) {
				System.out.println("WHOOPS CLASS NOT FOUND!");
			}
			
		}
		
		public IViewPart getView(String id) {
			IViewReference viewReferences[] = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().getViewReferences();
			for (int i = 0; i < viewReferences.length; i++) {
				if (id.equals(viewReferences[i].getId())) {
					return viewReferences[i].getView(false);
				}
			}
			return null;
		}

    }
}