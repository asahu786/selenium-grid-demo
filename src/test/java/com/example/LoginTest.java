package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.net.URL;

public class LoginTest {
    private WebDriver driver;

    private WebDriver createRemoteDriver() throws Exception {
        ChromeOptions options = new ChromeOptions();
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        return driver;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) driver.quit();
    }
    
    @Test
    public void testInvalidLogin() throws Exception {
        driver = createRemoteDriver();
        driver.get("https://the-internet.herokuapp.com/login");

        driver.findElement(By.id("username")).sendKeys("tomsmith");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        boolean successShown = driver.findElements(By.xpath("//div[contains(text(),'invalid')]")).size() > 0;
        Assertions.assertFalse(successShown, "Login Failed Ok3");
    }

//    @Test
//    public void testValidLogin() throws Exception {
//        driver = createRemoteDriver();
//        driver.get("https://the-internet.herokuapp.com/login");
//
//        driver.findElement(By.id("username")).sendKeys("wronguser");
//        driver.findElement(By.id("password")).sendKeys("wrongpass");
//        driver.findElement(By.cssSelector("button[type='submit']")).click();
//
//        boolean errorShown = driver.findElements(By.cssSelector(".flash.error")).size() > 0;
//        Assertions.assertTrue(errorShown, "Invalid login should show an error");
//    }
}
