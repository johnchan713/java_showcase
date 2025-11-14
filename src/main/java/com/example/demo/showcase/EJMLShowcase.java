package com.example.demo.showcase;

/**
 * Demonstrates EJML - Efficient Java Matrix Library
 * Covers matrix operations, decompositions, and linear algebra
 */
public class EJMLShowcase {

    public static void demonstrate() {
        System.out.println("\n========== EJML SHOWCASE ==========\n");

        System.out.println("--- EJML Overview ---");
        System.out.println("Efficient Java Matrix Library\n");

        System.out.println("Key features:");
        System.out.println("   • Dense and sparse matrices");
        System.out.println("   • Fast matrix operations");
        System.out.println("   • Linear solvers");
        System.out.println("   • Matrix decompositions");
        System.out.println("   • Optimized for performance");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.ejml</groupId>
                <artifactId>ejml-all</artifactId>
                <version>0.43.1</version>
            </dependency>
            """);

        System.out.println("\n--- Creating Matrices ---");
        System.out.println("""
            import org.ejml.simple.SimpleMatrix;
            import org.ejml.data.DMatrixRMaj;
            
            // SimpleMatrix (easiest API)
            SimpleMatrix A = new SimpleMatrix(new double[][] {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
            });
            
            // From array
            SimpleMatrix B = new SimpleMatrix(3, 3, true,
                1, 2, 3, 4, 5, 6, 7, 8, 9
            );
            
            // Identity matrix
            SimpleMatrix I = SimpleMatrix.identity(3);
            
            // Random matrix
            SimpleMatrix R = SimpleMatrix.random_DDRM(3, 3, 0, 1, new Random());
            
            // Zero matrix
            SimpleMatrix Z = new SimpleMatrix(3, 3);
            """);

        System.out.println("\n--- Matrix Operations ---");
        System.out.println("""
            // Addition and subtraction
            SimpleMatrix sum = A.plus(B);
            SimpleMatrix diff = A.minus(B);
            
            // Multiplication
            SimpleMatrix product = A.mult(B);
            SimpleMatrix scaled = A.scale(2.0);
            
            // Element-wise operations
            SimpleMatrix elementMult = A.elementMult(B);
            SimpleMatrix elementDiv = A.elementDiv(B);
            
            // Transpose
            SimpleMatrix transposed = A.transpose();
            
            // Inverse
            SimpleMatrix inverse = A.invert();
            
            // Determinant
            double det = A.determinant();
            
            // Trace
            double trace = A.trace();
            
            // Norm
            double norm = A.normF();  // Frobenius norm
            """);

        System.out.println("\n--- Solving Linear Systems ---");
        System.out.println("""
            import org.ejml.dense.row.CommonOps_DDRM;
            
            // Solve Ax = b
            SimpleMatrix A = new SimpleMatrix(new double[][] {
                {2, 1},
                {1, 3}
            });
            
            SimpleMatrix b = new SimpleMatrix(new double[][] {
                {5},
                {5}
            });
            
            SimpleMatrix x = A.solve(b);
            
            System.out.println("Solution: " + x);
            
            // Least squares (overdetermined system)
            SimpleMatrix leastSquares = A.pseudoInverse().mult(b);
            """);

        System.out.println("\n--- Matrix Decompositions ---");
        System.out.println("""
            import org.ejml.dense.row.decomposition.DecompositionFactory_DDRM;
            
            // LU Decomposition
            SimpleMatrix LU = A.copy();
            // ... decompose
            
            // QR Decomposition
            SimpleMatrix[] qr = A.qr();
            SimpleMatrix Q = qr[0];
            SimpleMatrix R = qr[1];
            
            // SVD (Singular Value Decomposition)
            SimpleMatrix[] svd = A.svd();
            SimpleMatrix U = svd[0];
            SimpleMatrix W = svd[1];  // Singular values
            SimpleMatrix V = svd[2];
            
            // Eigenvalue Decomposition
            SimpleMatrix[] eig = A.eig();
            // Real and imaginary parts of eigenvalues
            """);

        System.out.println("\n--- Advanced Operations ---");
        System.out.println("""
            // Extract submatrix
            SimpleMatrix sub = A.extractMatrix(0, 2, 0, 2);
            
            // Set submatrix
            A.setMatrix(0, 0, B.extractMatrix(0, 2, 0, 2));
            
            // Get/set elements
            double element = A.get(0, 0);
            A.set(0, 0, 10.0);
            
            // Row and column operations
            SimpleMatrix row = A.extractVector(true, 0);  // Extract row
            SimpleMatrix col = A.extractVector(false, 0);  // Extract column
            
            // Concatenate
            SimpleMatrix concat = A.concatRows(B);
            SimpleMatrix hconcat = A.concatColumns(B);
            
            // Reshape
            SimpleMatrix reshaped = A.reshape(1, 9);
            """);

        System.out.println("\n--- Sparse Matrices ---");
        System.out.println("""
            import org.ejml.data.DMatrixSparseCSC;
            import org.ejml.sparse.csc.CommonOps_DSCC;
            
            // Create sparse matrix
            DMatrixSparseCSC sparse = new DMatrixSparseCSC(100, 100, 10);
            
            // Set values
            sparse.set(0, 0, 1.0);
            sparse.set(5, 10, 2.5);
            
            // Sparse operations
            DMatrixSparseCSC result = new DMatrixSparseCSC(100, 100);
            CommonOps_DSCC.mult(sparse, sparse, result);
            """);

        System.out.println("\n--- Performance Tips ---");
        System.out.println("""
            // Reuse matrices to avoid allocation
            SimpleMatrix result = new SimpleMatrix(3, 3);
            
            // Use in-place operations when possible
            A.set(A.mult(B));  // In-place multiplication
            
            // Choose appropriate matrix type
            // - DMatrixRMaj: Row-major dense (default)
            // - FMatrixRMaj: Row-major float
            // - DMatrixSparseCSC: Sparse compressed column
            
            // For very large matrices, consider sparse representation
            if (sparsity > 0.9) {
                // Use sparse matrices
            }
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Reuse matrix objects");
        System.out.println("   ✓ Use SimpleMatrix for ease");
        System.out.println("   ✓ Use sparse matrices when appropriate");
        System.out.println("   ✓ Check matrix dimensions");
        System.out.println("   ✓ Handle singular matrices");
        System.out.println();
    }
}
