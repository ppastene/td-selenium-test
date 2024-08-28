package com.example.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class SeleniumTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    public void setUp() {
        // Configura ChromeDriver
        System.setProperty("webdriver.chrome.driver", "E:/Users/chromedriver-win64/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        // Quita el modo headless por ahora para ver si resuelve el problema
        // options.addArguments("--headless");

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-gpu"); // A veces necesario para sistemas Linux
        options.addArguments("--no-sandbox"); // Recomendado para entornos CI/CD
        options.addArguments("--disable-dev-shm-usage"); // Soluciona problemas de memoria en contenedores

        driver = new ChromeDriver(options);

        // Inicializa WebDriverWait con una duración de espera predeterminada
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    }

    @Test
    public void testGoogleSearch() {
        try {
            // Navega a Google
            driver.get("https://www.google.com");

            // Espera hasta que el campo de búsqueda esté presente
            WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));

            // Escribe "Selenium" y presiona Enter
            searchBox.sendKeys("Selenium");
            searchBox.submit();

            // Espera que los resultados aparezcan
            WebElement results = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));

            // Validaciones mejoradas
            assertTrue(results.isDisplayed(), "El contenedor de resultados no está visible.");
            assertTrue(results.getText().toLowerCase().contains("selenium"), "Los resultados no contienen 'Selenium'.");

        } catch (Exception e) {
            fail("Error durante la prueba: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {
        // Cierra el navegador
        if (driver != null) {
            driver.quit();
        }
    }
}
