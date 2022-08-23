package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CredentialTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private SignUpPage signUpPage;
    private LoginPage loginPage;
    private HomePage homePage;

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
        this.homePage = new HomePage(driver);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void checkToDeleteCredentials() {
        createTempUser("Exepta2");
        wait.until(ExpectedConditions.urlContains("login"));
        loginPage.login("Exepta2", "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getCredentialTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddCredential"))));
        homePage.addCredential("Exepta-del");
        getCredentialTab();
        Assertions.assertEquals(1,driver.findElements(By.id("tableCredentialUrl")).size());
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@class='btn btn-danger delCredential']"))));
        homePage.deleteCredential();
        Assertions.assertEquals(0,driver.findElements(By.id("tableCredentialUrl")).size());
    }

    @Test
    public void checkToAddCredentials() {
        createTempUser("Exepta");
        wait.until(ExpectedConditions.urlContains("login"));
        loginPage.login("Exepta", "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getCredentialTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddCredential"))));
        homePage.addCredential("Exepta");
        Assertions.assertEquals(1,driver.findElements(By.id("tableCredentialUrl")).size());

        List<WebElement> credentialPasswordList = driver.findElements(By.id("tableCredentialPassword"));
        for (WebElement credentialPassword : credentialPasswordList) {

            System.out.println("Credential Password: " + credentialPassword.getAttribute("innerHTML"));

            Assertions.assertNotEquals("123456", credentialPassword.getAttribute("tableCredentialPassword"));
            Assertions.assertNotEquals("123456789", credentialPassword.getAttribute("tableCredentialPassword"));
        }
    }

    @Test
    public void checkToUpdateCredentials() {
        createTempUser("Exepta1");
        wait.until(ExpectedConditions.urlContains("login"));
        loginPage.login("Exepta1", "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getCredentialTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddCredential"))));
        homePage.addCredential("Exepta");
        Assertions.assertEquals(1,driver.findElements(By.id("tableCredentialUrl")).size());
        getCredentialTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddCredential"))));
        homePage.updateCredential("Exepta-Updated");

        List<WebElement> credentialPasswordList = driver.findElements(By.id("tableCredentialPassword"));
        for (WebElement credentialPassword : credentialPasswordList) {

            System.out.println("Credential Password: " + credentialPassword.getAttribute("innerHTML"));

            Assertions.assertNotEquals("123456", credentialPassword.getAttribute("tableCredentialPassword"));
            Assertions.assertNotEquals("123456789", credentialPassword.getAttribute("tableCredentialPassword"));
        }
    }

    public void getCredentialTab(){
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();" , driver.findElement(By.xpath("//a[@href='#nav-credentials']")));
    }

    public void createTempUser(String username) {
        driver.get("http://localhost:" + port + "/signup");
        signUpPage.signUp(username, "123456");
        loginPage.login(username, "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getCredentialTab();
        wait.until(ExpectedConditions.attributeContains(By.id("nav-credentials-tab"), "class", "active"));
        //Home page
        driver.findElement(By.xpath("//*[@class='btn btn-secondary float-right']")).submit();
        wait.until(ExpectedConditions.urlContains("login"));
    }

}
