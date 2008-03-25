/*

 Copyright (c) 2001, 2002, 2003 Flo Ledermann <flo@subnet.at>

 This file is part of parvis - a parallel coordiante based data visualisation
 tool written in java. You find parvis and additional information on its
 website at http://www.mediavirus.org/parvis.

 parvis is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 parvis is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with parvis (in the file LICENSE.txt); if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

 */

package org.mediavirus.parvis;

import javax.swing.UIManager;

import org.mediavirus.parvis.gui.MainFrame;

/**
 * Launcher Class for unsing Parvis as a standalone application.
 * 
 * @author Flo Ledermann flo@subnet.at
 * @version 0.1
 * Modified: @author Ketan Mane <kmane@indiana.edu>
 *  
 */
public class ParvisMain {

	/**
	 * Main method which is called by the java interpreter. Basically displays
	 * the window and returns.
	 * 
	 * @param args
	 *            the command line arguments (currently none available)
	 */
	public static void main(String args[]) {
		UIManager.put("org.mediavirus.parvis.gui.ParallelDisplayUI",
				"org.mediavirus.parvis.gui.BasicParallelDisplayUI");
		
		MainFrame.getInstance().readDataFile(args[0]);
		MainFrame.getInstance().show();
	}
}
