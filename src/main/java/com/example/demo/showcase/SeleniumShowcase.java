package com.example.demo.showcase;

/**
 * Demonstrates Selenium WebDriver for browser automation and testing
 * Covers WebDriver setup, element location, interactions, waits, and Page Object Model
 *
 * Note: This is a demonstration - actual tests would require a browser and web application
 */
public class SeleniumShowcase {

    public static void demonstrate() {
        System.out.println("\n========== SELENIUM WEBDRIVER SHOWCASE ==========\n");

        seleniumOverviewDemo();
        webDriverSetupDemo();
        elementLocationDemo();
        interactionsDemo();
        waitsDemo();
        pageObjectModelDemo();
        advancedFeaturesDemo();
    }

    // ========== Overview ==========

    private static void seleniumOverviewDemo() {
        System.out.println("--- Selenium WebDriver Overview ---");
        System.out.println("Browser automation for testing and web scraping\n");

        System.out.println("1. Key features:");
        System.out.println("   • Cross-browser automation (Chrome, Firefox, Safari, Edge)");
        System.out.println("   • Simulate user interactions (click, type, scroll)");
        System.out.println("   • Element location strategies (ID, CSS, XPath)");
        System.out.println("   • Waits (implicit, explicit, fluent)");
        System.out.println("   • Screenshots and page source capture");
        System.out.println("   • JavaScript execution");
        System.out.println("   • Headless mode for CI/CD");

        System.out.println("\n2. Dependencies:");
        System.out.println("""
            <dependency>
                <groupId>org.seleniumhq.selenium</groupId>
                <artifactId>selenium-java</artifactId>
                <version>4.16.1</version>
            </dependency>

            <!-- WebDriverManager (automatic driver management) -->
            <dependency>
                <groupId>io.github.bonigarcia</groupId>
                <artifactId>webdrivermanager</artifactId>
                <version>5.6.3</version>
            </dependency>

            <!-- JUnit 5 for testing -->
            <dependency>
                <groupId>org.junit.jupiter</groupId>
                <artifactId>junit-jupiter</artifactId>
                <scope>test</scope>
            </dependency>
            """);

        System.out.println("3. Architecture:");
        System.out.println("   WebDriver (API) → Browser Driver → Browser");
        System.out.println("   • WebDriver: Java API for automation");
        System.out.println("   • Browser Driver: ChromeDriver, GeckoDriver, etc.");
        System.out.println("   • Browser: Chrome, Firefox, Safari, Edge");

        System.out.println();
    }

    // ========== WebDriver Setup ==========

    private static void webDriverSetupDemo() {
        System.out.println("--- WebDriver Setup ---");
        System.out.println("Initialize browsers with WebDriverManager\n");

        System.out.println("1. Chrome setup:");
        System.out.println("""
            import org.openqa.selenium.WebDriver;
            import org.openqa.selenium.chrome.ChromeDriver;
            import org.openqa.selenium.chrome.ChromeOptions;
            import io.github.bonigarcia.wdm.WebDriverManager;

            public class ChromeSetup {

                public WebDriver setupChrome() {
                    // Automatic driver management
                    WebDriverManager.chromedriver().setup();

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("--start-maximized");
                    options.addArguments("--disable-notifications");
                    options.addArguments("--disable-popup-blocking");

                    // Headless mode (no UI, for CI/CD)
                    // options.addArguments("--headless");

                    WebDriver driver = new ChromeDriver(options);
                    return driver;
                }
            }
            """);

        System.out.println("2. Firefox setup:");
        System.out.println("""
            import org.openqa.selenium.firefox.FirefoxDriver;
            import org.openqa.selenium.firefox.FirefoxOptions;

            public WebDriver setupFirefox() {
                WebDriverManager.firefoxdriver().setup();

                FirefoxOptions options = new FirefoxOptions();
                options.addArguments("--width=1920");
                options.addArguments("--height=1080");

                // Headless mode
                // options.addArguments("--headless");

                return new FirefoxDriver(options);
            }
            """);

        System.out.println("3. JUnit test class:");
        System.out.println("""
            import org.junit.jupiter.api.*;

            public class WebTest {

                private WebDriver driver;

                @BeforeEach
                public void setUp() {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver();
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                }

                @Test
                public void testGoogleSearch() {
                    driver.get("https://www.google.com");
                    WebElement searchBox = driver.findElement(By.name("q"));
                    searchBox.sendKeys("Selenium WebDriver");
                    searchBox.submit();

                    String title = driver.getTitle();
                    Assertions.assertTrue(title.contains("Selenium WebDriver"));
                }

                @AfterEach
                public void tearDown() {
                    if (driver != null) {
                        driver.quit();
                    }
                }
            }
            """);

        System.out.println();
    }

