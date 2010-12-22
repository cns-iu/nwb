package edu.iu.sci2.database.isi.load.utilities.parser.test.entity;


import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.load.model.entity.Address;
import edu.iu.sci2.database.isi.load.model.entity.Document;
import edu.iu.sci2.database.isi.load.model.entity.Publisher;
import edu.iu.sci2.database.isi.load.model.entity.relationship.PublisherAddress;
import edu.iu.sci2.database.isi.load.model.entity.relationship.ReprintAddress;
import edu.iu.sci2.database.isi.load.model.entity.relationship.ResearchAddress;
import edu.iu.sci2.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.relationship.PublisherAddressesTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.relationship.ReprintAddressesTest;
import edu.iu.sci2.database.isi.load.utilities.parser.test.relationship.ResearchAddressesTest;

public class AddressTest extends RowItemTest {
	// Test data.
	public static final String ONE_OF_EACH_ADDRESS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "OneAddressOfEach.isi";
	public static final String MULTIPLE_RESEARCH_ADDRESSES_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleResearchAddresses.isi";

	public static final String DOCUMENT_TITLE =
		"Planarians Maintain a Constant Ratio of Different Cell Types During Changes in Body " +
		"Size by Using the Stem Cell System";

	public static final String PUBLISHER_ADDRESS_CITY = "";
	public static final String PUBLISHER_ADDRESS_COUNTRY = "";
	public static final String PUBLISHER_ADDRESS_POSTAL_CODE = "";
	public static final String PUBLISHER_ADDRESS_RAW_STRING =
		"TOSHIN-BUILDING, HONGO 2-27-2, BUNKYO-KU, TOKYO, 113-0033, JAPAN";
	public static final String PUBLISHER_ADDRESS_STATE_OR_PROVINCE = "";
	public static final String PUBLISHER_ADDRESS_STREET_ADDRESS = "";

	public static final String PUBLISHER_NAME = "ZOOLOGICAL SOC JAPAN";

	public static final String FIRST_RESEARCH_ADDRESS_CITY = "";
	public static final String FIRST_RESEARCH_ADDRESS_COUNTRY = "";
	public static final String FIRST_RESEARCH_ADDRESS_POSTAL_CODE = "";
	public static final String FIRST_RESEARCH_ADDRESS_RAW_STRING =
		"[Takeda, Hiroyuki; Nishimura, Kaneyasu; Agata, Kiyokazu] Kyoto Univ, Dept Biophys, " +
		"Grad Sch Sci, Sakyo Ku, Kyoto 6068502, Japan.";
	public static final String FIRST_RESEARCH_ADDRESS_STATE_OR_PROVINCE = "";
	public static final String FIRST_RESEARCH_ADDRESS_STREET_ADDRESS = "";

	public static final String SECOND_RESEARCH_ADDRESS_CITY = "";
	public static final String SECOND_RESEARCH_ADDRESS_COUNTRY = "";
	public static final String SECOND_RESEARCH_ADDRESS_POSTAL_CODE = "";
	public static final String SECOND_RESEARCH_ADDRESS_RAW_STRING =
		"[OtherAuthor1, One; OtherAuthor2, Two] Indiana University, SLIS, Something, " +
		"Bloomington, IN  47404, USA.";
	public static final String SECOND_RESEARCH_ADDRESS_STATE_OR_PROVINCE = "";
	public static final String SECOND_RESEARCH_ADDRESS_STREET_ADDRESS = "";

	public static final String FIRST_REPRINT_ADDRESS_CITY = "";
	public static final String FIRST_REPRINT_ADDRESS_COUNTRY = "";
	public static final String FIRST_REPRINT_ADDRESS_POSTAL_CODE = "";
	public static final String FIRST_REPRINT_ADDRESS_RAW_STRING =
		"Agata, K, Kyoto Univ, Dept Biophys, Grad Sch Sci, Sakyo Ku, Kyoto 6068502, Japan.";
	public static final String FIRST_REPRINT_ADDRESS_STATE_OR_PROVINCE = "";
	public static final String FIRST_REPRINT_ADDRESS_STREET_ADDRESS = "";
	
