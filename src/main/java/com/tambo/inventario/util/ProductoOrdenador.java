package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.Comparator;
import java.util.List;

public class ProductoOrdenador {

    public void ordenarPorNombre(List<Producto> productos) {
        productos.sort(Comparator.comparing(Producto::getNombre));
    }

    public void ordenarPorPrecio(List<Producto> productos) {
        productos.sort(Comparator.comparingInt(Producto::getPrecioCentimos));
    }

    public void ordenarPorStockMinimo(List<Producto> productos) {
        productos.sort(Comparator.comparingInt(Producto::getStockMinimo));
    }

    public void ordenarPorCodigo(List<Producto> productos) {
        productos.sort(Comparator.comparing(Producto::getCodigo));
    }
}
