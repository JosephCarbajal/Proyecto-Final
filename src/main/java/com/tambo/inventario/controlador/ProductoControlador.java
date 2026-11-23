
package com.tambo.inventario.controlador;

import com.tambo.compartido.modelo.Categoria;
import com.tambo.compartido.modelo.Producto;
import com.tambo.inventario.modelo.TipoMovimiento;
import com.tambo.inventario.repositorio.CategoriaSQLiteRepository;
import com.tambo.inventario.repositorio.ProductoSQLiteRepository;
import com.tambo.inventario.repositorio.StockProductoSQLiteRepository;
import com.tambo.inventario.servicio.MovimientoInventarioServicio;
import com.tambo.inventario.vista.ProductoVista;
import com.tambo.inventario.vista.MovimientoInventarioVista;

import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductoControlador {

    private final ProductoVista vista;
    private final ProductoSQLiteRepository productoRepository;
    private final StockProductoSQLiteRepository stockRepository;
    private final CategoriaSQLiteRepository categoriaRepository;
    private final MovimientoInventarioServicio movimientoServicio;

    public ProductoControlador(ProductoVista vista) {
        this.vista = vista;
        this.productoRepository = new ProductoSQLiteRepository();
        this.stockRepository = new StockProductoSQLiteRepository();
        this.categoriaRepository = new CategoriaSQLiteRepository();
        this.movimientoServicio = new MovimientoInventarioServicio();

        vista.eventoBuscar(e -> buscarProducto());
        vista.eventoListar(e -> listarProductos());
        vista.eventoRegistrar(e -> registrarProducto());
        vista.eventoEditar(e -> editarProducto());
        vista.eventoDesactivar(e -> desactivarProducto());
        vista.eventoMovimientos(e -> registrarMovimiento());
        vista.eventoHistorial(e -> abrirHistorial());

        listarProductos();
    }

    private void listarProductos() {
        try {
            List<Producto> productos = productoRepository.listarProductos();
            Map<Integer, Integer> stocks = stockRepository.listarStocks();

            vista.mostrarProductos(productos, stocks);

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al listar productos: " + e.getMessage());
        }
    }

    private void buscarProducto() {
        String codigo = vista.getCodigoBuscar();

        if (codigo.isEmpty()) {
            vista.mostrarMensaje("Ingrese un codigo de producto.");
            return;
        }

        try {
            Producto producto = productoRepository.buscarPorCodigo(codigo);

            if (producto == null || !producto.isActivo()) {
                vista.mostrarMensaje("Producto no encontrado.");
                return;
            }

            Map<Integer, Integer> stocks = stockRepository.listarStocks();
            vista.mostrarProductos(List.of(producto), stocks);

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al buscar producto: " + e.getMessage());
        }
    }

    private void registrarProducto() {
        try {
            List<Categoria> categorias = categoriaRepository.listarCategorias();

            Producto producto = vista.solicitarProducto(categorias, null);

            if (producto == null) {
                return;
            }

            if (productoRepository.existeCodigo(producto.getCodigo())) {
                vista.mostrarMensaje("El codigo ya esta registrado.");
                return;
            }

            productoRepository.registrarProducto(producto);

            vista.mostrarMensaje("Producto registrado correctamente.");
            listarProductos();

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al registrar producto: " + e.getMessage());
        }
    }

    private void editarProducto() {
        String codigo = vista.getCodigoSeleccionado();

        if (codigo == null) {
            vista.mostrarMensaje("Seleccione un producto de la tabla.");
            return;
        }

        try {
            Producto producto = productoRepository.buscarPorCodigo(codigo);

            if (producto == null || !producto.isActivo()) {
                vista.mostrarMensaje("Producto no encontrado.");
                return;
            }

            List<Categoria> categorias = categoriaRepository.listarCategorias();
            Producto productoActualizado = vista.solicitarProducto(categorias, producto);

            if (productoActualizado == null) {
                return;
            }

            productoRepository.actualizarProducto(productoActualizado);

            vista.mostrarMensaje("Producto actualizado correctamente.");
            listarProductos();

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al editar producto: " + e.getMessage());
        }
    }

    private void desactivarProducto() {
        String codigo = vista.getCodigoSeleccionado();

        if (codigo == null) {
            vista.mostrarMensaje("Seleccione un producto de la tabla.");
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(
                vista,
                "¿Desea desactivar este producto?",
                "Confirmar desactivacion",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Producto producto = productoRepository.buscarPorCodigo(codigo);

            if (producto == null || !producto.isActivo()) {
                vista.mostrarMensaje("Producto no encontrado.");
                return;
            }

            productoRepository.desactivarProducto(producto.getId());

            vista.mostrarMensaje("Producto desactivado correctamente.");
            listarProductos();

        } catch (SQLException e) {
            vista.mostrarMensaje("Error al desactivar producto: " + e.getMessage());
        }
    }

    private void registrarMovimiento() {
        String codigo = vista.getCodigoSeleccionado();

        if (codigo == null) {
            vista.mostrarMensaje("Seleccione un producto de la tabla.");
            return;
        }

        try {
            Producto producto = productoRepository.buscarPorCodigo(codigo);

            if (producto == null || !producto.isActivo()) {
                vista.mostrarMensaje("Producto no encontrado.");
                return;
            }

            Object[] datos = vista.solicitarMovimiento();

            if (datos == null) {
                return;
            }

            String tipo = datos[0].toString();
            int cantidad = (int) datos[1];
            String referencia = datos[2].toString();

            movimientoServicio.registrarMovimientoSQLite(
                    producto.getId(),
                    TipoMovimiento.valueOf(tipo),
                    cantidad,
                    referencia,
                    "Administrador"
            );

            vista.mostrarMensaje("Movimiento registrado correctamente.");
            listarProductos();

        } catch (SQLException | IllegalArgumentException e) {
            vista.mostrarMensaje("Error al registrar movimiento: " + e.getMessage());
        }
    }

    private void abrirHistorial() {
        MovimientoInventarioVista historial = new MovimientoInventarioVista();

        new MovimientoInventarioControlador(historial);

        historial.setVisible(true);
    }
}
