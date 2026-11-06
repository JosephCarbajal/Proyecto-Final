package com.tambo.inventario.modelo;

public class StockProducto {
    // Datos del inventario
    private int productoId;
    private int stockActual;

    public StockProducto(int productoId, int stockActual) {
        this.productoId = productoId;
        this.stockActual = stockActual;
    }

    // Obtener datos del inventario
    public int getProductoId() { return productoId; }
    public int getStockActual() { return stockActual; }

    // Actualizar stock
    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }
}
