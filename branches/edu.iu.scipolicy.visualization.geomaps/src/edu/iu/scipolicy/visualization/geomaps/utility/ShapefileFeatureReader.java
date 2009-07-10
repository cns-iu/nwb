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
import org.osgi.service.log.LogService;

import edu.iu.scipolicy.visualization.geomaps.GeoMapsAlgorithm;

// Boxes up the ugly GeoTools idiom to get the Features from a shapefile.
public class ShapefileFeatureReader {

	private FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;

	public ShapefileFeatureReader(URL shapefileURL) throws AlgorithmExecutionException {
		featureSource = getFeatureSource(shapefileURL);
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

	private FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(URL shapefileURL) throws AlgorithmExecutionException {
		DataStore dataStore = getDataStore(shapefileURL);

		String[] typeNames;
		String typeName;
		FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
		try {
			typeNames = dataStore.getTypeNames();
			typeName = typeNames[0]; // TODO: Give readers a hint about what is
			// going on with this
			GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Reading content: " + typeName);
			featureSource = dataStore.getFeatureSource(typeName);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}

		return featureSource;
	}

	private DataStore getDataStore(URL shapefileURL) throws AlgorithmExecutionException {
		Map<String, Serializable> connectParameters = new HashMap<String, Serializable>();

		connectParameters.put("url", shapefileURL);
		connectParameters.put("create spatial index", true);

		DataStore dataStore;
		try {
			dataStore = DataStoreFinder.getDataStore(connectParameters);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}

		return dataStore;
	}
}
