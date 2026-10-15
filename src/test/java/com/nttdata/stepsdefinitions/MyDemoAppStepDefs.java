package com.nttdata.stepsdefinitions;

import com.nttdata.steps.AppConfigSteps;
import com.nttdata.steps.MyDemoAppSteps;
import com.nttdata.support.ScreenshotAttacher;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Step definitions del carrito de compras de Sauce Labs My Demo App.
 * <p>
 * Los steps de abrir y cerrar la app ("ingreso al aplicativo" y
 * "cierro el aplicativo") son genericos y viven en {@link AppConfigStepsDefs}.
 */
public class MyDemoAppStepDefs {

    private static final String PAQUETE_APP = "com.saucelabs.mydemoapp.android";

    private final MyDemoAppSteps myDemoAppSteps = new MyDemoAppSteps();
    private final AppConfigSteps appConfigSteps = new AppConfigSteps();

    private Scenario scenario;

    // Datos del producto agregado, que se usan en el step de validacion
    private String productoAgregado;
    private int unidadesAgregadas;
    private String precioUnitario;

    @Before
    public void configurarScenario(Scenario scenario) {
        this.scenario = scenario;
    }

    /**
     * Si un escenario falla, el step "cierro el aplicativo" no se ejecuta y la
     * sesion quedaria abierta para el siguiente escenario. Este hook la cierra
     * siempre. Se ejecuta DESPUES de Hooks (que toma el screenshot final),
     * porque los @After corren de mayor a menor "order".
     */
    @After(order = 1)
    public void cerrarAplicacionSiSigueAbierta() {
        appConfigSteps.cerrarAplicacion(PAQUETE_APP);
    }

    @And("valido que carguen correctamente los productos en la galería")
    public void valido_que_carguen_correctamente_los_productos_en_la_galeria() {
        List<String> nombres = myDemoAppSteps.obtenerNombresDeLaGaleria();
        List<String> precios = myDemoAppSteps.obtenerPreciosDeLaGaleria();

        assertFalse("La galeria no muestra ningun producto", nombres.isEmpty());
        assertEquals("Cada producto de la galeria debe tener su precio", nombres.size(), precios.size());

        for (String nombre : nombres) {
            assertFalse("Hay un producto sin nombre en la galeria", nombre.isBlank());
        }
        for (String precio : precios) {
            assertTrue("Precio con formato invalido en la galeria: " + precio, precio.startsWith("$"));
        }

        ScreenshotAttacher.attach(scenario, "Galeria de productos cargada");
    }

    @When("agrego {int} del siguiente producto {string}")
    public void agrego_unidades_del_siguiente_producto(int unidades, String producto) {
        productoAgregado = producto;
        unidadesAgregadas = unidades;

        myDemoAppSteps.seleccionarProducto(producto);

        assertEquals("El detalle abierto no corresponde al producto elegido",
                producto, myDemoAppSteps.obtenerNombreDetalle());
        assertEquals("La cantidad inicial del producto debe ser 1",
                "1", myDemoAppSteps.obtenerCantidadDetalle());

        precioUnitario = myDemoAppSteps.obtenerPrecioDetalle();

        // La cantidad empieza en 1, por eso se aumenta (unidades - 1) veces
        myDemoAppSteps.aumentarCantidad(unidades - 1);
        assertEquals("La cantidad mostrada no coincide con las unidades pedidas",
                String.valueOf(unidades), myDemoAppSteps.obtenerCantidadDetalle());
        ScreenshotAttacher.attach(scenario, "Detalle con la cantidad elegida");

        myDemoAppSteps.agregarAlCarrito();
    }

    @Then("valido el carrito de compra actualice correctamente")
    public void valido_el_carrito_de_compra_actualice_correctamente() {
        // 1. El numero sobre el icono del carrito
        assertEquals("El contador del carrito no se actualizo con las unidades agregadas",
                unidadesAgregadas, myDemoAppSteps.obtenerContadorCarrito());

        // 2. El contenido de la pantalla del carrito
        myDemoAppSteps.abrirCarrito();

        assertEquals("El producto del carrito no es el que se agrego",
                productoAgregado, myDemoAppSteps.obtenerNombreEnCarrito());
        assertEquals("La cantidad del carrito no coincide con las unidades agregadas",
                String.valueOf(unidadesAgregadas), myDemoAppSteps.obtenerCantidadEnCarrito());
        assertTrue("El total de items del carrito es incorrecto: " + myDemoAppSteps.obtenerTotalItemsCarrito(),
                myDemoAppSteps.obtenerTotalItemsCarrito().startsWith(unidadesAgregadas + " Item"));

        // 3. El precio total = precio unitario x unidades
        assertEquals("El precio total del carrito es incorrecto",
                calcularTotal(precioUnitario, unidadesAgregadas), myDemoAppSteps.obtenerPrecioTotalCarrito());

        ScreenshotAttacher.attach(scenario, "Carrito de compras actualizado");
    }

    /** Convierte "$ 29.99" y 2 en "$ 59.98". */
    private String calcularTotal(String precio, int unidades) {
        BigDecimal valor = new BigDecimal(precio.replace("$", "").trim());
        BigDecimal total = valor.multiply(BigDecimal.valueOf(unidades));
        return "$ " + total.setScale(2).toPlainString();
    }
}
