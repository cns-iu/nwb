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

import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;

/**
 * Provides a basic implementation of ParallelSpaceModel. Values are stored in a
 * two-dimensionsl array, labels are stored in arrays.
 *
 * @author  Flo Ledermann flo@subnet.at
 * @version 0.1
 */
public class SimpleParallelSpaceModel implements ParallelSpaceModel {

    /** Contains the float[] values. */
    protected Vector values = new Vector();
    
    /** Contains the record label Strings. */
    protected Vector recordLabels = new Vector();
    /** Contains the axis label Strings. */
    protected String axisLabels[];
    /** Contains the value label Hashtables. */
    protected Hashtable valueLabels[];
    /** Contains axis labels for nominal axes. These are supposed to appear along the axis
     * rather than at the bottom or top.
     */
    protected Map nominalAxisLabelsHash ;
    
    /** Number of dimensions of the model. */
    protected int numDimensions = 0;
    
    /** List to store our event subscribers. */
    protected EventListenerList listeners = new EventListenerList();

    /** 
     * Default Constructor. 
     */
    public SimpleParallelSpaceModel(){
        
    }
    
    /** 
     * Initializes the model with a given float[][] array of values.
     *
     * @param values A float[][] containing records (first index) with float values for each dimension (second index).<br>
     *               All records must have the same number of dimensions! 
     */
    public SimpleParallelSpaceModel(float values[][]){
        int i;
                
        int len = values[0].length;
        
        for (i=0; i<values.length; i++){
            if (values[i].length != len) {
                throw new IllegalArgumentException("Recordsets must have same number of dimensions");
            }
        }

        for (i=0; i<values.length; i++){
            this.values.addElement(values[i]);
        }

        initNumDimensions(len);
    }
   
    /**
     * Adds a record. The record must have the same number of dimensions as the currently
     * stored records.
     *
     * @param values The float values of the record.
     * @param label A String label for the record.
     */
    public void addRecord(float values[], String label){
        
        if (numDimensions == 0){
            initNumDimensions(values.length);
        }
        else if (values.length != numDimensions){
            throw new IllegalArgumentException("Recordsets must have same number of dimensions (" + numDimensions + ")");
        }
           
        this.values.addElement(values);
        recordLabels.addElement(label);
        
    }
    
    /**
     * Adds a record. The record must have the same number of dimensions as the currently
     * stored records.
     *
     * @param values The float values of the record.
     */
    public void addRecord(float values[]){
        addRecord(values, null);
    }
    
    /**
     * Sets up all internal variables for the given number of dimensions.
     * This must be done only once, usually in the constructor or by the first
     * addRecord(). After the first call the number of dimensions is fixed and
     * cannot be changed!
     *
     * @param num The number of dimensions.
     */
    protected void initNumDimensions(int num){
        
        if (numDimensions != 0){
            throw new IllegalArgumentException("Number of Dimensions already set to " + numDimensions + "!");
        }
        
        numDimensions = num;
        
        axisLabels = new String[num];
        valueLabels = new Hashtable[num];
        
        for (int i=0; i<num; i++){
            axisLabels[i] = null;
            valueLabels[i] = null;
        }
        
    }
            
    /**
     * Returns the number of dimnesions.
     *
     * @return The number of dimensions of the records in this model.
     */
    public int getNumDimensions() {
        return numDimensions;
    }
    
    /**
     * Returns the number of records.
     *
     * @return The number of records currently stored in the model.
     */
    public int getNumRecords() {
        return values.size();
    }
    
    /**
     * Returns the maximum value for the given dimension.
     *
     * @return Maximum value of all records for the given dimension.
     */
    public float getMaxValue(int dimension) {
        
        float maxval = ((float[])values.firstElement())[dimension];
        for (int i=0; i<values.size(); i++){
            if (((float[])values.elementAt(i))[dimension] > maxval) maxval = ((float[])values.elementAt(i))[dimension];
        }
        
        return maxval;
            
    }
    
    /**
     * Returns the minimum value for the given dimension.
     *
     * @return Minimum value of all records for the given dimension.
     */
    public float getMinValue(int dimension) {
        
        float minval = ((float[])values.firstElement())[dimension];
        for (int i=0; i<values.size(); i++){
            if (((float[])values.elementAt(i))[dimension] < minval) minval = ((float[])values.elementAt(i))[dimension];
        }
        
        return minval;
            
    }
    
