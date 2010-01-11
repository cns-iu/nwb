package edu.iu.scipolicy.loader.nsf.db.utilities;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.StringUtilities;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.FieldOfApplication;
import edu.iu.scipolicy.loader.nsf.db.model.entity.NSFFile;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Organization;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Person;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Program;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.AwardOccurences;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.FieldOfApplications;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.Investigator;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.InvestigatorOrganizations;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.ProgramManager;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.ProgramNameAndElementCodes;
import edu.iu.scipolicy.loader.nsf.db.model.entity.relationship.ProgramReferenceCodes;
import edu.iu.scipolicy.utilities.nsf.NsfNames;

public class NSFTableModelParser {

	/*
	 * For each type of entity (NSF File, Organization, FieldOfApplication, Program,
	 * Person and Award), create a master list of entities.
	 */
	private RowItemContainer<NSFFile> nsfFiles = new RowItemContainer<NSFFile>(
													NSFDatabase.NSF_FILE_HUMAN_READABLE_NAME, 
													NSFDatabase.NSF_FILE_TABLE_NAME, 
													NSFFile.SCHEMA);

	private RowItemContainer<Organization> organizations = new RowItemContainer<Organization>(
													NSFDatabase.ORGANIZATION_HUMAN_READABLE_NAME, 
													NSFDatabase.ORGANIZATION_TABLE_NAME, 
													Organization.SCHEMA);

	private RowItemContainer<FieldOfApplication> fieldOfApplications = new RowItemContainer<FieldOfApplication>(
													NSFDatabase.FIELD_OF_APPLICATION_HUMAN_READABLE_NAME, 
													NSFDatabase.FIELD_OF_APPLICATION_TABLE_NAME, 
													FieldOfApplication.SCHEMA);

	private RowItemContainer<Program> programs = new RowItemContainer<Program>(
													NSFDatabase.PROGRAM_HUMAN_READABLE_NAME, 
													NSFDatabase.PROGRAM_TABLE_NAME,
													Program.SCHEMA);

	private RowItemContainer<Person> people = new RowItemContainer<Person>(
													NSFDatabase.PERSON_HUMAN_READABLE_NAME, 
													NSFDatabase.PERSON_TABLE_NAME,
													Person.SCHEMA);

	private RowItemContainer<Award> awards = new RowItemContainer<Award>(
													NSFDatabase.AWARD_HUMAN_READABLE_NAME, 
													NSFDatabase.AWARD_TABLE_NAME,
													Award.SCHEMA);

	/*
	 * Create all of the entity joining tables (Field Of Applications, Investigator, 
	 *  Investigator Organizations, Program Manager, Program Name & Element Codes, 
	 *  Program References Codes and Award Occurrences).
	 */
	private RowItemContainer<InvestigatorOrganizations> investigatorOrganizations =
		new RowItemContainer<InvestigatorOrganizations>(
				NSFDatabase.INVESTIGATOR_ORGANIZATIONS_HUMAN_READABLE_NAME,
				NSFDatabase.INVESTIGATOR_ORGANIZATIONS_TABLE_NAME,
				InvestigatorOrganizations.SCHEMA);

	private RowItemContainer<Investigator> investigators =
		new RowItemContainer<Investigator>(
				NSFDatabase.INVESTIGATOR_HUMAN_READABLE_NAME,
				NSFDatabase.INVESTIGATOR_TABLE_NAME,
				Investigator.SCHEMA);

	private RowItemContainer<FieldOfApplications> awardFieldOfApplications =
		new RowItemContainer<FieldOfApplications>(
				NSFDatabase.AWARD_FIELD_OF_APPLICATION_HUMAN_READABLE_NAME,
				NSFDatabase.AWARD_FIELD_OF_APPLICATION_TABLE_NAME,
				FieldOfApplications.SCHEMA);

	private RowItemContainer<ProgramManager> programManagers =
		new RowItemContainer<ProgramManager>(
				NSFDatabase.PROGRAM_MANAGER_HUMAN_READABLE_NAME,
				NSFDatabase.PROGRAM_MANAGER_TABLE_NAME,
				ProgramManager.SCHEMA);

