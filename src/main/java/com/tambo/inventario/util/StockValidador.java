package com.tambo.inventario.util;

public class StockValidador {

    public boolean validarCantidad(int cantidad) {
        return cantidad >= 0;
    }

    public boolean hayStockSuficiente(int stockActual, int cantidadSolicitada) {
        return stockActual >= cantidadSolicitada
                && cantidadSolicitada > 0;
    }

    public boolean stockEstaBajo(int stockActual, int stockMinimo) {
        return stockActual <= stockMinimo;
    }

    public boolean validarStockMinimo(int stockMinimo) {
        return stockMinimo >= 0;
    }
}