    // ========== Element Location ==========

    private static void elementLocationDemo() {
        System.out.println("--- Element Location Strategies ---");
        System.out.println("Find elements on web pages\n");

        System.out.println("1. Basic locators:");
        System.out.println("""
            import org.openqa.selenium.By;
            import org.openqa.selenium.WebElement;

            // By ID
            WebElement element = driver.findElement(By.id("username"));

            // By name
            WebElement element = driver.findElement(By.name("email"));

            // By class name
            WebElement element = driver.findElement(By.className("btn-primary"));

            // By tag name
            WebElement element = driver.findElement(By.tagName("h1"));

            // By link text (exact match)
            WebElement element = driver.findElement(By.linkText("Click Here"));

            // By partial link text
            WebElement element = driver.findElement(By.partialLinkText("Click"));
            """);

        System.out.println("2. CSS selectors:");
        System.out.println("""
            // By CSS selector
            WebElement element = driver.findElement(By.cssSelector("#username"));
            WebElement element = driver.findElement(By.cssSelector(".btn-primary"));
            WebElement element = driver.findElement(By.cssSelector("input[type='email']"));
            WebElement element = driver.findElement(By.cssSelector("div.container > p"));
            WebElement element = driver.findElement(By.cssSelector("button:nth-child(2)"));

            // CSS selector examples:
            // #id                  - ID selector
            // .class               - Class selector
            // tag                  - Tag selector
            // [attribute=value]    - Attribute selector
            // parent > child       - Direct child
            // ancestor descendant  - Any descendant
            // element:nth-child(n) - Nth child
            """);

        System.out.println("3. XPath:");
        System.out.println("""
            // By XPath
            WebElement element = driver.findElement(By.xpath("//input[@id='username']"));
            WebElement element = driver.findElement(By.xpath("//button[text()='Submit']"));
            WebElement element = driver.findElement(By.xpath("//div[@class='container']//p"));

            // XPath examples:
            // //tag[@attribute='value']          - Basic XPath
            // //tag[text()='text']               - Text match
            // //tag[contains(@class, 'partial')] - Partial match
            // //tag[@id='parent']//tag           - Descendant
            // //tag[@id='parent']/tag            - Direct child
            // (//tag)[1]                         - First element
            // //tag[last()]                      - Last element
            // //tag[@id='x' and @class='y']      - Multiple conditions
            """);

        System.out.println("4. Finding multiple elements:");
        System.out.println("""
            import java.util.List;

            // Find all matching elements
            List<WebElement> links = driver.findElements(By.tagName("a"));

            for (WebElement link : links) {
                System.out.println(link.getText());
                System.out.println(link.getAttribute("href"));
            }

            // Find elements within an element
            WebElement table = driver.findElement(By.id("data-table"));
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                for (WebElement cell : cells) {
                    System.out.print(cell.getText() + "\\t");
                }
                System.out.println();
            }
            """);

        System.out.println();
    }

    // ========== Interactions ==========

