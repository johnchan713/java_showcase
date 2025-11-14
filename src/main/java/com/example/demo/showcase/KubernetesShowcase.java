package com.example.demo.showcase;

/**
 * Demonstrates Kubernetes Java Client
 * Covers pods, deployments, services, and cluster management
 */
public class KubernetesShowcase {

    public static void demonstrate() {
        System.out.println("\n========== KUBERNETES JAVA CLIENT SHOWCASE ==========\n");

        System.out.println("--- Kubernetes Client Overview ---");
        System.out.println("Manage Kubernetes resources from Java\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>io.kubernetes</groupId>
                <artifactId>client-java</artifactId>
                <version>19.0.0</version>
            </dependency>
            """);

        System.out.println("\n--- Client Setup ---");
        System.out.println("""
            import io.kubernetes.client.openapi.ApiClient;
            import io.kubernetes.client.openapi.Configuration;
            import io.kubernetes.client.util.Config;
            
            // Default configuration (uses ~/.kube/config)
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            
            // Or from file
            ApiClient client = Config.fromConfig("/path/to/kubeconfig");
            
            // In-cluster configuration (when running in pod)
            ApiClient client = Config.fromCluster();
            """);

        System.out.println("\n--- Pod Management ---");
        System.out.println("""
            import io.kubernetes.client.openapi.apis.CoreV1Api;
            import io.kubernetes.client.openapi.models.*;
            
            CoreV1Api api = new CoreV1Api();
            
            // List pods
            V1PodList podList = api.listNamespacedPod(
                "default",      // namespace
                null,           // pretty
                null,           // allowWatchBookmarks
                null,           // continue
                null,           // fieldSelector
                null,           // labelSelector
                null,           // limit
                null,           // resourceVersion
                null,           // resourceVersionMatch
                null,           // timeoutSeconds
                null            // watch
            );
            
            podList.getItems().forEach(pod -> {
                System.out.println("Pod: " + pod.getMetadata().getName());
                System.out.println("Status: " + pod.getStatus().getPhase());
            });
            
            // Create pod
            V1Pod pod = new V1PodBuilder()
                .withNewMetadata()
                    .withName("my-pod")
                    .withNamespace("default")
                .endMetadata()
                .withNewSpec()
                    .addNewContainer()
                        .withName("nginx")
                        .withImage("nginx:latest")
                        .addNewPort()
                            .withContainerPort(80)
                        .endPort()
                    .endContainer()
                .endSpec()
                .build();
            
            api.createNamespacedPod("default", pod, null, null, null, null);
            
            // Delete pod
            api.deleteNamespacedPod("my-pod", "default", null, null, null, null, null, null);
            """);

        System.out.println("\n--- Deployment Management ---");
        System.out.println("""
            import io.kubernetes.client.openapi.apis.AppsV1Api;
            
            AppsV1Api appsApi = new AppsV1Api();
            
            // Create deployment
            V1Deployment deployment = new V1DeploymentBuilder()
                .withNewMetadata()
                    .withName("my-deployment")
                    .withNamespace("default")
                .endMetadata()
                .withNewSpec()
                    .withReplicas(3)
                    .withNewSelector()
                        .addToMatchLabels("app", "my-app")
                    .endSelector()
                    .withNewTemplate()
                        .withNewMetadata()
                            .addToLabels("app", "my-app")
                        .endMetadata()
                        .withNewSpec()
                            .addNewContainer()
                                .withName("nginx")
                                .withImage("nginx:1.21")
                                .addNewPort()
                                    .withContainerPort(80)
                                .endPort()
                            .endContainer()
                        .endSpec()
                    .endTemplate()
                .endSpec()
                .build();
            
            appsApi.createNamespacedDeployment("default", deployment, null, null, null, null);
            
            // Scale deployment
            V1Scale scale = new V1Scale()
                .spec(new V1ScaleSpec().replicas(5));
            
            appsApi.replaceNamespacedDeploymentScale(
                "my-deployment", "default", scale, null, null, null, null
            );
            """);

        System.out.println("\n--- Service Management ---");
        System.out.println("""
            // Create service
            V1Service service = new V1ServiceBuilder()
                .withNewMetadata()
                    .withName("my-service")
                    .withNamespace("default")
                .endMetadata()
                .withNewSpec()
                    .withType("LoadBalancer")
                    .addToSelector("app", "my-app")
                    .addNewPort()
                        .withProtocol("TCP")
                        .withPort(80)
                        .withTargetPort(new IntOrString(80))
                    .endPort()
                .endSpec()
                .build();
            
            api.createNamespacedService("default", service, null, null, null, null);
            """);

        System.out.println("\n--- ConfigMap and Secrets ---");
        System.out.println("""
            // Create ConfigMap
            V1ConfigMap configMap = new V1ConfigMapBuilder()
                .withNewMetadata()
                    .withName("app-config")
                    .withNamespace("default")
                .endMetadata()
                .addToData("app.properties", "key1=value1\\nkey2=value2")
                .build();
            
            api.createNamespacedConfigMap("default", configMap, null, null, null, null);
            
            // Create Secret
            V1Secret secret = new V1SecretBuilder()
                .withNewMetadata()
                    .withName("app-secret")
                    .withNamespace("default")
                .endMetadata()
                .withType("Opaque")
                .addToStringData("password", "mypassword")
                .build();
            
            api.createNamespacedSecret("default", secret, null, null, null, null);
            """);

        System.out.println("\n--- Watch for Changes ---");
        System.out.println("""
            import io.kubernetes.client.util.Watch;
            
            // Watch pods
            Watch<V1Pod> watch = Watch.createWatch(
                client,
                api.listNamespacedPodCall("default", null, null, null, null,
                    null, null, null, null, 60, true, null),
                new TypeToken<Watch.Response<V1Pod>>(){}.getType()
            );
            
            for (Watch.Response<V1Pod> event : watch) {
                System.out.println("Event: " + event.type);
                System.out.println("Pod: " + event.object.getMetadata().getName());
                
                if (event.type.equals("DELETED")) {
                    break;
                }
            }
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Use namespace isolation");
        System.out.println("   ✓ Set resource limits");
        System.out.println("   ✓ Use labels for organization");
        System.out.println("   ✓ Handle API exceptions");
        System.out.println("   ✓ Close watches when done");
        System.out.println();
    }
}
