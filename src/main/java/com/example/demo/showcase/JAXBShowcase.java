package com.example.demo.showcase;

/**
 * Demonstrates JAXB (Java Architecture for XML Binding)
 * Covers XML marshalling/unmarshalling, annotations, and schema generation
 */
public class JAXBShowcase {

    public static void demonstrate() {
        System.out.println("\n========== JAXB XML BINDING SHOWCASE ==========\n");

        jaxbOverviewDemo();
        annotationsDemo();
        marshallingDemo();
        unmarshallingDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void jaxbOverviewDemo() {
        System.out.println("--- JAXB Overview ---");
        System.out.println("Convert between Java objects and XML\n");

        System.out.println("1. Core concepts:");
        System.out.println("   Marshalling: Java Object → XML");
        System.out.println("   Unmarshalling: XML → Java Object");
        System.out.println("   JAXBContext: Entry point for JAXB operations");
        System.out.println("   Marshaller: Converts objects to XML");
        System.out.println("   Unmarshaller: Converts XML to objects");

        System.out.println("\n2. Dependencies:");
        System.out.println("""
            <!-- Jakarta XML Binding API -->
            <dependency>
                <groupId>jakarta.xml.bind</groupId>
                <artifactId>jakarta.xml.bind-api</artifactId>
            </dependency>

            <!-- JAXB Runtime -->
            <dependency>
                <groupId>org.glassfish.jaxb</groupId>
                <artifactId>jaxb-runtime</artifactId>
            </dependency>
            """);

        System.out.println();
    }

    // ========== Annotations ==========

    private static void annotationsDemo() {
        System.out.println("--- JAXB Annotations ---");
        System.out.println("Map Java classes to XML\n");

        System.out.println("1. Basic annotations:");
        System.out.println("""
            import jakarta.xml.bind.annotation.*;

            @XmlRootElement(name = "user")  // Root element
            @XmlAccessorType(XmlAccessType.FIELD)  // Use fields, not getters
            public class User {

                @XmlElement(name = "user-name", required = true)
                private String name;

                @XmlElement
                private int age;

                @XmlAttribute  // As XML attribute, not element
                private String id;

                @XmlTransient  // Exclude from XML
                private String password;

                @XmlElementWrapper(name = "addresses")  // Wrapper element
                @XmlElement(name = "address")
                private List<Address> addresses;

                // Constructor, getters, setters
            }

            // XML output:
            <user id="123">
                <user-name>John</user-name>
                <age>30</age>
                <addresses>
                    <address>...</address>
                    <address>...</address>
                </addresses>
            </user>
            """);

        System.out.println("2. Nested objects:");
        System.out.println("""
            @XmlRootElement
            public class User {
                private String name;

                @XmlElement
                private Address address;  // Nested element
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            public class Address {
                private String street;
                private String city;
                private String zipCode;
            }

            // XML output:
            <user>
                <name>John</name>
                <address>
                    <street>123 Main St</street>
                    <city>New York</city>
                    <zipCode>10001</zipCode>
                </address>
            </user>
            """);

        System.out.println("3. Collections:");
        System.out.println("""
            @XmlRootElement
            public class Users {

                @XmlElement(name = "user")
                private List<User> users;
            }

            // XML output:
            <users>
                <user>...</user>
                <user>...</user>
            </users>

            // Without wrapper
            @XmlRootElement(name = "user-list")
            @XmlAccessorType(XmlAccessType.FIELD)
            public class UserList {

                @XmlElement(name = "user")
                private List<User> users;
            }
            """);

        System.out.println("4. Types and formats:");
        System.out.println("""
            @XmlRootElement
            public class Event {

                @XmlSchemaType(name = "dateTime")
                private XMLGregorianCalendar timestamp;

                @XmlJavaTypeAdapter(LocalDateAdapter.class)
                private LocalDate eventDate;

                @XmlValue  // Element text content
                private String description;

                @XmlAttribute
                private String type;
            }

            // Custom adapter for LocalDate
            public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

                @Override
                public LocalDate unmarshal(String v) {
                    return LocalDate.parse(v);
                }

                @Override
                public String marshal(LocalDate v) {
                    return v.toString();
                }
            }

            // XML output:
            <event type="meeting" eventDate="2025-01-15">
                Meeting with team
            </event>
            """);

        System.out.println();
    }

    // ========== Marshalling ==========

    private static void marshallingDemo() {
        System.out.println("--- Marshalling (Object to XML) ---");
        System.out.println("Convert Java objects to XML\n");

        System.out.println("1. Basic marshalling:");
        System.out.println("""
            import jakarta.xml.bind.JAXBContext;
            import jakarta.xml.bind.Marshaller;

            // Create user
            User user = new User("John", 30);
            user.setId("123");

            // Create JAXBContext (expensive, cache this!)
            JAXBContext context = JAXBContext.newInstance(User.class);

            // Create Marshaller
            Marshaller marshaller = context.createMarshaller();

            // Pretty print
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // XML declaration
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

            // Encoding
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            // Marshal to console
            marshaller.marshal(user, System.out);

            // Marshal to file
            marshaller.marshal(user, new File("user.xml"));

            // Marshal to string
            StringWriter sw = new StringWriter();
            marshaller.marshal(user, sw);
            String xml = sw.toString();
            """);

        System.out.println("2. Marshalling collections:");
        System.out.println("""
            List<User> userList = Arrays.asList(
                new User("John", 30),
                new User("Jane", 25)
            );

            // Wrap in root element
            Users users = new Users();
            users.setUsers(userList);

            JAXBContext context = JAXBContext.newInstance(Users.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(users, System.out);
            """);

        System.out.println("3. Schema location:");
        System.out.println("""
            marshaller.setProperty(
                Marshaller.JAXB_SCHEMA_LOCATION,
                "http://example.com/schema http://example.com/schema/user.xsd"
            );
            """);

        System.out.println();
    }

    // ========== Unmarshalling ==========

    private static void unmarshallingDemo() {
        System.out.println("--- Unmarshalling (XML to Object) ---");
        System.out.println("Convert XML to Java objects\n");

        System.out.println("1. Basic unmarshalling:");
        System.out.println("""
            import jakarta.xml.bind.JAXBContext;
            import jakarta.xml.bind.Unmarshaller;

            // Create JAXBContext
            JAXBContext context = JAXBContext.newInstance(User.class);

            // Create Unmarshaller
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Unmarshal from file
            User user = (User) unmarshaller.unmarshal(new File("user.xml"));

            // Unmarshal from string
            String xml = "<user id=\\"123\\">...</user>";
            StringReader reader = new StringReader(xml);
            User user = (User) unmarshaller.unmarshal(reader);

            // Unmarshal from InputStream
            InputStream is = new FileInputStream("user.xml");
            User user = (User) unmarshaller.unmarshal(is);
            """);

        System.out.println("2. Type-safe unmarshalling:");
        System.out.println("""
            // Avoid casting
            JAXBElement<User> element = unmarshaller.unmarshal(
                new StreamSource(new File("user.xml")),
                User.class
            );
            User user = element.getValue();
            """);

        System.out.println("3. Validation during unmarshalling:");
        System.out.println("""
            // Validate against schema
            SchemaFactory sf = SchemaFactory.newInstance(
                XMLConstants.W3C_XML_SCHEMA_NS_URI
            );
            Schema schema = sf.newSchema(new File("user.xsd"));

            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);

            // Validation error handler
            unmarshaller.setEventHandler(new ValidationEventHandler() {
                @Override
                public boolean handleEvent(ValidationEvent event) {
                    System.err.println("Validation error: " + event.getMessage());
                    return true;  // Continue
                }
            });

            User user = (User) unmarshaller.unmarshal(new File("user.xml"));
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced JAXB Features ---");
        System.out.println("Schema generation, namespaces, and best practices\n");

        System.out.println("1. Namespaces:");
        System.out.println("""
            @XmlRootElement(name = "user", namespace = "http://example.com/user")
            @XmlAccessorType(XmlAccessType.FIELD)
            public class User {

                @XmlElement(namespace = "http://example.com/user")
                private String name;

                @XmlElement(namespace = "http://example.com/user")
                private int age;
            }

            // package-info.java
            @XmlSchema(
                namespace = "http://example.com/user",
                elementFormDefault = XmlNsForm.QUALIFIED,
                xmlns = {
                    @XmlNs(prefix = "usr", namespaceURI = "http://example.com/user")
                }
            )
            package com.example.model;

            import jakarta.xml.bind.annotation.*;

            // XML output:
            <usr:user xmlns:usr="http://example.com/user">
                <usr:name>John</usr:name>
                <usr:age>30</usr:age>
            </usr:user>
            """);

        System.out.println("2. Schema generation:");
        System.out.println("""
            // Generate XSD from Java classes
            JAXBContext context = JAXBContext.newInstance(User.class);

            context.generateSchema(new SchemaOutputResolver() {
                @Override
                public Result createOutput(String namespaceUri, String suggestedFileName) {
                    File file = new File(suggestedFileName);
                    StreamResult result = new StreamResult(file);
                    result.setSystemId(file.toURI().toString());
                    return result;
                }
            });
            """);

        System.out.println("3. Inheritance:");
        System.out.println("""
            @XmlRootElement
            @XmlSeeAlso({Employee.class, Customer.class})
            public abstract class Person {
                private String name;
            }

            @XmlRootElement
            public class Employee extends Person {
                private String department;
            }

            // XML output:
            <employee xsi:type="employee">
                <name>John</name>
                <department>IT</department>
            </employee>
            """);

        System.out.println("4. Mixed content:");
        System.out.println("""
            @XmlRootElement
            @XmlAccessorType(XmlAccessType.FIELD)
            public class Message {

                @XmlMixed
                @XmlAnyElement
                private List<Object> content;
            }

            // Supports mixed text and elements:
            <message>
                Text before
                <bold>important</bold>
                text after
            </message>
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Cache JAXBContext (expensive to create)");
        System.out.println("   ✓ Reuse Marshaller/Unmarshaller when possible");
        System.out.println("   ✓ Use @XmlAccessorType(FIELD) for simplicity");
        System.out.println("   ✓ Validate against schema in production");
        System.out.println("   ✓ Use adapters for custom types");
        System.out.println("   ✓ Handle validation events properly");
        System.out.println("   ✗ Don't create JAXBContext per request");
        System.out.println("   ✗ Don't ignore validation errors");

        System.out.println("\n6. Spring Boot integration:");
        System.out.println("""
            @RestController
            public class UserController {

                @GetMapping(value = "/user/{id}", produces = MediaType.APPLICATION_XML_VALUE)
                public User getUser(@PathVariable Long id) {
                    return userService.findById(id);
                    // Automatically marshalled to XML
                }

                @PostMapping(value = "/user", consumes = MediaType.APPLICATION_XML_VALUE)
                public User createUser(@RequestBody User user) {
                    // Automatically unmarshalled from XML
                    return userService.save(user);
                }
            }
            """);

        System.out.println("\n7. Performance tips:");
        System.out.println("   ✓ Use @XmlAccessorType(FIELD) to avoid reflection on getters");
        System.out.println("   ✓ Pool JAXBContext instances");
        System.out.println("   ✓ Use streaming for large documents");
        System.out.println("   ✓ Disable validation if not needed");
        System.out.println("   ✓ Consider alternatives (Jackson XML) for better performance");

        System.out.println();
    }
}
