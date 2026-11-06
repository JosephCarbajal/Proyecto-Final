package com.tambo.inventario.controlador;

import com.tambo.compartido.modelo.Producto;
import com.tambo.inventario.repositorio.ProductoSQLiteRepository;
import com.tambo.inventario.vista.ProductoVista;
import java.sql.SQLException;
import java.util.List;

public class ProductoControlador {
    private ProductoVista vista;
    private ProductoSQLiteRepository repositorio;

    public ProductoControlador(ProductoVista vista) {
        this.vista = vista;
        repositorio = new ProductoSQLiteRepository();

        // Acciones de los botones
        vista.eventoBuscar(e -> buscarProducto());
        vista.eventoListar(e -> listarProductos());

        listarProductos();
    }

    // Mostrar todos los productos
    public void listarProductos() {
        try {
            List<Producto> productos = repositorio.listarProductos();
            vista.mostrarProductos(productos);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al listar productos: " + e.getMessage());
        }
    }

    // Buscar un producto por codigo
    public void buscarProducto() {
        String codigo = vista.getCodigoBuscar();

        if (codigo.isEmpty()) {
            vista.mostrarMensaje("Ingrese un codigo de producto");
            return;
        }

        try {
            Producto producto = repositorio.buscarPorCodigo(codigo);

            if (producto != null) {
                vista.mostrarProductos(List.of(producto));
            } else {
                vista.mostrarMensaje("Producto no encontrado");
            }
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar: " + e.getMessage());
        }
    }
}