    private static void interactionsDemo() {
        System.out.println("--- Element Interactions ---");
        System.out.println("Interact with web elements\n");

        System.out.println("1. Basic interactions:");
        System.out.println("""
            // Click
            WebElement button = driver.findElement(By.id("submit-btn"));
            button.click();

            // Type text
            WebElement input = driver.findElement(By.name("username"));
            input.sendKeys("john.doe@example.com");

            // Clear and type
            input.clear();
            input.sendKeys("new.email@example.com");

            // Submit form
            WebElement form = driver.findElement(By.id("login-form"));
            form.submit();

            // Get text
            WebElement heading = driver.findElement(By.tagName("h1"));
            String text = heading.getText();

            // Get attribute
            WebElement link = driver.findElement(By.id("home-link"));
            String href = link.getAttribute("href");
            String cssClass = link.getAttribute("class");

            // Check if displayed/enabled/selected
            boolean isDisplayed = element.isDisplayed();
            boolean isEnabled = element.isEnabled();
            boolean isSelected = element.isSelected();  // For checkboxes/radios
            """);

        System.out.println("2. Dropdowns and select:");
        System.out.println("""
            import org.openqa.selenium.support.ui.Select;

            WebElement dropdown = driver.findElement(By.id("country"));
            Select select = new Select(dropdown);

            // Select by visible text
            select.selectByVisibleText("United States");

            // Select by value
            select.selectByValue("us");

            // Select by index
            select.selectByIndex(1);

            // Get selected option
            WebElement selectedOption = select.getFirstSelectedOption();
            System.out.println("Selected: " + selectedOption.getText());

            // Get all options
            List<WebElement> options = select.getOptions();
            for (WebElement option : options) {
                System.out.println(option.getText());
            }

            // Multi-select
            if (select.isMultiple()) {
                select.selectByVisibleText("Option 1");
                select.selectByVisibleText("Option 2");
                select.deselectAll();
            }
            """);

        System.out.println("3. Advanced interactions:");
        System.out.println("""
            import org.openqa.selenium.interactions.Actions;

            Actions actions = new Actions(driver);

            // Hover over element
            WebElement menu = driver.findElement(By.id("main-menu"));
            actions.moveToElement(menu).perform();

            // Right-click
            actions.contextClick(element).perform();

            // Double-click
            actions.doubleClick(element).perform();

            // Drag and drop
            WebElement source = driver.findElement(By.id("draggable"));
            WebElement target = driver.findElement(By.id("droppable"));
            actions.dragAndDrop(source, target).perform();

            // Click and hold
            actions.clickAndHold(element).perform();

            // Key combinations
            actions.keyDown(Keys.CONTROL)
                   .sendKeys("a")
                   .keyUp(Keys.CONTROL)
                   .perform();

            // Chain actions
            actions.moveToElement(menu)
                   .click()
                   .sendKeys("search query")
                   .sendKeys(Keys.ENTER)
                   .perform();
            """);

        System.out.println("4. Alerts and popups:");
        System.out.println("""
            import org.openqa.selenium.Alert;

            // Handle JavaScript alert
            driver.findElement(By.id("alert-btn")).click();
            Alert alert = driver.switchTo().alert();
            System.out.println("Alert text: " + alert.getText());
            alert.accept();  // Click OK

            // Confirm dialog
            driver.findElement(By.id("confirm-btn")).click();
            Alert confirm = driver.switchTo().alert();
            confirm.dismiss();  // Click Cancel
            // or confirm.accept();  // Click OK

            // Prompt dialog
            driver.findElement(By.id("prompt-btn")).click();
            Alert prompt = driver.switchTo().alert();
            prompt.sendKeys("User input");
            prompt.accept();
            """);

        System.out.println();
    }

    // ========== Waits ==========

