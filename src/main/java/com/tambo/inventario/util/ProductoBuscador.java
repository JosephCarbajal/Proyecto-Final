package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoBuscador {

    public List<Producto> buscarPorNombre(List<Producto> productos, String nombre) {
        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.getNombre().toLowerCase()
                    .contains(nombre.toLowerCase())) {
                resultado.add(producto);
            }
        }

        return resultado;
    }

    public List<Producto> buscarPorMarca(List<Producto> productos, String marca) {
        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.getMarca().toLowerCase()
                    .contains(marca.toLowerCase())) {
                resultado.add(producto);
            }
        }

        return resultado;
    }

    public List<Producto> buscarPorCategoria(List<Producto> productos, int categoriaId) {
        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            if (producto.getCategoriaId() == categoriaId) {
                resultado.add(producto);
            }
        }

        return resultado;
    }
}
