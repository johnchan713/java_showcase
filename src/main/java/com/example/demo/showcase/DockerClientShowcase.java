package com.example.demo.showcase;

/**
 * Demonstrates Docker Java Client
 * Covers container management, images, networks, and volumes
 */
public class DockerClientShowcase {

    public static void demonstrate() {
        System.out.println("\n========== DOCKER JAVA CLIENT SHOWCASE ==========\n");

        System.out.println("--- Docker Client Overview ---");
        System.out.println("Manage Docker containers from Java\n");

        System.out.println("Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>com.github.docker-java</groupId>
                <artifactId>docker-java-core</artifactId>
                <version>3.3.4</version>
            </dependency>
            <dependency>
                <groupId>com.github.docker-java</groupId>
                <artifactId>docker-java-transport-httpclient5</artifactId>
                <version>3.3.4</version>
            </dependency>
            """);

        System.out.println("\n--- Docker Client Setup ---");
        System.out.println("""
            import com.github.dockerjava.api.DockerClient;
            import com.github.dockerjava.core.DefaultDockerClientConfig;
            import com.github.dockerjava.core.DockerClientBuilder;
            import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
            
            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("unix:///var/run/docker.sock")
                // Or TCP: .withDockerHost("tcp://localhost:2375")
                .build();
            
            DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .build();
            
            DockerClient dockerClient = DockerClientBuilder.getInstance(config)
                .withDockerHttpClient(httpClient)
                .build();
            """);

        System.out.println("\n--- Container Management ---");
        System.out.println("""
            // List containers
            List<Container> containers = dockerClient.listContainersCmd()
                .withShowAll(true)
                .exec();
            
            containers.forEach(container -> {
                System.out.println("ID: " + container.getId());
                System.out.println("Image: " + container.getImage());
                System.out.println("Status: " + container.getStatus());
            });
            
            // Create container
            CreateContainerResponse container = dockerClient
                .createContainerCmd("nginx:latest")
                .withName("my-nginx")
                .withExposedPorts(ExposedPort.tcp(80))
                .withHostConfig(
                    HostConfig.newHostConfig()
                        .withPortBindings(new PortBinding(
                            Ports.Binding.bindPort(8080),
                            ExposedPort.tcp(80)
                        ))
                )
                .exec();
            
            String containerId = container.getId();
            
            // Start container
            dockerClient.startContainerCmd(containerId).exec();
            
            // Stop container
            dockerClient.stopContainerCmd(containerId)
                .withTimeout(10)
                .exec();
            
            // Remove container
            dockerClient.removeContainerCmd(containerId)
                .withForce(true)
                .exec();
            """);

        System.out.println("\n--- Image Management ---");
        System.out.println("""
            // List images
            List<Image> images = dockerClient.listImagesCmd()
                .withShowAll(true)
                .exec();
            
            // Pull image
            dockerClient.pullImageCmd("redis:latest")
                .exec(new PullImageResultCallback())
                .awaitCompletion();
            
            // Build image
            BuildImageResultCallback callback = new BuildImageResultCallback() {
                @Override
                public void onNext(BuildResponseItem item) {
                    System.out.println(item.getStream());
                    super.onNext(item);
                }
            };
            
            String imageId = dockerClient.buildImageCmd()
                .withDockerfile(new File("Dockerfile"))
                .withTags(Set.of("my-app:1.0"))
                .exec(callback)
                .awaitImageId();
            
            // Remove image
            dockerClient.removeImageCmd("my-app:1.0").exec();
            """);

        System.out.println("\n--- Container Logs ---");
        System.out.println("""
            // Stream logs
            dockerClient.logContainerCmd(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .withFollowStream(true)
                .withTailAll()
                .exec(new LogContainerResultCallback() {
                    @Override
                    public void onNext(Frame frame) {
                        System.out.println(new String(frame.getPayload()));
                    }
                });
            """);

        System.out.println("\n--- Network Management ---");
        System.out.println("""
            // Create network
            CreateNetworkResponse network = dockerClient.createNetworkCmd()
                .withName("my-network")
                .withDriver("bridge")
                .exec();
            
            // Connect container to network
            dockerClient.connectToNetworkCmd()
                .withContainerId(containerId)
                .withNetworkId(network.getId())
                .exec();
            """);

        System.out.println("\n--- Volume Management ---");
        System.out.println("""
            // Create volume
            CreateVolumeResponse volume = dockerClient.createVolumeCmd()
                .withName("my-volume")
                .exec();
            
            // Use volume in container
            CreateContainerResponse container = dockerClient
                .createContainerCmd("postgres:latest")
                .withHostConfig(
                    HostConfig.newHostConfig()
                        .withBinds(new Bind(
                            "my-volume",
                            new Volume("/var/lib/postgresql/data")
                        ))
                )
                .exec();
            """);

        System.out.println("\nBest practices:");
        System.out.println("   ✓ Close DockerClient when done");
        System.out.println("   ✓ Handle callbacks properly");
        System.out.println("   ✓ Use try-with-resources");
        System.out.println("   ✓ Set appropriate timeouts");
        System.out.println();
    }
}
