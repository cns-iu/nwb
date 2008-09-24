package edu.iu.nwb.visualization.prefuse.alpha.smallworld.action;

import edu.iu.nwb.visualization.prefuse.alpha.smallworld.types.ProgressUpdate;



//Copyright (C) 2005 Andreas Noack
//
//This library is free software; you can redistribute it and/or
//modify it under the terms of the GNU Lesser General Public
//License as published by the Free Software Foundation; either
//version 2.1 of the License, or (at your option) any later version.
//
//This library is distributed in the hope that it will be useful,
//but WITHOUT ANY WARRANTY; without even the implied warranty of
//MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//Lesser General Public License for more details.
//
//You should have received a copy of the GNU Lesser General Public
//License along with this library; if not, write to the Free Software
//Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA 

/**
 * Minimizer for the LinLog energy model and its generalizations,
 * for computing graph layouts.  Based on the Barnes-Hut algorithm.  
 * For more information about the LinLog energy model, see
 *   "Energy-Based Clustering of Graphs with Nonuniform Degrees",
 *   to appear in: Proceedings of the 13th International Symposium 
 *   on Graph Drawing (GD 2005).
 * Available at 
 *   <a href="http://www.informatik.tu-cottbus.de/~an/">
 *   <code>www.informatik.tu-cottbus.de/~an/</code></a>.
 *
 * @author Andreas Noack (an@informatik.tu-cottbus.de)
 * @version 28.09.2005
 */
public class MinimizerBarnesHut {
	/** Number of nodes. */
    private final int nodeNr;
	/** Position in 3-dimensional space for each node. */
	private final float pos[][];

	/** For each node, list of its attraction weights. */
	private final float attrWeights[][];
	/** For each node, indexes of the nodes in its attraction weight list. */ 
	private final int attrIndexes[][];
	/** Exponent of the Euclidean distance in the attraction energy */
	private final float attrExponent;
	
    /** Position of the barycenter of the nodes. */
    private final float[] baryCenter = new float[3];
    /** Factor for the gravitation energy = attraction to the barycenter.
        Set to 0.0f for no gravitation. */
    private final float gravFactor;
	
	/** Repulsion of each node. */
	private final float repuWeights[];
	/** Current factor for repulsion energy.  Changes through pulsing. */
	private float repuFactor;
	/** Exponent of the Euclidean distance in the repulsion energy */
	private final float repuExponent;
	/** Factors for repulsion energy for pulsing */
	private static final float[] repuStrategy 
		= { 1.0f, 0.95f, 0.9f, 0.85f, 0.8f, 0.75f, 0.8f, 0.85f, 0.9f, 0.95f,
			1.0f, 1.1f, 1.2f, 1.3f, 1.4f, 1.5f, 1.4f, 1.3f, 1.2f, 1.1f };
    
