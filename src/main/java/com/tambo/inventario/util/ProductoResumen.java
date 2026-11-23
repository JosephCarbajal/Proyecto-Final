
package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;

public class ProductoResumen {

    public String obtenerResumen(Producto producto, int stockActual) {
        return "Producto: " + producto.getNombre()
                + "\nCodigo: " + producto.getCodigo()
                + "\nMarca: " + producto.getMarca()
                + "\nPrecio: S/ " + String.format("%.2f",
                producto.getPrecioCentimos() / 100.0)
                + "\nStock actual: " + stockActual
                + "\nStock minimo: " + producto.getStockMinimo();
    }

    public boolean tieneStockBajo(Producto producto, int stockActual) {
        return stockActual <= producto.getStockMinimo();
    }

    public double calcularMargen(Producto producto) {
        return (producto.getPrecioCentimos()
                - producto.getCostoCentimos()) / 100.0;
    }
}
