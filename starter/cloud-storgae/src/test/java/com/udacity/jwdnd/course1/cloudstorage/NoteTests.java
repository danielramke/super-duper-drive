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
public class NoteTests {

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
    public void checkToAddNotes() {
        createTempUser("Exepta");
        wait.until(ExpectedConditions.urlContains("login"));
        loginPage.login("Exepta", "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getNoteTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddNote"))));
        homePage.addNote("Test Added");
        Assertions.assertEquals(1,driver.findElements(By.id("tableNoteTitle")).size());

        List<WebElement> elements = driver.findElements(By.id("tableNoteTitle"));
        for(WebElement element : elements) {
            System.out.println("Note: " + element.getAttribute("innerHTML"));
        }
    }

    @Test
    public void checkToUpdateNotes() {
        createTempUser("Exepta1");
        wait.until(ExpectedConditions.urlContains("login"));
        loginPage.login("Exepta1", "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getNoteTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddNote"))));
        homePage.addNote("Test Added");
        Assertions.assertEquals(1,driver.findElements(By.id("tableNoteTitle")).size());
        getNoteTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddNote"))));
        homePage.updateNote("Updated Text", "New description set!");

        List<WebElement> elements = driver.findElements(By.id("tableNoteTitle"));
        for(WebElement element : elements) {
            System.out.println("Note: " + element.getAttribute("innerHTML"));
        }
    }

    @Test
    public void checkToDeleteNotes() {
        createTempUser("Exepta2");
        wait.until(ExpectedConditions.urlContains("login"));
        loginPage.login("Exepta2", "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getNoteTab();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("btnAddNote"))));
        homePage.addNote("Test Added");
        getNoteTab();
        Assertions.assertEquals(1,driver.findElements(By.id("tableNoteTitle")).size());
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[@class='btn btn-danger delNote']"))));
        homePage.deleteNote();
        Assertions.assertEquals(0,driver.findElements(By.id("tableNoteTitle")).size());
    }

    public void getNoteTab(){
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();" , driver.findElement(By.xpath("//a[@href='#nav-notes']")));
    }

    public void createTempUser(String username) {
        driver.get("http://localhost:" + port + "/signup");
        signUpPage.signUp(username, "123456");
        loginPage.login(username, "123456");
        wait.until(ExpectedConditions.urlContains("home"));
        getNoteTab();
        wait.until(ExpectedConditions.attributeContains(By.id("nav-notes-tab"), "class", "active"));
        //Home page
        driver.findElement(By.xpath("//*[@class='btn btn-secondary float-right']")).submit();
        wait.until(ExpectedConditions.urlContains("login"));
    }

}
