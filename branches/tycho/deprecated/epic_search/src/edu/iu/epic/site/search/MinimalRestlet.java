package edu.iu.epic.site.search;

import org.restlet.Server;
import org.restlet.data.Protocol;

public class MinimalRestlet {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		 MyRestlet restlet = new MyRestlet();
		   
		 // Create the HTTP server and listen on port 8182  
		 new Server(Protocol.HTTP, 8182, restlet).start();  

	}

}