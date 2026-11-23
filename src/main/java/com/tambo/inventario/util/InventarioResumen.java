
package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.List;
import java.util.Map;

public class InventarioResumen {

    public int contarProductos(List<Producto> productos) {
        return productos.size();
    }

    public int calcularUnidadesTotales(
            List<Producto> productos, Map<Integer, Integer> stocks) {

        int total = 0;

        for (Producto producto : productos) {
            total += stocks.getOrDefault(producto.getId(), 0);
        }

        return total;
    }

    public int contarProductosAgotados(
            List<Producto> productos, Map<Integer, Integer> stocks) {

        int contador = 0;

        for (Producto producto : productos) {
            int stock = stocks.getOrDefault(producto.getId(), 0);

            if (stock == 0) {
                contador++;
            }
        }

        return contador;
    }
}
