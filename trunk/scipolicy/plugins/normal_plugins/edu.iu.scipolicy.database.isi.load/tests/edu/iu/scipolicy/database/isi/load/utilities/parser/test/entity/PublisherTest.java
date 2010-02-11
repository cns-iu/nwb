package edu.iu.scipolicy.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.iu.scipolicy.database.isi.load.model.entity.Publisher;
import edu.iu.scipolicy.database.isi.load.model.entity.Source;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;

public class PublisherTest extends RowItemTest {
	@Test
	public void test() {
	}

	public static void checkPublisher(
			Publisher publisher, String city, String name, Source source, String webAddress) {
		if (publisher == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Publisher would have ended up null.";
			fail(failMessage);
		}

		compareProperty("City", publisher.getCity(), city);
		compareProperty("Name", publisher.getName(), name);
		compareProperty("Source", publisher.getSource().getPrimaryKey(), source.getPrimaryKey());
		compareProperty("Web Address", publisher.getWebAddress(), webAddress);
	}

	public static Publisher getPublisher(
			List<Publisher> publishers,
			String city,
			String name,
			Source source,
			String webAddress) {
		for (Publisher publisher : publishers) {
			try {
				checkPublisher(publisher, city, name, source, webAddress);
			} catch (Throwable e) {
			}
		}

		return null;
	}

	public static Publisher getPublisher(Collection<Publisher> publishers, String name) {
		for (Publisher publisher : publishers) {
			if (publisher.getName().equals(name)) {
				return publisher;
			}
		}

		return null;
	}
}
