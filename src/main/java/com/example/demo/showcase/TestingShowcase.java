package com.example.demo.showcase;

/**
 * Demonstrates JUnit 5 and Mockito testing frameworks
 * Covers assertions, mocking, argument captors, and testing best practices
 *
 * Note: This is a demonstration showcase showing testing concepts
 * Actual test files would be in src/test/java with @Test annotations
 */
public class TestingShowcase {

    public static void demonstrate() {
        System.out.println("\n========== TESTING SHOWCASE (JUnit 5 & Mockito) ==========\n");

        junitBasicsDemo();
        assertionsDemo();
        mockitoBasicsDemo();
        argumentCaptorsDemo();
        verificationDemo();
        stubbingDemo();
        spyDemo();
        annotationsDemo();
        advancedTestingDemo();
    }

    // ========== JUnit 5 Basics ==========

    private static void junitBasicsDemo() {
        System.out.println("--- JUnit 5 Basics ---");
        System.out.println("Modern testing framework for Java\n");

        System.out.println("1. Basic test structure:");
        System.out.println("""
            import org.junit.jupiter.api.*;
            import static org.junit.jupiter.api.Assertions.*;

            class CalculatorTest {

                private Calculator calculator;

                @BeforeAll
                static void setupAll() {
                    // Runs once before all tests (must be static)
                    System.out.println("Setting up test suite");
                }

                @BeforeEach
                void setUp() {
                    // Runs before each test method
                    calculator = new Calculator();
                }

                @Test
                void testAddition() {
                    int result = calculator.add(2, 3);
                    assertEquals(5, result, "2 + 3 should equal 5");
                }

                @Test
                @DisplayName("Test division by zero throws exception")
                void testDivisionByZero() {
                    assertThrows(ArithmeticException.class, () -> {
                        calculator.divide(10, 0);
                    });
                }

                @AfterEach
                void tearDown() {
                    // Runs after each test method
                    calculator = null;
                }

                @AfterAll
                static void tearDownAll() {
                    // Runs once after all tests (must be static)
                    System.out.println("Cleaning up test suite");
                }
            }
            """);

        System.out.println("2. Test lifecycle annotations:");
        System.out.println("   @BeforeAll - Runs once before all tests (static)");
        System.out.println("   @BeforeEach - Runs before each test");
        System.out.println("   @Test - Marks a test method");
        System.out.println("   @AfterEach - Runs after each test");
        System.out.println("   @AfterAll - Runs once after all tests (static)");
        System.out.println("   @DisplayName - Custom test name in reports");
        System.out.println("   @Disabled - Skip this test");
        System.out.println("   @Tag - Group tests (e.g., @Tag(\"slow\"))");

        System.out.println("\n3. Test execution order:");
        System.out.println("""
            @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
            class OrderedTests {

                @Test
                @Order(1)
                void firstTest() { }

                @Test
                @Order(2)
                void secondTest() { }
            }
            """);

        System.out.println();
    }

    // ========== Assertions ==========

