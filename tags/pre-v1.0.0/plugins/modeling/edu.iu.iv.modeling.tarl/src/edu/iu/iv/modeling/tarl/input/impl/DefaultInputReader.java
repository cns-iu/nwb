package edu.iu.iv.modeling.tarl.input.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.AuthorParameters;
import edu.iu.iv.modeling.tarl.input.ExecuterParameters;
import edu.iu.iv.modeling.tarl.input.HelperParameters;
import edu.iu.iv.modeling.tarl.input.InputReader;
import edu.iu.iv.modeling.tarl.input.MainParameters;
import edu.iu.iv.modeling.tarl.input.PublicationParameters;
import edu.iu.iv.modeling.tarl.input.TopicParameters;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This class defines a default implementation to the
<code>InputReaderInterface</code> by reading the model parameters from
an input script file stored on the file system.

 *
 * @author Jeegar T Maru
 * @see edu.iu.iv.modeling.tarl.input.MainParameters
 * @see InputReader
 */
public class DefaultInputReader implements InputReader {
        /**
         * Stores the model Parameters for the model
         */
        MainParameters modelParameters;

        /**
         * Initializes the model parameters with the appropriate values
         *
         * @param inputFile Specifies the input file name to read the
parameters from
         */
        public void initialize(File inputFile) {
                int arrIndex;
                int[] inputArray;
                int integer;
                String lastToken ;
                String line;
                StringTokenizer strToken;
                BufferedReader inputReader;
                YearInformation yearInformation;
                PublicationParameters publicationParameters;
                AuthorParameters authorParameters;
                TopicParameters topicParameters;
                HelperParameters helperParameters;
                ExecuterParameters executerParameters;

                inputArray = new int[100];
                arrIndex = 0;
                 try {
                inputReader = new BufferedReader(new FileReader(inputFile));
                        while (((line = inputReader.readLine()) != null) && (arrIndex < 16)) 
                        {
                                strToken = new StringTokenizer(line);
                          lastToken = null;
                                while(strToken.hasMoreTokens())
                                {
                                        lastToken = strToken.nextToken();
                                     if (lastToken !=null)
                                        try {
                                                integer = Integer.parseInt(lastToken,10) ;
                                                        inputArray[arrIndex] = integer ;
                                                        arrIndex++;
                                                
                                        } catch (NumberFormatException nfe) {
                                                continue;
                                                }
                                        else
                                                System.err.println("Error");
                        }
                }
                           inputReader.close();
                }catch(FileNotFoundException fnfe){
                        System.err.println("File not found");
                }catch(IOException ioe){
                        System.err.println("IOException occured");
                        } finally{
                                System.err.println("continue");
                        }
                
        
                modelParameters = new DefaultMainParameters();
                modelParameters.initializeDefault();

                helperParameters = modelParameters.getHelperParameters();
                executerParameters = helperParameters.getExecuterParameters();
                yearInformation = helperParameters.getYearInformation();
                publicationParameters = executerParameters.getPublicationParameters();
                authorParameters = executerParameters.getAuthorParameters();
                topicParameters = executerParameters.getTopicParameters();

                try {
                        if (arrIndex >= 1) {
                                if (inputArray[0] == 0)
                                        publicationParameters.setAgingEnabled(false);
                                else
                                        publicationParameters.setAgingEnabled(true);

                                if (arrIndex >= 2) {
                                        yearInformation.setStartYear(inputArray[1]);

                                        if (arrIndex >= 3) {
                                                yearInformation.setEndYear(inputArray[2]);

                                                if (arrIndex >= 4) {
                                                        executerParameters.setNumAuthorsAtStart(inputArray[3]);

                                                        if (arrIndex >= 5) {
                                                                executerParameters.setNumPublicationsAtStart(inputArray[4]);

                                                                if (arrIndex >= 6) {
                                                                        authorParameters.setMaximumAge(inputArray[5]);

                                                                        if (arrIndex >= 7) {
                                                                                topicParameters.setNumTopics(inputArray[6]);

                                                                                if (arrIndex >= 8) {
                                                                                        authorParameters
                                                                                        .setNumDeactivationAuthors(inputArray[7]);

                                                                                        if (arrIndex >= 9) {
                                                                                                authorParameters
                                                                                                .setNumDeactivationYears(inputArray[8]);

                                                                                                if (arrIndex >= 10) {
                                                                                                        executerParameters
                                                                                                        .setNumCreationAuthors(inputArray[9]);

                                                                                                        if (arrIndex >= 11) {
                                                                                                                executerParameters
                                                                                                                .setNumCreationYears(inputArray[10]);

                                                                                                                if (arrIndex >= 12) {
                                                                                                                        authorParameters
                                                                                                                        .setNumCoAuthors(inputArray[11]);

                                                                                                                        if (arrIndex >= 13) {
                                                                                                                                publicationParameters
                                                                                                                                .setNumPublicationsRead(inputArray[12]);

                                                                                                                                if (arrIndex >= 14) {
                                                                                                                                        publicationParameters
                                                                                                                                        .setNumPublicationsCited(inputArray[13]);

                                                                                                                                        if (arrIndex >= 15) {
                                                                                                                                                publicationParameters
                                                                                                                                                .setNumPublicationsWritten(inputArray[14]);

                                                                                                                                                if (arrIndex >= 16) {
                                                                                                                                                        publicationParameters
                                                                                                                                                        .setNumLevelsReferences(inputArray[15]);
                                                                                                                                                }
                                                                                                                                        }
                                                                                                                                }
                                                                                                                        }
                                                                                                                }
                                                                                                        }
                                                                                                }
                                                                                        }
                                                                                }
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                } catch (TarlException te) 
                {
                        System.err.println("Exception occurred : " + te + "\n");
                    System.err.println ("System continuing with default model parameter " + "values ... \n");
                }
                 } 
                 
        /**
         * Returns the Model Parameters for the model to run.
         *
         * @return all the model parameters for the model to run
         */
        public MainParameters getModelParameters() {
                return modelParameters;
        }
}