    private static void waitsDemo() {
        System.out.println("--- Waits ---");
        System.out.println("Handle dynamic content and timing issues\n");

        System.out.println("1. Implicit wait:");
        System.out.println("""
            import java.time.Duration;

            // Global timeout for findElement
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // Now all findElement calls wait up to 10 seconds
            WebElement element = driver.findElement(By.id("dynamic-content"));
            """);

        System.out.println("2. Explicit wait:");
        System.out.println("""
            import org.openqa.selenium.support.ui.WebDriverWait;
            import org.openqa.selenium.support.ui.ExpectedConditions;

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Wait for element to be visible
            WebElement element = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("content"))
            );

            // Wait for element to be clickable
            WebElement button = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("submit"))
            );

            // Wait for title to contain text
            wait.until(ExpectedConditions.titleContains("Dashboard"));

            // Wait for URL to be
            wait.until(ExpectedConditions.urlToBe("https://example.com/home"));

            // Wait for text to be present
            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.id("status"), "Success"
            ));

            // Wait for invisibility
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.id("loading-spinner")
            ));

            // Wait for number of elements
            wait.until(ExpectedConditions.numberOfElementsToBe(
                By.className("item"), 5
            ));
            """);

        System.out.println("3. Fluent wait:");
        System.out.println("""
            import org.openqa.selenium.support.ui.FluentWait;
            import org.openqa.selenium.NoSuchElementException;

            FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class);

            WebElement element = fluentWait.until(driver -> {
                WebElement el = driver.findElement(By.id("dynamic-element"));
                return el.isDisplayed() ? el : null;
            });
            """);

        System.out.println("4. Custom wait conditions:");
        System.out.println("""
            // Wait for AJAX to complete
            wait.until(driver -> {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return js.executeScript("return jQuery.active == 0").equals(true);
            });

            // Wait for page to be fully loaded
            wait.until(driver -> {
                return ((JavascriptExecutor) driver)
                    .executeScript("return document.readyState")
                    .equals("complete");
            });

            // Custom condition
            wait.until(driver -> {
                WebElement counter = driver.findElement(By.id("counter"));
                int count = Integer.parseInt(counter.getText());
                return count > 100;
            });
            """);

        System.out.println();
    }

    // ========== Page Object Model ==========

    private static void pageObjectModelDemo() {
        System.out.println("--- Page Object Model (POM) ---");
        System.out.println("Design pattern for maintainable test code\n");

        System.out.println("1. Page Object class:");
        System.out.println("""
            import org.openqa.selenium.support.FindBy;
            import org.openqa.selenium.support.PageFactory;

            public class LoginPage {

                private WebDriver driver;

                // Elements
                @FindBy(id = "username")
                private WebElement usernameField;

                @FindBy(id = "password")
                private WebElement passwordField;

                @FindBy(css = "button[type='submit']")
                private WebElement loginButton;

                @FindBy(className = "error-message")
                private WebElement errorMessage;

                // Constructor
                public LoginPage(WebDriver driver) {
                    this.driver = driver;
                    PageFactory.initElements(driver, this);
                }

                // Actions
                public void enterUsername(String username) {
                    usernameField.clear();
                    usernameField.sendKeys(username);
                }

                public void enterPassword(String password) {
                    passwordField.clear();
                    passwordField.sendKeys(password);
                }

                public DashboardPage clickLogin() {
                    loginButton.click();
                    return new DashboardPage(driver);
                }

                public LoginPage loginAs(String username, String password) {
                    enterUsername(username);
                    enterPassword(password);
                    clickLogin();
                    return this;
                }

                public String getErrorMessage() {
                    return errorMessage.getText();
                }

                public boolean isErrorDisplayed() {
                    return errorMessage.isDisplayed();
                }
            }
            """);

        System.out.println("2. Using Page Objects in tests:");
        System.out.println("""
            @Test
            public void testSuccessfulLogin() {
                driver.get("https://example.com/login");

                LoginPage loginPage = new LoginPage(driver);
                DashboardPage dashboard = loginPage
                    .enterUsername("john.doe@example.com")
                    .enterPassword("password123")
                    .clickLogin();

                Assertions.assertTrue(dashboard.isLoaded());
                Assertions.assertEquals("Welcome, John", dashboard.getWelcomeMessage());
            }

            @Test
            public void testFailedLogin() {
                driver.get("https://example.com/login");

                LoginPage loginPage = new LoginPage(driver);
                loginPage.loginAs("invalid@example.com", "wrongpassword");

                Assertions.assertTrue(loginPage.isErrorDisplayed());
                Assertions.assertEquals("Invalid credentials",
                    loginPage.getErrorMessage());
            }
            """);

        System.out.println();
    }

    // ========== Advanced Features ==========

