package edu.iu.nwb.analysis.pathfindergraphnetworkscaling.old;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

/**
 * @author Shashikant Penumarthy
 */
public class PathFinder {

    private int q, r, n;

    private DoubleMatrix2D weightMatrix;

    private DoubleMatrix2D pfnet;

    /**
     * Initializes the PFNET algorithm. The q parameter must be at most
     * <code>n - 1</code> where <code>n</code> is the number of rows (or
     * columns) of the matrix. <br>
     * The r parameter must be non-negative. A value of 0 is taken to mean that
     * the Minkowski metric is to be evaluated in the limit r -&gt; infinity.
     * <br>
     * The input matrix must be a symmetric square matrix. The elements of the
     * matrix are treated as weights of the edges and the rows (and columns) are
     * treated as vertices of a graph. Since only symmetric matrices are
     * considered, this means that this PFNET applies only to <i>undirected </i>
     * graphs. The weights are considered as <i>distance </i>, hence a higher
     * weight indicates higher distance. A weight of 0.0 is assumed on the
     * diagonal since the weight from a vertex to itself is zero. <b>However
     * </b>, a weight of 0.0 at a non-diagonal location is taken to mean that
     * the corresponding edge <b>does not exist </b> in the graph and
     * consequently, the weight is interpreted as being <b>infinite </b>. Hence,
     * only the <b>strictly upper triangular </b> part of the matrix is
     * significant.
     * 
     * @param q
     *            The q parameter of the PFNET(r, q)
     * @param r
     *            The r parameter of the PFNET(r, q)
     * @param matrix
     *            The matrix to which the PFNET is to be applied.
     * @throws IllegalArgumentException
     */
    public PathFinder(int q, int r, DoubleMatrix2D matrix)
            throws IllegalArgumentException {
        int rows = matrix.rows();
		if (rows != matrix.columns())
            throw new IllegalArgumentException("Matrix must be square!");
        this.weightMatrix = matrix;

        if (r < 0)
            throw new IllegalArgumentException(
                    "r parameter must be non-negative.");
        this.r = r;

        if (q > rows - 1)
            throw new IllegalArgumentException(
                    "q parameter cannot be greater than number of rows/columns of matrix minus 1.");
        this.q = q;

        this.n = rows;
    }

    private void applyScaling() {
        DoubleMatrix2D distanceMatrix = this.weightMatrix.copy();

        DoubleMatrix2D higherOrderMatrix = this.weightMatrix.copy();

        // At this point the weightMatrix, distanceMatrix and higherOrderMatrix
        // have the same elements.
        // Now we compute the q higher order matrices. We start from 1 because
        // the first matrix W1 is the same as W. Then after each step we compute
        // the distanceMatrix. To understand what is going on here, refer to
        // "Roger W. Schvaneveldt (Ed) 1989, Pathfinder Associative Networks:
        // Studies in
        // Knowledge Organization, Ablex Publishing, New Jersey pp (7 - 8)",
        // chapter 1, the section
        // titled "Properties of Pathfinder Networks", section on "Generation
        // Algorithms
        // for PFNETs"
        //System.out.print("Computing..") ;
        for (int i = 1; i < q; ++i) {
            higherOrderMatrix = computeHigherOrderMatrix(higherOrderMatrix);
            distanceMatrix = computeMinimumDistanceMatrix(distanceMatrix,
                    higherOrderMatrix);
        }
        //System.out.println("done") ;
        // at the end compare Dq and W1 and set the edges in the PFNET
        // accordingly.
        this.pfnet = compareWithWeightMatrix(distanceMatrix);
    }
    
    
    
