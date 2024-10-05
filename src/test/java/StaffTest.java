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
public class StaffTest {

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
    public void getAllStaffTest() throws InterruptedException {

        driverWait.until(ExpectedConditions.elementToBeClickable(By.linkText("Display staff"))).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/staff.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    /*@Test
    @Order(3)
    public void createStaffTest() throws InterruptedException {

        driver.findElement(By.linkText("Display staff")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Create new staff'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt7:email'])")).sendKeys("test@selenium.com");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt7:firstName'])")).sendKeys("Test");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt7:lastName'])")).sendKeys("Selenium");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt7:phone'])")).sendKeys("1234567890");
        Thread.sleep(1000);
        Select stateSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt7:chooseState'])")));
        stateSelect.selectByValue("ACTIVE");
        Thread.sleep(1000);
        Select managerSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt7:chooseManager'])")));
        managerSelect.selectByValue("33");
        Thread.sleep(1000);
        Select storeSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt7:chooseStore'])")));
        storeSelect.selectByValue("2");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Create'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Yes'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Ok'])")).click();

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/staff.xhtml", currentUrl);

        Thread.sleep(2000);
    }*/

    /*@Test
    @Order(4)
    public void getSingleStaffTest() throws InterruptedException {

        driver.findElement(By.linkText("Display staff")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Next'])")).click();
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Next'])")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='button' and @value='To detailed view'])[9]")).click();
        Thread.sleep(1000);
        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_detail_pages/productDetails.xhtml?productId=73", currentUrl);

        Thread.sleep(2000);
    }*/

    /*@Test
    @Order(5)
    public void updateProductTest() throws InterruptedException {

        driver.findElement(By.linkText("Display staff")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Next'])")).click();
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Next'])")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='button' and @value='To detailed view'])[9]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Edit staff'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:email'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:email'])")).sendKeys("master@selenium.com");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:firstName'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:firstName'])")).sendKeys("Master");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:lastName'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:lastName'])")).sendKeys("Seleniumtester");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:phone'])")).clear();
        Thread.sleep(10);
        driver.findElement(By.xpath("(//input[@type='text' and @id='j_idt34:phone'])")).sendKeys("9876543210");
        Thread.sleep(1000);
        Select stateSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt34:chooseState'])")));
        stateSelect.selectByValue("INACTIVE");
        Thread.sleep(1000);
        Select managerSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt34:chooseManager'])")));
        managerSelect.selectByValue("2");
        Thread.sleep(1000);
        Select storeSelect = new Select(driver.findElement(By.xpath("(//select[@id='j_idt34:chooseStore'])")));
        storeSelect.selectByValue("1");
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Update'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Update'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Ok'])")).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_detail_pages/staffDetails.xhtml", currentUrl);

        Thread.sleep(2000);
    }*/

    /*@Test
    @Order(6)
    public void deleteStaffTest() throws InterruptedException {

        driver.findElement(By.linkText("Display staff")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Next'])")).click();
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Next'])")).click();
        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='button' and @value='To detailed view'])[9]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Delete'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='submit' and @value='Proceed'])")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//input[@type='button' and @value='Ok'])")).click();
        Thread.sleep(1000);

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/staff.xhtml", currentUrl);

        Thread.sleep(2000);
    }*/

    @Test
    @Order(7)
    public void logoutTest() throws InterruptedException {

        driver.findElement(By.linkText("Display staff")).click();

        Thread.sleep(1000);

        driver.findElement(By.xpath("(//input[@type='submit' and @value='Logout'])")).click();

        String currentUrl = driver.getCurrentUrl();

        Assertions.assertEquals("https://localhost:8443/team-05/protected/master_pages/staff.xhtml", currentUrl);

        Thread.sleep(2000);
    }

    @AfterAll
    public static void close() {

        driver.close();
    }
}