    /**
     * Returns a specific value of the dataset.
     *
     * @param record The number of the record to be queried.
     * @param dimension The value of the record to be returned.
     *
     * @return The value specified by record, dimension.
     */
    public float getValue(int record, int dimension) {
        return ((float[])values.elementAt(record))[dimension];
    }

    /**
     * Returns a String label for a specific dimension.
     *
     * @param dimension The dimension.
     *
     * @return A Human-readable label for the dimension.
     */
    public String getAxisLabel(int dimension) {
        return axisLabels[dimension];
    }
    
    public String[] getAxisNominalLabels(int dimension) {
    	return (String[]) this.nominalAxisLabelsHash.get(axisLabels[dimension]) ;
    }

    /**
     * Sets the labels for all axes.
     * Note that this method is not included in the ParallelSpaceModel interface,
     * which defines only read-only methods. It is used for filling the model
     * before passing it on to a consumer.
     *
     * @param labels An Array of Strings to be used as human-readable labels for the axes.
     */ 
    public void setAxisLabels(String labels[]){
        for (int i=0; i<labels.length; i++){
            axisLabels[i] = labels[i];
        }
    }
    
    public void setNominalAxisLabelsHash(Map nominalAxisLabelsHash) {
    	this.nominalAxisLabelsHash = nominalAxisLabelsHash ;
    }
    
    

    /**
     * Sets the label of a single axis.
     *
     * @param dimension The dimension this label is for.
     * @param label The label.
     */
    public void setAxisLabel(int dimension, String label){
        axisLabels[dimension] = label;
    }
    
    /**
     * Returns a Hashtable with labels for specific values. This is provided for
     * ordinal values, which might be added as keys to the Hashtable, with the
     * corresponding human-readable labels as values.
     *
     * @param dimension The dimension to retrieve value labels for.
     *
     * @return A Hashtable containing value-label pairs.
     */
    public Hashtable getValueLabels(int dimension) {
        return valueLabels[dimension];
    }
    
    /**
     * Returns the label for a single value in a specific dimension, if present.
     * 
     * @param dimension The dimension.
     * @param value The value to look up a label for.
     *
     * @return A String with the label, null if no label is set.
     */
    public String getValueLabel(int dimension, float value){
        if (valueLabels[dimension] != null){
            return (String) (valueLabels[dimension].get(new Float(value)));
        }
        else {
            return null;
        }
    }

    /**
     * Sets the value labels for a dimension.
     * Note that this method is not included in the ParallelSpaceModel interface,
     * which defines only read-only methods. It is used for filling the model
     * before passing it on to a consumer.
     *
     * @param dimension The dimension the labels are to be set for.
     * @param values The values to assign labels to. Note that the number of labels an values must match.
     * @param labels The String labels for the values. Note that the number of labels an values must match. 
     */ 
    public void setValueLabels(int dimension, float values[], String labels[]){
        if (values.length != labels.length) {
            throw new IllegalArgumentException("number of values and labels do not match!");
        }
        
        if (valueLabels[dimension] == null){
            valueLabels[dimension] = new Hashtable();
        }
        
        for (int i=0; i<values.length; i++){
            valueLabels[dimension].put(new Float(values[i]), labels[i]);
        }
    }
    
    /**
     * Sets a single value label for a specific axis.
     *
     * @param dimension The dimension to set the label for.
     * @param value The value to set the label for.
     * @param label The label to set.
     */
    public void setValueLabel(int dimension, float value, String label){
        
        if (valueLabels[dimension] == null){
            valueLabels[dimension] = new Hashtable();
        }
        
        valueLabels[dimension].put(new Float(value), label);

    }
        
    /**
     * Returns all values of a specific record.
     *
     * @param record The number of the record to be returned.
     *
     * @return All values of the specified record..
     */
    public float[] getValues(int recordnum) {
        return (float[])values.elementAt(recordnum);
    }
    
    /**
     * Subscribes a ChangeListener with the model.
     *
     * @param l The ChangeListener to be notified when values change.
     */
    public void addChangeListener(ChangeListener l) {
        listeners.add(ChangeListener.class, l);
    }
    
    /**
     * Removes a previously subscribed changeListener.
     *
     * @param l The ChangeListener to be removed from the model.
     */
    public void removeChangeListener(ChangeListener l) {
        listeners.remove(ChangeListener.class, l);
    }
        
    /**
     * Returns a human-readable label for a specific record.
     *
     * @param num The record number.
     *
     * @return A human-readable label for the record.
     */
    public String getRecordLabel(int num) {
        return (String)recordLabels.elementAt(num);
    }
    
}
