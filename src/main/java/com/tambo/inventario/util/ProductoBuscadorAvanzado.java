
package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoBuscadorAvanzado {

    public List<Producto> buscar(
            List<Producto> productos,
            String texto,
            int categoriaId) {

        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : productos) {
            boolean coincideTexto =
                    producto.getNombre().toLowerCase().contains(texto.toLowerCase())
                            || producto.getMarca().toLowerCase().contains(texto.toLowerCase());

            boolean coincideCategoria =
                    categoriaId == 0 || producto.getCategoriaId() == categoriaId;

            if (coincideTexto && coincideCategoria) {
                resultado.add(producto);
            }
        }

        return resultado;
    }
}