    private static void assertionsDemo() {
        System.out.println("--- JUnit Assertions ---");
        System.out.println("Verify expected vs actual values\n");

        System.out.println("1. Basic assertions:");
        System.out.println("""
            import static org.junit.jupiter.api.Assertions.*;

            @Test
            void basicAssertions() {
                // Equality
                assertEquals(expected, actual);
                assertEquals(expected, actual, "Custom failure message");
                assertEquals(5, calculator.add(2, 3));

                // Boolean assertions
                assertTrue(condition);
                assertFalse(condition);
                assertTrue(result > 0, "Result should be positive");

                // Null checks
                assertNull(object);
                assertNotNull(object);

                // Same/Not Same (reference equality)
                assertSame(expected, actual);  // Same object reference
                assertNotSame(obj1, obj2);

                // Array equality
                assertArrayEquals(expectedArray, actualArray);
                int[] expected = {1, 2, 3};
                int[] actual = {1, 2, 3};
                assertArrayEquals(expected, actual);
            }
            """);

        System.out.println("2. Advanced assertions:");
        System.out.println("""
            @Test
            void advancedAssertions() {
                // Exception assertions
                assertThrows(Exception.class, () -> {
                    methodThatThrows();
                });

                Exception ex = assertThrows(IllegalArgumentException.class, () -> {
                    new User(null); // Null name should throw
                });
                assertEquals("Name cannot be null", ex.getMessage());

                // Timeout assertions
                assertTimeout(Duration.ofSeconds(1), () -> {
                    // This should complete within 1 second
                    slowMethod();
                });

                assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
                    // Aborts if timeout exceeded
                    verySlowMethod();
                });

                // Iterable assertions
                List<String> list = Arrays.asList("a", "b", "c");
                assertIterableEquals(expected, list);

                // Lines assertions (for multi-line strings)
                String expected = "line1\\nline2\\nline3";
                String actual = getMultiLineString();
                assertLinesMatch(Arrays.asList(expected.split("\\n")),
                                Arrays.asList(actual.split("\\n")));
            }
            """);

        System.out.println("3. Grouped assertions:");
        System.out.println("""
            @Test
            void groupedAssertions() {
                // All assertions executed, all failures reported together
                assertAll("person",
                    () -> assertEquals("John", person.getFirstName()),
                    () -> assertEquals("Doe", person.getLastName()),
                    () -> assertEquals(30, person.getAge())
                );

                // Grouped assertions with heading
                assertAll("address",
                    () -> assertNotNull(person.getAddress()),
                    () -> assertEquals("123 Main St", person.getAddress().getStreet()),
                    () -> assertEquals("New York", person.getAddress().getCity())
                );
            }
            """);

        System.out.println("4. Custom assertions:");
        System.out.println("""
            @Test
            void customAssertions() {
                // Fail explicitly
                fail("Not yet implemented");
                fail(() -> "Lazy failure message: " + expensive());

                // Assume (skip test if condition not met)
                assumeTrue(System.getenv("ENV").equals("DEV"));
                assumeFalse(isProduction());
            }
            """);

        System.out.println("5. AssertJ (alternative assertion library):");
        System.out.println("""
            import static org.assertj.core.api.Assertions.*;

            @Test
            void assertJExamples() {
                // Fluent API
                assertThat(person.getName()).isEqualTo("John");
                assertThat(person.getAge()).isGreaterThan(18).isLessThan(65);

                // Collection assertions
                assertThat(list)
                    .hasSize(3)
                    .contains("apple", "banana")
                    .doesNotContain("orange");

                // Exception assertions
                assertThatThrownBy(() -> divide(1, 0))
                    .isInstanceOf(ArithmeticException.class)
                    .hasMessage("/ by zero");

                // Object field assertions
                assertThat(person)
                    .extracting("name", "age")
                    .containsExactly("John", 30);
            }
            """);

        System.out.println();
    }

    // ========== Mockito Basics ==========

    private static void mockitoBasicsDemo() {
        System.out.println("--- Mockito Basics ---");
        System.out.println("Mock dependencies for unit testing\n");

        System.out.println("1. Create mocks:");
        System.out.println("""
            import static org.mockito.Mockito.*;

            @Test
            void createMocks() {
                // Method 1: Mockito.mock()
                UserRepository mockRepo = mock(UserRepository.class);

                // Method 2: @Mock annotation
                @Mock
                UserRepository userRepository;

                // Initialize mocks (needed for @Mock)
                MockitoAnnotations.openMocks(this);

                // Method 3: @ExtendWith (JUnit 5)
                @ExtendWith(MockitoExtension.class)
                class MyTest {
                    @Mock
                    UserRepository userRepository;  // Auto-initialized
                }
            }
            """);

        System.out.println("2. Basic usage:");
        System.out.println("""
            @ExtendWith(MockitoExtension.class)
            class UserServiceTest {

                @Mock
                private UserRepository userRepository;

                @InjectMocks  // Injects mocks into this object
                private UserService userService;

                @Test
                void testGetUser() {
                    // Arrange (Given)
                    User mockUser = new User("John", "john@example.com");
                    when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

                    // Act (When)
                    User result = userService.getUser(1L);

                    // Assert (Then)
                    assertEquals("John", result.getName());
                    verify(userRepository).findById(1L);  // Verify interaction
                }
            }
            """);

        System.out.println("3. Why use mocks:");
        System.out.println("   ✓ Isolate unit under test (remove dependencies)");
        System.out.println("   ✓ Test without database/network/external services");
        System.out.println("   ✓ Control return values and exceptions");
        System.out.println("   ✓ Verify interactions (method calls)");
        System.out.println("   ✓ Fast test execution");

        System.out.println();
    }

