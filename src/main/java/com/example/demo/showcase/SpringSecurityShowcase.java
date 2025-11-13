package com.example.demo.showcase;

/**
 * Demonstrates Spring Security configuration and CSRF protection
 * Note: This is a demonstration showcase - not enabled by default to avoid breaking the application
 *
 * To enable Spring Security, create a SecurityConfig class in your application
 */
public class SpringSecurityShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SPRING SECURITY SHOWCASE ==========\n");

        securityOverviewDemo();
        authenticationDemo();
        authorizationDemo();
        csrfProtectionDemo();
        corsConfigurationDemo();
        passwordEncodingDemo();
        methodSecurityDemo();
        jwtDemo();
    }

    // ========== Security Overview ==========

    private static void securityOverviewDemo() {
        System.out.println("--- Spring Security Overview ---");
        System.out.println("Comprehensive security framework for Spring applications\n");

        System.out.println("1. Core Security Concepts:");
        System.out.println("   Authentication: Who you are (identity verification)");
        System.out.println("   Authorization: What you can do (access control)");
        System.out.println("   Principal: Currently logged-in user");
        System.out.println("   Granted Authority: Permission/role");
        System.out.println("   Security Context: Holds authentication information");

        System.out.println("\n2. Spring Security Features:");
        System.out.println("   ✓ Authentication (form login, OAuth2, LDAP, etc.)");
        System.out.println("   ✓ Authorization (URL-based, method-level)");
        System.out.println("   ✓ CSRF protection");
        System.out.println("   ✓ Session management");
        System.out.println("   ✓ Password encryption");
        System.out.println("   ✓ Security headers (XSS, clickjacking protection)");
        System.out.println("   ✓ Remember-me authentication");

        System.out.println("\n3. Dependencies:");
        System.out.println("   <dependency>");
        System.out.println("       <groupId>org.springframework.boot</groupId>");
        System.out.println("       <artifactId>spring-boot-starter-security</artifactId>");
        System.out.println("   </dependency>");

        System.out.println();
    }

    // ========== Authentication ==========

    private static void authenticationDemo() {
        System.out.println("--- Authentication Configuration ---");
        System.out.println("How to configure user authentication\n");

        System.out.println("1. Basic SecurityFilterChain configuration:");
        System.out.println("""
            @Configuration
            @EnableWebSecurity
            public class SecurityConfig {

                @Bean
                public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                    http
                        .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/public/**").permitAll()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().authenticated()
                        )
                        .formLogin(form -> form
                            .loginPage("/login")
                            .defaultSuccessUrl("/home")
                            .permitAll()
                        )
                        .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout")
                            .permitAll()
                        );
                    return http.build();
                }
            }
            """);

        System.out.println("2. In-Memory Authentication (for testing):");
        System.out.println("""
            @Bean
            public UserDetailsService userDetailsService() {
                UserDetails user = User.builder()
                    .username("user")
                    .password(passwordEncoder().encode("password"))
                    .roles("USER")
                    .build();

                UserDetails admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder().encode("admin"))
                    .roles("USER", "ADMIN")
                    .build();

                return new InMemoryUserDetailsManager(user, admin);
            }
            """);

        System.out.println("3. Database Authentication (production):");
        System.out.println("""
            @Service
            public class CustomUserDetailsService implements UserDetailsService {

                @Autowired
                private UserRepository userRepository;

                @Override
                public UserDetails loadUserByUsername(String username)
                        throws UsernameNotFoundException {
                    User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                    return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRoles().toArray(new String[0]))
                        .build();
                }
            }
            """);

        System.out.println("4. Authentication types:");
        System.out.println("   Form-based login: Traditional username/password form");
        System.out.println("   HTTP Basic: Base64 encoded credentials in header");
        System.out.println("   OAuth2/OpenID Connect: Third-party authentication (Google, GitHub)");
        System.out.println("   JWT: Token-based stateless authentication");
        System.out.println("   LDAP: Enterprise directory authentication");
        System.out.println("   Remember-me: Persistent login across sessions");

        System.out.println();
    }

    // ========== Authorization ==========

    private static void authorizationDemo() {
        System.out.println("--- Authorization (Access Control) ---");
        System.out.println("Control access to resources based on roles/authorities\n");

        System.out.println("1. URL-based authorization:");
        System.out.println("""
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.authorizeHttpRequests(auth -> auth
                    // Public endpoints
                    .requestMatchers("/", "/home", "/public/**").permitAll()

                    // Role-based access
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")

                    // Authority-based access
                    .requestMatchers("/api/write").hasAuthority("WRITE_PRIVILEGE")
                    .requestMatchers("/api/read").hasAuthority("READ_PRIVILEGE")

                    // Ant matchers with wildcards
                    .requestMatchers("/products/*").authenticated()
                    .requestMatchers("/products/*/edit").hasRole("EDITOR")

                    // Regex matchers
                    .regexMatchers("/api/v[0-9]+/.*").authenticated()

                    // All other requests require authentication
                    .anyRequest().authenticated()
                );
                return http.build();
            }
            """);

        System.out.println("2. Method-level security:");
        System.out.println("   Enable with @EnableMethodSecurity");
        System.out.println("""
            @Service
            public class ProductService {

                @PreAuthorize("hasRole('ADMIN')")
                public void deleteProduct(Long id) {
                    // Only ADMIN can delete
                }

                @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
                public void updateProduct(Product product) {
                    // Requires WRITE_PRIVILEGE
                }

                @PostAuthorize("returnObject.owner == authentication.name")
                public Product getProduct(Long id) {
                    // User can only access their own products
                }

                @Secured({"ROLE_USER", "ROLE_ADMIN"})
                public List<Product> getAllProducts() {
                    // Multiple roles allowed
                }
            }
            """);

        System.out.println("3. Expression-based access control:");
        System.out.println("   hasRole('ROLE') - User has specific role");
        System.out.println("   hasAnyRole('ROLE1', 'ROLE2') - User has any of the roles");
        System.out.println("   hasAuthority('AUTH') - User has specific authority");
        System.out.println("   hasAnyAuthority('AUTH1', 'AUTH2') - User has any authority");
        System.out.println("   isAuthenticated() - User is logged in");
        System.out.println("   isAnonymous() - User is not authenticated");
        System.out.println("   permitAll() - Allow all users");
        System.out.println("   denyAll() - Deny all users");
        System.out.println("   principal - Access current user");
        System.out.println("   authentication - Access Authentication object");

        System.out.println();
    }

    // ========== CSRF Protection ==========

    private static void csrfProtectionDemo() {
        System.out.println("--- CSRF Protection ---");
        System.out.println("Cross-Site Request Forgery protection (enabled by default)\n");

        System.out.println("1. What is CSRF:");
        System.out.println("   ✗ Attacker tricks user's browser to send unauthorized request");
        System.out.println("   ✗ Uses user's existing authentication");
        System.out.println("   ✓ CSRF token prevents this attack");

        System.out.println("\n2. CSRF protection (enabled by default):");
        System.out.println("""
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                    .csrf(csrf -> csrf
                        // CSRF enabled by default for POST, PUT, DELETE, PATCH
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

                        // Ignore CSRF for specific endpoints (e.g., public APIs)
                        .ignoringRequestMatchers("/api/public/**")
                    );
                return http.build();
            }
            """);

        System.out.println("3. Disable CSRF (only for stateless APIs with JWT):");
        System.out.println("""
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf(csrf -> csrf.disable());  // Only for stateless APIs
                return http.build();
            }
            """);

        System.out.println("4. Include CSRF token in Thymeleaf forms:");
        System.out.println("""
            <form method="post" th:action="@{/submit}">
                <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
                <!-- form fields -->
                <button type="submit">Submit</button>
            </form>
            """);

        System.out.println("5. CSRF token in JavaScript (AJAX):");
        System.out.println("""
            const token = document.querySelector('meta[name="_csrf"]').content;
            const header = document.querySelector('meta[name="_csrf_header"]').content;

            fetch('/api/data', {
                method: 'POST',
                headers: {
                    [header]: token,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            });
            """);

        System.out.println("\n6. CSRF recommendations:");
        System.out.println("   ✓ Keep CSRF enabled for session-based authentication");
        System.out.println("   ✓ Disable CSRF only for stateless REST APIs with JWT");
        System.out.println("   ✓ Use SameSite cookies as additional protection");
        System.out.println("   ✗ Never disable CSRF for form-based authentication");

        System.out.println();
    }

    // ========== CORS Configuration ==========

    private static void corsConfigurationDemo() {
        System.out.println("--- CORS Configuration ---");
        System.out.println("Cross-Origin Resource Sharing for API access\n");

        System.out.println("1. Global CORS configuration:");
        System.out.println("""
            @Configuration
            public class CorsConfig {

                @Bean
                public CorsConfigurationSource corsConfigurationSource() {
                    CorsConfiguration configuration = new CorsConfiguration();

                    // Allow specific origins
                    configuration.setAllowedOrigins(Arrays.asList(
                        "http://localhost:3000",
                        "https://myapp.com"
                    ));

                    // Allow all origins (use carefully!)
                    // configuration.addAllowedOriginPattern("*");

                    // Allowed methods
                    configuration.setAllowedMethods(Arrays.asList(
                        "GET", "POST", "PUT", "DELETE", "OPTIONS"
                    ));

                    // Allowed headers
                    configuration.setAllowedHeaders(Arrays.asList(
                        "Authorization", "Content-Type", "X-Requested-With"
                    ));

                    // Expose headers
                    configuration.setExposedHeaders(Arrays.asList(
                        "Authorization", "X-Total-Count"
                    ));

                    // Allow credentials (cookies)
                    configuration.setAllowCredentials(true);

                    // Max age for preflight request cache
                    configuration.setMaxAge(3600L);

                    UrlBasedCorsConfigurationSource source =
                        new UrlBasedCorsConfigurationSource();
                    source.registerCorsConfiguration("/**", configuration);
                    return source;
                }
            }
            """);

        System.out.println("2. CORS in Security configuration:");
        System.out.println("""
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable());  // Often disabled with CORS
                return http.build();
            }
            """);

        System.out.println("3. Controller-level CORS:");
        System.out.println("""
            @RestController
            @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
            public class ApiController {

                @GetMapping("/api/data")
                @CrossOrigin(origins = "https://specific-domain.com")
                public ResponseEntity<Data> getData() {
                    // Method-specific CORS overrides class-level
                }
            }
            """);

        System.out.println();
    }

    // ========== Password Encoding ==========

    private static void passwordEncodingDemo() {
        System.out.println("--- Password Encoding ---");
        System.out.println("Secure password storage\n");

        System.out.println("1. BCrypt password encoder (recommended):");
        System.out.println("""
            @Bean
            public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
                // BCrypt: ✓ Slow hashing (prevents brute force)
                //         ✓ Automatic salting
                //         ✓ Configurable strength (default 10)
            }

            // With custom strength
            @Bean
            public PasswordEncoder strongPasswordEncoder() {
                return new BCryptPasswordEncoder(12);  // Higher = slower but more secure
            }
            """);

        System.out.println("2. Other password encoders:");
        System.out.println("""
            // Argon2 (most secure, higher CPU/memory cost)
            @Bean
            public PasswordEncoder argon2Encoder() {
                return new Argon2PasswordEncoder();
            }

            // SCrypt
            @Bean
            public PasswordEncoder scryptEncoder() {
                return new SCryptPasswordEncoder();
            }

            // PBKDF2
            @Bean
            public PasswordEncoder pbkdf2Encoder() {
                return new Pbkdf2PasswordEncoder();
            }

            // Delegating encoder (supports multiple algorithms)
            @Bean
            public PasswordEncoder delegatingEncoder() {
                String idForEncode = "bcrypt";
                Map<String, PasswordEncoder> encoders = new HashMap<>();
                encoders.put("bcrypt", new BCryptPasswordEncoder());
                encoders.put("scrypt", new SCryptPasswordEncoder());
                return new DelegatingPasswordEncoder(idForEncode, encoders);
            }
            """);

        System.out.println("3. Usage:");
        System.out.println("""
            @Service
            public class UserService {
                @Autowired
                private PasswordEncoder passwordEncoder;

                public void registerUser(String username, String rawPassword) {
                    String encodedPassword = passwordEncoder.encode(rawPassword);
                    // Save encodedPassword to database
                }

                public boolean verifyPassword(String rawPassword, String encodedPassword) {
                    return passwordEncoder.matches(rawPassword, encodedPassword);
                }
            }
            """);

        System.out.println("4. Recommendations:");
        System.out.println("   ✓ Use BCrypt (default choice, well-tested)");
        System.out.println("   ✓ Use Argon2 (most secure, higher cost)");
        System.out.println("   ✗ NEVER use plain text passwords");
        System.out.println("   ✗ NEVER use simple hashing (MD5, SHA1) for passwords");
        System.out.println("   ✓ Encode passwords before saving to database");
        System.out.println("   ✓ Use matches() method to verify, never compare directly");

        System.out.println();
    }

    // ========== Method Security ==========

    private static void methodSecurityDemo() {
        System.out.println("--- Method-Level Security ---");
        System.out.println("Secure methods with annotations\n");

        System.out.println("1. Enable method security:");
        System.out.println("""
            @Configuration
            @EnableMethodSecurity(
                prePostEnabled = true,     // @PreAuthorize, @PostAuthorize
                securedEnabled = true,     // @Secured
                jsr250Enabled = true       // @RolesAllowed
            )
            public class MethodSecurityConfig {
            }
            """);

        System.out.println("2. @PreAuthorize (check before method execution):");
        System.out.println("""
            @Service
            public class OrderService {

                @PreAuthorize("hasRole('ADMIN')")
                public void deleteOrder(Long id) {
                    // Only ADMIN can delete
                }

                @PreAuthorize("hasAuthority('ORDER_WRITE')")
                public void createOrder(Order order) {
                    // Requires ORDER_WRITE authority
                }

                @PreAuthorize("#order.userId == authentication.principal.id")
                public void updateOrder(Order order) {
                    // Users can only update their own orders
                }

                @PreAuthorize("hasRole('USER') and #id == authentication.principal.id")
                public void cancelOrder(Long id) {
                    // Complex expressions
                }
            }
            """);

        System.out.println("3. @PostAuthorize (check after method execution):");
        System.out.println("""
            @PostAuthorize("returnObject.owner == authentication.name")
            public Order getOrder(Long id) {
                // Executes method first, then checks if user owns the order
                return orderRepository.findById(id);
            }

            @PostAuthorize("hasPermission(returnObject, 'read')")
            public Document getDocument(Long id) {
                // Custom permission checking
            }
            """);

        System.out.println("4. @Secured (simpler, only roles):");
        System.out.println("""
            @Secured("ROLE_ADMIN")
            public void adminOperation() {
                // Only ADMIN role
            }

            @Secured({"ROLE_USER", "ROLE_ADMIN"})
            public void userOrAdminOperation() {
                // Multiple roles
            }
            """);

        System.out.println("5. @RolesAllowed (JSR-250 standard):");
        System.out.println("""
            @RolesAllowed("ADMIN")
            public void jsr250AdminMethod() {
                // Standard Java security annotation
            }
            """);

        System.out.println();
    }

    // ========== JWT Authentication ==========

    private static void jwtDemo() {
        System.out.println("--- JWT Authentication ---");
        System.out.println("Stateless token-based authentication\n");

        System.out.println("1. JWT structure:");
        System.out.println("   Header.Payload.Signature");
        System.out.println("   eyJhbGc...  .  eyJzdWI...  .  SflKxw...");
        System.out.println("   (algorithm)    (claims)      (signature)");

        System.out.println("\n2. JWT filter configuration:");
        System.out.println("""
            @Component
            public class JwtAuthenticationFilter extends OncePerRequestFilter {

                @Autowired
                private JwtUtil jwtUtil;

                @Autowired
                private UserDetailsService userDetailsService;

                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                              HttpServletResponse response,
                                              FilterChain chain)
                        throws ServletException, IOException {

                    String authHeader = request.getHeader("Authorization");
                    if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        String token = authHeader.substring(7);
                        String username = jwtUtil.extractUsername(token);

                        if (username != null && SecurityContextHolder.getContext()
                                .getAuthentication() == null) {
                            UserDetails userDetails =
                                userDetailsService.loadUserByUsername(username);

                            if (jwtUtil.validateToken(token, userDetails)) {
                                UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                                SecurityContextHolder.getContext()
                                    .setAuthentication(authToken);
                            }
                        }
                    }
                    chain.doFilter(request, response);
                }
            }
            """);

        System.out.println("3. Security configuration for JWT:");
        System.out.println("""
            @Bean
            public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                    .csrf(csrf -> csrf.disable())  // Disable CSRF for stateless API
                    .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // Login/register
                        .anyRequest().authenticated()
                    )
                    .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
                return http.build();
            }
            """);

        System.out.println("4. JWT utility class (using jjwt library):");
        System.out.println("""
            @Component
            public class JwtUtil {
                private String secret = "your-secret-key";
                private long expiration = 86400000; // 24 hours

                public String generateToken(UserDetails userDetails) {
                    Map<String, Object> claims = new HashMap<>();
                    return Jwts.builder()
                        .setClaims(claims)
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + expiration))
                        .signWith(SignatureAlgorithm.HS256, secret)
                        .compact();
                }

                public String extractUsername(String token) {
                    return extractClaim(token, Claims::getSubject);
                }

                public boolean validateToken(String token, UserDetails userDetails) {
                    String username = extractUsername(token);
                    return username.equals(userDetails.getUsername())
                        && !isTokenExpired(token);
                }
            }
            """);

        System.out.println("5. JWT best practices:");
        System.out.println("   ✓ Store in HttpOnly cookie (XSS protection)");
        System.out.println("   ✓ Use short expiration times (15-60 minutes)");
        System.out.println("   ✓ Implement refresh tokens");
        System.out.println("   ✓ Use strong secret key (256-bit minimum)");
        System.out.println("   ✓ Validate signature and expiration");
        System.out.println("   ✗ Don't store sensitive data in JWT payload");
        System.out.println("   ✗ Don't use localStorage (XSS vulnerable)");

        System.out.println();
    }
}
