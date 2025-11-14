package com.example.demo.showcase;

/**
 * Demonstrates SMILE - Statistical Machine Intelligence and Learning Engine
 * Covers machine learning algorithms, classification, regression, clustering
 */
public class SMILEShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SMILE MACHINE LEARNING SHOWCASE ==========\n");

        System.out.println("--- SMILE Overview ---");
        System.out.println("Comprehensive machine learning library\n");

        System.out.println("Key features:");
        System.out.println("   • Classification (SVM, Random Forest, Neural Networks)");
        System.out.println("   • Regression (Linear, Lasso, Ridge, Random Forest)");
        System.out.println("   • Clustering (K-Means, DBSCAN, Hierarchical)");
        System.out.println("   • Dimensionality Reduction (PCA, t-SNE, UMAP)");
        System.out.println("   • Feature selection and preprocessing");

        System.out.println("\nDependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.github.haifengl</groupId>
                <artifactId>smile-core</artifactId>
                <version>3.0.2</version>
            </dependency>
            """);

        System.out.println("\n--- Data Preparation ---");
        System.out.println("""
            import smile.data.*;
            import smile.io.*;
            
            // Load CSV
            DataFrame df = Read.csv("data.csv");
            
            // Create dataset manually
            double[][] x = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
            };
            int[] y = {0, 1, 0};
            
            // Train-test split
            int[] indices = MathEx.permutate(x.length);
            int trainSize = (int) (x.length * 0.8);
            
            double[][] trainX = new double[trainSize][];
            int[] trainY = new int[trainSize];
            double[][] testX = new double[x.length - trainSize][];
            int[] testY = new int[x.length - trainSize];
            
            // Split data...
            """);

        System.out.println("\n--- Classification ---");
        System.out.println("""
            import smile.classification.*;
            
            // Random Forest
            RandomForest rf = RandomForest.fit(
                Formula.lhs("class"),
                data,
                100,  // Number of trees
                20,   // Max depth
                5,    // Min node size
                1.0   // Sample rate
            );
            
            // Predict
            int prediction = rf.predict(newSample);
            int[] predictions = rf.predict(testX);
            
            // Logistic Regression
            LogisticRegression lr = LogisticRegression.fit(trainX, trainY);
            
            // SVM
            SVM<double[]> svm = SVM.fit(
                trainX,
                trainY,
                new GaussianKernel(1.0),
                1.0,  // C parameter
                0.001 // Tolerance
            );
            
            // Naive Bayes
            NaiveBayes nb = NaiveBayes.fit(trainX, trainY);
            
            // Decision Tree
            DecisionTree dt = DecisionTree.fit(trainX, trainY);
            
            // Gradient Boosting
            GradientTreeBoost gbm = GradientTreeBoost.fit(
                Formula.lhs("class"),
                data,
                100   // Number of trees
            );
            """);

        System.out.println("\n--- Model Evaluation ---");
        System.out.println("""
            import smile.validation.*;
            import smile.validation.metric.*;
            
            // Accuracy
            double accuracy = Accuracy.of(actualY, predictedY);
            
            // Confusion Matrix
            ConfusionMatrix cm = ConfusionMatrix.of(actualY, predictedY);
            System.out.println("Accuracy: " + cm.accuracy);
            System.out.println("Precision: " + cm.precision);
            System.out.println("Recall: " + cm.recall);
            System.out.println("F1 Score: " + cm.f1);
            
            // Cross-validation
            CrossValidation cv = CrossValidation.classification(
                10,  // 10-fold CV
                trainX,
                trainY,
                (x, y) -> RandomForest.fit(Formula.lhs("class"), 
                    DataFrame.of(x).merge(Vector.of("class", y)))
            );
            
            System.out.println("CV Accuracy: " + cv.avg);
            System.out.println("CV Std Dev: " + cv.sd);
            """);

        System.out.println("\n--- Regression ---");
        System.out.println("""
            import smile.regression.*;
            
            // Linear Regression
            OLS ols = OLS.fit(Formula.lhs("y"), data);
            
            // Ridge Regression (L2 regularization)
            RidgeRegression ridge = RidgeRegression.fit(trainX, trainY, 0.1);
            
            // Lasso (L1 regularization)
            LASSO lasso = LASSO.fit(trainX, trainY, 0.1);
            
            // Random Forest Regression
            RandomForest rfr = RandomForest.fit(
                Formula.lhs("target"),
                data,
                100
            );
            
            // Predict
            double prediction = ols.predict(newSample);
            
            // Evaluation
            double rmse = RMSE.of(actualY, predictedY);
            double mae = MAE.of(actualY, predictedY);
            double r2 = R2.of(actualY, predictedY);
            """);

        System.out.println("\n--- Clustering ---");
        System.out.println("""
            import smile.clustering.*;
            
            // K-Means
            KMeans kmeans = KMeans.fit(data, 3);  // 3 clusters
            int[] clusters = kmeans.y;
            
            // DBSCAN (density-based)
            DBSCAN<double[]> dbscan = DBSCAN.fit(
                data,
                new EuclideanDistance(),
                5,    // Min points
                0.5   // Epsilon
            );
            
            // Hierarchical Clustering
            HierarchicalClustering hc = HierarchicalClustering.fit(
                Linkage.COMPLETE,
                pdist(data)
            );
            
            // Get clusters at height
            int[] labels = hc.partition(3);
            """);

        System.out.println("\n--- Dimensionality Reduction ---");
        System.out.println("""
            import smile.manifold.*;
            
            // PCA
            PCA pca = PCA.fit(data);
            double[][] reduced = pca.project(data);
            
            // Get components
            double[][] components = pca.loadings();
            double[] variance = pca.variance();
            
            // t-SNE
            TSNE tsne = new TSNE(data, 2);  // Reduce to 2D
            double[][] embedded = tsne.coordinates;
            
            // UMAP
            UMAP umap = new UMAP(data, 2);
            double[][] umapEmbedding = umap.coordinates;
            """);

        System.out.println("\n--- Feature Selection ---");
        System.out.println("""
            import smile.feature.*;
            
            // Feature importance from Random Forest
            double[] importance = rf.importance();
            
            // Select top features
            int[] selected = Arrays.stream(importance)
                .boxed()
                .sorted(Collections.reverseOrder())
                .limit(10)
                .mapToInt(i -> i)
                .toArray();
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Normalize/standardize features");
        System.out.println("   ✓ Use cross-validation");
        System.out.println("   ✓ Tune hyperparameters");
        System.out.println("   ✓ Handle missing values");
        System.out.println("   ✓ Check for overfitting");
        System.out.println();
    }
}
