package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private SignUpPage signUpPage;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofMillis(1000));
        this.signUpPage = new SignUpPage(driver);
        this.loginPage = new LoginPage(driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void checkUnauthenticatedAccess() {
        driver.get("http://localhost:" + port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
        wait.until(ExpectedConditions.urlContains("login"));

        driver.get("http://localhost:" + port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
        wait.until(ExpectedConditions.urlContains("signup"));

        driver.get("http://localhost:" + port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
        wait.until(ExpectedConditions.urlContains("login"));

        driver.get("http://localhost:" + port + "/result");
        Assertions.assertEquals("Login", driver.getTitle());
        wait.until(ExpectedConditions.urlContains("login"));
    }

    @Test
    public void checkUserAuthenticateFlow() {
        driver.get("http://localhost:" + port + "/signup");
        wait.until(ExpectedConditions.urlContains("signup"));
        signUpPage.signUp("Exepta", "test12346");

        loginPage.login("Exepta", "test12346");
        wait.until(ExpectedConditions.urlContains("home"));
        Assertions.assertEquals("Home", driver.getTitle());

        driver.findElement(By.xpath("//*[@class='btn btn-secondary float-right']")).submit();
        wait.until(ExpectedConditions.urlContains("login"));
        Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + port + "/home");
        wait.until(ExpectedConditions.urlContains("login"));
        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    public void checkBadUrl() {
        driver.get("http://localhost:" + port + "/signup");
        wait.until(ExpectedConditions.urlContains("signup"));
        signUpPage.signUp("Exepta1", "test12346");

        loginPage.login("Exepta1", "test12346");
        wait.until(ExpectedConditions.urlContains("home"));

        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }

    @Test
    public void checkToLargeUpload() {
        driver.get("http://localhost:" + port + "/signup");
        wait.until(ExpectedConditions.urlContains("signup"));
        signUpPage.signUp("Exepta2", "test12346");

        loginPage.login("Exepta2", "test12346");

        String filename = "upload5m.zip";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(filename).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
    }
}
