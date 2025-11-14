package com.example.demo.showcase;

/**
 * Demonstrates ojAlgo - Linear Algebra and Optimization
 * Covers matrices, linear programming, and optimization
 */
public class OjAlgoShowcase {

    public static void demonstrate() {
        System.out.println("\n========== OJALGO SHOWCASE ==========\n");

        System.out.println("--- ojAlgo Overview ---");
        System.out.println("Mathematics, linear algebra, and optimization library\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.ojalgo</groupId>
                <artifactId>ojalgo</artifactId>
                <version>53.1.1</version>
            </dependency>
            """);

        System.out.println("\n--- Matrices ---");
        System.out.println("""
            import org.ojalgo.matrix.store.*;
            
            // Create matrix
            MatrixStore<Double> matrix = PrimitiveDenseStore.FACTORY.rows(
                new double[][] {
                    {1, 2, 3},
                    {4, 5, 6},
                    {7, 8, 9}
                }
            );
            
            // Matrix operations
            MatrixStore<Double> transposed = matrix.transpose();
            MatrixStore<Double> multiplied = matrix.multiply(matrix);
            
            // Get element
            double element = matrix.get(0, 0);
            
            // Print matrix
            System.out.println(matrix);
            """);

        System.out.println("\n--- Linear Equations (Ax = b) ---");
        System.out.println("""
            import org.ojalgo.matrix.*;
            
            // Solve Ax = b
            PrimitiveDenseStore A = PrimitiveDenseStore.FACTORY.rows(
                new double[][] {
                    {2, 1},
                    {1, 3}
                }
            );
            
            PrimitiveDenseStore b = PrimitiveDenseStore.FACTORY.column(
                new double[] {5, 5}
            );
            
            MatrixStore<Double> x = A.solve(b);
            System.out.println("Solution: " + x);
            """);

        System.out.println("\n--- Linear Programming ---");
        System.out.println("""
            import org.ojalgo.optimisation.*;
            import org.ojalgo.optimisation.linear.*;
            
            // Maximize: 3x + 4y
            // Subject to:
            //   x + 2y <= 14
            //   3x - y >= 0
            //   x - y <= 2
            //   x, y >= 0
            
            ExpressionsBasedModel model = new ExpressionsBasedModel();
            
            Variable x = model.addVariable("x").lower(0);
            Variable y = model.addVariable("y").lower(0);
            
            // Objective function (maximize)
            Expression objective = model.addExpression("Profit").weight(1);
            objective.set(x, 3);
            objective.set(y, 4);
            
            // Constraints
            Expression c1 = model.addExpression("C1").upper(14);
            c1.set(x, 1);
            c1.set(y, 2);
            
            Expression c2 = model.addExpression("C2").lower(0);
            c2.set(x, 3);
            c2.set(y, -1);
            
            Expression c3 = model.addExpression("C3").upper(2);
            c3.set(x, 1);
            c3.set(y, -1);
            
            // Solve
            Optimisation.Result result = model.maximise();
            
            System.out.println("Optimal value: " + result.getValue());
            System.out.println("x = " + result.get(0));
            System.out.println("y = " + result.get(1));
            """);

        System.out.println("\n--- Eigenvalues and Eigenvectors ---");
        System.out.println("""
            import org.ojalgo.matrix.decomposition.*;
            
            PrimitiveDenseStore matrix = PrimitiveDenseStore.FACTORY.rows(
                new double[][] {
                    {4, -2},
                    {1, 1}
                }
            );
            
            Eigenvalue<Double> evd = Eigenvalue.PRIMITIVE.make();
            evd.decompose(matrix);
            
            // Get eigenvalues
            MatrixStore<Double> eigenvalues = evd.getD();
            System.out.println("Eigenvalues: " + eigenvalues);
            
            // Get eigenvectors
            MatrixStore<Double> eigenvectors = evd.getV();
            System.out.println("Eigenvectors: " + eigenvectors);
            """);

        System.out.println("\n--- SVD (Singular Value Decomposition) ---");
        System.out.println("""
            SingularValue<Double> svd = SingularValue.PRIMITIVE.make();
            svd.decompose(matrix);
            
            MatrixStore<Double> U = svd.getU();
            MatrixStore<Double> S = svd.getD();
            MatrixStore<Double> V = svd.getV();
            
            // Reconstruct matrix: A = U * S * V^T
            MatrixStore<Double> reconstructed = U.multiply(S).multiply(V.transpose());
            """);

        System.out.println("\n--- Statistics ---");
        System.out.println("""
            import org.ojalgo.array.*;
            
            Array1D<Double> data = Array1D.PRIMITIVE64.copy(
                1.0, 2.0, 3.0, 4.0, 5.0
            );
            
            double mean = PrimitiveScalar.of(data.stream()
                .average()
                .orElse(0.0)).doubleValue();
            
            double variance = data.stream()
                .map(v -> Math.pow(v - mean, 2))
                .average()
                .orElse(0.0);
            
            double stdDev = Math.sqrt(variance);
            
            System.out.println("Mean: " + mean);
            System.out.println("Std Dev: " + stdDev);
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use appropriate matrix types");
        System.out.println("   ✓ Reuse decompositions");
        System.out.println("   ✓ Check matrix dimensions");
        System.out.println("   ✓ Handle numerical instability");
        System.out.println();
    }
}