    // ========== Argument Captors ==========

    private static void argumentCaptorsDemo() {
        System.out.println("--- Argument Captors ---");
        System.out.println("Capture arguments passed to mocked methods\n");

        System.out.println("1. Basic argument captor:");
        System.out.println("""
            @Test
            void testArgumentCaptor() {
                // Create argument captor
                ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

                // Execute method that calls mock
                userService.createUser("John", "john@example.com");

                // Verify and capture
                verify(userRepository).save(userCaptor.capture());

                // Assert on captured argument
                User capturedUser = userCaptor.getValue();
                assertEquals("John", capturedUser.getName());
                assertEquals("john@example.com", capturedUser.getEmail());
            }
            """);

        System.out.println("2. Capture multiple arguments:");
        System.out.println("""
            @Test
            void testMultipleCaptures() {
                ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

                // Method called multiple times
                userService.createUser("John", "john@example.com");
                userService.createUser("Jane", "jane@example.com");

                // Capture all invocations
                verify(userRepository, times(2)).save(captor.capture());

                // Get all captured values
                List<User> capturedUsers = captor.getAllValues();
                assertEquals(2, capturedUsers.size());
                assertEquals("John", capturedUsers.get(0).getName());
                assertEquals("Jane", capturedUsers.get(1).getName());
            }
            """);

        System.out.println("3. Argument captor with @Captor:");
        System.out.println("""
            @ExtendWith(MockitoExtension.class)
            class UserServiceTest {

                @Mock
                private UserRepository userRepository;

                @Captor
                private ArgumentCaptor<User> userCaptor;

                @InjectMocks
                private UserService userService;

                @Test
                void testWithCaptorAnnotation() {
                    userService.createUser("John", "john@example.com");

                    verify(userRepository).save(userCaptor.capture());
                    assertEquals("John", userCaptor.getValue().getName());
                }
            }
            """);

        System.out.println("4. Captor for complex types:");
        System.out.println("""
            @Test
            void testListCaptor() {
                ArgumentCaptor<List<String>> listCaptor =
                    ArgumentCaptor.forClass(List.class);

                service.processBatch(Arrays.asList("item1", "item2", "item3"));

                verify(processor).process(listCaptor.capture());
                List<String> captured = listCaptor.getValue();
                assertEquals(3, captured.size());
                assertTrue(captured.contains("item1"));
            }
            """);

        System.out.println("5. When to use argument captors:");
        System.out.println("   ✓ Verify complex object arguments");
        System.out.println("   ✓ Check argument values (not just types)");
        System.out.println("   ✓ Test transformations on arguments");
        System.out.println("   ✗ Don't overuse - prefer simple verification when possible");

        System.out.println();
    }

    // ========== Verification ==========

