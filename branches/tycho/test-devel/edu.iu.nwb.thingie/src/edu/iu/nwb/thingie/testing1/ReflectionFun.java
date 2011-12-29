package edu.iu.nwb.thingie.testing1;

import edu.iu.nwb.thingie.testing2.LookInMe;

public class ReflectionFun {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Cover cover = new LookInMe();
		
		SecretReceiver secretReceiver = new SecretReceiver();
		secretReceiver.findOutTheSecret(cover);
	}

}