	private ProgressUpdate progress = null;
	/**
	 * Initializes the attributes.
	 * @param attrIndexes  list of outneighbors for each node.
	 *   <code>attrIndexes[i][k]</code> is the index of the target node
	 *   of the <code>k</code>th outgoing edge of node <code>i</code>.
	 *   Is not copied and not modified by this class.
	 * @param attrWeights  list of edge weights for each node.
     *   <code>attrWeights[i][k]</code> is the weight of the <code>k</code>th
     *   outgoing edge of node <code>i</code>.  
     *   Omit edges with weight 0.0f (i.e. non-edges).  
     *   For unweighted graphs use weight 1.0f for all edges.
	 *   All elements must not be negative.   
	 *   Weights must be symmetric, i.e. the weight  
	 *   from node <code>i</code> to node <code>j</code> must be equal to
	 *   the weight from node <code>j</code> to node <code>i</code>. 
	 *   Is not copied and not modified by this class.
	 * @param repuWeights  repulsion vector, specifies the repulsion of each node.
	 *   It is recommended to use so-called "edge-repulsion", which means
	 *   to set <code>repuWeights[i]</code> to the sum of the values
	 *   in <code>attrWeights[i]</code> (for all <code>i</code>).
	 *   The repulsion between the nodes with the indexes <code>i</code>
	 *   and <code>j</code> is proportional to <code>repuWeights[i]*repuWeights[j]</code>.
	 *   All elements must not be negative.   
	 *   Is not copied and not modified by this class.
	 * @param attrExponent exponent of the distance in the attraction energy.
	 *   Is 1.0f in the LinLog model (which is used for computing clusters,
	 *   i.e. dense subgraphs), 
	 *   and 3.0f in standard energy model of Fruchterman and Reingold.  
	 *   Must be greater than 0.0f.
	 * @param repuExponent exponent of the distance in the repulsion energy.
	 *   Exception: The value 0.0f corresponds to logarithmic repulsion.  
	 *   Is 0.0f in both the LinLog and the Fruchterman-Reingold energy model.
	 *   Negative values are permitted.
     * @param gravFactor  factor for the gravitation energy.
     *   Gravitation attracts each node to the barycenter of all nodes,
     *   to prevent distances between unconnected graph components
     *   from approaching infinity.  
     *   Typical values are 0.0f if the graph is guaranteed to be connected,
     *   and values that are significantly smaller than the <code>attrWeights</code>
     *   (e.g. 0.01f for unweighted graphs) otherwise.
	 * @param pos  position matrix.  
	 *   Is not copied and serves as input and output of the method
	 *   <code>minimizeEnergy</code>.  For each node index <code>i</code>,
	 *   <code>pos[i]</code> must be a <code>float[3]</code> 
	 *   and specifies the position of the <code>i</code>th node in 3D space. 
	 *   If the input is two-dimensional (i.e. <code>pos[i][2] == 0.0f</code> 
	 *   for all <code>i</code>), the output is also two-dimensional.
	 *   Random initial positions are appropriate.
	 */
	public MinimizerBarnesHut( 
			final int[][] attrIndexes, final float[][] attrWeights, final float[] repuWeights, 
			final float attrExponent, final float repuExponent, final float gravFactor, 
			final float[][] pos, ProgressUpdate update) {
		nodeNr = attrWeights.length;
		this.attrWeights = attrWeights;
		this.attrIndexes = attrIndexes;
		this.repuWeights = repuWeights;
		this.attrExponent = attrExponent;
		this.gravFactor = gravFactor;
		this.repuExponent = repuExponent;
		this.pos = pos;
        this.progress = update;
	}


