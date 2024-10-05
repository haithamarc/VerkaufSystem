import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductTest {

    public static WebDriver driver;
    public static WebDriverWait driverWait;

    @BeforeAll
    public static void setup() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(chromeOptions);
        driverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    public void backToIndex() {

        driver.get("http://localhost:8080/team-05/");
    }

    private void login(String username, String password) throws InterruptedException {

        driver.findElement(By.cssSelector("input#use_name")).sendKeys(username);
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("input#user_password")).sendKeys(password);
        Thread.sleep(1000);
        driver.findElement(By.cssSelector("input#user_password")).sendKeys(Keys.ENTER);
        Thread.sleep(2000);
    }


    @Test
    @Order(1)
    public void loginTest() throws InterruptedException {

        //WebDriverManager.chromedriver().setup();
        //ChromeDriver chromeDriver = new ChromeDriver();

        driver.get("http://localhost:8080/team-05/");
        driver.manage().window().maximize();
        driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("input#use_name")));

        login("fabiola.jackson@bikes.shop", "555-5554");

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/", currentUrl);
    }

    @Test
    @Order(2)
    public void getAllProductsTest() throws InterruptedException {

        driverWait.until(ExpectedConditions.elementToBeClickable(By.linkText("Display products"))).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/products.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    @Test
    @Order(3)
    public void createProductTest() throws InterruptedException {

        driver.findElement(By.linkText("Display products")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Create new product'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt6:listPrice'])")).sendKeys("799.99");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt6:modelYear'])")).sendKeys("2023");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt6:name'])")).sendKeys("Selenium Test Bike 2023");
        Thread.sleep(1000);
        Select brandSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt6:chooseBrandCreate'])")));
        brandSelect.selectByValue("4");
        Thread.sleep(1000);
        Select categorySelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt6:chooseCategoryCreate'])")));
        categorySelect.selectByValue("7");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Create'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Yes'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Ok'])")).click();

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/products.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    @Test
    @Order(4)
    public void getSingleProductTest() throws InterruptedException {

        driver.findElement(By.linkText("Display products")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='End'])")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='button' and @value='To detailed view'])[4]")).click();
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_detail_pages/productDetails.xhtml?productId=381", currentUrl);

        Thread.sleep(2000);
    }

    @Test
    @Order(5)
    public void updateProductTest() throws InterruptedException {

        driver.findElement(By.linkText("Display products")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='End'])")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='button' and @value='To detailed view'])[4]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Edit product'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt30:listPrice'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt30:listPrice'])")).sendKeys("1799.99");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt30:modelYear'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt30:modelYear'])")).sendKeys("2024");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt30:name'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt30:name'])")).sendKeys("Selenium Edit Bike 2024");
        Thread.sleep(1000);
        Select brandSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt30:chooseBrandCreate'])")));
        brandSelect.selectByValue("2");
        Thread.sleep(1000);
        Select categorySelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt30:chooseCategoryCreate'])")));
        categorySelect.selectByValue("4");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Update'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Update'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Ok'])")).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_detail_pages/productDetails.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    @Test
    @Order(6)
    public void deleteProductTest() throws InterruptedException {

        driver.findElement(By.linkText("Display products")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='End'])")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='button' and @value='To detailed view'])[4]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Delete'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Proceed'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='button' and @value='Ok'])")).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/products.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    @Test
    @Order(7)
    public void logoutTest() throws InterruptedException {

        driver.findElement(By.linkText("Display products")).click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Logout'])")).click();

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/products.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    @AfterAll
    public static void close() {

        driver.close();
    }
}