	private RowItemContainer<ProgramNameAndElementCodes> programNameAndElementCodes =
		new RowItemContainer<ProgramNameAndElementCodes>(
				NSFDatabase.PROGRAM_NAME_AND_ELEMENT_CODES_HUMAN_READABLE_NAME,
				NSFDatabase.PROGRAM_NAME_AND_ELEMENT_CODES_TABLE_NAME,
				ProgramNameAndElementCodes.SCHEMA);

	private RowItemContainer<ProgramReferenceCodes> programReferenceCodes =
		new RowItemContainer<ProgramReferenceCodes>(
				NSFDatabase.PROGRAM_REFERENCE_CODES_HUMAN_READABLE_NAME,
				NSFDatabase.PROGRAM_REFERENCE_CODES_TABLE_NAME,
				ProgramReferenceCodes.SCHEMA);

	private RowItemContainer<AwardOccurences> awardOccurences =
		new RowItemContainer<AwardOccurences>(
				NSFDatabase.AWARD_OCCURRENCES_HUMAN_READABLE_NAME,
				NSFDatabase.AWARD_OCCURRENCES_TABLE_NAME,
				AwardOccurences.SCHEMA);


	/**
	 * This is an instance method instead of a static method so all of the tables can be instance
	 *  variables and thus don't clutter up this method.
	 * @param columnNameToColumnIndex 
	 * @param nsfCsv 
	 */
	public DatabaseModel createInMemoryModel(Map<String, Integer> columnNameToColumnIndex, 
								 CSVReader cSVReaderHandle, 
								 File nsfCsvFile) {
		
		NSFFile nSFFile = NSFFileParser.parseNSFFile(this.nsfFiles.getKeyGenerator(), 
													 nsfCsvFile);
		
		NSFFile mergedNSFFile = this.nsfFiles.addOrMerge(nSFFile);
		
		/*			
		 * Parse each field of the nsf file and update the nsf model as you go.
		 * For each row after the column headers row, create entity VO (VO = Value Object) 
		 * in the following order,
		 */  
		String[] nextAwardLine = null;
		int rowsProcessedSoFar = 0;
		try {
			while ((nextAwardLine = cSVReaderHandle.readNext()) != null) {

				System.out.println(" ++++ " + nextAwardLine.length + " ++++ ");

				/*
				 * For Award VO parse following columns, Title, Start Date, 
				 * Last Ammendment Date, Expiration Date, Awarded Amount to Date,
				 * Award Instrument, NSF Organization, NSF Directorate & Abstract.
				 * */
				Award award = parseAward(columnNameToColumnIndex, nextAwardLine);

				this.awardOccurences.addOrMerge(new AwardOccurences(award, mergedNSFFile));
				
				/*
				 * For Person VO parse Principal Investigator column. 
				 * */
				Person principalInvestigatorPerson = 
					parsePrincipalInvestigatorPerson(columnNameToColumnIndex,
							nextAwardLine);
				/*
				 * For Investigator VO parse PI Email Address, State Columns.
				 * Also, since it is PI set "Is Main" boolean to true for the VO.
				 * Add a reference to it's Person VO. Add a reference to it's Award VO.
				 * */
				Investigator principalInvestigator = 
					parsePrincipalInvestigator(principalInvestigatorPerson,
							award,
							columnNameToColumnIndex,
							nextAwardLine);

				/*
				 * For Organization VO parse following columns, Organization Phone, 
				 * Organization Zip, Organization Street Address, Organization State,
				 * Organization & Organization City.
				 * */
				Organization organization = parseOrganization(columnNameToColumnIndex, 
						nextAwardLine);
				/*
				 * Create a relationship VO of "Investigator - Organization" and add 
				 * reference in it of the "Investigator" &  "Organization".
				 * */
				this.investigatorOrganizations.addOrMerge(
						new InvestigatorOrganizations(principalInvestigator, 
								organization));
				/*
				 * Parse the "Co-PI Name(s)" column to get individual CO-PI name and 
				 * create new Person, Investigator VOs accordingly.
				 * Make sure that splitting up of the multiple values is done properly &
				 * Parsing of the value in to its individual components of Last, First 
				 * name etc is done properly.
				 * */
				List<Person> coPrinciaplInvestigatorPeople = 
					parseCOPrincipalInvestigatorPeople(columnNameToColumnIndex, 
							nextAwardLine,
							award);

				for (Person coPrinciaplInvestigatorPerson : coPrinciaplInvestigatorPeople) {

					this.investigators.addOrMerge(
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
				ProgramManager programManager = parseProgramManager(award,
						columnNameToColumnIndex,
						nextAwardLine);
				/*
				 *  For the Field of Application VO parse Field Of Application(s) column.
				 *  Create a list which will contain references to the FOA objects.
				 *  Make sure that splitting up of the multiple values is done properly
				 *  Create a new VO for each value obtained (if not already present, that is)
				 *  If there is a duplicate just get the reference and append it to the list of FOA
				 * */
				List<FieldOfApplication> fieldOfApplicationObjects = 
					parseFieldOfApplications(columnNameToColumnIndex,
							nextAwardLine);

				/*
				 * For the FOA - Award VO for each FOA in the list do, create a new VO 
				 * having reference to both FOA & the current Award.
				 * */
				for (FieldOfApplication fieldOfApplication : fieldOfApplicationObjects) {
					this.awardFieldOfApplications.addOrMerge(new FieldOfApplications(fieldOfApplication, award));
				}


				List<Program> programNamesAndElementCodes = parseProgramNamesAndElementCodes(
						columnNameToColumnIndex,
						nextAwardLine);

				for (Program programNameAndElementCode : programNamesAndElementCodes) {
					this.programNameAndElementCodes.addOrMerge(
							new ProgramNameAndElementCodes(programNameAndElementCode, award));
				}

				List<Program> programReferenceCodes = parseProgramReferenceCodes(
						columnNameToColumnIndex,
						nextAwardLine);

				for (Program programReferenceCode : programReferenceCodes) {
					this.programReferenceCodes.addOrMerge(
							new ProgramReferenceCodes(programReferenceCode, award));
				}


				System.out.println();
				rowsProcessedSoFar++;

			}
			System.out.println(rowsProcessedSoFar);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//			for (IntIterator rows = cSVReaderHandle.rows(); rows.hasNext(); ) {
		//				Tuple row = cSVReaderHandle.getTuple(rows.nextInt());
		catch (AlgorithmExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * Create entity objects.
		 * */

		//				Person person = parsePerson(row);


		// For each of the entities just extracted:
		// Check if it is a duplicate.
		/*
		 * If it is, it will be a duplicate with at most one other entity.
		 * Update any references to it to refer to the entity it is duplicate with.
		 * Remove it.
		 */


		//			}
		// Given all of the master lists of entities, construct an NSFModel and return it.

		// Create new NSFModel, passing in all entity tables and relationship tables.

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


	private List<Program> parseProgramNamesAndElementCodes(
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) {

		List<Program> programs = new ArrayList<Program>();

		String rawProgramNamesString = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex.get(NsfNames.CSV.PROGRAM_NAMES)]);

		String rawProgramElementCodesString = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex.get(NsfNames.CSV.PROGRAM_ELEMENT_CODES)]);

		String[] programNames = StringUtilities.filterEmptyStrings(rawProgramNamesString.split("\\|"));
		String[] programElementCodes = StringUtilities
											.filterEmptyStrings(rawProgramElementCodesString.split("\\|"));

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
			? programElementCodes.length : programNames.length;

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
		String cleanedProgramName = 
			StringUtilities.simpleClean(programName);


		String cleanedProgramElementCode = 
			StringUtilities.simpleClean(programFundingCode);

		Program program = new Program(this.programs.getKeyGenerator(),
				cleanedProgramName,
				cleanedProgramElementCode);

		Program mergedProgram = this.programs.addOrMerge(program);
		return mergedProgram;
	}


	private List<Program> parseProgramReferenceCodes(
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) {

		List<Program> programs = new ArrayList<Program>();

		String rawProgramReferenceCodesString = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex.get(NsfNames.CSV.PROGRAM_REFERENCE_CODES)]);

