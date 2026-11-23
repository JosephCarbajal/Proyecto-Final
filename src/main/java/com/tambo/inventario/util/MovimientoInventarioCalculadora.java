
package com.tambo.inventario.util;

public class MovimientoInventarioCalculadora {

    public int calcularEntrada(int stockActual, int cantidad) {
        return stockActual + cantidad;
    }

    public int calcularSalida(int stockActual, int cantidad) {
        return stockActual - cantidad;
    }

    public boolean hayStockSuficiente(int stockActual, int cantidad) {
        return stockActual >= cantidad;
    }

    public int calcularStockRestante(int stockActual, int cantidad) {
        if (cantidad > stockActual || cantidad <= 0) {
            return stockActual;
        }

        return stockActual - cantidad;
    }
}
