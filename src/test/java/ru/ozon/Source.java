package ru.ozon;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Source {


    public static ChromeDriver driver;

    public static void RunChromeDriver() {
        driver = new ChromeDriver();
    }

    public static void createScreenshot(String name) {
        Allure.addAttachment(name,
                new ByteArrayInputStream(((TakesScreenshot) Source.driver)
                        .getScreenshotAs(OutputType.BYTES)));
    }

    public static void clickXpath(String path) {
        (new WebDriverWait(driver, 20))
                .until(ExpectedConditions
                        .presenceOfElementLocated(By.xpath(path))).click();
    }

    public static void doubleClickXpath(String path) {
        Actions actions = new Actions(driver);
        WebElement element =
                (new WebDriverWait(driver, 20))
                        .until(ExpectedConditions
                                .presenceOfElementLocated(By.xpath(path)));
        actions.doubleClick(element).perform();
    }


    public static WebElement findElementByXpathWait(int timeout, String xpath) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions
                .presenceOfElementLocated(By.xpath(xpath)));
    }

    public static WebElement findElementByClassWait(int timeout, String name) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions
                .presenceOfElementLocated(By.className(name)));
    }

    public static List<WebElement> findElementsByClassWait(int timeout, String name) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        return wait.until(ExpectedConditions
                .presenceOfAllElementsLocatedBy(By.className(name)));
    }

    public static void timeOut() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void timeOutMax() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int buildInt(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++)
            if (Character.isDigit(str.charAt(i)))
                sb.append(str.charAt(i));
        return Integer.parseInt(sb.toString());
    }
}