	@Test
	public void testZeroAddressesGetParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<Address> addresses = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ADDRESS_TABLE_NAME);

		checkItemContainerValidity(addresses, "addresses");
		checkItemCount(addresses, 0);
	}

	@Test
	public void testAddressOfEachGetsParsed() throws Exception {
		DatabaseModel model = parseTestData(ONE_OF_EACH_ADDRESS_TEST_DATA_PATH);
		RowItemContainer<Address> addresses = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ADDRESS_TABLE_NAME);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Publisher> publishers = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PUBLISHER_TABLE_NAME);
		RowItemContainer<PublisherAddress> publisherAddresses =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.PUBLISHER_ADDRESSES_TABLE_NAME);
		RowItemContainer<ResearchAddress> researchAddresses =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.RESEARCH_ADDRESSES_TABLE_NAME);
		RowItemContainer<ReprintAddress> reprintAddresses =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.REPRINT_ADDRESSES_TABLE_NAME);

		checkItemContainerValidity(addresses, "addresses");
		checkItemCount(addresses, 3);
		checkItemContainerValidity(publisherAddresses, "publisher addresses");
		checkItemCount(publisherAddresses, 1);
		checkItemContainerValidity(researchAddresses, "research addresses");
		checkItemCount(researchAddresses, 1);
		checkItemContainerValidity(reprintAddresses, "reprint addresses");
		checkItemCount(reprintAddresses, 1);

		Document document = getDocumentForTheseTests(documents);
		Publisher publisher = getPublisherForTheseTests(publishers);
		PublisherAddress publisherAddress =
			getPublisherAddress(addresses, publisherAddresses, publisher);
		ResearchAddress firstResearchAddress =
			getFirstResearchAddress(addresses, researchAddresses, document);
		ReprintAddress firstReprintAddress =
			getFirstReprintAddress(addresses, reprintAddresses, document);

		checkPublisherAddress(publisherAddress);
		checkResearchAddress(firstResearchAddress);
		checkReprintAddress(firstReprintAddress);
	}

	@Test
	public void testMultipleResearchAddressesGetParsed() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_RESEARCH_ADDRESSES_TEST_DATA_PATH);
		RowItemContainer<Address> addresses = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.ADDRESS_TABLE_NAME);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<ResearchAddress> researchAddresses =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.RESEARCH_ADDRESSES_TABLE_NAME);
		RowItemContainer<ReprintAddress> reprintAddresses =
			model.getRowItemListOfTypeByDatabaseTableName(
				ISI.REPRINT_ADDRESSES_TABLE_NAME);

		checkItemContainerValidity(addresses, "addresses");
		checkItemCount(addresses, 4);
		checkItemContainerValidity(researchAddresses, "research addresses");
		checkItemCount(researchAddresses, 2);
		checkItemContainerValidity(reprintAddresses, "reprint addresses");
		checkItemCount(reprintAddresses, 1);

		Document document = getDocumentForTheseTests(documents);
		ResearchAddress firstResearchAddress =
			getFirstResearchAddress(addresses, researchAddresses, document);
		ResearchAddress secondResearchAddress =
			getSecondResearchAddress(addresses, researchAddresses, document);
		/*ReprintAddress firstReprintAddress =
			getFirstReprintAddress(addresses, reprintAddresses, document);
		ReprintAddress firstReprintAddress =
			getFirstReprintAddress(addresses, reprintAddresses, document);*/

		checkResearchAddress(firstResearchAddress);
		checkResearchAddress(secondResearchAddress);
		//checkReprintAddress(firstReprintAddress);
	}

	public static Address getAddress(
			Collection<Address> addresses,
			String city,
			String country,
			String postalCode,
			String rawAddress,
			String stateOrProvince,
			String streetAddress) {
		for (Address address : addresses) {
			try {
				checkAddress(
					address,
					city,
					country,
					postalCode,
					rawAddress,
					stateOrProvince,
					streetAddress);

				return address;
			} catch (Throwable e) {
			}
		}

		return null;
	}

	public static void verifyAddressExists(
			Collection<Address> addresses,
			String city,
			String country,
			String postalCode,
			String rawAddress,
			String stateOrProvince,
			String streetAddress) throws Exception {
		if (getAddress(
				addresses,
				city,
				country,
				postalCode,
				rawAddress,
				stateOrProvince,
				streetAddress) == null) {
			String exceptionMessage =
				"No address with the following specifications were found:" +
				"\n\tCity: " + city +
				"\n\tCountry: " + country +
				"\n\tPostal code: " + postalCode +
				"\n\tRaw address: " + rawAddress +
				"\n\tState or province: " + stateOrProvince +
				"\n\tStreet address: " + streetAddress;
			throw new Exception(exceptionMessage);
		}
	}

	public static void checkAddress(
			Address address,
			String city,
			String country,
			String postalCode,
			String rawAddress,
			String stateOrProvince,
			String streetAddress) {
		if (address == null) {
			String failMessage =
				"An exception should have been thrown " +
				"if the result Address would have ended up null.";
			fail(failMessage);
		}

		compareProperty("City", address.getCity(), city);
		compareProperty("Country", address.getCountry(), country);
		compareProperty("Postal Code", address.getPostalCode(), postalCode);
		compareProperty("rawAddress", address.getRawAddress(), rawAddress);
		compareProperty("State or Province", address.getStateOrProvince(), stateOrProvince);
		compareProperty("streetAddress", address.getStreetAddress(), streetAddress);
	}

	private static Document getDocumentForTheseTests(RowItemContainer<Document> documents) {
		return DocumentTest.getDocument(documents.getItems(), DOCUMENT_TITLE);
	}

	private static Publisher getPublisherForTheseTests(RowItemContainer<Publisher> publishers) {
		return PublisherTest.getPublisher(publishers.getItems(), PUBLISHER_NAME);
	}

	private static PublisherAddress getPublisherAddress(
			RowItemContainer<Address> addresses,
			RowItemContainer<PublisherAddress> publisherAddresses,
			Publisher publisher) {
		Address address = getAddress(
			addresses.getItems(),
			PUBLISHER_ADDRESS_CITY,
			PUBLISHER_ADDRESS_COUNTRY,
			PUBLISHER_ADDRESS_POSTAL_CODE,
			PUBLISHER_ADDRESS_RAW_STRING,
			PUBLISHER_ADDRESS_STATE_OR_PROVINCE,
			PUBLISHER_ADDRESS_STREET_ADDRESS);

		return PublisherAddressesTest.getPublisherAddress(
			publisherAddresses.getItems(), publisher, address);
	}

	private static ResearchAddress getFirstResearchAddress(
			RowItemContainer<Address> addresses,
			RowItemContainer<ResearchAddress> researchAddresses,
			Document document) {
		Address address = getAddress(
			addresses.getItems(),
			FIRST_RESEARCH_ADDRESS_CITY,
			FIRST_RESEARCH_ADDRESS_COUNTRY,
			FIRST_RESEARCH_ADDRESS_POSTAL_CODE,
			FIRST_RESEARCH_ADDRESS_RAW_STRING,
			FIRST_RESEARCH_ADDRESS_STATE_OR_PROVINCE,
			FIRST_RESEARCH_ADDRESS_STREET_ADDRESS);

		return ResearchAddressesTest.getResearchAddress(
			researchAddresses.getItems(), document, address);
	}

	private static ResearchAddress getSecondResearchAddress(
			RowItemContainer<Address> addresses,
			RowItemContainer<ResearchAddress> researchAddresses,
			Document document) {
		Address address = getAddress(
			addresses.getItems(),
			FIRST_RESEARCH_ADDRESS_CITY,
			FIRST_RESEARCH_ADDRESS_COUNTRY,
			FIRST_RESEARCH_ADDRESS_POSTAL_CODE,
			FIRST_RESEARCH_ADDRESS_RAW_STRING,
			FIRST_RESEARCH_ADDRESS_STATE_OR_PROVINCE,
			FIRST_RESEARCH_ADDRESS_STREET_ADDRESS);

		return ResearchAddressesTest.getResearchAddress(
			researchAddresses.getItems(), document, address);
	}

	private static ReprintAddress getFirstReprintAddress(
			RowItemContainer<Address> addresses,
			RowItemContainer<ReprintAddress> reprintAddresses,
			Document document) {
		Address address = getAddress(
			addresses.getItems(),
			FIRST_REPRINT_ADDRESS_CITY,
			FIRST_REPRINT_ADDRESS_COUNTRY,
			FIRST_REPRINT_ADDRESS_POSTAL_CODE,
			FIRST_REPRINT_ADDRESS_RAW_STRING,
			FIRST_REPRINT_ADDRESS_STATE_OR_PROVINCE,
			FIRST_REPRINT_ADDRESS_STREET_ADDRESS);

		return ReprintAddressesTest.getReprintAddress(
			reprintAddresses.getItems(), document, address);
	}

	private static void checkPublisherAddress(PublisherAddress publisherAddress) {
		if (publisherAddress == null) {
			fail("Publisher address is null.");
		}

		if (publisherAddress.getPublisher() == null) {
			fail("Publisher in PublisherAddress is null.");
		}

		if (publisherAddress.getAddress() == null) {
			fail("Address in PublisherAddress is null.");
		}
	}

	private static void checkResearchAddress(ResearchAddress researchAddress) {
		if (researchAddress == null) {
			fail("Research address is null.");
		}

		if (researchAddress.getDocument() == null) {
			fail("Document in ResearchAddress is null.");
		}

		if (researchAddress.getAddress() == null) {
			fail("Address in ResearchAddress is null.");
		}
	}

	private static void checkReprintAddress(ReprintAddress reprintAddress) {
		if (reprintAddress == null) {
			fail("Reprint address is null.");
		}

		if (reprintAddress.getDocument() == null) {
			fail("Document in ReprintAddress is null.");
		}

		if (reprintAddress.getAddress() == null) {
			fail("Address in ReprintAddress is null.");
		}
	}
}
