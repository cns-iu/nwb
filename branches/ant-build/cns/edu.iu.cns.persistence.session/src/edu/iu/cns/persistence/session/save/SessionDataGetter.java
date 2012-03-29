package edu.iu.cns.persistence.session.save;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;

import org.cishell.app.service.datamanager.DataManagerService;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;

public class SessionDataGetter {
	private Data[] data;
	private DataManagerService dataManager;

	public SessionDataGetter(DataManagerService dataManager) {
		this.dataManager = dataManager;
	}

	public Data[] readData() {
		if (data != null) {
			return this.data;
		}
		
		this.data = dataManager.getAllData();
		Collection<Data> allData = Arrays.asList(this.data);
		Collection<Data> sortedData = sortDataByDataManagerView(allData);
		this.data = sortedData.toArray(new Data[0]);
		
		return data;
	}

	// 1
	/* TODO: Even though this has been tested, this is a potential source of bugs. Note the
	 * possibility of infinite loops and infinite loops and general incorrectness.
	 */
	private Collection<Data> sortDataByDataManagerView(Collection<Data> dataToSort) {
		Collection<Data> parentlessData = findParentlessData(dataToSort);
		Collection<Data> parentedData = findParentedData(dataToSort);

		if (parentedData.isEmpty()) {
			return parentlessData;
		} else {
			Collection<Data> sortedData = new ArrayList<Data>();

			for (Data rootDatum : parentlessData) {
				sortedData.add(rootDatum);
				Collection<Data> descendantData = gatherDescendantData(rootDatum, parentedData);
				sortedData.addAll(sortDataByDataManagerView(descendantData));
			}

			return sortedData;
		}
	}
	
	private Collection<Data> findParentedData(Collection<Data> data) {
		return Collections2.filter(data, new HasParentPredicate(data));
	}
	
	private Collection<Data> findParentlessData(Collection<Data> data) {
		return Collections2.filter(data, Predicates.not(new HasParentPredicate(data)));
	}

	// 2
	/* TODO: Even though this has been tested, this is a potential source of bugs. Note the
	 * possibility of infinite loops and infinite loops and general incorrectness.
	 */
	private Collection<Data> gatherDescendantData(Data rootDatum, Collection<Data> parentedData) {
		Collection<Data> descendantData = new ArrayList<Data>();

		for (Data parentedDatum : parentedData) {
			Dictionary<String, Object> metadata = parentedDatum.getMetadata();

			// NOTE: The (parentedDatum != rootDatum) is to prevent data from parenting itself.
			if ((metadata.get(DataProperty.PARENT) == rootDatum) && (parentedDatum != rootDatum)) {
				Data childDatum = parentedDatum;
			
				descendantData.add(childDatum);
				descendantData.addAll(gatherDescendantData(childDatum, parentedData));
			}
		}

		return descendantData;
	}
	
	private static class HasParentPredicate implements Predicate<Data> {
		private Collection<Data> collection;

		public HasParentPredicate(Collection<Data> collection) {
			this.collection = collection;
		}

		public boolean apply(Data data){
			return collection.contains(data.getMetadata().get(DataProperty.PARENT));
		}
	}
}