    /**
     * The final step in the PFNET algorithm, where the 'q' <sup>th </sup>
     * distance matrix (D <sup>q </sup>) and the original weight matrix (W (
     * <sup>1 </sup>) are compared. An edge is allowed to be an edge in the
     * PFNET whenever the corresponding elements of D <sup>q </sup> and W <sup>1
     * </sup> are the same (and not zero). <br>
     * This operation may result in a substantially sparser matrix. The original
     * weight matrix is already expected to be sparse. During the scaling process
     * many edges will be eliminated.
     * 
     * @param distanceMatrixQ
     *            The distance matrix D <sup>q </sup>.
     * @return The result of the comparison: the PFNET(r, q).
     */
    private DoubleMatrix2D compareWithWeightMatrix(
            DoubleMatrix2D distanceMatrixQ) {
        DoubleMatrix2D finalMatrix = new SparseDoubleMatrix2D(n, n);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                double valWeight = this.weightMatrix.getQuick(i, j);
                double valD = distanceMatrixQ.getQuick(i, j);
                // add to PFNET if they are equal
                if (valWeight == valD) {
                    finalMatrix.setQuick(i, j, valD);
                }
            }
        }
        return finalMatrix;
    }

    /**
     * Computes the minimum distance matrix given the current distance matrix
     * and the higher order matrix.
     * 
     * @param distanceMatrix
     *            The current distance matrix.
     * @param higherOrderMatrix
     *            The higher order matrix.
     * @return The new minimum distance matrix.
     */
    private DoubleMatrix2D computeMinimumDistanceMatrix(
            DoubleMatrix2D distanceMatrix,
            final DoubleMatrix2D higherOrderMatrix) {
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j) {
                if (i == j)
                    continue;
                double valD = distanceMatrix.getQuick(i, j);
                double valH = higherOrderMatrix.getQuick(i, j);
                if (valH > 0) //previously if (valD > 0 && valH >0)
                	//but if valD = 0 we don't mind replacing it w/ the min, as that will be zero
                    distanceMatrix.setQuick(i, j, Math.min(valD, valH));
            }
        return distanceMatrix;
    }

    /**
     * Takes a matrix of a lower order (i) as input and computes a higher order
     * matrix (i+1) where each element of the higher order matrix is given by:
     * <br>
     * <br>
     * w <sub>jk </sub> <sup>i+1 </sup>= MIN [ (w <sub>jm </sub>) <sup>r </sup>+
     * (w <sub>jm </sub> <sup>i </sup>) <sup>r </sup>] <sup>1/r </sup> for all 1
     * &lt;= m &lt;= n <br>
     * <br>
     * where n is the number of rows or columns in the matrix, <br>
     * where w_jm is the weight of the edge in the original matrix, <br>
     * w_jm(i) is the weight of the edge in the lower order matrix(i), <br>
     * w_jk(i+1) is the weight of the edge in the higher order matrix(i+1), <br>
     * and r is the 'r' parameter of the PFNET that defines the Minkowski
     * distance between the elements of the matrix (MIN stands for minimum).
     * <br>
     * When r is infinity (represented in the code as zero) the Minkowski metric
     * is replaced by MAX, i.e. it then computes the max of w_jm and w_jm(i).
     * 
     * @param lowerOrderMatrix
     *            The lower order matrix.
     * @return The higher order matrix.
     */
    private DoubleMatrix2D computeHigherOrderMatrix(
            DoubleMatrix2D lowerOrderMatrix) {

        DoubleMatrix2D higherOrderMatrix = new SparseDoubleMatrix2D(n, n);

        if (this.r == 0) {
            // r parameter is infinity, use MAX
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    double minWeight = Double.POSITIVE_INFINITY;
                    for (int m = 0; m < n; ++m) {
                        double temp = Math.max(lowerOrderMatrix.getQuick(m, k),
                                this.weightMatrix.getQuick(j, m));
                        if (temp == 0)
                            continue ;
                        if (minWeight > temp)
                            minWeight = temp;
                    }
                    if (minWeight != Double.POSITIVE_INFINITY)
                        higherOrderMatrix.setQuick(j, k, minWeight);
                }
            }
        } else {
            // else use Minkowski metric
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    double minWeight = Double.POSITIVE_INFINITY;
                    for (int m = 0; m < n; ++m) {
                        double temp = Math.pow(Math.pow(this.weightMatrix.getQuick(
                                j, m), this.r)
                                + Math.pow(lowerOrderMatrix.getQuick(m, k), this.r),
                                1.0 / this.r);
                        if (temp == 0)
                            continue ;
                        if (minWeight > temp)
                            minWeight = temp;
                    }
                    if (minWeight != Double.POSITIVE_INFINITY)
                        higherOrderMatrix.setQuick(j, k, minWeight);
                }
            }
        }
        return higherOrderMatrix;
    }

    /**
     * @return The resulting matrix
     */
    public DoubleMatrix2D getResultMatrix() {
        if (this.pfnet == null)
            this.applyScaling();
        return this.pfnet;
    }
}