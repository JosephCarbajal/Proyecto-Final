package com.tambo.inventario.util;

public class InventarioCalculadora {

    public int calcularStockFinal(int stockActual, int entrada, int salida) {
        return stockActual + entrada - salida;
    }

    public int calcularDiferencia(int stockSistema, int stockFisico) {
        return stockFisico - stockSistema;
    }

    public int calcularCantidadFaltante(int stockActual, int stockMinimo) {
        if (stockActual >= stockMinimo) {
            return 0;
        }

        return stockMinimo - stockActual;
    }

    public boolean hayExistencias(int stock) {
        return stock > 0;
    }
}
