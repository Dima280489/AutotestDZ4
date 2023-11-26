package ru.netology.rest;

import com.codeborne.selenide.Condition;
import dev.failsafe.internal.util.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class AppCardDeliveryTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void afterEach() {
        driver.quit();
        driver = null;
    }

    private String generateDate(int adddays, String pattern) {
        return LocalDate.now().plusDays(adddays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void deliveryFormWorks () {
        open("http://localhost:9999");
        $ ("[data-test-id='city'] input").setValue("Москва");
        String planningDate = generateDate(4,"dd.MM.yyyy");
        $ ("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $ ("[data-test-id='date'] input").setValue(planningDate);
        $ ("[data-test-id='name'] input").setValue("Жуков Дмитрий");
        $ ("[data-test-id='phone'] input").setValue("+79210000000");
        $ ("[data-test-id='agreement']").click();
        $ ("button.button").click();
        $ (".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + planningDate));
    }
}
