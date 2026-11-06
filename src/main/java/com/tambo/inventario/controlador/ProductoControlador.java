package com.tambo.inventario.controlador;

import com.tambo.compartido.modelo.Producto;
import com.tambo.inventario.repositorio.ProductoSQLiteRepository;
import com.tambo.inventario.repositorio.StockProductoSQLiteRepository;
import com.tambo.inventario.vista.ProductoVista;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductoControlador {

    private final ProductoVista vista;
    private final ProductoSQLiteRepository productoRepositorio;
    private final StockProductoSQLiteRepository stockRepositorio;

    public ProductoControlador(ProductoVista vista) {
        this.vista = vista;
        this.productoRepositorio = new ProductoSQLiteRepository();
        this.stockRepositorio = new StockProductoSQLiteRepository();

        // Eventos de los botones
        vista.eventoBuscar(e -> buscarProducto());
        vista.eventoListar(e -> listarProductos());

        // Mostrar productos al iniciar
        listarProductos();
    }

    // Mostrar todos los productos con su stock
    private void listarProductos() {
        try {
            List<Producto> productos = productoRepositorio.listarProductos();
            Map<Integer, Integer> stocks = stockRepositorio.listarStocks();

            vista.mostrarProductos(productos, stocks);

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al listar productos: " + e.getMessage());
        }
    }

    // Buscar un producto por su código
    private void buscarProducto() {
        String codigo = vista.getCodigoBuscar();

        if (codigo.isEmpty()) {
            vista.mostrarMensaje("Ingrese un código de producto.");
            return;
        }

        try {
            Producto producto = productoRepositorio.buscarPorCodigo(codigo);

            if (producto == null) {
                vista.mostrarMensaje("Producto no encontrado.");
                return;
            }

            Map<Integer, Integer> stocks = stockRepositorio.listarStocks();

            vista.mostrarProductos(List.of(producto), stocks);

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar producto: " + e.getMessage());
        }
    }
}
