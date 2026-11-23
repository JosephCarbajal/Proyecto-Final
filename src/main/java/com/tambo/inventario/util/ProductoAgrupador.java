package com.tambo.inventario.util;

import com.tambo.compartido.modelo.Producto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoAgrupador {

    public Map<Integer, List<Producto>> agruparPorCategoria(
            List<Producto> productos) {

        Map<Integer, List<Producto>> grupos = new HashMap<>();

        for (Producto producto : productos) {
            int categoriaId = producto.getCategoriaId();

            grupos.putIfAbsent(categoriaId, new ArrayList<>());
            grupos.get(categoriaId).add(producto);
        }

        return grupos;
    }
}

