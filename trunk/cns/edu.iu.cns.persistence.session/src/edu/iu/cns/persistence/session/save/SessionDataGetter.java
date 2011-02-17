package edu.iu.cns.persistence.session.save;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.Pair;
import org.osgi.service.log.LogService;

public class SessionDataGetter {
	private Data[] data;

	public SessionDataGetter(LogService logger, DataManagerService dataManager) {
		this.data = getAndValidateData(logger, dataManager);

		if (this.data == null) {
			return;
		}

		Collection<Data> allData = Arrays.asList(this.data);
		Collection<Data> sortedData = sortDataByDataManagerView(allData);
		System.err.println("sortedData: " + sortedData);
		this.data = sortedData.toArray(new Data[0]);
	}

	public Data[] getData() {
		return this.data;
	}

	// 1
	private Data[] getAndValidateData(LogService logger, DataManagerService dataManager) {
		Data[] allData = dataManager.getAllData();

		if (allData.length == 0) {
			String logMessage = "There is no data to save to a session. Ignoring operation.";
			logger.log(LogService.LOG_WARNING, logMessage);

			return null;
		}

		return allData;
	}

	// 1
	private Collection<Data> sortDataByDataManagerView(Collection<Data> dataToSort) {
		Pair<Collection<Data>, Collection<Data>> parentlessAndParentedData =
			findParentlessAndParentedData(dataToSort);
		Collection<Data> parentlessData = parentlessAndParentedData.getFirstObject();
		Collection<Data> dataWithParent = parentlessAndParentedData.getSecondObject();

		if (dataWithParent.size() == 0) {
			return parentlessData;
		} else {
			Collection<Data> sortedData = new ArrayList<Data>();

			for (Data rootDatum : parentlessData) {
				sortedData.add(rootDatum);
				Collection<Data> ancestorData = gatherAncestorData(rootDatum, dataWithParent);
				sortedData.addAll(sortDataByDataManagerView(ancestorData));
			}

			return sortedData;
		}
	}

	// 2
	private Pair<Collection<Data>, Collection<Data>> findParentlessAndParentedData(
			Collection<Data> dataToSort) {
		Collection<Data> parentlessData = new ArrayList<Data>();

		for (Data datum : dataToSort) {
			Dictionary<String, Object> metadata = datum.getMetadata();

			if (!dataToSort.contains(metadata.get(DataProperty.PARENT))) {
//			if (metadata.get(DataProperty.PARENT) == null) {
				parentlessData.add(datum);
			}
		}

		Collection<Data> dataWithParent = new ArrayList<Data>(dataToSort);
		dataWithParent.removeAll(parentlessData);

		return new Pair<Collection<Data>, Collection<Data>>(parentlessData, dataWithParent);
	}

	// 2
	private Collection<Data> gatherAncestorData(Data rootDatum, Collection<Data> dataToSort) {
		Collection<Data> ancestorData = new ArrayList<Data>();

		for (Data nonRootDatum : dataToSort) {
			Dictionary<String, Object> metadata = nonRootDatum.getMetadata();

			if (metadata.get(DataProperty.PARENT) == rootDatum) {
				ancestorData.add(nonRootDatum);
				ancestorData.addAll(gatherAncestorData(nonRootDatum, dataToSort));
			}
		}

		return ancestorData;
	}
}