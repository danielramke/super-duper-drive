package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    //NOTES
    @FindBy(id = "btnAddNote")
    private WebElement addNote;

    @FindBy(xpath = "//a[@class='btn btn-danger delNote']")
    private WebElement deleteNote;

    @FindBy(xpath = "//button[@class='btn btn-success editNote']")
    private WebElement editNote;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmit;

    //CREDENTIALS
    @FindBy(id = "btnAddCredential")
    private WebElement addCredential;

    @FindBy(xpath = "//a[@class='btn btn-danger delCredential']")
    private WebElement deleteCredential;

    @FindBy(xpath = "//button[@class='btn btn-success editCredential']")
    private WebElement editCredential;

    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "credentialSubmit")
    private WebElement credentialSubmit;

    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, Duration.ofMillis(1000));
    }

    //Note methods
    public void addNote(String title) {
        addNote.click();
        wait.until(ExpectedConditions.visibilityOf(noteTitle));
        noteTitle.sendKeys(title);
        noteDescription.sendKeys(title + " - First note.");
        noteSubmit.submit();
    }

    public void updateNote(String title, String description) {
        wait.until(ExpectedConditions.urlContains("home"));
        editNote.click();
        wait.until(ExpectedConditions.visibilityOf(noteTitle));
        noteTitle.clear();
        noteTitle.sendKeys(title);
        noteDescription.clear();
        noteDescription.sendKeys(description);
        noteSubmit.submit();
    }

    public void deleteNote() {
        wait.until(ExpectedConditions.urlContains("home"));
        deleteNote.click();
    }

    //Credential methods
    public void addCredential(String username) {
        addCredential.click();
        wait.until(ExpectedConditions.visibilityOf(credentialUrl));
        credentialUrl.sendKeys("http://localhost:8080/login");
        credentialUsername.sendKeys(username);
        credentialPassword.sendKeys("123456");
        credentialSubmit.submit();
    }

    public void updateCredential(String username) {
        editCredential.click();
        wait.until(ExpectedConditions.visibilityOf(credentialUrl));
        credentialUrl.clear();
        credentialUrl.sendKeys("https://www.facebook.com/login");
        credentialUsername.clear();
        credentialUsername.sendKeys(username);
        credentialPassword.sendKeys("112");
        credentialSubmit.submit();
    }

    public void deleteCredential() {
        wait.until(ExpectedConditions.urlContains("home"));
        deleteCredential.click();
    }

}
