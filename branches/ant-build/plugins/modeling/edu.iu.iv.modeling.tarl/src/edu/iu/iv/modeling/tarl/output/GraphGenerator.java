/*
 * Created on Aug 17, 2004
 */
package edu.iu.iv.modeling.tarl.output;

import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;

/**
 * @author Shashikant
 */
public interface GraphGenerator {

    /**
     * Set the publication manager for this generator. This manager is used
     * to access the publications in the model.
     * 
     * @param pubManager
     */
    public void setPublicationManager(PublicationManager pubManager) ;
    
    /**
     * Set the author manager for this generator. This manager is used to access
     * the authors in the model.
     * 
     * @param authorManager
     */
    public void setAuthorManager(AuthorManager authorManager) ;
    
    /**
     * 
     * @return The co-citation network.
     */
    public Object generateCitationGraph() ;
    
    /**
     * 
     * @return The co-author network.
     */
    public Object generateCoAuthorGraph() ;
    
    /**
     * 
     * @return The author-publication network.
     */
    public Object generateAuthorPublicationGraph() ;
    
    /**
     * 
     * @return The class of the object returned by the implementation of the GraphGenerator.
     */
    public Class getGraphClass() ;
    
}
