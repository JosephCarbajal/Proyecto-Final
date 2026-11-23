package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;

public class ProductoClasificador {

    public String clasificar(Producto producto, int stockActual) {

        if (stockActual == 0) {
            return "Agotado";
        }

        if (stockActual <= producto.getStockMinimo()) {
            return "Stock bajo";
        }

        if (stockActual <= producto.getStockMinimo() * 2) {
            return "Stock moderado";
        }

        return "Stock suficiente";
    }
}