	/**
	 * Iteratively minimizes energy using the Barnes-Hut algorithm.
	 * Starts from the positions in the attribute <code>pos</code>, 
	 * and stores the computed positions in <code>pos</code>.
	 * @param nrIterations  number of iterations. Choose appropriate values
	 *   by observing the convergence of energy.  A typical value is 100.
	 *                      
	 */
	public void minimizeEnergy(final int nrIterations) {
		if (nodeNr <= 1) return;

		final float finalRepuFactor = computeRepuFactor();
		repuFactor = finalRepuFactor;

		// compute initial energy
		computeBaryCenter();
		OctTree octTree = buildOctTree(); // for efficient repulsion computation
//		printStatistics(octTree);
		float energySum = 0.0f;
		for (int i = 0; i < nodeNr; i++) energySum += getEnergy(i, octTree);
//		System.out.println("initial energy " + energySum);

		// minimize energy
		final float[] oldPos = new float[3];
		final float[] bestDir = new float[3];
		for (int step = 1; step <= nrIterations; step++) {

            if( progress != null ) {
                progress.update( );
//                try {
//                    Thread.sleep(25);
//                } catch ( InterruptedException e ) {
//                    e.printStackTrace();
//                }
            }
            
            computeBaryCenter();
			octTree = buildOctTree();

			// except in the last 20 iterations, vary the repulsion factor
			// according to repuStrategy
			repuFactor = finalRepuFactor;
			if (step/repuStrategy.length < (nrIterations-20)/repuStrategy.length) {
				repuFactor *= (float)Math.pow(repuStrategy[step%repuStrategy.length], 
											  attrExponent-repuExponent);
			} 

			// move each node
			energySum = 0.0f;
			for (int i = 0; i < nodeNr; i++) {
				final float oldEnergy = getEnergy(i, octTree);
					
				// compute direction of the move of the node
				getDirection(i, octTree, bestDir);

				// line search: compute length of the move
				oldPos[0] = pos[i][0]; oldPos[1] = pos[i][1]; oldPos[2] = pos[i][2]; 
				float bestEnergy = oldEnergy;
				int bestMultiple = 0;
				bestDir[0] /= 32; bestDir[1] /= 32; bestDir[2] /= 32;
				for (int multiple = 32;
					 multiple >= 1 && (bestMultiple==0 || bestMultiple/2==multiple);
					 multiple /= 2) {
					pos[i][0] = oldPos[0] + bestDir[0] * multiple;
					pos[i][1] = oldPos[1] + bestDir[1] * multiple; 
					pos[i][2] = oldPos[2] + bestDir[2] * multiple; 
					float curEnergy = getEnergy(i, octTree);
					if (curEnergy < bestEnergy) {
						bestEnergy = curEnergy;
						bestMultiple = multiple;
					}
				}

				for (int multiple = 64; 
					 multiple <= 128 && bestMultiple == multiple/2; 
					 multiple *= 2) {
					pos[i][0] = oldPos[0] + bestDir[0] * multiple;
					pos[i][1] = oldPos[1] + bestDir[1] * multiple; 
					pos[i][2] = oldPos[2] + bestDir[2] * multiple; 
					float curEnergy = getEnergy(i, octTree);
					if (curEnergy < bestEnergy) {
						bestEnergy = curEnergy;
						bestMultiple = multiple;
					}
				}

				pos[i][0] = oldPos[0] + bestDir[0] * bestMultiple;
				pos[i][1] = oldPos[1] + bestDir[1] * bestMultiple; 
				pos[i][2] = oldPos[2] + bestDir[2] * bestMultiple;
				if (bestMultiple > 0) {
					octTree.moveNode(oldPos, pos[i], repuWeights[i]);
				}
				energySum += bestEnergy;
			}
//			System.out.println("iteration " + step 
//			  + "   energy " + energySum
//			  + "   repulsion " + repuFactor);
		}
//		printStatistics(octTree);
	}

	/**
	 * Chooses a factor for the repulsion energy such that the maximum distances 
	 * in the resulting layout approximate (very) roughly the square root 
	 * of the sum of the repuWeights.  This is appropriate when each graph node 
	 * is visualized as a geometric object whose area is the node's repuWeight.
	 */
    private float computeRepuFactor() {
        float attrSum = 0.0f;
        for (int i = 0; i < nodeNr; i++) {
            for (int j = 0; j < attrWeights[i].length; j++) {
                attrSum += attrWeights[i][j];
            }
        }
		float repuSum = 0.0f;
		for (int i = 0; i < nodeNr; i++) repuSum += repuWeights[i];
		if (repuSum > 0 && attrSum > 0) {
			return attrSum / repuSum / repuSum
				* (float)Math.pow(repuSum, 0.5f*(attrExponent-repuExponent));
		}
		return 1.0f;
    }


	/**
	 * Returns the Euclidean distance between the positions pos1 and pos2.
	 * @return Euclidean distance between the positions pos1 and pos2
	 */
    private final float getDist(final float[] pos1, final float[] pos2) {
        float xDiff = pos1[0] - pos2[0];
        float yDiff = pos1[1] - pos2[1];
        float zDiff = pos1[2] - pos2[2];
        return (float)Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
    }


	/** 
	 * Returns the repulsion energy between the node with the specified index
	 * and the nodes in the octtree.
	 * 
	 * @param index  index of the node
	 * @param tree   octtree containing repulsing nodes
	 * @return repulsion energy between the node with the specified index
	 * 		   and the nodes in the octtree
	 */
	private float getRepulsionEnergy(final int index, final OctTree tree) {
		if (tree == null || tree.index == index || index >= repuWeights.length) {
			return 0.0f;
		}
		
		float dist = getDist(pos[index], tree.position);
		if (tree.index < 0 && dist < 2.0f * tree.width()) {
			float energy = 0.0f;
			for (int i = 0; i < tree.children.length; i++) {
				energy += getRepulsionEnergy(index, tree.children[i]);
			}
			return energy;
		} 
		
		if (repuExponent == 0.0f) {
			return -repuFactor * repuWeights[index] * tree.weight * (float)Math.log(dist);
		} else {
			return -repuFactor * repuWeights[index] * tree.weight
				* (float)Math.pow(dist, repuExponent) / repuExponent;
		}
	}

