package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProductoFiltro {

    public List<Producto> filtrarStockBajo(
            List<Producto> productos, Map<Integer, Integer> stocks) {

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            int stock = stocks.getOrDefault(producto.getId(), 0);

            if (stock <= producto.getStockMinimo()) {
                resultado.add(producto);
            }
        }

        return resultado;
    }

    public List<Producto> filtrarSinStock(
            List<Producto> productos, Map<Integer, Integer> stocks) {

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            int stock = stocks.getOrDefault(producto.getId(), 0);

            if (stock == 0) {
                resultado.add(producto);
            }
        }

        return resultado;
    }

    public List<Producto> filtrarConStock(
            List<Producto> productos, Map<Integer, Integer> stocks) {

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            int stock = stocks.getOrDefault(producto.getId(), 0);

            if (stock > 0) {
                resultado.add(producto);
            }
        }

        return resultado;
    }
}
