package com.nttdata.screens;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static com.nttdata.screens.AppConfigScreen.getDriver;

/**
 * MyDemoAppScreen
 * ---------------
 * Pantallas de la app Sauce Labs My Demo App: galeria de productos,
 * detalle del producto y carrito.
 * <p>
 * Solo interactua con los elementos. No levanta ni cierra la app
 * (eso lo hace {@link AppConfigScreen}) y no hace aserciones: devuelve
 * los valores leidos para que los step definitions los validen.
 */
public class MyDemoAppScreen {

    private static final Duration TIMEOUT = Duration.ofSeconds(20);
    private static final String ID = "com.saucelabs.mydemoapp.android:id/";

    // Galeria
    private final By tituloGaleria = AppiumBy.id(ID + "productTV");
    private final By nombresProductos = AppiumBy.id(ID + "titleTV");
    private final By preciosProductos = AppiumBy.id(ID + "priceTV");

    // Detalle del producto
    private final By nombreDetalle = AppiumBy.id(ID + "productTV");
    private final By precioDetalle = AppiumBy.id(ID + "priceTV");
    private final By cantidadDetalle = AppiumBy.id(ID + "noTV");
    private final By btnAumentarCantidad = AppiumBy.id(ID + "plusIV");
    private final By btnAgregarAlCarrito = AppiumBy.id(ID + "cartBt");

    // Icono del carrito (aparece en todas las pantallas)
    private final By contadorCarrito = AppiumBy.id(ID + "cartTV");
    private final By iconoCarrito = AppiumBy.id(ID + "cartRL");

    // Pantalla del carrito
    private final By tituloCarrito = AppiumBy.id(ID + "productTV");
    private final By nombreEnCarrito = AppiumBy.id(ID + "titleTV");
    private final By cantidadEnCarrito = AppiumBy.id(ID + "noTV");
    private final By totalItemsCarrito = AppiumBy.id(ID + "itemsTV");
    private final By precioTotalCarrito = AppiumBy.id(ID + "totalPriceTV");

    // ---------------------------------------------------------
    // Galeria
    // ---------------------------------------------------------

    /** Espera el titulo "Products" y devuelve los nombres de los productos mostrados. */
    public List<String> obtenerNombresDeLaGaleria() {
        System.out.println("[MyDemoAppScreen] Esperando la galeria de productos...");
        WebDriverWait wait = esperar();

        wait.until(ExpectedConditions.textToBe(tituloGaleria, "Products"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nombresProductos));

        return getDriver().findElements(nombresProductos).stream()
                .map(WebElement::getText)
                .toList();
    }

    public List<String> obtenerPreciosDeLaGaleria() {
        return getDriver().findElements(preciosProductos).stream()
                .map(WebElement::getText)
                .toList();
    }

    public void seleccionarProducto(String nombreProducto) {
        System.out.println("[MyDemoAppScreen] Seleccionando producto: " + nombreProducto);
        // En la galeria, la imagen del producto tiene como content-desc el nombre
        By producto = AppiumBy.accessibilityId(nombreProducto);
        esperar().until(ExpectedConditions.elementToBeClickable(producto)).click();
    }

    // ---------------------------------------------------------
    // Detalle del producto
    // ---------------------------------------------------------

    public String obtenerNombreDetalle() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(nombreDetalle)).getText();
    }

    public String obtenerPrecioDetalle() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(precioDetalle)).getText();
    }

    public String obtenerCantidadDetalle() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(cantidadDetalle)).getText();
    }

    public void aumentarCantidad(int veces) {
        System.out.println("[MyDemoAppScreen] Aumentando cantidad " + veces + " vez/veces");
        for (int i = 0; i < veces; i++) {
            esperar().until(ExpectedConditions.elementToBeClickable(btnAumentarCantidad)).click();
        }
    }

    public void agregarAlCarrito() {
        System.out.println("[MyDemoAppScreen] Haciendo clic en Add to cart");
        esperar().until(ExpectedConditions.elementToBeClickable(btnAgregarAlCarrito)).click();
    }

    // ---------------------------------------------------------
    // Carrito
    // ---------------------------------------------------------

    /**
     * Lee el numero que aparece sobre el icono del carrito.
     * Si el contador no aparece (carrito vacio o la app no lo actualizo)
     * devuelve 0 en vez de lanzar error.
     */
    public int obtenerContadorCarrito() {
        try {
            String texto = esperar().until(ExpectedConditions.visibilityOfElementLocated(contadorCarrito)).getText();
            return Integer.parseInt(texto.trim());
        } catch (TimeoutException e) {
            System.out.println("[MyDemoAppScreen] El contador del carrito no aparece");
            return 0;
        }
    }

    public void abrirCarrito() {
        System.out.println("[MyDemoAppScreen] Abriendo el carrito");
        esperar().until(ExpectedConditions.elementToBeClickable(iconoCarrito)).click();
        esperar().until(ExpectedConditions.textToBe(tituloCarrito, "My Cart"));
    }

    public String obtenerNombreEnCarrito() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(nombreEnCarrito)).getText();
    }

    public String obtenerCantidadEnCarrito() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(cantidadEnCarrito)).getText();
    }

    /** Texto como "2 Items". */
    public String obtenerTotalItemsCarrito() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(totalItemsCarrito)).getText();
    }

    /** Texto como "$ 59.98". */
    public String obtenerPrecioTotalCarrito() {
        return esperar().until(ExpectedConditions.visibilityOfElementLocated(precioTotalCarrito)).getText();
    }

    private WebDriverWait esperar() {
        AndroidDriver driver = getDriver();
        return new WebDriverWait(driver, TIMEOUT);
    }
}
