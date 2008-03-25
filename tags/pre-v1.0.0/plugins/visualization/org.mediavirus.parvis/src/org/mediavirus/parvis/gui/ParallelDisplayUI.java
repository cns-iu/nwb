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

package org.mediavirus.parvis.gui;

import javax.swing.plaf.ComponentUI;

/**
 * Abstract UI Delegate for the rendering of the ParallelDisplay component. This
 * is a swing guideline to provide an empty abstract class as a UI delegat base
 * class. See BasicParallelDisplayUI for the actual implementation.
 *
 * @author Flo Ledermann flo@subnet.at
 * @version 0.1
 */
public abstract class ParallelDisplayUI extends ComponentUI { 

    public ParallelDisplayUI(){ }
}
