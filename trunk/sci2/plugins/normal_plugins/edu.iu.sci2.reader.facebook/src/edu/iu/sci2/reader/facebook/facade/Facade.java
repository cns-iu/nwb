package edu.iu.sci2.reader.facebook.facade;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.osgi.service.log.LogService;
	
public class Facade {
	private LogService logger;
	private FileWriter fw;

	public Facade(LogService logger)
	{
	    this.logger= logger;
	}
	public String getAccessToken()
	{
		System.out.println("In facade");
		try {
			URI url = new URI("https://www.facebook.com/dialog/oauth?client_id=283202715139589"
							+ "&redirect_uri=https://morning-fjord-1741.herokuapp.com/token.php&scope=manage_friendlists"
							+ "&response_type=token"
							+ "&scope=email,user_about_me,user_activities,user_birthday,user_education_history,"
							+ "user_events,user_hometown,user_interests,friends_interests,user_likes,user_groups,friends_hometown,friends_birthday,user_location,friends_location,user_religion_politics,friends_religion_politics,friends_events,read_friendlists,friends_relationships,friends_status,friends_about_me");		
			Desktop.getDesktop().browse(url);			
		} catch (URISyntaxException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		} catch (IOException e1) {
			logger.log(LogService.LOG_INFO, e1.getMessage());
		}
		
		String input =  JOptionPane.showInputDialog("Enter Access Token:");
	     return input;
	}
	
public void writeToken(String token) throws IOException	{
		
		final JFileChooser fc = new JFileChooser();
		int userSelection = fc.showSaveDialog(null);

		File fileToSave = null;
		if (userSelection == JFileChooser.APPROVE_OPTION) {
		    fileToSave = fc.getSelectedFile();
		    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		}
		if(fileToSave == null) return;
		fw = new FileWriter(fileToSave.getAbsolutePath());
		fw.write(token);
		fw.close();
	}
}