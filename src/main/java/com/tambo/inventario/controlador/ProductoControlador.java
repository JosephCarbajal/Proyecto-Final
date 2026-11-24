
package com.tambo.inventario.controlador;

import com.tambo.compartido.modelo.Categoria;
import com.tambo.compartido.modelo.Producto;
import com.tambo.inventario.modelo.TipoMovimiento;
import com.tambo.inventario.modelo.Lote;
import com.tambo.inventario.repositorio.CategoriaSQLiteRepository;
import com.tambo.inventario.repositorio.ProductoSQLiteRepository;
import com.tambo.inventario.repositorio.StockProductoSQLiteRepository;
import com.tambo.inventario.servicio.MovimientoInventarioServicio;
import com.tambo.inventario.vista.ProductoVista;
import com.tambo.inventario.vista.MovimientoInventarioVista;
import com.tambo.inventario.vista.CategoriaVista;
import com.tambo.inventario.util.ProductoValidador;
import com.tambo.inventario.util.StockValidador;
import com.tambo.inventario.util.ProductoBuscador;
import com.tambo.inventario.util.ProductoFiltro;
import com.tambo.inventario.util.ProductoOrdenador;
import com.tambo.inventario.util.ProductoEstadistica;
import com.tambo.inventario.util.ProductoPrecioCalculadora;
import com.tambo.inventario.util.InventarioResumen;
import com.tambo.inventario.util.StockAlerta;
import com.tambo.inventario.util.InventarioIndicador;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoControlador {

    private final ProductoVista vista;
    private final ProductoSQLiteRepository productoRepository;
    private final StockProductoSQLiteRepository stockRepository;
    private final CategoriaSQLiteRepository categoriaRepository;
    private final MovimientoInventarioServicio movimientoServicio;
    private final ProductoValidador productoValidador;
    private final StockValidador stockValidador;
    private final ProductoBuscador productoBuscador;
    private final ProductoFiltro productoFiltro;
    private final ProductoOrdenador productoOrdenador;
    private final ProductoEstadistica productoEstadistica;
    private final ProductoPrecioCalculadora precioCalculadora;
    private final InventarioResumen inventarioResumen;
    private final StockAlerta stockAlerta;
    private final InventarioIndicador inventarioIndicador;

    public ProductoControlador(ProductoVista vista) {
        this.vista = vista;
        this.productoRepository = new ProductoSQLiteRepository();
        this.stockRepository = new StockProductoSQLiteRepository();
        this.categoriaRepository = new CategoriaSQLiteRepository();
        this.movimientoServicio = new MovimientoInventarioServicio();
        this.productoValidador = new ProductoValidador();
        this.stockValidador = new StockValidador();
        this.productoBuscador = new ProductoBuscador();
        this.productoFiltro = new ProductoFiltro();
        this.productoOrdenador = new ProductoOrdenador();
        this.productoEstadistica = new ProductoEstadistica();
        this.precioCalculadora = new ProductoPrecioCalculadora();
        this.inventarioResumen = new InventarioResumen();
        this.stockAlerta = new StockAlerta();
        this.inventarioIndicador = new InventarioIndicador();

        vista.eventoBuscar(e -> buscarProducto());
        vista.eventoListar(e -> listarProductos());
        vista.eventoRegistrar(e -> registrarProducto());
        vista.eventoEditar(e -> editarProducto());
        vista.eventoDesactivar(e -> desactivarProducto());
        vista.eventoMovimientos(e -> registrarMovimiento());
        vista.eventoHistorial(e -> abrirHistorial());
        vista.eventoCategorias(e -> abrirCategorias());
        vista.eventoFiltro(e -> actualizarCatalogo(true));
        vista.eventoOrden(e -> actualizarCatalogo(true));
        vista.eventoEstadisticas(e -> mostrarEstadisticas());
        vista.eventoCalcularPrecio(e -> calcularPrecioProducto());
        vista.eventoResumenAlertas(e -> mostrarResumenAlertas());
        vista.eventoLotes(e -> abrirLotes());

        listarProductos();
        actualizarNotificaciones();
    }

    private void listarProductos() {
        actualizarCatalogo(false);
    }

    private void actualizarCatalogo(boolean aplicarBusqueda) {
        try {
            List<Producto> productos = new ArrayList<>(
                    productoRepository.listarProductos()
            );

            Map<Integer, Integer> stocks = stockRepository.listarStocks();

            if (aplicarBusqueda) {
                String texto = vista.getCodigoBuscar();
                String tipo = vista.getTipoBusqueda();

                if (!texto.isEmpty()) {
                    switch (tipo) {
                        case "Nombre":
                            productos = productoBuscador.buscarPorNombre(
                                    productos, texto
                            );
                            break;

                        case "Marca":
                            productos = productoBuscador.buscarPorMarca(
                                    productos, texto
                            );
                            break;

                        case "Codigo":
                            Producto producto =
                                    productoRepository.buscarPorCodigo(texto);

                            productos.clear();

                            if (producto != null && producto.isActivo()) {
                                productos.add(producto);
                            }
                            break;
                    }
                }
            }

            switch (vista.getFiltroStock()) {
                case "Stock bajo":
                    productos = productoFiltro.filtrarStockBajo(
                            productos, stocks
                    );
                    break;

                case "Sin stock":
                    productos = productoFiltro.filtrarSinStock(
                            productos, stocks
                    );
                    break;

                case "Con stock":
                    productos = productoFiltro.filtrarConStock(
                            productos, stocks
                    );
                    break;
            }

            switch (vista.getOrden()) {
                case "Nombre":
                    productoOrdenador.ordenarPorNombre(productos);
                    break;

                case "Precio":
                    productoOrdenador.ordenarPorPrecio(productos);
                    break;

                case "Codigo":
                    productoOrdenador.ordenarPorCodigo(productos);
                    break;

                case "Stock minimo":
                    productoOrdenador.ordenarPorStockMinimo(productos);
                    break;
            }

            vista.mostrarProductos(productos, stocks);

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al actualizar el catalogo: " + e.getMessage()
            );
        }
    }

    private void buscarProducto() {
        if (vista.getCodigoBuscar().isEmpty()) {
            vista.mostrarMensaje("Ingrese un criterio de busqueda.");
            return;
        }

        actualizarCatalogo(true);
    }

    private void registrarProducto() {
        try {
            List<Categoria> categorias = categoriaRepository.listarCategorias();

            Producto producto = vista.solicitarProducto(categorias, null);

            if (producto == null) {
                return;
            }

            if (!productoValidador.validar(producto)) {
                vista.mostrarMensaje(
                        "Revise los datos: codigo y nombre son obligatorios y los valores no pueden ser negativos."
                );
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
            vista.mostrarMensaje(
                    "Error al registrar producto: " + e.getMessage()
            );
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
            Producto productoActualizado =
                    vista.solicitarProducto(categorias, producto);

            if (productoActualizado == null) {
                return;
            }

            if (!productoValidador.validar(productoActualizado)) {
                vista.mostrarMensaje(
                        "Revise los datos del producto antes de actualizarlo."
                );
                return;
            }

            productoRepository.actualizarProducto(productoActualizado);

            vista.mostrarMensaje("Producto actualizado correctamente.");
            listarProductos();

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al editar producto: " + e.getMessage()
            );
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
            vista.mostrarMensaje(
                    "Error al desactivar producto: " + e.getMessage()
            );
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

            if (!stockValidador.validarCantidad(cantidad)
                    || (tipo.equals("SALIDA")
                    && !stockValidador.hayStockSuficiente(
                    stockRepository.buscarPorProducto(producto.getId())
                            .getStockActual(), cantidad))) {

                vista.mostrarMensaje(
                        "Stock insuficiente o cantidad no valida."
                );
                return;
            }

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
            vista.mostrarMensaje(
                    "Error al registrar movimiento: " + e.getMessage()
            );
        }
    }

    private void mostrarEstadisticas() {
        try {
            List<Producto> productos = productoRepository.listarProductos();
            Map<Integer, Integer> stocks = stockRepository.listarStocks();

            int total = productoEstadistica.contarProductos(productos);

            int stockBajo =
                    productoEstadistica.contarProductosConStockBajo(
                            productos, stocks
                    );

            int sinStock = inventarioResumen.contarProductosAgotados(
                    productos, stocks
            );

            String mensaje = String.format(
                    "ESTADISTICAS DE PRODUCTOS\n\n" +
                            "Total de productos: %d\n" +
                            "Productos con stock bajo: %d\n" +
                            "Productos sin stock: %d",
                    total, stockBajo, sinStock
            );

            vista.mostrarMensaje(mensaje);

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al calcular estadisticas: " + e.getMessage()
            );
        }
    }

    private void calcularPrecioProducto() {
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

            Map<Integer, Integer> stocks = stockRepository.listarStocks();

            int stockActual = stocks.getOrDefault(producto.getId(), 0);

            double ganancia =
                    precioCalculadora.calcularGanancia(producto);

            double margen =
                    precioCalculadora.calcularMargenPorcentaje(producto);

            double valorStock =
                    precioCalculadora.calcularValorStock(
                            producto, stockActual
                    );

            String mensaje = String.format(
                    "CALCULO DE PRECIOS\n\n" +
                            "Producto: %s\n" +
                            "Precio de venta: S/ %.2f\n" +
                            "Costo: S/ %.2f\n" +
                            "Ganancia por unidad: S/ %.2f\n" +
                            "Margen de ganancia: %.2f%%\n" +
                            "Stock actual: %d\n" +
                            "Valor del inventario: S/ %.2f",
                    producto.getNombre(),
                    producto.getPrecioCentimos() / 100.0,
                    producto.getCostoCentimos() / 100.0,
                    ganancia,
                    margen,
                    stockActual,
                    valorStock
            );

            vista.mostrarMensaje(mensaje);

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al calcular precio: " + e.getMessage()
            );
        }
    }

    private void mostrarResumenAlertas() {
        try {
            List<Producto> productos = productoRepository.listarProductos();
            Map<Integer, Integer> stocks = stockRepository.listarStocks();

            int total = inventarioResumen.contarProductos(productos);

            int unidades = inventarioResumen.calcularUnidadesTotales(
                    productos, stocks
            );

            int agotados = inventarioResumen.contarProductosAgotados(
                    productos, stocks
            );

            int stockBajo = 0;
            int disponibles = 0;

            StringBuilder alertas = new StringBuilder();

            for (Producto producto : productos) {
                int stock = stocks.getOrDefault(producto.getId(), 0);

                String estado = inventarioIndicador.obtenerEstado(
                        stock, producto.getStockMinimo()
                );

                if (stock > 0 && stock <= producto.getStockMinimo()) {
                    stockBajo++;
                }

                if (stock > producto.getStockMinimo()) {
                    disponibles++;
                }

                if (stockAlerta.necesitaReposicion(producto, stock)) {
                    int cantidad = stockAlerta.calcularReposicion(
                            producto, stock
                    );

                    alertas.append("\n")
                            .append(producto.getNombre())
                            .append(" - ").append(estado)
                            .append(" - Reponer: ")
                            .append(cantidad).append(" unidades");
                }
            }

            double porcentajeBajo =
                    inventarioIndicador.calcularPorcentajeStockBajo(
                            total, stockBajo
                    );

            double porcentajeDisponibilidad =
                    inventarioIndicador.calcularPorcentajeDisponibilidad(
                            total, disponibles
                    );

            String mensaje = String.format(
                    "RESUMEN Y ALERTAS DE INVENTARIO\n\n" +
                            "Total de productos: %d\n" +
                            "Unidades totales: %d\n" +
                            "Productos agotados: %d\n" +
                            "Productos con stock bajo: %d\n" +
                            "Productos disponibles: %d\n\n" +
                            "Porcentaje de stock bajo: %.2f%%\n" +
                            "Porcentaje de disponibilidad: %.2f%%\n\n" +
                            "ALERTAS DE REPOSICION:\n%s",
                    total,
                    unidades,
                    agotados,
                    stockBajo,
                    disponibles,
                    porcentajeBajo,
                    porcentajeDisponibilidad,
                    alertas.length() == 0
                            ? "No hay productos que necesiten reposicion."
                            : alertas.toString()
            );

            JTextArea area = new JTextArea(mensaje, 20, 45);
            area.setEditable(false);
            area.setCaretPosition(0);

            JOptionPane.showMessageDialog(
                    vista,
                    new JScrollPane(area),
                    "Resumen y alertas de inventario",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al generar resumen: " + e.getMessage()
            );
        }
    }

    private void actualizarNotificaciones() {
        try {
            LoteControlador loteControlador = new LoteControlador();

            List<Lote> lotes = new ArrayList<>();
            lotes.addAll(loteControlador.listarLotesVencidos());
            lotes.addAll(loteControlador.listarProximosAVencer(30));

            List<Producto> productos = productoRepository.listarProductos();
            Map<Integer, String> nombresProductos = new HashMap<>();

            for (Producto producto : productos) {
                nombresProductos.put(
                        producto.getId(),
                        producto.getNombre()
                );
            }

            vista.mostrarNotificaciones(
                    lotes,
                    nombresProductos,
                    lote -> abrirLotesDesdeNotificacion(lote.getProductoId())
            );

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al cargar las notificaciones: " + e.getMessage()
            );
        }
    }

    private void abrirLotesDesdeNotificacion(int productoId) {
        new LoteControladorVista(productoId);
    }

    private void abrirHistorial() {
        MovimientoInventarioVista historial =
                new MovimientoInventarioVista();

        new MovimientoInventarioControlador(historial);

        historial.setVisible(true);
    }

    private void abrirCategorias() {
        CategoriaVista categorias = new CategoriaVista();

        new CategoriaControlador(categorias);

        categorias.setVisible(true);
    }

    private void abrirLotes() {
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

            if (!producto.isRequiereVencimiento()) {
                vista.mostrarMensaje(
                        "Este producto no requiere control de vencimiento."
                );
                return;
            }

            new LoteControladorVista(producto.getId());

        } catch (SQLException e) {
            vista.mostrarMensaje(
                    "Error al abrir la gestion de lotes: " + e.getMessage()
            );
        }
    }
}