		String[] programReferenceCodes = StringUtilities
												.filterEmptyStrings(rawProgramReferenceCodesString.split("\\|"));

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
			String[] nextAwardLine) {
		List<FieldOfApplication> fieldOfApplicationCandidates = 
			new ArrayList<FieldOfApplication>();

		String rawFieldOfApplicationsString = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex.get(NsfNames.CSV.FIELD_OF_APPLICATIONS)]);

		String[] fieldOfApplicationsStrings = StringUtilities
													.filterEmptyStrings(rawFieldOfApplicationsString.split("\\|"));

		for (String fieldOfApplicationString : fieldOfApplicationsStrings) {
			
			String cleanedFOAString = StringUtilities.simpleClean(fieldOfApplicationString);

			FieldOfApplication fieldApplication = 
				FieldOfApplicationParser.parseFieldOfApplication(
						this.fieldOfApplications.getKeyGenerator(), 
						cleanedFOAString);

			FieldOfApplication mergedFOA = this.fieldOfApplications.addOrMerge(fieldApplication);

			fieldOfApplicationCandidates.add(mergedFOA);
			
		}

		return fieldOfApplicationCandidates;
	}


	private ProgramManager parseProgramManager(Award award,
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) {

		String cleanedProgramManagerName = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex
				              .get(NsfNames.CSV.PROGRAM_MANAGER)]);

		Person programManagerPerson = PersonParser.parsePerson(
				this.people.getKeyGenerator(), 
				cleanedProgramManagerName);

		Person mergedProgramManagerPerson = this.people.addOrMerge(programManagerPerson);

		return this.programManagers.addOrMerge(
				new ProgramManager(mergedProgramManagerPerson, 
						award));
	}


	private List<Person> parseCOPrincipalInvestigatorPeople(
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine, Award award) {

		List<Person> coPrincipalInvestigatorPeople = new ArrayList<Person>();

		String rawCOPIString = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                                 .get(NsfNames.CSV.CO_PI_NAMES)]);
		String[] coPIStrings = StringUtilities.filterEmptyStrings(rawCOPIString.split("\\|"));

		for (String coPIString : coPIStrings) {
				String cleanedCOPIString = StringUtilities.simpleClean(coPIString);
				Person coPIPerson = PersonParser.parsePerson(
						this.people.getKeyGenerator(), 
						cleanedCOPIString);
				Person mergedCOPIPerson = this.people.addOrMerge(coPIPerson);

				coPrincipalInvestigatorPeople.add(mergedCOPIPerson);	
		}

		return coPrincipalInvestigatorPeople;
	}


	private Organization parseOrganization(
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) {

		String name = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                        .get(NsfNames.CSV.ORGANIZATION_NAME)]);
		String phone = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                         .get(NsfNames.CSV.ORGANIZATION_PHONE)]);
		String streetAddress = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                                 .get(NsfNames.CSV.ORGANIZATION_STREET_ADDRESS)]);
		String city = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                        .get(NsfNames.CSV.ORGANIZATION_CITY)]);
		String state = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                         .get(NsfNames.CSV.ORGANIZATION_STATE)]);
		String zip = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                       .get(NsfNames.CSV.ORGANIZATION_ZIP)]);

		Organization organization = new Organization(this.organizations.getKeyGenerator(),
				name,
				phone,
				streetAddress,
				city,
				state,
				zip);

		return this.organizations.addOrMerge(organization);
	}


	private Investigator parsePrincipalInvestigator(
			Person principalInvestigatorPerson, Award award,
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) {

		String emailAddress = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                                .get(NsfNames.CSV.PRINCIPAL_INVESTIGATOR_EMAIL)]);

		String state = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                         .get(NsfNames.CSV.PRINCIPAL_INVESTIGATOR_STATE)]);
		boolean isMainPI = true;

		Investigator principalInvestigator = new Investigator(this.investigators.getKeyGenerator(), 
				award,
				principalInvestigatorPerson,
				emailAddress,
				state,
				isMainPI); 

		return this.investigators.addOrMerge(principalInvestigator);

	}


	private Award parseAward(
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) throws AlgorithmExecutionException {
		
		

		String awardNumber = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                               .get(NsfNames.CSV.AWARD_NUMBER)]);



		String title = StringUtilities.simpleClean(nextAwardLine[columnNameToColumnIndex
		                                                         .get(NsfNames.CSV.AWARD_TITLE)]);

		Date startDate = parseDate(
				StringUtilities.simpleClean(
						nextAwardLine[columnNameToColumnIndex
						              .get(NsfNames.CSV.AWARD_START_DATE)]));	  

		Date expirationDate = parseDate(
				StringUtilities.simpleClean(
						nextAwardLine[columnNameToColumnIndex
						              .get(NsfNames.CSV.AWARD_EXPIRATION_DATE)]));

		Date lastAmmendmentDate = parseDate(
				StringUtilities.simpleClean(
						nextAwardLine[columnNameToColumnIndex
						              .get(NsfNames.CSV.AWARD_LAST_AMENDMENT_DATE)]));         

		double awardedAmountToDate = Double.parseDouble(
				StringUtilities.simpleClean(
						nextAwardLine[columnNameToColumnIndex
						              .get(NsfNames.CSV.AWARDED_AMOUNT_TO_DATE)]));                          	

		String awardInstrument = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex
				              .get(NsfNames.CSV.AWARD_INSTRUMENT)]);	                                   		                                     	                                 					                                   	

		String nSFDirectorate = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex
				              .get(NsfNames.CSV.NSF_DIRECTORATE)]);

		String nSFOrganization = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex
				              .get(NsfNames.CSV.NSF_ORGANIZATION)]); 


		String abstractText = StringUtilities.simpleClean(
				nextAwardLine[columnNameToColumnIndex
				              .get(NsfNames.CSV.ABSTRACT)]);			   

		/*
		 * Merge all the columns right to the abstract column to offset all csv corruption.
		 * */
		                           	
		if (nextAwardLine.length > 26) {
			for (int ii = 26; ii < nextAwardLine.length; ii++) {
				abstractText += nextAwardLine[ii];
			}
		}			                               	                            	                              	                                 

		Award award = new Award(this.awards.getKeyGenerator(), 
				awardNumber,
				title,
				startDate,
				expirationDate,
				lastAmmendmentDate,
				awardedAmountToDate,
				awardInstrument,
				nSFDirectorate,
				nSFOrganization,
				abstractText);

		return this.awards.addOrMerge(award);

	}


	/**
	 * @param columnNameToColumnIndex
	 */
	private Person parsePrincipalInvestigatorPerson(
			Map<String, Integer> columnNameToColumnIndex,
			String[] nextAwardLine) {

		String pIpersonName = nextAwardLine[columnNameToColumnIndex
		                                    .get(NsfNames.CSV.PRINCIPAL_INVESTIGATOR_NAME)];

		String cleanedPersonName = StringUtilities.simpleClean(pIpersonName);

		System.out.println("PI:" + cleanedPersonName);
		
		Person person = PersonParser.parsePerson(
				this.people.getKeyGenerator(), 
				cleanedPersonName);

		return this.people.addOrMerge(person);
	}


	private Date parseDate(String dateString)
	throws AlgorithmExecutionException {
		try {
			java.util.Date standardDate = DateUtilities.parseDate(dateString);

			return new java.sql.Date(standardDate.getTime());
		}
		catch (ParseException parseDateException) {
			String exceptionMessage = "Could not parse the field " +
			"'" + dateString + "'" +
			" as a date. Aborting the algorithm.";

			throw new AlgorithmExecutionException(exceptionMessage);
		}
	}
}