    private static void verificationDemo() {
        System.out.println("--- Mockito Verification ---");
        System.out.println("Verify method calls on mocks\n");

        System.out.println("1. Basic verification:");
        System.out.println("""
            @Test
            void basicVerification() {
                // Verify method called once
                verify(userRepository).findById(1L);

                // Verify method never called
                verify(userRepository, never()).deleteById(anyLong());

                // Verify exact number of times
                verify(userRepository, times(2)).save(any(User.class));

                // Verify at least / at most
                verify(userRepository, atLeast(1)).findAll();
                verify(userRepository, atMost(5)).findAll();

                // Verify no more interactions
                verifyNoMoreInteractions(userRepository);

                // Verify no interactions at all
                verifyNoInteractions(userRepository);
            }
            """);

        System.out.println("2. Argument matchers:");
        System.out.println("""
            @Test
            void argumentMatchers() {
                // Any argument
                verify(userRepository).save(any(User.class));
                verify(userRepository).findById(anyLong());
                verify(emailService).send(anyString(), anyString());

                // Specific value
                verify(userRepository).findById(1L);
                verify(emailService).send(eq("john@example.com"), anyString());

                // Null/Not null
                verify(service).process(isNull());
                verify(service).process(notNull());

                // Custom matchers
                verify(userRepository).save(argThat(user ->
                    user.getName().equals("John") && user.getAge() > 18
                ));

                // Collection matchers
                verify(service).processList(anyList());
                verify(service).processMap(anyMap());
                verify(service).processSet(anySet());
            }
            """);

        System.out.println("3. Verification order:");
        System.out.println("""
            @Test
            void verifyOrder() {
                // Verify order of calls
                InOrder inOrder = inOrder(userRepository, emailService);

                inOrder.verify(userRepository).save(any(User.class));
                inOrder.verify(emailService).sendWelcomeEmail(anyString());
                inOrder.verify(userRepository).flush();
            }
            """);

        System.out.println("4. Verification timeouts:");
        System.out.println("""
            @Test
            void verifyWithTimeout() {
                // Verify within timeout (for async operations)
                verify(asyncService, timeout(1000)).processAsync(any());

                // Verify at least once within timeout
                verify(asyncService, timeout(1000).atLeast(1)).processAsync(any());
            }
            """);

        System.out.println();
    }

    // ========== Stubbing ==========

    private static void stubbingDemo() {
        System.out.println("--- Mockito Stubbing ---");
        System.out.println("Define mock behavior (return values, exceptions)\n");

        System.out.println("1. Basic stubbing:");
        System.out.println("""
            @Test
            void basicStubbing() {
                // Return value
                when(userRepository.findById(1L))
                    .thenReturn(Optional.of(new User("John")));

                // Return multiple values (sequential calls)
                when(service.getValue())
                    .thenReturn(1)
                    .thenReturn(2)
                    .thenReturn(3);

                // Throw exception
                when(userRepository.findById(-1L))
                    .thenThrow(new IllegalArgumentException("Invalid ID"));

                // Alternative: doReturn (for void methods, spies)
                doReturn(user).when(userRepository).findById(1L);
            }
            """);

        System.out.println("2. Stubbing with callbacks:");
        System.out.println("""
            @Test
            void stubbingCallbacks() {
                // thenAnswer - custom logic
                when(calculator.add(anyInt(), anyInt())).thenAnswer(invocation -> {
                    int a = invocation.getArgument(0);
                    int b = invocation.getArgument(1);
                    return a + b;
                });

                // Custom behavior based on arguments
                when(userRepository.findById(anyLong())).thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);
                    if (id == 1L) {
                        return Optional.of(new User("John"));
                    }
                    return Optional.empty();
                });
            }
            """);

        System.out.println("3. Stubbing void methods:");
        System.out.println("""
            @Test
            void stubVoidMethods() {
                // Do nothing (default for void methods)
                doNothing().when(emailService).send(anyString());

                // Throw exception
                doThrow(new RuntimeException("Email failed"))
                    .when(emailService).send("invalid@email");

                // Custom callback
                doAnswer(invocation -> {
                    String email = invocation.getArgument(0);
                    System.out.println("Sending email to: " + email);
                    return null;  // void methods return null
                }).when(emailService).send(anyString());
            }
            """);

        System.out.println("4. Stubbing with argument matchers:");
        System.out.println("""
            @Test
            void stubWithMatchers() {
                // Match any argument
                when(userRepository.findById(anyLong()))
                    .thenReturn(Optional.of(new User("Default")));

                // Match specific value
                when(userRepository.findById(1L))
                    .thenReturn(Optional.of(new User("John")));

                // Custom matcher
                when(userRepository.save(argThat(user ->
                    user.getAge() >= 18
                ))).thenReturn(user);
            }
            """);

        System.out.println("5. Default stubbing:");
        System.out.println("""
            @Test
            void defaultStubbing() {
                // Unstubbed methods return:
                // - null for objects
                // - 0 for numbers
                // - false for boolean
                // - empty collections

                // Can change default behavior
                User mockUser = mock(User.class, RETURNS_SMART_NULLS);
                User mockUser2 = mock(User.class, RETURNS_DEEP_STUBS);
            }
            """);

        System.out.println();
    }

