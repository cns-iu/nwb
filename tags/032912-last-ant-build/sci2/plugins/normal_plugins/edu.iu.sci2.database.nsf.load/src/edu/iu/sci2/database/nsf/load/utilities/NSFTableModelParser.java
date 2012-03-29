package edu.iu.sci2.database.nsf.load.utilities;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.algorithm.AlgorithmCanceledException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.ProgressMonitorUtilities;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.osgi.logging.LogMessageHandler;
import org.osgi.service.log.LogService;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.load.framework.EntityContainer;
import edu.iu.cns.database.load.framework.RelationshipContainer;
import edu.iu.cns.database.load.framework.RowItemContainer;
import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.sci2.database.nsf.load.model.entity.Award;
import edu.iu.sci2.database.nsf.load.model.entity.FieldOfApplication;
import edu.iu.sci2.database.nsf.load.model.entity.NSFFile;
import edu.iu.sci2.database.nsf.load.model.entity.Organization;
import edu.iu.sci2.database.nsf.load.model.entity.Person;
import edu.iu.sci2.database.nsf.load.model.entity.Program;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.AwardFieldOfApplication;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.AwardOccurence;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.Investigator;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.InvestigatorOrganization;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.ProgramManager;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.ProgramNameAndElementCode;
import edu.iu.sci2.database.nsf.load.model.entity.relationship.ProgramReferenceCode;
import edu.iu.sci2.utilities.nsf.NsfCsvFieldNames;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class NSFTableModelParser {

	private LogMessageHandler logMessageHandler;
	private LogMessageHandler.MessageTypeDescriptor invalidAwardAmountType;
	public static final String ROW_WITH_INVALID_AWARDED_AMOUNT =
		"row(s) with an invalid awarded amount to date";
	public static final int ROW_WITH_INVALID_AWARDED_AMOUNT_COUNT = 5;
	
	private LogMessageHandler.MessageTypeDescriptor invalidAwardDateType;
	public static final String ROW_WITH_INVALID_AWARD_DATE =
		"row(s) with an invalid award date";
	public static final int ROW_WITH_INVALID_AWARD_DATE_COUNT = 5;
	
	/*
	 * For each type of entity (NSF File, Organization, FieldOfApplication, Program,
	 * Person and Award), create a master list of entities.
	 */
	private RowItemContainer<NSFFile> nsfFiles = new EntityContainer<NSFFile>(
		NsfDatabaseFieldNames.NSF_FILE_HUMAN_READABLE_NAME, 
		NsfDatabaseFieldNames.NSF_FILE_TABLE_NAME, 
		NSFFile.SCHEMA);

	private RowItemContainer<Organization> organizations = new EntityContainer<Organization>(
		NsfDatabaseFieldNames.ORGANIZATION_HUMAN_READABLE_NAME, 
		NsfDatabaseFieldNames.ORGANIZATION_TABLE_NAME, 
		Organization.SCHEMA);

	private RowItemContainer<FieldOfApplication> fieldOfApplications =
		new EntityContainer<FieldOfApplication>(
			NsfDatabaseFieldNames.FIELD_OF_APPLICATION_HUMAN_READABLE_NAME, 
			NsfDatabaseFieldNames.FIELD_OF_APPLICATION_TABLE_NAME, 
			FieldOfApplication.SCHEMA);

	private RowItemContainer<Program> programs = new EntityContainer<Program>(
		NsfDatabaseFieldNames.PROGRAM_HUMAN_READABLE_NAME, 
		NsfDatabaseFieldNames.PROGRAM_TABLE_NAME,
		Program.SCHEMA);

	private RowItemContainer<Person> people = new EntityContainer<Person>(
		NsfDatabaseFieldNames.PERSON_HUMAN_READABLE_NAME, 
		NsfDatabaseFieldNames.PERSON_TABLE_NAME,
		Person.SCHEMA);

	private RowItemContainer<Award> awards = new EntityContainer<Award>(
		NsfDatabaseFieldNames.AWARD_HUMAN_READABLE_NAME, 
		NsfDatabaseFieldNames.AWARD_TABLE_NAME,
		Award.SCHEMA);

	/*
	 * Create all of the entity joining tables (Field Of Applications, Investigator, 
	 *  Investigator Organizations, Program Manager, Program Name & Element Codes, 
	 *  Program References Codes and Award Occurrences).
	 */
	private RowItemContainer<InvestigatorOrganization> investigatorOrganizations =
		new RelationshipContainer<InvestigatorOrganization>(
			NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_TABLE_NAME,
			InvestigatorOrganization.SCHEMA);

	private RowItemContainer<Investigator> investigators =
		new RelationshipContainer<Investigator>(
			NsfDatabaseFieldNames.INVESTIGATOR_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.INVESTIGATOR_TABLE_NAME,
			Investigator.SCHEMA);

	private RowItemContainer<AwardFieldOfApplication> awardFieldOfApplications =
		new RelationshipContainer<AwardFieldOfApplication>(
			NsfDatabaseFieldNames.AWARD_FIELD_OF_APPLICATION_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.AWARD_FIELD_OF_APPLICATION_TABLE_NAME,
			AwardFieldOfApplication.SCHEMA);

	private RowItemContainer<ProgramManager> programManagers =
		new RelationshipContainer<ProgramManager>(
			NsfDatabaseFieldNames.PROGRAM_MANAGER_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.PROGRAM_MANAGER_TABLE_NAME,
			ProgramManager.SCHEMA);

	private RowItemContainer<ProgramNameAndElementCode> programNameAndElementCodes =
		new RelationshipContainer<ProgramNameAndElementCode>(
			NsfDatabaseFieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_TABLE_NAME,
			ProgramNameAndElementCode.SCHEMA);

	private RowItemContainer<ProgramReferenceCode> programReferenceCodes =
		new RelationshipContainer<ProgramReferenceCode>(
			NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.PROGRAM_REFERENCE_CODES_TABLE_NAME,
			ProgramReferenceCode.SCHEMA);

	private RowItemContainer<AwardOccurence> awardOccurences =
		new RelationshipContainer<AwardOccurence>(
			NsfDatabaseFieldNames.AWARD_OCCURRENCES_HUMAN_READABLE_NAME,
			NsfDatabaseFieldNames.AWARD_OCCURRENCES_TABLE_NAME,
			AwardOccurence.SCHEMA);

	/**
	 * Parse each field of the nsf file and update the nsf model as you go.
	 * 		1. Make sure that all the values are trimmed.
	 * 		2. There will be some entities that we know for sure that it can be multi-valued. 
	 * 		So handle that appropriately.
	 * 		3. Make sure that duplicate entities are handled. for e.g. in the "Organization" 
	 * 		field if you again come across already registered "Organization" than dont add it 
	 * 		into model.
	 * 			a. Try to add an exception handler on the add method of each model entity.
	 * @param columnNameToColumnIndex
	 * @param unknownColumnNameToColumnIndex
	 * @param csvReaderHandle
	 * @param nsfMetadata
	 * @param logger
	 * @return
	 * @throws IOException
	 */
	public DatabaseModel parseModel(
			CSVReader csvReaderHandle,
			NSFMetadata nsfMetadata,
			LogService logger,
			ProgressMonitor progressMonitor)
			throws AlgorithmCanceledException, IOException {
		this.logMessageHandler = new LogMessageHandler(logger);
		this.invalidAwardAmountType = logMessageHandler.addMessageType(
				ROW_WITH_INVALID_AWARDED_AMOUNT,
				ROW_WITH_INVALID_AWARDED_AMOUNT_COUNT);
		
		/*
		 * Since we are going to parse data from one file only, we only need to create 
		 * the NSF File entity only once. 
		 */
		NSFFile mergedNSFFile = parseNSFFile(nsfMetadata);

		int unitsWorked = 0;
		
		/*			
		 * Parse each field of the nsf file and update the nsf model as you go.
		 * For each row after the column headers row, create entity VO (VO = Value Object) 
		 * in the following order,
		 */  
		String[] row = null;
		
		while ((row = csvReaderHandle.readNext()) != null) {
			ProgressMonitorUtilities.handleCanceledOrPausedAlgorithm(progressMonitor);

			/*
			 * For Award VO parse following columns, Title, Start Date, 
			 * Last Amendment Date, Expiration Date, Awarded Amount to Date,
			 * Award Instrument, NSF Organization, NSF Directorate & Abstract.
			 * */
			Award award = parseAward(nsfMetadata.getColumnNameToColumnIndex(), 
									 nsfMetadata.getUnknownColumnNameToColumnIndex(), 
									 row);

			this.awardOccurences.add(new AwardOccurence(award, mergedNSFFile));
			
			/*
			 * For Person VO parse Principal Investigator column. 
			 * */
			Person principalInvestigatorPerson = 
				parsePrincipalInvestigatorPerson(nsfMetadata.getColumnNameToColumnIndex(),
						row);
			/*
			 * For Investigator VO parse PI Email Address, State Columns.
			 * Also, since it is PI set "Is Main" boolean to true for the VO.
			 * Add a reference to it's Person VO. Add a reference to it's Award VO.
			 * */
			Investigator principalInvestigator = 
				parsePrincipalInvestigator(principalInvestigatorPerson,
						award,
						nsfMetadata.getColumnNameToColumnIndex(),
						row);

			/*
			 * For Organization VO parse following columns, Organization Phone, 
			 * Organization Zip, Organization Street Address, Organization State,
			 * Organization & Organization City.
			 * */
			Organization organization = parseOrganization(
					nsfMetadata.getColumnNameToColumnIndex(), 
					row);
			/*
			 * Create a relationship VO of "Investigator - Organization" and add 
			 * reference in it of the "Investigator" &  "Organization".
			 * */
			this.investigatorOrganizations.add(
					new InvestigatorOrganization(principalInvestigator, 
							organization));
			/*
			 * Parse the "Co-PI Name(s)" column to get individual CO-PI name and 
			 * create new Person, Investigator VOs accordingly.
			 * Make sure that splitting up of the multiple values is done properly &
			 * Parsing of the value in to its individual components of Last, First 
			 * name etc is done properly.
			 * */
			List<Person> coPrinciaplInvestigatorPeople = 
				parseCOPrincipalInvestigatorPeople(nsfMetadata.getColumnNameToColumnIndex(), 
						row,
						award);

			for (Person coPrinciaplInvestigatorPerson : coPrinciaplInvestigatorPeople) {

				this.investigators.add(
						new Investigator(this.investigators.getKeyGenerator(), 
								award,
								coPrinciaplInvestigatorPerson,
								"",
								"",
								false));
			}

			/*
			 * Parse the "Program Manager" column to create new Person.
			 * Create a Program Manager VOs and, Add a reference to it's Person VO 
			 * and it's Award VO.
			 * */
			ProgramManager programManager = parseProgramManager(
					award,
					nsfMetadata.getColumnNameToColumnIndex(),
					row);
			/*
			 *  For the Field of Application VO parse Field Of Application(s) column.
			 *  Create a list which will contain references to the FOA objects.
			 *  Make sure that splitting up of the multiple values is done properly
			 *  Create a new VO for each value obtained (if not already present, that is)
			 *  If there is a duplicate just get the reference and append it to the list of FOA
			 * */
			List<FieldOfApplication> currentFieldOfApplications = 
				parseFieldOfApplications(nsfMetadata.getColumnNameToColumnIndex(),
										 row);

			/*
			 * For the FOA - Award VO for each FOA in the list do, create a new VO 
			 * having reference to both FOA & the current Award.
			 * */
			for (FieldOfApplication fieldOfApplication : currentFieldOfApplications) {
				this.awardFieldOfApplications.add(
						new AwardFieldOfApplication(fieldOfApplication, award));
			}

			/*
			 * Create "Program" entity objects form "Program(s)" & "Program Element Code(s)"
			 * columns.  
			 * */
			List<Program> programNamesAndElementCodes = parseProgramNamesAndElementCodes(
					nsfMetadata.getColumnNameToColumnIndex(),
					row);

			/*
			 * Create "Award - Program Name & Element Code" VO with references to Award & 
			 * Program VOs. 
			 * */
			for (Program programNameAndElementCode : programNamesAndElementCodes) {
				this.programNameAndElementCodes.add(
						new ProgramNameAndElementCode(programNameAndElementCode, award));
			}

			/*
			 * Similarly, parse for Program Reference Codes.
			 * */
			List<Program> programReferenceCodes = parseProgramReferenceCodes(
					nsfMetadata.getColumnNameToColumnIndex(),
					row);

			for (Program programReferenceCode : programReferenceCodes) {
				this.programReferenceCodes.add(
						new ProgramReferenceCode(programReferenceCode, award));
			}

			progressMonitor.worked(unitsWorked);
			unitsWorked++;
		}
		
		/*
		 * Print all the parse errors.
		 * */	
		this.logMessageHandler.printOverloadedMessageTypes(
				LogService.LOG_WARNING);

		/*
		 * Given all of the master lists of entities, construct an NSFModel and return it.
		 * */
		return new DatabaseModel(// Entities
								 this.nsfFiles, 
								 this.organizations, 
								 this.fieldOfApplications, 
								 this.programs, 
								 this.people, 
								 this.awards,
								 // Relationships
								 this.investigatorOrganizations, 
								 this.investigators, 
								 this.awardFieldOfApplications, 
								 this.programManagers, 
								 this.programNameAndElementCodes,
								 this.programReferenceCodes, 
								 this.awardOccurences);
	}

	/**
	 * @param nsfMetadata
	 * @return
	 */
	private NSFFile parseNSFFile(NSFMetadata nsfMetadata) {
		NSFFile nsfFile = new NSFFile(this.nsfFiles.getKeyGenerator(),
									  nsfMetadata.getFileName(),
									  nsfMetadata.getFileType(),
									  nsfMetadata.getMD5Checksum());
		
		NSFFile mergedNSFFile = this.nsfFiles.add(nsfFile);
		return mergedNSFFile;
	}


	/**
	 *  There is a direct co-relation between values in the "Program(s)" & "Program 
	 *  Element Code(s)" column. So for the Program VO parse "Program(s)" & "Program 
	 *  Element Code(s)" columns. 
	 *  	Create a key, value pair such that if "Program(s)" has values 
	 *  ['A', 'B', 'C'] & "Program Element Code(s)" has values [1, 2, 3] create a map 
	 *  so {'A':1, 'B':2, 'C':3} and for each key, value pair create a Program VO.
	 * @param columnNameToColumnIndex
	 * @param row
	 * @return
	 */
	private List<Program> parseProgramNamesAndElementCodes(
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {

		List<Program> programs = new ArrayList<Program>();

		String rawProgramNamesString = StringUtilities.simpleClean(
				row[columnNameToColumnIndex.get(NsfCsvFieldNames.CSV.PROGRAM_NAMES)]);

		String rawProgramElementCodesString = StringUtilities.simpleClean(
				row[columnNameToColumnIndex.get(NsfCsvFieldNames.CSV.PROGRAM_ELEMENT_CODES)]);

		String[] programNames = StringUtilities
									.filterEmptyStrings(rawProgramNamesString.split("\\|"));
		
		String[] programElementCodes = StringUtilities
											.filterEmptyStrings(
													rawProgramElementCodesString.split("\\|"));

		boolean isNumberOfNamesEqualToNumberOfElementCodes = 
			programNames.length == programElementCodes.length ? true : false;

		if (isNumberOfNamesEqualToNumberOfElementCodes) {

			for (int ii = 0; ii < programNames.length; ii++) {
				
				/*
				 * Empty or Whitespace cannot be considered as a proper Program Name or Element 
				 * Code. So ignore such values. We know for a fact that 
				 * */
				String programName = programNames[ii];
				String programFundingCode = programElementCodes[ii];

				Program mergedProgram = createProgram(programName,
						programFundingCode);

				programs.add(mergedProgram);

			}

		} else {

			/*
			 * This case should never happen. So pick the element with least
			 * number of elements.
			 * */
			int numberOfIterations = programNames.length > programElementCodes.length 
											? programElementCodes.length 
													: programNames.length;

			for (int ii = 0; ii < numberOfIterations; ii++) {
				String programName = programNames[ii];
				String programFundingCode = programElementCodes[ii];

				Program mergedProgram = createProgram(programName,
						programFundingCode);

				programs.add(mergedProgram);
			}
		}
		return programs;
	}


	/**
	 * @param programName
	 * @param programFundingCode
	 * @return
	 */
	private Program createProgram(String programName,
			String programFundingCode) {
		String cleanedProgramName = StringUtilities.simpleClean(programName);


		String cleanedProgramElementCode = StringUtilities.simpleClean(programFundingCode);

		Program program = new Program(this.programs.getKeyGenerator(),
									  cleanedProgramName,
									  cleanedProgramElementCode);

		Program mergedProgram = this.programs.add(program);
		return mergedProgram;
	}


	/**
	 * Since Program Element Code & Reference Code are essentially part of the same
	 * NSF Funding Code Schema while parsing the "Program Reference Code(s)" column I
	 * check if the code is already present in the "Program" funding code.
	 * If it is present then I will just get the reference to that object and create the
	 * "Award - Reference Code" VO with appropriate references.
	 * 		If not then I will create a new Program VO w/o the Name field and follow 
	 * appropriately. We know for a fact that there is a 1 - 1 correspondence between 
	 * a name & a funding code.
	 * @param columnNameToColumnIndex
	 * @param row
	 * @return
	 */
	private List<Program> parseProgramReferenceCodes(
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {

		List<Program> programs = new ArrayList<Program>();

		String rawProgramReferenceCodesString = StringUtilities.simpleClean(
				row[columnNameToColumnIndex.get(NsfCsvFieldNames.CSV.PROGRAM_REFERENCE_CODES)]);

		String[] programReferenceCodes = StringUtilities
												.filterEmptyStrings(
														rawProgramReferenceCodesString
																.split("\\|"));

		for (int ii = 0; ii < programReferenceCodes.length; ii++) {
			String programReferenceCode = programReferenceCodes[ii];

			Program mergedProgram = createProgram("",
					programReferenceCode);

			programs.add(mergedProgram);

		}

		return programs;
	}

	private List<FieldOfApplication> parseFieldOfApplications(
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {
		List<FieldOfApplication> fieldOfApplicationCandidates = 
			new ArrayList<FieldOfApplication>();

		String rawFieldOfApplicationsString = StringUtilities.simpleClean(
				row[columnNameToColumnIndex.get(NsfCsvFieldNames.CSV.FIELD_OF_APPLICATIONS)]);

		String[] fieldOfApplicationsStrings = StringUtilities
													.filterEmptyStrings(
															rawFieldOfApplicationsString
																	.split("\\|"));

		for (String fieldOfApplicationString : fieldOfApplicationsStrings) {
			
			String cleanedFOAString = StringUtilities.simpleClean(fieldOfApplicationString);

			FieldOfApplication fieldApplication = 
				FieldOfApplicationParser.parseFieldOfApplication(
						this.fieldOfApplications.getKeyGenerator(), 
						cleanedFOAString);

			FieldOfApplication mergedFOA = this.fieldOfApplications.add(fieldApplication);

			fieldOfApplicationCandidates.add(mergedFOA);
			
		}

		return fieldOfApplicationCandidates;
	}


	private ProgramManager parseProgramManager(Award award,
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {

		String cleanedProgramManagerName = StringUtilities.simpleClean(
				row[columnNameToColumnIndex
				              .get(NsfCsvFieldNames.CSV.PROGRAM_MANAGER)]);

		Person programManagerPerson = PersonParser.parsePerson(
				this.people.getKeyGenerator(), 
				cleanedProgramManagerName);

		Person mergedProgramManagerPerson = this.people.add(programManagerPerson);

		return this.programManagers.add(
				new ProgramManager(mergedProgramManagerPerson, 
						award));
	}


	private List<Person> parseCOPrincipalInvestigatorPeople(
			Map<String, Integer> columnNameToColumnIndex,
			String[] row, Award award) {

		List<Person> coPrincipalInvestigatorPeople = new ArrayList<Person>();

		String rawCOPIString = StringUtilities.simpleClean(
										row[columnNameToColumnIndex
										         .get(NsfCsvFieldNames.CSV.CO_PI_NAMES)]);
		
		String[] coPIStrings = StringUtilities.filterEmptyStrings(rawCOPIString.split("\\|"));

		for (String coPIString : coPIStrings) {
				String cleanedCOPIString = StringUtilities.simpleClean(coPIString);
				Person coPIPerson = PersonParser.parsePerson(
						this.people.getKeyGenerator(), 
						cleanedCOPIString);
				Person mergedCOPIPerson = this.people.add(coPIPerson);

				coPrincipalInvestigatorPeople.add(mergedCOPIPerson);	
		}

		return coPrincipalInvestigatorPeople;
	}


	private Organization parseOrganization(
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {

		String name = StringUtilities.simpleClean(
							row[columnNameToColumnIndex
							         .get(NsfCsvFieldNames.CSV.ORGANIZATION_NAME)]);
		
		String phone = StringUtilities.simpleClean(
							row[columnNameToColumnIndex
							         .get(NsfCsvFieldNames.CSV.ORGANIZATION_PHONE)]);
		
		String streetAddress = StringUtilities.simpleClean(
									row[columnNameToColumnIndex
									         .get(NsfCsvFieldNames.CSV.ORGANIZATION_STREET_ADDRESS)]);
		
		String city = StringUtilities.simpleClean(
							row[columnNameToColumnIndex
							         .get(NsfCsvFieldNames.CSV.ORGANIZATION_CITY)]);
		
		String state = StringUtilities.simpleClean(
							row[columnNameToColumnIndex
							         .get(NsfCsvFieldNames.CSV.ORGANIZATION_STATE)]);
		
		String zip = StringUtilities.simpleClean(
							row[columnNameToColumnIndex.get(NsfCsvFieldNames.CSV.ORGANIZATION_ZIP)]);

		Organization organization = new Organization(this.organizations.getKeyGenerator(),
				name,
				phone,
				streetAddress,
				city,
				state,
				zip);

		return this.organizations.add(organization);
	}

	private Investigator parsePrincipalInvestigator(
			Person principalInvestigatorPerson, Award award,
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {

		String emailAddress = StringUtilities.simpleClean(
									row[columnNameToColumnIndex
									         .get(NsfCsvFieldNames.CSV.PRINCIPAL_INVESTIGATOR_EMAIL)]);

		String state = StringUtilities.simpleClean(
							row[columnNameToColumnIndex
							         .get(NsfCsvFieldNames.CSV.PRINCIPAL_INVESTIGATOR_STATE)]);
		boolean isMainPI = true;

		Investigator principalInvestigator = new Investigator(this.investigators.getKeyGenerator(), 
				award,
				principalInvestigatorPerson,
				emailAddress,
				state,
				isMainPI); 

		return this.investigators.add(principalInvestigator);

	}


	private Award parseAward(Map<String, Integer> columnNameToColumnIndex,
							 Map<String, Integer> unknownColumnNameToColumnIndex,
							 String[] row) {
		
		String awardNumber = StringUtilities.simpleClean(
			row[columnNameToColumnIndex.get(NsfCsvFieldNames.CSV.AWARD_NUMBER)]);

		String title = StringUtilities.simpleClean(row[columnNameToColumnIndex
		                                                 .get(NsfCsvFieldNames.CSV.AWARD_TITLE)]);

		String rawStartDate = StringUtilities.simpleClean(
									row[columnNameToColumnIndex
									      .get(NsfCsvFieldNames.CSV.AWARD_START_DATE)]);
		
		Date startDate = parseDate(awardNumber, rawStartDate, NsfCsvFieldNames.CSV.AWARD_START_DATE);	  

		String rawExpirationDate = StringUtilities.simpleClean(
											row[columnNameToColumnIndex
											        .get(NsfCsvFieldNames.CSV.AWARD_EXPIRATION_DATE)]);
		
		Date expirationDate = parseDate(awardNumber, 
										rawExpirationDate, 
										NsfCsvFieldNames.CSV.AWARD_EXPIRATION_DATE);

		String rawLastAmendmentDate = StringUtilities.simpleClean(
											row[columnNameToColumnIndex
											     .get(NsfCsvFieldNames.CSV.AWARD_LAST_AMENDMENT_DATE)]);
		
		Date lastAmendmentDate = parseDate(awardNumber, 
											rawLastAmendmentDate, 
											NsfCsvFieldNames.CSV.AWARD_LAST_AMENDMENT_DATE);         

		String rawAwardedAmountToDate = StringUtilities.simpleClean(
												row[columnNameToColumnIndex
												        .get(NsfCsvFieldNames.CSV.AWARDED_AMOUNT_TO_DATE)]);
		double awardedAmountToDate = -1;
		try {
			awardedAmountToDate = Double.parseDouble(rawAwardedAmountToDate);
		} catch (NumberFormatException e) {
			String logMessage = "Error parsing \"Awarded Amount To Date\" field " 
				 					+ "having value \"" + rawAwardedAmountToDate 
				 					+ "\" for Award number \"" + awardNumber + "\"";
			this.logMessageHandler.handleMessage(
					this.invalidAwardAmountType,
					LogService.LOG_WARNING,
					logMessage);
		}

		String awardInstrument = StringUtilities.simpleClean(
				row[columnNameToColumnIndex
				              .get(NsfCsvFieldNames.CSV.AWARD_INSTRUMENT)]);

		String nSFDirectorate = StringUtilities.simpleClean(
				row[columnNameToColumnIndex
				              .get(NsfCsvFieldNames.CSV.NSF_DIRECTORATE)]);

		String nSFOrganization = StringUtilities.simpleClean(
				row[columnNameToColumnIndex
				              .get(NsfCsvFieldNames.CSV.NSF_ORGANIZATION)]); 


		int abstractTextIndex = columnNameToColumnIndex
		              .get(NsfCsvFieldNames.CSV.ABSTRACT).intValue();
		
		String abstractText = StringUtilities.simpleClean(
				row[abstractTextIndex]);			   

		/*
		 * Merge all the columns right to the abstract column to offset all csv corruption
		 * that leads to "Abstract" being broken into multiple columns.
		 * */
		if (row.length > NsfCsvFieldNames.CSV.DEFAULT_TOTAL_NSF_FIELDS) {
			for (int ii = abstractTextIndex + 1; ii < row.length; ii++) {
				abstractText += row[ii];
			}
		}

		Award award = new Award(this.awards.getKeyGenerator(), 
				awardNumber,
				title,
				startDate,
				rawStartDate,
				expirationDate,
				rawExpirationDate,
				lastAmendmentDate,
				rawLastAmendmentDate,
				awardedAmountToDate,
				rawAwardedAmountToDate,
				awardInstrument,
				nSFDirectorate,
				nSFOrganization,
				abstractText);
		
		/*
		 * Add arbitrary columns to the Award object as is.
		 * */
		for (Entry<String, Integer> unknownColumnEntry 
					: unknownColumnNameToColumnIndex.entrySet()) {
			
			award.addArbitraryColumn(unknownColumnEntry.getKey(), 
									 StringUtilities.simpleClean(
											 row[unknownColumnEntry.getValue()]));
		}
		
		return this.awards.add(award);
	}


	/**
	 * @param columnNameToColumnIndex
	 */
	private Person parsePrincipalInvestigatorPerson(
			Map<String, Integer> columnNameToColumnIndex,
			String[] row) {

		String piPersonName = row[columnNameToColumnIndex
		                                    .get(NsfCsvFieldNames.CSV.PRINCIPAL_INVESTIGATOR_NAME)];

		String cleanedPersonName = StringUtilities.simpleClean(piPersonName);

		Person person = PersonParser.parsePerson(
				this.people.getKeyGenerator(), 
				cleanedPersonName);

		return this.people.add(person);
	}


	private Date parseDate(String awardNumber, String dateString, String awardDateType) {
		try {
			java.util.Date standardDate = DateUtilities.parseDate(dateString, false);
			return new java.sql.Date(standardDate.getTime());
		} catch (ParseException parseDateException) {
			String logMessage = "Error parsing \"" + awardDateType + "\"" 
							   	+ " having value \"" + dateString 
							   	+ "\" for Award number \"" + awardNumber + "\"";
			
			this.logMessageHandler.handleMessage(this.invalidAwardDateType,
											  LogService.LOG_WARNING,
											  logMessage);
			return null;
		}
	}
}