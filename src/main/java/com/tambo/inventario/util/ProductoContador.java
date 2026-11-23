
package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.List;

public class ProductoContador {

    public int contarPorCategoria(List<Producto> productos, int categoriaId) {
        int contador = 0;

        for (Producto producto : productos) {
            if (producto.getCategoriaId() == categoriaId) {
                contador++;
            }
        }

        return contador;
    }

    public int contarPorMarca(List<Producto> productos, String marca) {
        int contador = 0;

        for (Producto producto : productos) {
            if (producto.getMarca().equalsIgnoreCase(marca)) {
                contador++;
            }
        }

        return contador;
    }

    public int contarProductosActivos(List<Producto> productos) {
        int contador = 0;

        for (Producto producto : productos) {
            if (producto.isActivo()) {
                contador++;
            }
        }

        return contador;
    }
}
