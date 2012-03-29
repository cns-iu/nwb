package edu.iu.iv.modeling.tarl.output.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;

import edu.iu.iv.modeling.tarl.author.Author;
import edu.iu.iv.modeling.tarl.author.AuthorGroup;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthor;
import edu.iu.iv.modeling.tarl.author.impl.DefaultAuthorGroup;
import edu.iu.iv.modeling.tarl.output.OutputWriter;
import edu.iu.iv.modeling.tarl.publication.Publication;
import edu.iu.iv.modeling.tarl.publication.PublicationGroup;
import edu.iu.iv.modeling.tarl.publication.impl.DefaultPublication;
import edu.iu.iv.modeling.tarl.publication.impl.DefaultPublicationGroup;
import edu.iu.iv.modeling.tarl.topic.TopicGroup;
import edu.iu.iv.modeling.tarl.topic.impl.DefaultTopic;
import edu.iu.iv.modeling.tarl.topic.impl.DefaultTopicGroup;


/**
 * This class provides a default implementation of the <code>OutputWriterInterface</code> in order to write the Output Files.  It assumes the default implementation of the standard interfaces.
 *
 * @author Jeegar T Maru
 * @see DefaultPublicationGroup
 * @see DefaultAuthorGroup
 * @see DefaultTopicGroup
 */
public class DefaultOutputWriter implements OutputWriter {
	/**
	 * Stores the <code>PublicationGroupInterface</code> for the model
	 */
	public PublicationGroup publications;

	/**
	 * Stores the <code>AuthorGroupInterface</code> for the model
	 */
	public AuthorGroup authors;

	/**
	 * Stores the <code>TopicGroupInterface</code> for the model
	 */
	public TopicGroup topics;

	/**
	 * Stores an array to map the publication id integers to the Pajek identifiers
	 */
	public int[] publicationMap;

	/**
	 * Stores an array to map the author id integers to the Pajek identifiers
	 */
	public int[] authorMap;

	/**
	 * Stores the offset for the author list in the Pajek file if publications are included
	 */
	public int authorOffset;

	/**
	 * Specifies a default constructor
	 */
	public DefaultOutputWriter() {
		publications = null;
		authors = null;
		topics = null;
	}

	/**
	 * Initializes the <code>OutputWriter</code> with the given publications and authors
	 *
	 * @param publicationGroup Specifies the group of all the publications for the model
	 * @param authorGroup Specifies the group of all authors for the model
	 * @param topicGroup Specifies the group of all topics for the model
	 */
	public void initialize(PublicationGroup publicationGroup,
	    AuthorGroup authorGroup, TopicGroup topicGroup) {
		publications = publicationGroup;
		authors = authorGroup;
		topics = topicGroup;

		publicationMap = new int[publications.size()];
		authorMap = new int[authors.size()];
		authorOffset = 0;
	}

	/**
	 * Writes out Pajek Files to the file system in the given folder
	 *
	 * @param folderPath Specifies the location of the folder for the output files
	 *
	 * @exception FileNotFoundException if the files cannot be written to the file system
	 */
	public void writePajekFiles(String folderPath) throws FileNotFoundException {
		PrintWriter coCitPajekWriter;
		PrintWriter coAuthPajekWriter;
		PrintWriter pubAuthPajekWriter;

		coCitPajekWriter =
			new PrintWriter(new FileOutputStream(
			        new File(new String(folderPath + "cocitation.net"))));
		coAuthPajekWriter =
			new PrintWriter(new FileOutputStream(
			        new File(new String(folderPath + "coauthorship.net"))));
		pubAuthPajekWriter =
			new PrintWriter(new FileOutputStream(
			        new File(new String(folderPath + "publicationauthor.net"))));

		this.writeCoCitationPajekFile(coCitPajekWriter);
		this.writeCoAuthorPajekFile(coAuthPajekWriter);
		this.writePubAuthPajekFile(pubAuthPajekWriter);

		coCitPajekWriter.close();
		coAuthPajekWriter.close();
		pubAuthPajekWriter.close();
	}

