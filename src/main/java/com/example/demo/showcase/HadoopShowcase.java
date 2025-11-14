package com.example.demo.showcase;

/**
 * Demonstrates Apache Hadoop - Distributed storage and processing
 * Covers HDFS operations, MapReduce, configuration, and integration
 *
 * Note: This is a demonstration - requires Hadoop cluster or local setup
 */
public class HadoopShowcase {

    public static void demonstrate() {
        System.out.println("\n========== HADOOP SHOWCASE ==========\n");

        hadoopOverviewDemo();
        hdfsOperationsDemo();
        mapReduceDemo();
        configurationDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void hadoopOverviewDemo() {
        System.out.println("--- Hadoop Overview ---");
        System.out.println("Distributed storage and processing for big data\n");

        System.out.println("1. Core components:");
        System.out.println("   • HDFS (Hadoop Distributed File System): Distributed storage");
        System.out.println("   • MapReduce: Distributed processing framework");
        System.out.println("   • YARN (Yet Another Resource Negotiator): Resource management");
        System.out.println("   • Hadoop Common: Utilities and libraries");

        System.out.println("\n2. Key concepts:");
        System.out.println("   • NameNode: HDFS master (metadata)");
        System.out.println("   • DataNode: HDFS slave (actual data blocks)");
        System.out.println("   • Block: 128MB default (configurable)");
        System.out.println("   • Replication: 3 copies default");
        System.out.println("   • Rack awareness: Optimize data locality");

        System.out.println("\n3. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.apache.hadoop</groupId>
                <artifactId>hadoop-client</artifactId>
                <version>3.3.6</version>
                <exclusions>
                    <exclusion>
                        <groupId>org.slf4j</groupId>
                        <artifactId>slf4j-reload4j</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>

            <!-- For HDFS operations -->
            <dependency>
                <groupId>org.apache.hadoop</groupId>
                <artifactId>hadoop-hdfs-client</artifactId>
                <version>3.3.6</version>
            </dependency>

            <!-- For MapReduce -->
            <dependency>
                <groupId>org.apache.hadoop</groupId>
                <artifactId>hadoop-mapreduce-client-core</artifactId>
                <version>3.3.6</version>
            </dependency>
            """);

        System.out.println();
    }

    // ========== HDFS Operations ==========

    private static void hdfsOperationsDemo() {
        System.out.println("--- HDFS Operations ---");
        System.out.println("Read, write, and manage files in HDFS\n");

        System.out.println("1. HDFS configuration:");
        System.out.println("""
            import org.apache.hadoop.conf.Configuration;
            import org.apache.hadoop.fs.FileSystem;
            import org.apache.hadoop.fs.Path;

            @Service
            public class HdfsService {

                private FileSystem fileSystem;

                @PostConstruct
                public void init() throws IOException {
                    Configuration conf = new Configuration();

                    // HDFS connection
                    conf.set("fs.defaultFS", "hdfs://localhost:9000");
                    conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

                    // User configuration
                    System.setProperty("HADOOP_USER_NAME", "hdfs");

                    // Optional: Kerberos authentication
                    // conf.set("hadoop.security.authentication", "kerberos");

                    fileSystem = FileSystem.get(conf);
                }

                @PreDestroy
                public void cleanup() throws IOException {
                    if (fileSystem != null) {
                        fileSystem.close();
                    }
                }
            }
            """);

        System.out.println("2. File operations:");
        System.out.println("""
            import org.apache.hadoop.fs.FSDataInputStream;
            import org.apache.hadoop.fs.FSDataOutputStream;

            public class HdfsFileOperations {

                // Write file
                public void writeFile(String hdfsPath, String content) throws IOException {
                    Path path = new Path(hdfsPath);

                    try (FSDataOutputStream outputStream = fileSystem.create(path)) {
                        outputStream.writeUTF(content);
                        outputStream.flush();
                    }

                    System.out.println("File written: " + hdfsPath);
                }

                // Read file
                public String readFile(String hdfsPath) throws IOException {
                    Path path = new Path(hdfsPath);

                    try (FSDataInputStream inputStream = fileSystem.open(path)) {
                        return inputStream.readUTF();
                    }
                }

                // Read file as lines
                public List<String> readLines(String hdfsPath) throws IOException {
                    Path path = new Path(hdfsPath);
                    List<String> lines = new ArrayList<>();

                    try (BufferedReader reader = new BufferedReader(
                            new InputStreamReader(fileSystem.open(path)))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines.add(line);
                        }
                    }

                    return lines;
                }

                // Append to file
                public void appendFile(String hdfsPath, String content) throws IOException {
                    Path path = new Path(hdfsPath);

                    try (FSDataOutputStream outputStream = fileSystem.append(path)) {
                        outputStream.writeUTF(content);
                    }
                }

                // Copy from local to HDFS
                public void copyFromLocal(String localPath, String hdfsPath)
                        throws IOException {
                    Path local = new Path(localPath);
                    Path hdfs = new Path(hdfsPath);

                    fileSystem.copyFromLocalFile(local, hdfs);
                    System.out.println("Copied: " + localPath + " -> " + hdfsPath);
                }

                // Copy from HDFS to local
                public void copyToLocal(String hdfsPath, String localPath)
                        throws IOException {
                    Path hdfs = new Path(hdfsPath);
                    Path local = new Path(localPath);

                    fileSystem.copyToLocalFile(hdfs, local);
                    System.out.println("Copied: " + hdfsPath + " -> " + localPath);
                }
            }
            """);

        System.out.println("3. Directory operations:");
        System.out.println("""
            // Create directory
            public void createDirectory(String hdfsPath) throws IOException {
                Path path = new Path(hdfsPath);
                fileSystem.mkdirs(path);
                System.out.println("Directory created: " + hdfsPath);
            }

            // List files
            public void listFiles(String hdfsPath) throws IOException {
                Path path = new Path(hdfsPath);
                FileStatus[] statuses = fileSystem.listStatus(path);

                for (FileStatus status : statuses) {
                    System.out.println(status.getPath().getName() +
                        " - " + status.getLen() + " bytes" +
                        " - Replication: " + status.getReplication());
                }
            }

            // Check if exists
            public boolean exists(String hdfsPath) throws IOException {
                return fileSystem.exists(new Path(hdfsPath));
            }

            // Delete file/directory
            public void delete(String hdfsPath, boolean recursive) throws IOException {
                Path path = new Path(hdfsPath);
                boolean deleted = fileSystem.delete(path, recursive);

                if (deleted) {
                    System.out.println("Deleted: " + hdfsPath);
                } else {
                    System.out.println("Failed to delete: " + hdfsPath);
                }
            }

            // Rename/move
            public void rename(String oldPath, String newPath) throws IOException {
                Path source = new Path(oldPath);
                Path destination = new Path(newPath);

                boolean renamed = fileSystem.rename(source, destination);
                if (renamed) {
                    System.out.println("Renamed: " + oldPath + " -> " + newPath);
                }
            }

            // Get file status
            public void getFileInfo(String hdfsPath) throws IOException {
                Path path = new Path(hdfsPath);
                FileStatus status = fileSystem.getFileStatus(path);

                System.out.println("Path: " + status.getPath());
                System.out.println("Length: " + status.getLen());
                System.out.println("Block size: " + status.getBlockSize());
                System.out.println("Replication: " + status.getReplication());
                System.out.println("Modified: " + new Date(status.getModificationTime()));
                System.out.println("Owner: " + status.getOwner());
                System.out.println("Group: " + status.getGroup());
                System.out.println("Permissions: " + status.getPermission());
            }
            """);

        System.out.println();
    }

    // ========== MapReduce ==========

    private static void mapReduceDemo() {
        System.out.println("--- MapReduce Programming ---");
        System.out.println("Distributed data processing with Map and Reduce\n");

        System.out.println("1. Word Count example (Map):");
        System.out.println("""
            import org.apache.hadoop.io.IntWritable;
            import org.apache.hadoop.io.LongWritable;
            import org.apache.hadoop.io.Text;
            import org.apache.hadoop.mapreduce.Mapper;

            public class WordCountMapper
                    extends Mapper<LongWritable, Text, Text, IntWritable> {

                private final static IntWritable one = new IntWritable(1);
                private Text word = new Text();

                @Override
                protected void map(LongWritable key, Text value, Context context)
                        throws IOException, InterruptedException {

                    // Split line into words
                    String line = value.toString();
                    String[] words = line.split("\\\\s+");

                    // Emit (word, 1) for each word
                    for (String w : words) {
                        word.set(w.toLowerCase());
                        context.write(word, one);
                    }
                }
            }
            """);

        System.out.println("2. Word Count example (Reduce):");
        System.out.println("""
            import org.apache.hadoop.mapreduce.Reducer;

            public class WordCountReducer
                    extends Reducer<Text, IntWritable, Text, IntWritable> {

                @Override
                protected void reduce(Text key, Iterable<IntWritable> values,
                                     Context context)
                        throws IOException, InterruptedException {

                    // Sum all counts for this word
                    int sum = 0;
                    for (IntWritable value : values) {
                        sum += value.get();
                    }

                    // Emit (word, total_count)
                    context.write(key, new IntWritable(sum));
                }
            }
            """);

        System.out.println("3. Job configuration and execution:");
        System.out.println("""
            import org.apache.hadoop.mapreduce.Job;
            import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
            import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

            public class WordCountJob {

                public static void main(String[] args) throws Exception {
                    if (args.length != 2) {
                        System.err.println("Usage: WordCountJob <input> <output>");
                        System.exit(-1);
                    }

                    // Create configuration
                    Configuration conf = new Configuration();

                    // Create job
                    Job job = Job.getInstance(conf, "Word Count");
                    job.setJarByClass(WordCountJob.class);

                    // Set Mapper and Reducer
                    job.setMapperClass(WordCountMapper.class);
                    job.setReducerClass(WordCountReducer.class);

                    // Optional: Combiner (local reduce before network transfer)
                    job.setCombinerClass(WordCountReducer.class);

                    // Set output types
                    job.setOutputKeyClass(Text.class);
                    job.setOutputValueClass(IntWritable.class);

                    // Set input/output paths
                    FileInputFormat.addInputPath(job, new Path(args[0]));
                    FileOutputFormat.setOutputPath(job, new Path(args[1]));

                    // Set number of reducers
                    job.setNumReduceTasks(3);

                    // Execute and wait
                    boolean success = job.waitForCompletion(true);
                    System.exit(success ? 0 : 1);
                }
            }
            """);

        System.out.println("4. Custom data types (Writable):");
        System.out.println("""
            import org.apache.hadoop.io.Writable;

            public class SalesRecord implements Writable {
                private String product;
                private double amount;
                private long timestamp;

                @Override
                public void write(DataOutput out) throws IOException {
                    out.writeUTF(product);
                    out.writeDouble(amount);
                    out.writeLong(timestamp);
                }

                @Override
                public void readFields(DataInput in) throws IOException {
                    product = in.readUTF();
                    amount = in.readDouble();
                    timestamp = in.readLong();
                }

                // Getters, setters, constructors
            }

            // Use in MapReduce
            public class SalesMapper
                    extends Mapper<LongWritable, Text, Text, SalesRecord> {

                @Override
                protected void map(LongWritable key, Text value, Context context)
                        throws IOException, InterruptedException {
                    // Parse and emit custom type
                    String[] fields = value.toString().split(",");
                    SalesRecord record = new SalesRecord(
                        fields[0], Double.parseDouble(fields[1]),
                        Long.parseLong(fields[2])
                    );
                    context.write(new Text(fields[0]), record);
                }
            }
            """);

        System.out.println();
    }

    // ========== Configuration ==========

    private static void configurationDemo() {
        System.out.println("--- Hadoop Configuration ---");
        System.out.println("Cluster setup and tuning\n");

        System.out.println("1. Configuration files:");
        System.out.println("""
            # core-site.xml
            <configuration>
                <property>
                    <name>fs.defaultFS</name>
                    <value>hdfs://namenode:9000</value>
                </property>
                <property>
                    <name>hadoop.tmp.dir</name>
                    <value>/var/hadoop/tmp</value>
                </property>
            </configuration>

            # hdfs-site.xml
            <configuration>
                <property>
                    <name>dfs.replication</name>
                    <value>3</value>
                </property>
                <property>
                    <name>dfs.blocksize</name>
                    <value>134217728</value>  <!-- 128MB -->
                </property>
                <property>
                    <name>dfs.namenode.name.dir</name>
                    <value>/var/hadoop/namenode</value>
                </property>
                <property>
                    <name>dfs.datanode.data.dir</name>
                    <value>/var/hadoop/datanode</value>
                </property>
            </configuration>

            # mapred-site.xml
            <configuration>
                <property>
                    <name>mapreduce.framework.name</name>
                    <value>yarn</value>
                </property>
                <property>
                    <name>mapreduce.map.memory.mb</name>
                    <value>1024</value>
                </property>
                <property>
                    <name>mapreduce.reduce.memory.mb</name>
                    <value>2048</value>
                </property>
            </configuration>

            # yarn-site.xml
            <configuration>
                <property>
                    <name>yarn.resourcemanager.hostname</name>
                    <value>resourcemanager</value>
                </property>
                <property>
                    <name>yarn.nodemanager.resource.memory-mb</name>
                    <value>8192</value>
                </property>
                <property>
                    <name>yarn.nodemanager.resource.cpu-vcores</name>
                    <value>4</value>
                </property>
            </configuration>
            """);

        System.out.println("2. Programmatic configuration:");
        System.out.println("""
            Configuration conf = new Configuration();

            // HDFS settings
            conf.set("fs.defaultFS", "hdfs://localhost:9000");
            conf.setInt("dfs.replication", 3);
            conf.setLong("dfs.blocksize", 134217728);

            // MapReduce settings
            conf.set("mapreduce.framework.name", "yarn");
            conf.setInt("mapreduce.map.memory.mb", 1024);
            conf.setInt("mapreduce.reduce.memory.mb", 2048);
            conf.setInt("mapreduce.job.reduces", 3);

            // Compression
            conf.setBoolean("mapreduce.output.fileoutputformat.compress", true);
            conf.set("mapreduce.output.fileoutputformat.compress.codec",
                "org.apache.hadoop.io.compress.GzipCodec");

            // Custom properties
            conf.set("custom.property", "value");

            // Get properties
            String hdfsUri = conf.get("fs.defaultFS");
            int replication = conf.getInt("dfs.replication", 1);  // default 1
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Hadoop Features ---");
        System.out.println("Counters, distributed cache, and best practices\n");

        System.out.println("1. Counters (tracking metrics):");
        System.out.println("""
            public enum RecordCounters {
                VALID_RECORDS,
                INVALID_RECORDS,
                NULL_RECORDS
            }

            public class DataMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

                @Override
                protected void map(LongWritable key, Text value, Context context)
                        throws IOException, InterruptedException {

                    String line = value.toString();

                    if (line == null || line.isEmpty()) {
                        context.getCounter(RecordCounters.NULL_RECORDS).increment(1);
                        return;
                    }

                    if (isValid(line)) {
                        context.getCounter(RecordCounters.VALID_RECORDS).increment(1);
                        // Process
                    } else {
                        context.getCounter(RecordCounters.INVALID_RECORDS).increment(1);
                    }
                }
            }

            // Read counters after job
            Counters counters = job.getCounters();
            long validRecords = counters.findCounter(RecordCounters.VALID_RECORDS).getValue();
            long invalidRecords = counters.findCounter(RecordCounters.INVALID_RECORDS).getValue();
            """);

        System.out.println("2. Distributed Cache (share files):");
        System.out.println("""
            import org.apache.hadoop.mapreduce.filecache.DistributedCache;

            // Add file to distributed cache
            job.addCacheFile(new URI("/user/data/lookup.txt#lookup"));

            // Access in Mapper
            public class LookupMapper extends Mapper<LongWritable, Text, Text, Text> {

                private Map<String, String> lookupTable = new HashMap<>();

                @Override
                protected void setup(Context context)
                        throws IOException, InterruptedException {
                    // Read from distributed cache
                    URI[] cacheFiles = context.getCacheFiles();

                    if (cacheFiles != null && cacheFiles.length > 0) {
                        try (BufferedReader reader = new BufferedReader(
                                new FileReader("lookup"))) {
                            String line;
                            while ((line = reader.readLine()) != null) {
                                String[] parts = line.split(",");
                                lookupTable.put(parts[0], parts[1]);
                            }
                        }
                    }
                }

                @Override
                protected void map(LongWritable key, Text value, Context context)
                        throws IOException, InterruptedException {
                    String id = value.toString();
                    String enrichedData = lookupTable.get(id);
                    context.write(value, new Text(enrichedData));
                }
            }
            """);

        System.out.println("3. Partitioner (control reducer assignment):");
        System.out.println("""
            import org.apache.hadoop.mapreduce.Partitioner;

            public class CustomPartitioner extends Partitioner<Text, IntWritable> {

                @Override
                public int getPartition(Text key, IntWritable value, int numPartitions) {
                    // Route by first letter
                    char firstLetter = key.toString().charAt(0);

                    if (firstLetter >= 'a' && firstLetter <= 'm') {
                        return 0 % numPartitions;
                    } else {
                        return 1 % numPartitions;
                    }
                }
            }

            // Use in job
            job.setPartitionerClass(CustomPartitioner.class);
            job.setNumReduceTasks(2);
            """);

        System.out.println("4. Best practices:");
        System.out.println("   ✓ Use appropriate block size (128MB - 256MB)");
        System.out.println("   ✓ Set replication factor based on importance (3 default)");
        System.out.println("   ✓ Use Combiner to reduce network traffic");
        System.out.println("   ✓ Compress intermediate and output data");
        System.out.println("   ✓ Configure proper memory for Map/Reduce tasks");
        System.out.println("   ✓ Monitor HDFS health and disk usage");
        System.out.println("   ✓ Use SequenceFile or Parquet for better performance");
        System.out.println("   ✓ Partition data appropriately");
        System.out.println("   ✗ Don't store small files in HDFS (NameNode memory)");
        System.out.println("   ✗ Don't use too many reducers (overhead)");
        System.out.println("   ✗ Don't skip error handling");

        System.out.println("\n5. Common operations:");
        System.out.println("""
            # HDFS shell commands
            hdfs dfs -ls /user/data                  # List files
            hdfs dfs -put local.txt /user/data/      # Upload
            hdfs dfs -get /user/data/file.txt .      # Download
            hdfs dfs -cat /user/data/file.txt        # Read file
            hdfs dfs -mkdir /user/newdir             # Create directory
            hdfs dfs -rm /user/data/file.txt         # Delete file
            hdfs dfs -du -h /user/data               # Disk usage
            hdfs dfsadmin -report                    # Cluster status

            # Submit MapReduce job
            hadoop jar wordcount.jar WordCountJob /input /output

            # YARN commands
            yarn application -list                   # List running jobs
            yarn application -kill <app-id>          # Kill job
            yarn logs -applicationId <app-id>       # View logs
            """);

        System.out.println();
    }
}
