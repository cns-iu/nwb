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

package org.mediavirus.parvis.model;

import javax.swing.event.*;
import java.util.*;

/**
 * Defines the data model for Parallel Coordinate visualisation.
 * The model is defined as read-only because the source of the data is not
 * known and currently no data modification operations are provided. An optional
 * writeback interface might be developed in the future.
 *
 * @author  flo
 * @version 0.1 
 */
public interface ParallelSpaceModel {

    /**
     * Subscribes a ChangeListener with the model.
     *
     * @param l The ChangeListener to be notified when values change.
     */
    void addChangeListener(ChangeListener l);
    
    /**
     * Removes a previously subscribed changeListener.
     *
     * @param l The ChangeListener to be removed from the model.
     */
    void removeChangeListener(ChangeListener l);
    
    /**
     * Returns the number of dimensions (=columns) of the dataset.
     *
     * @return Number of dimensions of the data.
     */
    public int getNumDimensions();
    
    /**
     * Returns the number of records in the dataset.
     *
     * @return Number of records in the dataset.
     */
    public int getNumRecords();
    
    /**
     * Returns the maximum value for the given dimension.
     *
     * @return Maximum value of all records for the given dimension.
     */
    public float getMaxValue(int dimension);
    
    /**
     * Returns the minimum value for the given dimension.
     *
     * @return Minimum value of all records for the given dimension.
     */
    public float getMinValue(int dimension);
    
    /**
     * Returns a specific value of the dataset.
     *
     * @param record The number of the record to be queried.
     * @param dimension The value of the record to be returned.
     *
     * @return The value specified by record, dimension.
     */
    public float getValue(int record, int dimension);

    /**
     * Returns all values of a specific record.
     *
     * @param record The number of the record to be returned.
     *
     * @return All values of the specified record..
     */
    public float[] getValues(int record);
        
    /**
     * Returns a String label for a specific dimension.
     *
     * @param dimension The dimension.
     *
     * @return A Human-readable label for the dimension.
     */
    public String getAxisLabel(int dimension);
    
    /**
     * Returns a Hashtable with labels for specific values. This is provided for
     * ordinal values, which might be added as keys to the Hashtable, with the
     * corresponding human-readable labels as values.
     *
     * @param dimension The dimension to retrieve value labels for.
     *
     * @return A Hashtable containing value-label pairs.
     */
    public Hashtable getValueLabels(int dimension);
    
    /**
     * Returns the label for a single value in a specific dimension, if present.
     * 
     * @param dimension The dimension.
     * @param value The value to look up a label for.
     *
     * @return A String with the label, null if no label is set.
     */
    public String getValueLabel(int dimension, float value);
    
    /**
     * Returns a human-readable label for a specific record.
     *
     * @param num The record number.
     *
     * @return A human-readable label for the record.
     */
     public String getRecordLabel(int num);
     
     public String[] getAxisNominalLabels(int dimension) ;

}