	/**
	 * Writes out the Data Files to the file system in the given folder
	 *
	 * @param folderPath Specifies the location of the folder for the output files
	 *
	 * @exception FileNotFoundException if the files cannot be written to the file system
	 */
	public void writeDataFiles(String folderPath) throws FileNotFoundException {
		Iterator iterator;
		PrintWriter topicDataWriter;
		PrintWriter authorDataWriter;
		PrintWriter pubDataWriter;

		topicDataWriter =
			new PrintWriter(new FileOutputStream(
			        new File(new String(folderPath + "topic.txt"))));
		authorDataWriter =
			new PrintWriter(new FileOutputStream(
			        new File(new String(folderPath + "author.txt"))));
		pubDataWriter =
			new PrintWriter(new FileOutputStream(
			        new File(new String(folderPath + "publication.txt"))));

		topicDataWriter.print(new String("All Topics :\n"));
		iterator = topics.getIterator();

		while (iterator.hasNext()) {
			topicDataWriter.print(((DefaultTopic)iterator.next()).toString());
			topicDataWriter.print(new String("\n"));
		}

		authorDataWriter.print(new String("All Authors :\n"));
		iterator = authors.getIterator();

		while (iterator.hasNext()) {
			authorDataWriter.print(((DefaultAuthor)iterator.next()).toString());
			authorDataWriter.print(new String("\n"));
		}

		pubDataWriter.print(new String("All Publications :\n"));
		iterator = publications.getIterator();

		while (iterator.hasNext()) {
			pubDataWriter.print(((DefaultPublication)iterator.next()).toString());
			pubDataWriter.print(new String("\n"));
		}

		topicDataWriter.close();
		authorDataWriter.close();
		pubDataWriter.close();
	}

	/**
	 * Writes the Pajek File for the co-citation network
	 *
	 * @param coCitWriter Specifies the PrintWriter to write to for the co-citation network
	 */
	private void writeCoCitationPajekFile(PrintWriter coCitWriter) {
		coCitWriter.println("*Vertices      " + publications.size());
		this.writePubVertices(coCitWriter);

		coCitWriter.println("*Arcs");
		this.writePubArcs(coCitWriter);

		coCitWriter.println("*Edges");
	}

	/**
	 * Writes the publication vertices in Pajek format using the given output writer
	 *
	 * @param pubVerticesWriter Specifies the print writer to be used for output
	 */
	private void writePubVertices(PrintWriter pubVerticesWriter) {
		int pubIndex;
		int publicationId;
		Iterator pubIterator;

		pubIterator = publications.getIterator();
		pubIndex = 0;

		while (pubIterator.hasNext()) {
			publicationId = ((DefaultPublication)pubIterator.next()).getId();
			pubVerticesWriter.println(" " + (pubIndex + 1) + " " + "p"
			    + (publicationId) + " 0.0 0.0 triangle ic Maroon bc Black");

			publicationMap[publicationId] = pubIndex;
			pubIndex++;
		}
	}

	/**
	 * Writes the publication arcs in Pajek format using the given output writer
	 *
	 * @param pubArcsWriter Specifies the print writer to be used for output
	 */
	private void writePubArcs(PrintWriter pubArcsWriter) {
		int publicationId;
		int citationId;
		Iterator pubIterator;
		Iterator citIterator;
		Publication tempPub;
		PublicationGroup tempCitations;

		pubIterator = publications.getIterator();

		while (pubIterator.hasNext()) {
			tempPub = (DefaultPublication)pubIterator.next();
			publicationId = tempPub.getId();
			tempCitations = tempPub.getCitations();

			if (tempCitations != null) {
				citIterator = tempCitations.getIterator();

				while (citIterator.hasNext()) {
					citationId =
						((DefaultPublication)citIterator.next()).getId();
					pubArcsWriter.println(" "
					    + (publicationMap[citationId] + 1) + " "
					    + (publicationMap[publicationId] + 1) + " 1 c Maroon");
				}
			}
		}
	}

	/**
	 * Writes the Coauthorship Network Pajek Files by collecting information from the <code>AuthorManagerInterface</code> and the <code>PublicationManagerInterface</code>
	 *
	 * @param coAuthWriter Specifies the PrintWriter to write the output to
	 */
	private void writeCoAuthorPajekFile(PrintWriter coAuthWriter) {
		authorOffset = 0;

		coAuthWriter.println("*Vertices " + authors.size());
		this.writeAuthVertices(coAuthWriter);

		coAuthWriter.println("*Arcs\n*Edges");
		this.writeAuthEdges(coAuthWriter);
	}

	/**
	 * Writes the author vertices in Pajek format using the given output writer
	 *
	 * @param authVerticesWriter Specifies the print writer to be used for output
	 */
	private void writeAuthVertices(PrintWriter authVerticesWriter) {
		int authIndex;
		int authorId;
		Iterator iterator;
		Author tempAuthor;

		iterator = authors.getIterator();
		authIndex = 0;

		while (iterator.hasNext()) {
			tempAuthor = ((DefaultAuthor)iterator.next());
			authorId = tempAuthor.getId();
			authVerticesWriter.println(" " + (authIndex + authorOffset + 1)
			    + " " + "a" + (authorId) + " 0.0 0.0 circle ic Violet bc Black");
			authorMap[authorId] = authIndex;
			authIndex++;
		}
	}