	/** 
	 * Returns the attraction energy of the node with the specified index.
	 * @param index  index of the node
	 * @return attraction energy of the node with the specified index
	 */
	private float getAttractionEnergy(final int index) {
		float energy = 0.0f;
		for (int i = 0; i < attrIndexes[index].length; i++) {
			if (attrIndexes[index][i] != index) {
				float dist = getDist(pos[attrIndexes[index][i]], pos[index]);
				energy += attrWeights[index][i] * Math.pow(dist, attrExponent) / attrExponent;
			}
		}
		return energy;
	}
	
	/** 
	 * Returns the gravitation energy of the node with the specified index.
	 * @param index  index of the node
	 * @return gravitation energy of the node with the specified index
	 */
	private float getGravitationEnergy(final int index) {
        float dist = getDist(pos[index], baryCenter);
        return gravFactor * repuFactor * repuWeights[index] * (float)Math.pow(dist, attrExponent) / attrExponent;
	}

	/**
	 * Returns the energy of the node with the specified index.
	 * @param   index   index of the node
	 * @return  energy of the node with the specified index
 	 */
    private float getEnergy(final int index, final OctTree octTree) {
		return getRepulsionEnergy(index, octTree)
			+ getAttractionEnergy(index) + getGravitationEnergy(index);
    }


	/**
	 * Computes the direction of the repulsion force from the tree 
	 *     on the specified node.
	 * @param  index index of the node
	 * @param  tree  repulsing octtree
	 * @param  dir   direction of the repulsion force acting on the node
	 * 				 is added to this variable (output parameter)
	 * @return approximate second derivation of the repulsion energy
	 */
	private float addRepulsionDir(final int index, final OctTree tree, final float[] dir) {
		if (tree == null || tree.index == index || repuWeights[index] == 0.0f) {
			return 0.0f;
		}
		
		float dist = getDist(pos[index], tree.position);
		if (tree.index < 0 && dist < 2.0f * tree.width()) {
			float dir2 = 0.0f;
			for (int i = 0; i < tree.children.length; i++) {
				dir2 += addRepulsionDir(index, tree.children[i], dir);
			}
			return dir2;
		} 

		if (dist != 0.0) {
			float tmp =   repuFactor * repuWeights[index] * tree.weight 
					    * (float)Math.pow(dist, repuExponent-2);
			for (int j = 0; j < 3; j++) {
				dir[j] -= (tree.position[j] - pos[index][j]) * tmp;
			}
			return tmp * Math.abs(repuExponent-1);
		}
		
		return 0.0f;
	}

	/**
	 * Computes the direction of the attraction force on the specified node.
	 * @param  index   index of the node
	 * @param  dir     direction of the attraction force acting on the node
	 * 				   is added to this variable (output parameter)
	 * @return approximate second derivation of the attraction energy
	 */
	private float addAttractionDir(final int index, final float[] dir) {
		float dir2 = 0.0f;
		for (int i = 0; i < attrIndexes[index].length; i++) {
			if (attrIndexes[index][i] == index) continue;
			float dist = getDist(pos[attrIndexes[index][i]], pos[index]);
			if (dist == 0.0f) continue;
			float tmp = attrWeights[index][i] * (float)Math.pow(dist, attrExponent-2);
			dir2 += tmp * Math.abs(attrExponent-1);
			for (int j = 0; j < 3; j++) {
				dir[j] += (pos[attrIndexes[index][i]][j] - pos[index][j]) * tmp;
			}
		}
		return dir2;
	}

	/**
	 * Computes the direction of the gravitation force on the specified node.
	 * @param  index   index of the node
	 * @param  dir     direction of the gravitation force acting on the node
	 * 				   is added to this variable (output parameter)
	 * @return approximate second derivation of the gravitation energy
	 */
	private float addGravitationDir(final int index, final float[] dir) {
        float dist = getDist(pos[index], baryCenter);
		float tmp = gravFactor * repuFactor * repuWeights[index] * (float)Math.pow(dist, attrExponent-2);
        for (int j = 0; j < 3; j++) {
            dir[j] += (baryCenter[j] - pos[index][j]) * tmp;
        }
		return tmp * Math.abs(attrExponent-1);
	}
		
