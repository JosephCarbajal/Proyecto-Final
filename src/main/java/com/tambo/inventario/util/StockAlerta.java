
package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;

public class StockAlerta {

    public boolean necesitaReposicion(Producto producto, int stockActual) {
        return stockActual <= producto.getStockMinimo();
    }

    public String obtenerMensaje(Producto producto, int stockActual) {
        if (stockActual == 0) {
            return "El producto " + producto.getNombre()
                    + " esta agotado.";
        }

        if (necesitaReposicion(producto, stockActual)) {
            return "El producto " + producto.getNombre()
                    + " necesita reposicion.";
        }

        return "El producto tiene stock suficiente.";
    }

    public int calcularReposicion(Producto producto, int stockActual) {
        if (stockActual > producto.getStockMinimo()) {
            return 0;
        }

        return producto.getStockMinimo() + 1 - stockActual;
    }
}
