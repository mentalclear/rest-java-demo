package stg.challenges;

import io.restassured.http.Cookies;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import java.util.*;


import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;

public class Challenge9 {
    public WebDriver driver;
    public WebDriverWait driverWait;

    @BeforeSuite
    public void startSuite() {
        System.setProperty("webdriver.chrome.driver", "./driver/chromedriver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driverWait = new WebDriverWait(driver, 10);
    }

    @Test(priority = 1)
    public void verifyThatCopartIsOpened(){
        driver.get("https://copart.com");
        Assert.assertEquals(driver.getTitle(),
                "Copart USA - Online Live Vehicle Auctions - Bid & Win");
    }

    @Test (priority = 2)
    public void getValueTypeForEachKey() {
        String endpointURL = "https://www.copart.com/public/lots/search";

        Set<Cookie> seleniumCookies = driver.manage().getCookies();
        List<io.restassured.http.Cookie> restAssuredCookies = new ArrayList<>();
        for (org.openqa.selenium.Cookie cookie : seleniumCookies) {
            restAssuredCookies.add(new io.restassured.http.Cookie.Builder(cookie.getName(), cookie.getValue()).build());
        }

        String response = given()
                .cookies(new Cookies(restAssuredCookies))
                .header("accept", "application/json")
                .queryParam("query", "Mitsubishi Lancer EVO")
                .when()
                .post(endpointURL).asString();

        Map<Object, Object> allItems = from(response).getMap("data.results.content[0]");
        for (Map.Entry<Object,Object> item : allItems.entrySet())
            System.out.println("Key: " + item.getKey() +
                    ", Value is a: " + item.getValue().getClass().getSimpleName());
    }

    @AfterSuite
    public void stopSuite() {
       driver.quit();
    }
}