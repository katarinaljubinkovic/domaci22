import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

/*
Test 1: Verifikovati da se u url-u stranice javlja ruta "/login".
Verifikovati da atribut type u polju za unos email ima vrednost "email" i za password da ima atribut type "password.
Test 2: Koristeci Faker uneti nasumicno generisan email i password i verifikovati da se pojavljuje poruka "User does not exist".
Test 3: Verifikovati da kad se unese admin@admin.com (sto je dobar email) i pogresan password (generisan faker-om),
da se pojavljuje poruka "Wrong password"

 */
public class LoginPageTest {
    private WebDriver driver;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Ivan\\Documents\\IT Bootcamp\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        Faker faker = new Faker();
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get("https://vue-demo.daniel-avellaneda.com");
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div/div/header/div/div[3]/a[3]/span"));
        loginButton.click();
    }

    @Test
    public void Test1() {
        //PRVI NAČIN NA KOJI SAM URADILA BEZ GLEDANJA 29. PREDAVANJA
        //String expectedUrl = "https://vue-demo.daniel-avellaneda.com/login";
        //String actualUrl = driver.getCurrentUrl();
        //Assert.assertEquals(actualUrl, expectedUrl);

        String actualUrl = driver.getCurrentUrl();
        Assert.assertTrue(actualUrl.contains("/login"));

        WebElement email = driver.findElement(By.name("email"));

        String expectedType = "email";
        String actualType = email.getAttribute("type");

        Assert.assertEquals(actualType, expectedType);


        WebElement password = driver.findElement(By.id("password"));

        String expectedResult = "password";
        String actualResult = password.getAttribute("type");

        Assert.assertEquals(actualResult, expectedResult);
    }

    @Test
    public void Test2() {
        Faker faker = new Faker();

        WebElement email = driver.findElement(By.name("email"));
        email.sendKeys(faker.internet().emailAddress());

        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys(faker.internet().password());

        WebElement login = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[3]/span/form/div/div[3]/button"));
        login.submit();

        WebElement popUpWindow = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[4]/div/div/div/div/div[1]"));

        //String expectedMassage = "User does not exists";      //DRUGI NAČIN
        String actualMassage = popUpWindow.getText();       //u xpath sam dodala /ul/li  kako se ne bi uzimao sav tekst iz popUp-a
                                                            //i poredila sa Assert.assertEquals(actual,expected)
        Assert.assertTrue(actualMassage.contains("User does not exists"));
    }

    @Test
    public void Test3() {
        Faker faker = new Faker();

        WebElement email = driver.findElement(By.name("email"));
        email.sendKeys("admin@admin.com");

        WebElement password = driver.findElement(By.id("password"));
        password.sendKeys(faker.internet().password());

        WebElement login = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[3]/span/form/div/div[3]/button"));
        login.submit();

        WebElement popUpWindow = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[4]/div/div/div/div/div[1]"));

        //String expectedMassage = "Wrong password";
        String actualMassage = popUpWindow.getText();

        Assert.assertTrue(actualMassage.contains("Wrong password"));
    }

    @AfterMethod
    public void afterMethod() {

    }
    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}