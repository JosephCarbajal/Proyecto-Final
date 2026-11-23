package com.tambo.inventario.util;

public class StockComparador {

    public boolean compararStock(int stockSistema, int stockFisico) {
        return stockSistema == stockFisico;
    }

    public int obtenerDiferencia(int stockSistema, int stockFisico) {
        return stockFisico - stockSistema;
    }

    public boolean hayFaltante(int stockSistema, int stockFisico) {
        return stockFisico < stockSistema;
    }

    public boolean hayExcedente(int stockSistema, int stockFisico) {
        return stockFisico > stockSistema;
    }
}
