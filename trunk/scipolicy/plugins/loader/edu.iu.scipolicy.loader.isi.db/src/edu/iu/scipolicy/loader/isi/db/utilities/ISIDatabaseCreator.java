package edu.iu.scipolicy.loader.isi.db.utilities;

import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;

public class ISIDatabaseCreator {
	public static ISIDatabase createFromModel(ISIModel model) {
		// Create an empty ISI database.

		// For each type of entity in the ISI model:
			/*
			 * Given all of the entities of the current entity type, form an SQL query that
			 *  constructs the corresponding entity table in the ISI database, and then run
			 *  that query.
			 */

		// For each entity joining table in the ISI model:
			/*
			 * Given all of the joins in the current entity join table, form an SQL query that
			 *  constructs the corresponding entity joining table in the ISI database, and then
			 *  run that query.
			 */

		// Return the now-filled ISI database.

		return null;
	}
}