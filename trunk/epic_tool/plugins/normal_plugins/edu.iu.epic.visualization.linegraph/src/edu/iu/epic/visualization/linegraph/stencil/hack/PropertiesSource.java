package edu.iu.epic.visualization.linegraph.stencil.hack;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.cishell.utilities.FileUtilities;

public interface PropertiesSource {
	public Properties loadProperties() throws LoadPropertiesException;
	
	public class InputStreamPropertiesSource implements PropertiesSource {
		private InputStream propertiesInputStream;
		
		public InputStreamPropertiesSource(InputStream propertiesInputStream) {
			this.propertiesInputStream = propertiesInputStream;
		}
		
		public Properties loadProperties() throws LoadPropertiesException {
			Properties properties = new Properties();
			try {
				String s = FileUtilities.readEntireInputStream(this.propertiesInputStream);
				this.propertiesInputStream = new StringBufferInputStream(s);
				//System.err.println(s);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
			
			try {
				properties.loadFromXML(propertiesInputStream);
			} catch (InvalidPropertiesFormatException invalidPropertiesException) {
				throw new LoadPropertiesException(
					invalidPropertiesException.getMessage(), invalidPropertiesException);
			} catch (IOException ioException) {
				throw new LoadPropertiesException(ioException.getMessage(), ioException);
			}
			
			return properties;
		}
	}
	
	public class FileNamePropertiesSource extends InputStreamPropertiesSource {
		public FileNamePropertiesSource(URL baseURL, String fileName)
				throws MalformedURLException, IOException {
			this(new URL(baseURL, fileName));
		}
		
		public FileNamePropertiesSource(URL fullURL)
				throws MalformedURLException, IOException {
			super(fullURL.openStream());
		}
	}
	
	public class LoadPropertiesException extends Exception {
		public LoadPropertiesException(String message, Exception exception) {
			super(message, exception);
		}

		public LoadPropertiesException(Exception exception) {
			super(exception);
		}
	}
}