	/**
	 * Writes the author edges in Pajek format using the given output writer
	 *
	 * @param authEdgesWriter Specifies the print writer to be used for output
	 */
	private void writeAuthEdges(PrintWriter authEdgesWriter) {
		int numNonZero;
		int matIndex;
		boolean found;
		Author tempAuthor1;
		Author tempAuthor2;
		AuthorGroup tempAuthorGroup;
		Iterator iterator;
		Iterator iterator1;
		Iterator iterator2;
		int[] collaborationRow;
		int[] collaborationCol;
		int[] collaborationVal;

		final int MAX_NNZ = 1000000;

		collaborationRow = new int[MAX_NNZ];
		collaborationCol = new int[MAX_NNZ];
		collaborationVal = new int[MAX_NNZ];

		numNonZero = 0;
		iterator = publications.getIterator();

		while (iterator.hasNext()) {
			tempAuthorGroup =
				((DefaultPublication)iterator.next()).getAuthors();
			iterator1 = tempAuthorGroup.getIterator();

			while (iterator1.hasNext()) {
				tempAuthor1 = ((DefaultAuthor)iterator1.next());
				iterator2 = tempAuthorGroup.getIterator();

				while (iterator2.hasNext()) {
					tempAuthor2 = ((DefaultAuthor)iterator2.next());

					if (tempAuthor1.getId() < tempAuthor2.getId()) {
						// Search Sparse Matrix
						for (matIndex = 0, found = false;
							    ((matIndex < numNonZero) && (!found));
							    matIndex++)
							if (((collaborationRow[matIndex] == tempAuthor1
								    .getId())
								    && (collaborationCol[matIndex] == tempAuthor2
								    .getId()))
								    || ((collaborationRow[matIndex] == tempAuthor2
								    .getId())
								    && (collaborationCol[matIndex] == tempAuthor1
								    .getId()))) {
								collaborationVal[matIndex]++;
								found = true;
							}

						if (!found) {
							collaborationRow[numNonZero] = tempAuthor1.getId();
							collaborationCol[numNonZero] = tempAuthor2.getId();
							collaborationVal[numNonZero] = 1;
							numNonZero++;
						}
					}
				}
			}
		}

		for (matIndex = 0; matIndex < numNonZero; matIndex++)
			authEdgesWriter.println(" "
			    + (authorMap[collaborationRow[matIndex]] + authorOffset + 1)
			    + " "
			    + (authorMap[collaborationCol[matIndex]] + authorOffset + 1)
			    + " " + collaborationVal[matIndex] + " c Violet ");
	}

	/**
	 * Writes the Pajek File for the publication-author network
	 *
	 * @param pubAuthWriter Specifies the PrintWriter to write to for the publication-author network
	 */
	private void writePubAuthPajekFile(PrintWriter pubAuthWriter) {
		authorOffset = publications.size();

		pubAuthWriter.println("*Vertices      "
		    + (publications.size() + authors.size()));
		this.writePubVertices(pubAuthWriter);
		this.writeAuthVertices(pubAuthWriter);

		pubAuthWriter.println("*Arcs");
		this.writePubArcs(pubAuthWriter);
		this.writePubAuthArcs(pubAuthWriter);

		pubAuthWriter.println("*Edges");
		this.writeAuthEdges(pubAuthWriter);
	}

	/**
	 * Writes the publication-author arcs in Pajek format using the given output writer
	 *
	 * @param pubAuthArcsWriter Specifies the print writer to be used for output
	 */
	private void writePubAuthArcs(PrintWriter pubAuthArcsWriter) {
		int pubId;
		int authorId;
		Iterator pubIterator;
		Iterator authIterator;
		Publication tempPub;
		AuthorGroup authorGroup;

		pubIterator = publications.getIterator();

		while (pubIterator.hasNext()) {
			tempPub = (DefaultPublication)pubIterator.next();
			pubId = tempPub.getId();
			authorGroup = tempPub.getAuthors();
			authIterator = authorGroup.getIterator();

			while (authIterator.hasNext()) {
				authorId = ((DefaultAuthor)authIterator.next()).getId();
				pubAuthArcsWriter.println(" "
				    + (authorMap[authorId] + authorOffset + 1) + " "
				    + (publicationMap[pubId] + 1) + " 1 c Aquamarine");
			}
		}
	}
}