    // ========== Spy ==========

    private static void spyDemo() {
        System.out.println("--- Mockito Spy ---");
        System.out.println("Partial mocking - real object with stubbed methods\n");

        System.out.println("1. Create spy:");
        System.out.println("""
            @Test
            void createSpy() {
                // Method 1: Mockito.spy()
                List<String> realList = new ArrayList<>();
                List<String> spyList = spy(realList);

                // Method 2: @Spy annotation
                @Spy
                List<String> spyList = new ArrayList<>();
            }
            """);

        System.out.println("2. Spy vs Mock:");
        System.out.println("""
            @Test
            void spyVsMock() {
                // Mock - all methods stubbed by default
                List<String> mockList = mock(List.class);
                mockList.add("item");  // Does nothing
                System.out.println(mockList.size());  // Returns 0 (default)

                // Spy - real methods called unless stubbed
                List<String> spyList = spy(new ArrayList<>());
                spyList.add("item");  // Actually adds to list
                System.out.println(spyList.size());  // Returns 1 (real behavior)

                // Stub specific method on spy
                when(spyList.size()).thenReturn(100);
                System.out.println(spyList.size());  // Returns 100 (stubbed)
                spyList.add("another");  // Still uses real add() method
            }
            """);

        System.out.println("3. Stubbing spy methods:");
        System.out.println("""
            @Test
            void stubSpyMethods() {
                UserService spyService = spy(new UserService());

                // Stub specific method
                doReturn(user).when(spyService).getUser(1L);

                // Other methods use real implementation
                spyService.createUser("John", "john@example.com");  // Real method

                // Verify interactions
                verify(spyService).getUser(1L);
                verify(spyService).createUser(anyString(), anyString());
            }
            """);

        System.out.println("4. When to use spy:");
        System.out.println("   ✓ Testing legacy code (can't inject dependencies)");
        System.out.println("   ✓ Partial mocking (mostly real, few stubbed methods)");
        System.out.println("   ✓ Verify calls to real object methods");
        System.out.println("   ✗ Usually prefer mocks for clean unit tests");
        System.out.println("   ✗ Sign of poor design if needed often");

        System.out.println();
    }

    // ========== Annotations ==========

