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

    // ---------- Data Provider ----------
    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][] {
            {"standard_user", "secret_sauce", "valid"} // ✅ Valid credentials
        };
    }

    // ---------- Test Case ----------
    @Test(dataProvider = "loginData")
    public void verifyProductAddToCart(String username, String password, String expectedResultType) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Step 1: Login
        driver.findElement(By.xpath("//input[@id='user-name']")).sendKeys(username);
        driver.findElement(By.xpath("//input[@id='password']")).sendKeys(password);
        driver.findElement(By.xpath("//input[@id='login-button']")).click();

        // Step 2: Wait for inventory page
        wait.until(ExpectedConditions.urlContains("inventory"));

        // Step 3: Get product name
        WebElement productName = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[normalize-space()='Sauce Labs Backpack']")
        ));

        // Step 4: Get product price
        WebElement productPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//div[@class='inventory_list']//div[1]//div[2]//div[2]//div[1]")
        ));

        System.out.println("👜 Product Name: " + productName.getText());
        System.out.println("💰 Product Price: " + productPrice.getText());

        // Step 5: Click on Add to Cart button
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//button[@id='add-to-cart-sauce-labs-backpack']")
        ));
        addToCartButton.click();

        // Step 6: Verify cart badge shows 1 item
        WebElement cartBadge = wait.until(ExpectedConditions.visibilityOfElementLocated(
            By.xpath("//a[@class='shopping_cart_link']")
        ));

        String cartCount = cartBadge.getText();
        System.out.println("🛒 Items in Cart: " + cartCount);

        Assert.assertEquals(cartCount, "1", "✅ Cart count should be 1 after adding the product.");

        // Step 7: (Optional) Click cart icon to verify navigation
        driver.findElement(By.xpath("//a[@class='shopping_cart_link']")).click();
        wait.until(ExpectedConditions.urlContains("cart"));
        System.out.println("✅ Navigated to cart page successfully.");
    }

   
    
}
