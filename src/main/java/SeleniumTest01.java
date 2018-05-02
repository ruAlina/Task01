import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SeleniumTest01 {
    private WebDriver driver;
    private String baseUrl;

    @Before
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver", "drv/chromedriver.exe");
        driver = new ChromeDriver();
        baseUrl = " http://www.sberbank.ru/ru/person";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    public void TestMethod() throws Exception {
        driver.get(baseUrl + "/");

        click(By.xpath("//SPAN[text()='Застраховать себя ']"));
        click(By.xpath("//A[text()='Страхование путешественников']"));

        assertEquals("Страхование путешественников", driver.findElement(By.xpath("//div[@class='sbrf-rich-outer']")).getText());
        click(By.xpath("//IMG[@src='/portalserver/content/atom/contentRepository/content/person/travel/banner-zashita-traveler.jpg?id=f6c836e1-5c5c-4367-b0d0-bbfb96be9c53']"));

        assertTrue(switchTo("Сбербанк страхование"));

        click(By.xpath("//DIV[text()='Минимальная']"));
        click(By.xpath("//SPAN[text()='Оформить']"));

        assertEquals("Оформление", driver.findElement(By.xpath("(//LI[contains(@class, 'active')]//SPAN[contains(@class, 'b-heading-tabset-title')])")).getText());
        fillField(By.name("insured0_surname"), "Ivanov");
        fillField(By.name("insured0_name"), "Ivan");
        fillField(By.name("insured0_birthDate"), "25.03.2000");
        fillField(By.name("surname"), "Иванов");
        fillField(By.name("name"), "Иван");
        fillField(By.name("middlename"), "Иванович");
        fillField(By.name("birthDate"), "25.03.2000");
        driver.findElement(By.name("male")).click();
        fillField(By.name("passport_series"), "5525");
        fillField(By.name("passport_number"), "587469");
        fillField(By.name("issueDate"), "25.03.2015");
        fillField(By.name("issuePlace"), "кем-то выдан");

        assertEquals("Ivanov", driver.findElement(By.name("insured0_surname")).getAttribute("value"));
        assertEquals("Ivan", driver.findElement(By.name("insured0_name")).getAttribute("value"));
        assertEquals("25.03.2000", driver.findElement(By.name("insured0_birthDate")).getAttribute("value"));
        assertEquals("Иванов", driver.findElement(By.name("surname")).getAttribute("value"));
        assertEquals("Иван", driver.findElement(By.name("name")).getAttribute("value"));
        assertEquals("Иванович", driver.findElement(By.name("middlename")).getAttribute("value"));
        assertEquals("25.03.2000", driver.findElement(By.name("birthDate")).getAttribute("value"));
        assertEquals("0", driver.findElement(By.name("male")).getAttribute("value"));
        assertEquals("5525", driver.findElement(By.name("passport_series")).getAttribute("value"));
        assertEquals("587469", driver.findElement(By.name("passport_number")).getAttribute("value"));
        assertEquals("25.03.2015", driver.findElement(By.name("issueDate")).getAttribute("value"));
        assertEquals("кем-то выдан", driver.findElement(By.name("issuePlace")).getAttribute("value"));

        click(By.xpath("//SPAN[text()='Продолжить']"));
        assertEquals("Заполнены не все обязательные поля", driver.findElement(By.xpath("//DIV[@ng-show='tryNext && myForm.$invalid']")).getText());
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    private void fillField(By locator, String value) {
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

    private void click(By locator) {
        Wait<WebDriver> wait = new WebDriverWait(driver, 5, 1000);
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private boolean switchTo(String titlePart) {
        String old = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (handle.equals(old))
                continue;
            driver.switchTo().window(handle);
            if (driver.getTitle().contains(titlePart))
                return true;
        }
        driver.switchTo().window(old);
        return false;
    }
}
