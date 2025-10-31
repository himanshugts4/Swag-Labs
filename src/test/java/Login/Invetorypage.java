package Login;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

public class Invetorypage {

    WebDriver driver;

    // ---------- Setup ----------
    @BeforeMethod
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    // ---------- Teardown ----------
    @AfterMethod
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ---------- Data Provider ----------
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"standard_user", "secret_sauce", "valid"}
        };
    }

    // ---------- Test Case ----------
    @Test(dataProvider = "loginData")
    public void verifyProductAddToCart(String username, String password, String expectedResultType) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Step 1: Login
            System.out.println("🔐 Logging in with username: " + username);
            driver.findElement(By.id("user-name")).sendKeys(username);
            driver.findElement(By.id("password")).sendKeys(password);
            driver.findElement(By.id("login-button")).click();

            // Step 2: Wait for inventory page
            wait.until(ExpectedConditions.urlContains("inventory"));
            System.out.println("✅ Successfully logged in!");

            // Step 3: Get first product details
            WebElement productName1 = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[normalize-space()='Sauce Labs Backpack']")
            ));
            WebElement productPrice1 = driver.findElement(
                By.xpath("//div[@class='inventory_list']//div[1]//div[2]//div[2]//div[1]")
            );

            System.out.println("👜 Product 1: " + productName1.getText() + " - " + productPrice1.getText());

            // Step 4: Add first product to cart
            WebElement addToCartButton1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("add-to-cart-sauce-labs-backpack")
            ));
            addToCartButton1.click();
            System.out.println("✅ Added Product 1 to cart");

            // Step 5: Add second product to cart
            WebElement addToCartButton2 = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("add-to-cart-sauce-labs-bike-light")
            ));
            addToCartButton2.click();
            System.out.println("✅ Added Product 2 to cart");

            // Step 6: Verify cart badge shows 2 items
            WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.className("shopping_cart_badge")
            ));

            String cartCount = cartBadge.getText();
            System.out.println("🛒 Items in Cart: " + cartCount);

            // Fixed assertion - should be 2 since we added 2 products
            Assert.assertEquals(cartCount, "2", "✅ Cart count should be 2 after adding two products.");

            // Step 7: Navigate to cart page
            driver.findElement(By.className("shopping_cart_link")).click();
            wait.until(ExpectedConditions.urlContains("cart"));
            System.out.println("✅ Navigated to cart page successfully.");

            // Step 8: Take screenshot
            takeScreenshot("cart_page_screenshot");

        } catch (Exception e) {
            System.err.println("❌ Test failed: " + e.getMessage());
            takeScreenshot("error_screenshot");
            throw e;
        }
    }

    // ---------- Helper Method for Screenshots ----------
    private void takeScreenshot(String fileName) {
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File(fileName + ".png");
            Files.copy(screenshot.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("📸 Screenshot saved at: " + destination.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("❌ Failed to save screenshot: " + e.getMessage());
        }
    }
}