	/**
	 * Computes the direction of the total force acting on the specified node.
	 * @param  index   index of a node
	 * @param  dir     direction of the total force acting on the node
	 *                 (output parameter)
	 */
    private void getDirection(final int index, final OctTree octTree, final float[] dir) {
		dir[0] = 0.0f; dir[1] = 0.0f; dir[2] = 0.0f;

		float dir2 = addRepulsionDir(index, octTree, dir);
		dir2 += addAttractionDir(index, dir);
		dir2 += addGravitationDir(index, dir);

		if (dir2 != 0.0f) {
			// normalize force vector with second derivation of energy
			dir[0] /= dir2; dir[1] /= dir2; dir[2] /= dir2;
         
			// ensure that the length of dir is at most 1/8
			// of the maximum Euclidean distance between nodes
			float length = (float)Math.sqrt(dir[0]*dir[0] + dir[1]*dir[1] + dir[2]*dir[2]);
			if (length > octTree.width()/8) {
				length /= octTree.width()/8;
				dir[0] /= length; dir[1] /= length; dir[2] /= length;
			}
		} else {
			dir[0] = 0.0f; dir[1] = 0.0f; dir[2] = 0.0f;
		}
    }    


	/**
	 * Builds the octtree.
	 */
	private OctTree buildOctTree() {
		// compute mimima and maxima of positions in each dimension
		float[] minPos = { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE };
		float[] maxPos = {-Float.MAX_VALUE,-Float.MAX_VALUE,-Float.MAX_VALUE };
		for (int i = 0; i < repuWeights.length; i++) {
			if (repuWeights[i] == 0.0f) continue;
			for (int j = 0; j < 3; j++) {
				minPos[j] = Math.min(pos[i][j], minPos[j]);
				maxPos[j] = Math.max(pos[i][j], maxPos[j]);
			}
		}
		
		// add nodes with non-zero repuWeight to the octtree
		OctTree result = null;
		for (int i = 0; i < repuWeights.length; i++) {
			if (repuWeights[i] == 0.0f) continue;
			if (result == null) {
				result = new OctTree(i, pos[i], repuWeights[i], minPos, maxPos);
			} else {
				result.addNode(i, pos[i], repuWeights[i], 0);
			}
		}
		return result;
	}

    /** 
     * Computes the position of the barycenter of all nodes
     * and stores it in the attribute <code>baryCenter</code>.
     */
    private void computeBaryCenter() {
        baryCenter[0] = 0.0f; baryCenter[1] = 0.0f; baryCenter[2] = 0.0f;
		float repuWeightSum = 0.0f;
        for (int i = 0; i < nodeNr; i++) {
			repuWeightSum += repuWeights[i];
            baryCenter[0] += repuWeights[i] * pos[i][0];
            baryCenter[1] += repuWeights[i] * pos[i][1];
            baryCenter[2] += repuWeights[i] * pos[i][2];
        }
		if (repuWeightSum > 0.0f) {
	        baryCenter[0] /= repuWeightSum;
	        baryCenter[1] /= repuWeightSum;
	        baryCenter[2] /= repuWeightSum;
		}
    }

	/**
	 * Octtree for graph nodes with positions in 3D space.
	 * Contains all graph nodes that are located in a given cuboid in 3D space.
	 * 
	 * @author Andreas Noack
	 */
	static class OctTree {
		/** For leafs, the unique index of the graph node; for non-leafs -1. */
		protected int index;
		/** Children of this tree node. */
		protected OctTree[] children = new OctTree[8];
		/** Barycenter of the contained graph nodes. */
		protected float[] position;
		/** Total weight of the contained graph nodes. */
		protected float weight;
		/** Minimum coordinates of the cuboid in each of the 3 dimensions. */
		protected float[] minPos;
		/** Maximum coordinates of the cuboid in each of the 3 dimensions. */
		protected float[] maxPos;
		
