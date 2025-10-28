package Login;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Signin {

    WebDriver driver;

    // ---------- Setup ----------
    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    // ---------- Data Provider ----------
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
            {"standard_user", "secret_sauce", "valid"},          // ✅ Valid credentials
            {"invalidEmail", "12345678", "invalid_email"},       // ❌ Invalid email format
            {"standard_user", "wrongpass", "invalid_pass"},      // ❌ Wrong password
            {"", "", "blank"}                                    // ❌ Blank fields
        };
    }

    // ---------- Test Case ----------
    @Test(dataProvider = "loginData")
    public void loginTest(String username, String password, String expectedResultType) {

        // Enter username and password
        driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);

        // Click login button ✅ Fixed XPath
        driver.findElement(By.xpath("//input[@id='login-button']")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        switch (expectedResultType) {
            case "valid":
                // Wait for inventory page URL to appear after successful login
                boolean success = wait.until(ExpectedConditions.urlContains("inventory"));
                Assert.assertTrue(success, "✅ Login should be successful, redirected to inventory page.");
                break;

            case "invalid_email":
            case "invalid_pass":
            case "blank":
                // Wait for the error message to appear
                WebElement errorMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//h3[contains(text(),'Epic sadface:')]")));
                Assert.assertTrue(errorMsg.isDisplayed(),
                        "❌ Error message should appear for invalid credentials or blank fields.");
                break;

            default:
                Assert.fail("Unknown expected result type: " + expectedResultType);
        }
    }

    // ---------- Teardown ----------
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
