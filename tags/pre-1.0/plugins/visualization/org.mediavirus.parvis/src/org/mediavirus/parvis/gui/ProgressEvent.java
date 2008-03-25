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

/**
 *
 * @author  flo
 * @version 1.0
 */
public class ProgressEvent extends java.util.EventObject {

    // type constants
    public static final int PROGRESS_START = 0;
    public static final int PROGRESS_UPDATE = 1;
    public static final int PROGRESS_FINISH = 2;
    public static final int PROGRESS_CANCEL = 3;
    
    private int type;
    private float progress = 0.0f;
    private long timestamp;
    private String message;
    
    /** Creates new RenderEvent */
    public ProgressEvent(Object source, int type) {
        this(source, type, 0.0f);
    }
    
    public ProgressEvent(Object source, int type, float progress) {
        this(source, type, progress, null);
    }
    
    public ProgressEvent(Object source, int type, float progress, String message) {
        super(source);
        this.type = type;
        this.progress = progress;
        this.message = message;
        
        this.timestamp = System.currentTimeMillis();
    }
    
    public int getType(){
        return type;
    }
    
    public long getTimestamp(){
        return timestamp;
    }
    
    public float getProgress(){
        return progress;
    }
    
    public String getMessage(){
        return message;
    }

}
