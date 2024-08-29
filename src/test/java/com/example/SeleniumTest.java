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

public class SeleniumTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Identifica el sistema operativo
        String os = System.getProperty("os.name").toLowerCase();

        // Configura ChromeDriver según el sistema operativo
        if (os.contains("win")) {
            // Si es Windows, usa la ruta correspondiente
            System.setProperty("webdriver.chrome.driver", "C:/chromedriver-win64/chromedriver.exe");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
            // Si es Linux o macOS, usa la ruta correspondiente (generalmente se usa en Docker)
            System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver/chromedriver");
        }
        ChromeOptions options = new ChromeOptions();
        // Quita el modo headless por ahora para ver si resuelve el problema
        // options.addArguments("--headless");

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--disable-gpu"); // A veces necesario para sistemas Linux
        options.addArguments("--no-sandbox"); // Recomendado para entornos CI/CD
        options.addArguments("--disable-dev-shm-usage"); // Soluciona problemas de memoria en contenedores
        options.addArguments("--headless=new");

        driver = new ChromeDriver(options);
    }

    @Test
    public void testGoogleSearch() {
        // Navega a Google
        driver.get("https://www.google.com");

        // Espera hasta que el campo de búsqueda esté presente
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchBox = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        
        // Escribe "Selenium" y presiona Enter
        searchBox.sendKeys("Selenium");
        searchBox.submit();

        // Espera que los resultados aparezcan
        WebElement results = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("search")));
        assertTrue(results.getText().contains("Selenium"));
    }

    @Test
    public void testBioBioChileLink() {
        // Navega a BioBioChile
        driver.get("https://www.biobiochile.cl");

        // Espera hasta que el enlace esté presente
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement linkElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#header > nav.navbar-bottom > div.menu-container > div > a")));
        
        // Verifica que el atributo href sea igual a la URL esperada
        String expectedHref = "https://www.biobiochile.cl/";
        String actualHref = linkElement.getAttribute("href");
        assertTrue(expectedHref.equals(actualHref), "El href del enlace no coincide con la URL esperada.");
    }

    @AfterEach
    public void tearDown() {
        // Cierra el navegador
        if (driver != null) {
            driver.quit();
        }
    }
}
