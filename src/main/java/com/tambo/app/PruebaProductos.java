package com.tambo.app;

import com.tambo.compartido.modelo.Producto;
import com.tambo.inventario.repositorio.ProductoSQLiteRepository;
import java.sql.SQLException;
import java.util.List;

public class PruebaProductos {
    public static void main(String[] args) {
        ProductoSQLiteRepository repositorio = new ProductoSQLiteRepository();

        try {
            // Obtener todos los productos
            List<Producto> productos = repositorio.listarProductos();
            System.out.println("Total de productos: " + productos.size());

            // Mostrar los primeros 5 productos
            System.out.println("\nPrimeros 5 productos:");
            for (int i = 0; i < Math.min(5, productos.size()); i++) {
                System.out.println(productos.get(i));
            }

            // Buscar un producto por su codigo
            System.out.println("\nBuscando producto GAS0001:");
            Producto producto = repositorio.buscarPorCodigo("GAS0001");

            if (producto != null) {
                System.out.println(producto);
            } else {
                System.out.println("Producto no encontrado");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
