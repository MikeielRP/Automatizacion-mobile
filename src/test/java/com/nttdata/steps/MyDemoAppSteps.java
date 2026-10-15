package com.nttdata.steps;

import com.nttdata.screens.MyDemoAppScreen;

import java.util.List;

/**
 * Casos de uso del carrito de compras en Sauce Labs My Demo App.
 * Delega toda la interaccion de UI en {@link MyDemoAppScreen}.
 * No sabe como se levanta o se cierra la app: eso lo maneja {@link AppConfigSteps}.
 */
public class MyDemoAppSteps {

    private final MyDemoAppScreen screen = new MyDemoAppScreen();

    public List<String> obtenerNombresDeLaGaleria() {
        return screen.obtenerNombresDeLaGaleria();
    }

    public List<String> obtenerPreciosDeLaGaleria() {
        return screen.obtenerPreciosDeLaGaleria();
    }

    public void seleccionarProducto(String nombreProducto) {
        screen.seleccionarProducto(nombreProducto);
    }

    public String obtenerNombreDetalle() {
        return screen.obtenerNombreDetalle();
    }

    public String obtenerPrecioDetalle() {
        return screen.obtenerPrecioDetalle();
    }

    public String obtenerCantidadDetalle() {
        return screen.obtenerCantidadDetalle();
    }

    public void aumentarCantidad(int veces) {
        screen.aumentarCantidad(veces);
    }

    public void agregarAlCarrito() {
        screen.agregarAlCarrito();
    }

    public int obtenerContadorCarrito() {
        return screen.obtenerContadorCarrito();
    }

    public void abrirCarrito() {
        screen.abrirCarrito();
    }

    public String obtenerNombreEnCarrito() {
        return screen.obtenerNombreEnCarrito();
    }

    public String obtenerCantidadEnCarrito() {
        return screen.obtenerCantidadEnCarrito();
    }

    public String obtenerTotalItemsCarrito() {
        return screen.obtenerTotalItemsCarrito();
    }

    public String obtenerPrecioTotalCarrito() {
        return screen.obtenerPrecioTotalCarrito();
    }
}