		/**
		 * Creates an octtree containing one graph node.
		 *  
		 * @param index    unique index of the graph node
		 * @param position position of the graph node
		 * @param weight   weight of the graph node
		 * @param minPos   minimum coordinates of the cuboid
		 * @param maxPos   maximum coordinates of the cuboid
		 */
		public OctTree(int index, float[] position, float weight, float[] minPos, float[] maxPos) {
			this.index = index;
			this.position = new float[] { position[0], position[1], position[2] };
			this.weight = weight;
			this.minPos = minPos;
			this.maxPos = maxPos;
		}
		
		/**
		 * Adds a graph node to the octtree.
		 * 
		 * @param nodeIndex  unique nonnegative index of the graph node
		 * @param nodePos    position of the graph node
		 * @param nodeWeight weight of the graph node
		 */
		public void addNode(int nodeIndex, float[] nodePos, float nodeWeight, int depth) {
			if (depth > 20) {
				System.out.println("OctTree: Graph node dropped because tree depth > 20.");
				System.out.println("Graph node position: " + nodePos[0] + " " + nodePos[1] + " " + nodePos[2] + ".");
				System.out.println("Tree node position: " + position[0] + " " + position[1] + " " + position[2] + ".");
				return;
			}
			
			if (index >= 0) {
				addNode2(index, position, weight, depth);
				index = -1;
			}

			for (int i = 0; i < 3; i++) {
				position[i] = (position[i]*weight + nodePos[i]*nodeWeight) / (weight+nodeWeight);
			}
			weight += nodeWeight;
			
			addNode2(nodeIndex, nodePos, nodeWeight, depth);
		}
		
		/**
		 * Adds a graph node to the octtree, 
		 * without changing the position and weight of the root.
		 * 
		 * @param nodeIndex  unique index of the graph node
		 * @param nodePos    position of the graph node
		 * @param nodeWeight weight of the graph node
		 */
		protected void addNode2(int nodeIndex, float[] nodePos, float nodeWeight, int depth) {
			int childIndex = 0;
			for (int i = 0; i < 3; i++) {
				if (nodePos[i] > (minPos[i]+maxPos[i])/2) {
					childIndex += 1 << i;
				}
			}
			
			if (children[childIndex] == null) {
				float[] newMinPos = new float[3]; 			
				float[] newMaxPos = new float[3];
				for (int i = 0; i < 3; i++) {
					if ((childIndex & 1<<i) == 0) {
						newMinPos[i] = minPos[i];
						newMaxPos[i] = (minPos[i] + maxPos[i]) / 2;
					} else {
						newMinPos[i] = (minPos[i] + maxPos[i]) / 2;
						newMaxPos[i] = maxPos[i];
					}
				}
				children[childIndex] = new OctTree(nodeIndex, nodePos, nodeWeight, newMinPos, newMaxPos);
			} else {
				children[childIndex].addNode(nodeIndex, nodePos, nodeWeight, depth+1);
			}
		}
		
		/**
		 * Updates the positions of the octtree nodes 
		 * when the position of a graph node has changed.
		 * 
		 * @param oldPos     previous position of the graph node
		 * @param newPos     new position of the graph node
		 * @param nodeWeight weight of the graph node
		 */
		public void moveNode(float[] oldPos, float[] newPos, float nodeWeight) {
			for (int i = 0; i < 3; i++) {
				position[i] += (newPos[i]-oldPos[i]) * (nodeWeight/weight);
			}
			
			int childIndex = 0;
			for (int i = 0; i < 3; i++) {
				if (oldPos[i] > (minPos[i]+maxPos[i])/2) {
					childIndex += 1 << i;
				}
			}
			if (children[childIndex] != null) {
				children[childIndex].moveNode(oldPos, newPos, nodeWeight);
			}
		}

		/**
		 * Returns the maximum extension of the octtree.
		 * 
		 * @return maximum over all dimensions of the extension of the octtree
		 */
		public float width() {
			float width = 0.0f;
			for (int i = 0; i < 3; i++) {
				if (maxPos[i] - minPos[i] > width) {
					width = maxPos[i] - minPos[i];
				}
			}
			return width;
		}

	}

}