    private static void advancedFeaturesDemo() {
        System.out.println("--- Advanced Features ---");
        System.out.println("JavaScript, screenshots, frames, and best practices\n");

        System.out.println("1. JavaScript execution:");
        System.out.println("""
            import org.openqa.selenium.JavascriptExecutor;

            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Execute JavaScript
            js.executeScript("alert('Hello from Selenium');");

            // Scroll to element
            WebElement element = driver.findElement(By.id("footer"));
            js.executeScript("arguments[0].scrollIntoView(true);", element);

            // Click using JavaScript
            js.executeScript("arguments[0].click();", element);

            // Get return value
            String title = (String) js.executeScript("return document.title;");

            // Modify DOM
            js.executeScript("document.getElementById('username').value = 'test@example.com';");

            // Wait for page load
            js.executeScript("return document.readyState").equals("complete");
            """);

        System.out.println("2. Screenshots:");
        System.out.println("""
            import org.openqa.selenium.OutputType;
            import org.openqa.selenium.TakesScreenshot;
            import org.apache.commons.io.FileUtils;

            // Full page screenshot
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            File srcFile = screenshot.getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshot.png");
            FileUtils.copyFile(srcFile, destFile);

            // Element screenshot
            WebElement element = driver.findElement(By.id("logo"));
            File elementScreenshot = element.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(elementScreenshot, new File("element.png"));

            // Base64 screenshot
            String base64 = screenshot.getScreenshotAs(OutputType.BASE64);
            """);

        System.out.println("3. Frames and windows:");
        System.out.println("""
            // Switch to iframe
            driver.switchTo().frame("iframe-name");
            driver.switchTo().frame(0);  // By index
            WebElement iframe = driver.findElement(By.id("iframe"));
            driver.switchTo().frame(iframe);  // By element

            // Switch back to main content
            driver.switchTo().defaultContent();

            // Handle multiple windows/tabs
            String mainWindow = driver.getWindowHandle();

            // Click link that opens new window
            driver.findElement(By.id("new-window-link")).click();

            // Switch to new window
            Set<String> windows = driver.getWindowHandles();
            for (String window : windows) {
                if (!window.equals(mainWindow)) {
                    driver.switchTo().window(window);
                    break;
                }
            }

            // Work in new window
            System.out.println(driver.getTitle());

            // Close and switch back
            driver.close();
            driver.switchTo().window(mainWindow);
            """);

        System.out.println("4. Cookies:");
        System.out.println("""
            import org.openqa.selenium.Cookie;

            // Add cookie
            Cookie cookie = new Cookie("session", "abc123");
            driver.manage().addCookie(cookie);

            // Get cookie
            Cookie sessionCookie = driver.manage().getCookieNamed("session");
            System.out.println("Value: " + sessionCookie.getValue());

            // Get all cookies
            Set<Cookie> allCookies = driver.manage().getCookies();
            for (Cookie c : allCookies) {
                System.out.println(c.getName() + " = " + c.getValue());
            }

            // Delete cookie
            driver.manage().deleteCookieNamed("session");
            driver.manage().deleteAllCookies();
            """);

        System.out.println("5. Best practices:");
        System.out.println("   ✓ Use WebDriverManager for automatic driver management");
        System.out.println("   ✓ Use Page Object Model for maintainability");
        System.out.println("   ✓ Use explicit waits over Thread.sleep()");
        System.out.println("   ✓ Take screenshots on test failure");
        System.out.println("   ✓ Use CSS selectors over XPath (faster)");
        System.out.println("   ✓ Always close/quit driver in @AfterEach");
        System.out.println("   ✓ Use headless mode in CI/CD pipelines");
        System.out.println("   ✓ Handle StaleElementReferenceException");
        System.out.println("   ✗ Don't use hardcoded Thread.sleep()");
        System.out.println("   ✗ Don't use overly complex XPath");
        System.out.println("   ✗ Don't forget to switch back from frames/windows");

        System.out.println("\n6. Common patterns:");
        System.out.println("""
            // Retry logic for flaky tests
            @RepeatedTest(3)
            public void flakyTest() {
                // Test code
            }

            // Test data from CSV/Excel
            @ParameterizedTest
            @CsvSource({"user1,pass1", "user2,pass2"})
            public void testMultipleLogins(String username, String password) {
                loginPage.loginAs(username, password);
            }

            // Screenshot on failure
            @AfterEach
            public void tearDown(TestInfo testInfo) {
                if (testInfo.getTags().contains("failed")) {
                    takeScreenshot(testInfo.getDisplayName());
                }
                driver.quit();
            }
            """);

        System.out.println();
    }
}
