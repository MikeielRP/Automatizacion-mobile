# language: es
Característica: Carrito de compras en Sauce Labs My Demo App

  Como cliente de la tienda en línea
  Quiero agregar productos al carrito
  Para comprarlos después

  @Carrito
  Esquema del escenario: Agregar producto al carrito y validar que se actualice
    Dado ingreso al aplicativo "mydemoapp"
    Y valido que carguen correctamente los productos en la galería
    Cuando agrego <UNIDADES> del siguiente producto "<PRODUCTO>"
    Entonces valido el carrito de compra actualice correctamente
    Y cierro el aplicativo "com.saucelabs.mydemoapp.android"

    Ejemplos:
      | PRODUCTO                  | UNIDADES |
      | Sauce Labs Backpack       | 1        |
      | Sauce Labs Bolt T-Shirt   | 1        |
      | Sauce Labs Bike Light     | 2        |