    private static void annotationsDemo() {
        System.out.println("--- Mockito Annotations ---");
        System.out.println("Simplified mock creation and injection\n");

        System.out.println("1. Core annotations:");
        System.out.println("""
            @ExtendWith(MockitoExtension.class)  // JUnit 5
            class UserServiceTest {

                @Mock  // Create mock
                private UserRepository userRepository;

                @Mock
                private EmailService emailService;

                @Spy  // Create spy
                private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

                @InjectMocks  // Inject mocks into this object
                private UserService userService;

                @Captor  // Argument captor
                private ArgumentCaptor<User> userCaptor;

                @Test
                void test() {
                    // Mocks and captors are ready to use
                }
            }
            """);

        System.out.println("2. @InjectMocks behavior:");
        System.out.println("   ✓ Constructor injection (preferred)");
        System.out.println("   ✓ Setter injection");
        System.out.println("   ✓ Field injection");
        System.out.println("   ✓ Automatically injects @Mock and @Spy fields");

        System.out.println("\n3. Manual initialization (JUnit 4 or no extension):");
        System.out.println("""
            class UserServiceTest {

                @Mock
                private UserRepository userRepository;

                @InjectMocks
                private UserService userService;

                @BeforeEach
                void setUp() {
                    MockitoAnnotations.openMocks(this);  // Initialize mocks
                }
            }
            """);

        System.out.println();
    }

    // ========== Advanced Testing ==========

    private static void advancedTestingDemo() {
        System.out.println("--- Advanced Testing Patterns ---");
        System.out.println("Testing best practices and patterns\n");

        System.out.println("1. Parameterized tests:");
        System.out.println("""
            @ParameterizedTest
            @ValueSource(ints = {1, 2, 3, 5, 8})
            void testIsPrime(int number) {
                assertTrue(isPrime(number));
            }

            @ParameterizedTest
            @CsvSource({
                "1, 1, 2",
                "2, 3, 5",
                "10, 20, 30"
            })
            void testAddition(int a, int b, int expected) {
                assertEquals(expected, calculator.add(a, b));
            }

            @ParameterizedTest
            @MethodSource("userProvider")
            void testUsers(User user) {
                assertNotNull(user.getName());
            }

            static Stream<User> userProvider() {
                return Stream.of(
                    new User("John"),
                    new User("Jane"),
                    new User("Bob")
                );
            }
            """);

        System.out.println("2. Nested tests:");
        System.out.println("""
            @Nested
            @DisplayName("When user is authenticated")
            class WhenAuthenticated {

                @BeforeEach
                void authenticate() {
                    // Setup authenticated user
                }

                @Test
                void canAccessProtectedResource() {
                    // Test authenticated access
                }

                @Nested
                @DisplayName("And user is admin")
                class AndIsAdmin {

                    @BeforeEach
                    void grantAdminRole() {
                        // Setup admin role
                    }

                    @Test
                    void canAccessAdminPanel() {
                        // Test admin access
                    }
                }
            }
            """);

        System.out.println("3. Test doubles pattern:");
        System.out.println("""
            // Dummy - passed but never used
            User dummy = mock(User.class);

            // Stub - returns canned answers
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));

            // Spy - records calls, delegates to real object
            List<String> spy = spy(new ArrayList<>());

            // Mock - full test double with verification
            UserRepository mock = mock(UserRepository.class);
            verify(mock).save(any(User.class));

            // Fake - working implementation (e.g., in-memory database)
            UserRepository fake = new InMemoryUserRepository();
            """);

        System.out.println("4. Testing best practices:");
        System.out.println("   ✓ One assertion per test (or use assertAll)");
        System.out.println("   ✓ Follow AAA pattern: Arrange, Act, Assert");
        System.out.println("   ✓ Test behavior, not implementation");
        System.out.println("   ✓ Use descriptive test names");
        System.out.println("   ✓ Keep tests independent");
        System.out.println("   ✓ Use @DisplayName for readable reports");
        System.out.println("   ✓ Don't test framework code");
        System.out.println("   ✗ Don't mock everything (prefer real objects when simple)");
        System.out.println("   ✗ Don't test private methods directly");
        System.out.println("   ✗ Avoid test interdependencies");

        System.out.println("\n5. Code coverage:");
        System.out.println("   ✓ Aim for 80%+ coverage");
        System.out.println("   ✓ Focus on critical business logic");
        System.out.println("   ✓ Test edge cases and error paths");
        System.out.println("   ✗ 100% coverage != bug-free code");
        System.out.println("   Tools: JaCoCo, Cobertura");

        System.out.println();
    }
}
