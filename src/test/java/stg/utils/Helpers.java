package stg.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helpers {
    public static void waitForSpinnerToClose(WebDriverWait wait) {
        WebElement spinner = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='serverSideDataTable_processing']")));
        wait.until(ExpectedConditions.attributeToBe(spinner, "style", "display: none;"));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotPath) throws IOException {
        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(screenshotPath));
    }

    public static String addTimeStamp() {
        LocalDateTime localTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy_hh-mm-ss");
        return localTime.format(formatter);
    }
}
