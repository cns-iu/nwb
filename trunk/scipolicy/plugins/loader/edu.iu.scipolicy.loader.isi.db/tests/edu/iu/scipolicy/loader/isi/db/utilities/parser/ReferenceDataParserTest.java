package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import org.junit.After;
import org.junit.Before;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;

public class ReferenceDataParserTest {
	public static final String ZERO_REFERENCE_TOKENS_STRING = "";
	public static final String ONE_REFERENCE_TOKEN_STRING = "token1";
	public static final String SIX_REFERENCE_TOKENS_STRING =
		"token1,token2,token3,token4,token5,token6";

	private DatabaseTableKeyGenerator personKeyGenerator;
	private DatabaseTableKeyGenerator sourceKeyGenerator;

	@Before
	public void setUp() throws Exception {
		this.personKeyGenerator = new DatabaseTableKeyGenerator();
		this.sourceKeyGenerator = new DatabaseTableKeyGenerator();
	}

	@After
	public void tearDown() throws Exception {
	}

	protected ReferenceDataParser runTest(String rawString) throws ReferenceParsingException {
		return new ReferenceDataParser(
			this.personKeyGenerator, this.sourceKeyGenerator, rawString);
	}
}
