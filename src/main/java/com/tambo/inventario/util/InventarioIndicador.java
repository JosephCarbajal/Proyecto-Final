
package com.tambo.inventario.util;

public class InventarioIndicador {

    public double calcularPorcentajeStockBajo(
            int totalProductos, int productosStockBajo) {

        if (totalProductos == 0) {
            return 0;
        }

        return (productosStockBajo * 100.0) / totalProductos;
    }

    public double calcularPorcentajeDisponibilidad(
            int totalProductos, int productosDisponibles) {

        if (totalProductos == 0) {
            return 0;
        }

        return (productosDisponibles * 100.0) / totalProductos;
    }

    public String obtenerEstado(int stockActual, int stockMinimo) {
        if (stockActual == 0) {
            return "Agotado";
        }

        if (stockActual <= stockMinimo) {
            return "Stock bajo";
        }

        return "Disponible";
    }
}
