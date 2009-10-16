package edu.iu.scipolicy.visualization.geomaps.utility;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

// Boxes up the ugly GeoTools idiom to get the Features from a shapefile.
public class ShapefileFeatureReader {

	private FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;

	public ShapefileFeatureReader(URL shapefileURL) throws AlgorithmExecutionException {
		this.featureSource = getFeatureSource(shapefileURL);
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatureCollection() throws AlgorithmExecutionException {
		FeatureCollection<SimpleFeatureType, SimpleFeature> featureCollection = null;

		try {
			featureCollection = featureSource.getFeatures();
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}

		return featureCollection;
	}

	/* GeoTools boilerplate.
	 * Returns the Features in the shapefile at shapefileURL
	 */
	private FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(URL shapefileURL)
			throws AlgorithmExecutionException {
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
		
		try {
			DataStore dataStore = getDataStore(shapefileURL);
			
			String[] typeNames = dataStore.getTypeNames();
			String typeName = typeNames[0];
			
			featureSource = dataStore.getFeatureSource(typeName);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error accessing shapefile: " + e.getMessage(),
					e);
		}

		return featureSource;
	}

	private DataStore getDataStore(URL shapefileURL) throws IOException {
		Map<String, Serializable> connectParameters =
			new HashMap<String, Serializable>();

		connectParameters.put("url", shapefileURL);
		connectParameters.put("create spatial index", true);

		return DataStoreFinder.getDataStore(connectParameters);
	}
}
