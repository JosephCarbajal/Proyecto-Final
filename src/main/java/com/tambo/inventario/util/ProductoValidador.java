package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;

public class ProductoValidador {

    public boolean validar(Producto producto) {
        if (producto == null) {
            return false;
        }

        return producto.getCodigo() != null
                && !producto.getCodigo().trim().isEmpty()
                && producto.getNombre() != null
                && !producto.getNombre().trim().isEmpty()
                && producto.getPrecioCentimos() >= 0
                && producto.getCostoCentimos() >= 0
                && producto.getStockMinimo() >= 0;
    }

    public boolean validarCodigo(String codigo) {
        return codigo != null && !codigo.trim().isEmpty();
    }

    public boolean validarPrecio(int precio) {
        return precio >= 0;
    }

    public boolean validarStock(int stock) {
        return stock >= 0;
    }
}
