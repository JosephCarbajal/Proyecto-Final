
package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.List;

public class ProductoEstadistica {

    public int contarProductos(List<Producto> productos) {
        return productos.size();
    }

    public double calcularPrecioPromedio(List<Producto> productos) {
        if (productos.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (Producto producto : productos) {
            total += producto.getPrecioCentimos();
        }

        return (total / (double) productos.size()) / 100.0;
    }

    public int contarProductosConStockBajo(
            List<Producto> productos, java.util.Map<Integer, Integer> stocks) {

        int contador = 0;

        for (Producto producto : productos) {
            int stock = stocks.getOrDefault(producto.getId(), 0);

            if (stock <= producto.getStockMinimo()) {
                contador++;
            }
        }

        return contador;
    }
}
