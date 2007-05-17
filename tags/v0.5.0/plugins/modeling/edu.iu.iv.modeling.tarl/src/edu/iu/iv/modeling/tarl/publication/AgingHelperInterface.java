package edu.iu.iv.modeling.tarl.publication;

import java.io.File;


/**
 * The Aging Helper provides utility functions in order to successfully implement the aging process of citations in the TARL model.  When aging is enabled, the <code>PublicationManager</code> uses this interface to randomly select the time slice from which to pick the citation for a newly created <code>Publication</code>.  The Aging Helper is responsible for providing a random number (according to any probability distribution) due to which the selected citations would represent the aging effect - i.e. past publications before their time get cited more and more often and past publications past their prime, are cited less and less often as time increases
 *
 * @author Jeegar T Maru
 * @see PublicationManager
 */
public interface AgingHelperInterface {
	/**
	 * Initializes the aging helper
	 */
	public void initialize(File agingFile);

	/**
	 * Returns a random positive number (or 0) which simulates the aging effect among citations
	 *
	 * @return the random positive number (or 0)
	 */
	public int getRandomYearDifference();
}
