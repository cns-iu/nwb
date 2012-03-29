package edu.iu.iv.analysis.pathfindernetworkscaling;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;

/**
 * @author Shashikant Penumarthy
 */
public class PathFinder {

    private int q, r, n, percentageDone;

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
            throws PathFinderParameterException {
        if (matrix.rows() != matrix.columns())
            throw new PathFinderParameterException("Matrix must be square!");
        this.weightMatrix = matrix;

        if (r < 0)
            throw new PathFinderParameterException(
                    "r parameter must be non-negative.");
        this.r = r;

        if (q > matrix.rows() - 1)
            throw new PathFinderParameterException(
                    "q parameter cannot be greater than number of rows/columns of matrix minus 1.");
        this.q = q;

        this.n = this.weightMatrix.rows();
    }

    public void applyScaling() {
        // allocate memory for d matrix
        DoubleMatrix2D distanceMatrix = new SparseDoubleMatrix2D(n, n);
        // copy weight matrix into it
        distanceMatrix = copyMatrixValues(this.weightMatrix, distanceMatrix);

        // allocate memory for higher order matrix
        DoubleMatrix2D higherOrderMatrix = new SparseDoubleMatrix2D(n, n);
        // copy weight matrix into it
        higherOrderMatrix = copyMatrixValues(this.weightMatrix,
                higherOrderMatrix);

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
            if (i % 10 == 0) {
                float per = ((float)i/q)*100 ;
                //System.out.print((int)per + "%..") ;
                percentageDone = (int) per;
            }
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
     * @return the percentage done computing while computing
     */
    public int getPercentageDone() {
        return percentageDone;
    }
    
    /**
     * The final step in the PFNET algorithm, where the 'q' <sup>th </sup>
     * distance matrix (D <sup>q </sup>) and the original weight matrix (W (
     * <sup>1 </sup>) are compared. An edge is allowed to be an edge in the
     * PFNET whenever the corresponding elements of D <sup>q </sup> and W <sup>1
     * </sup> are the same (and not zero). <br>
     * This operation may result in a substantially sparser matrix. The original
     * weight matrix is already expected to be sparse. In addition to that,
     * during the scaling process may edges will be eliminated. This is taken
     * advantage of by returning a matrix with only the non-zero values filled
     * in. For very large networks, this will probably result in a lot of
     * spacing saving.
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
                double valWeight = this.weightMatrix.get(i, j);
                double valD = distanceMatrixQ.get(i, j);
                // add to PFNET if they are equal
                if (valWeight == valD) {
                    finalMatrix.set(i, j, valD);
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
                double valD = distanceMatrix.get(i, j);
                double valH = higherOrderMatrix.get(i, j);
                if (valD > 0 && valH > 0)
                    distanceMatrix.set(i, j, Math.min(valD, valH));
            }
        return distanceMatrix;
    }

    /**
     * Copies contents of the upper triangle of one matrix into another.
     * 
     * @param matrixSrc
     *            The source matrix.
     * @param matrixDest
     *            The destination matrix.
     * @return The destination matrix with the contents copied.
     */
    private DoubleMatrix2D copyMatrixValues(
            final DoubleMatrix2D matrixSrc, DoubleMatrix2D matrixDest) {
        int rows = matrixSrc.rows();
        int cols = matrixSrc.columns();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < cols; ++j) {
                double val = matrixSrc.get(i, j);
                if (val != 0)
                    matrixDest.set(i, j, val);
            }
        return matrixDest;
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
    public DoubleMatrix2D computeHigherOrderMatrix(
            DoubleMatrix2D lowerOrderMatrix) {

        DoubleMatrix2D higherOrderMatrix = new SparseDoubleMatrix2D(n, n);

        if (this.r == 0) {
            // r parameter is infinity, use MAX
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    double minWeight = Double.POSITIVE_INFINITY;
                    for (int m = 0; m < n; ++m) {
                        double temp = Math.max(lowerOrderMatrix.get(m, k),
                                this.weightMatrix.get(j, m));
                        if (temp == 0)
                            continue ;
                        if (minWeight > temp)
                            minWeight = temp;
                    }
                    if (minWeight != Double.POSITIVE_INFINITY)
                        higherOrderMatrix.set(j, k, minWeight);
                }
            }
        } else {
            // else use Minkowski metric
            for (int j = 0; j < n; ++j) {
                for (int k = 0; k < n; ++k) {
                    double minWeight = Double.POSITIVE_INFINITY;
                    for (int m = 0; m < n; ++m) {
                        double temp = Math.pow(Math.pow(this.weightMatrix.get(
                                j, m), this.r)
                                + Math.pow(lowerOrderMatrix.get(m, k), this.r),
                                1.0 / this.r);
                        if (temp == 0)
                            continue ;
                        if (minWeight > temp)
                            minWeight = temp;
                    }
                    if (minWeight != Double.POSITIVE_INFINITY)
                        higherOrderMatrix.set(j, k, minWeight);
                }
            }
        }
        return higherOrderMatrix;
    }

    public DoubleMatrix2D getResultMatrix() {
        if (this.pfnet == null)
            this.applyScaling();
        return this.pfnet;
    }
}