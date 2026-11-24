
package com.tambo.inventario.vista;

import com.tambo.compartido.modelo.Producto;
import com.tambo.compartido.modelo.Categoria;
import com.tambo.inventario.modelo.Lote;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ProductoVista extends JPanel {
    // Componentes de busqueda, filtros y botones.
    private final JTextField txtBuscar;
    private final JComboBox<String> cbTipoBusqueda, cbFiltroStock, cbOrden;
    private final JButton btnBuscar, btnListar, btnNotificaciones;
    private final JButton btnRegistrar, btnEditar, btnDesactivar, btnMovimientos;
    private final JButton btnHistorial, btnCategorias, btnEstadisticas;
    private final JButton btnCalcularPrecio, btnResumenAlertas, btnLotes;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public ProductoVista() {
        setLayout(new BorderLayout());

        // Panel superior para busqueda y filtros.
        JPanel panelSuperior = new JPanel(new GridLayout(2, 1));
        JPanel panelBuscar = new JPanel(new FlowLayout());

        txtBuscar = new JTextField(15);
        cbTipoBusqueda = new JComboBox<>(new String[]{"Codigo", "Nombre", "Marca"});
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Mostrar todos");
        btnNotificaciones = new JButton("🔔 0");
        btnNotificaciones.setToolTipText("Notificaciones de vencimiento");
        btnNotificaciones.setForeground(Color.RED);

        panelBuscar.add(new JLabel("Buscar por:"));
        panelBuscar.add(cbTipoBusqueda);
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnListar);
        panelBuscar.add(btnNotificaciones);

        // Opciones para filtrar y ordenar productos.
        JPanel panelFiltros = new JPanel(new FlowLayout());
        cbFiltroStock = new JComboBox<>(new String[]{"Todos", "Stock bajo", "Sin stock", "Con stock"});
        cbOrden = new JComboBox<>(new String[]{"Sin ordenar", "Nombre", "Precio", "Codigo", "Stock minimo"});

        panelFiltros.add(new JLabel("Filtrar stock:"));
        panelFiltros.add(cbFiltroStock);
        panelFiltros.add(new JLabel("Ordenar por:"));
        panelFiltros.add(cbOrden);

        panelSuperior.add(panelBuscar);
        panelSuperior.add(panelFiltros);
        add(panelSuperior, BorderLayout.NORTH);

        // Tabla donde se muestran los productos y su stock.
        String[] columnas = {"Codigo", "Nombre", "Marca", "Presentacion", "Precio", "Stock actual", "Stock minimo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Botones para administrar el inventario.
        JPanel panelAcciones = new JPanel(new GridLayout(3, 4, 5, 5));
        btnRegistrar = new JButton("Registrar producto");
        btnEditar = new JButton("Editar producto");
        btnDesactivar = new JButton("Desactivar producto");
        btnMovimientos = new JButton("Movimientos de stock");
        btnHistorial = new JButton("Historial de movimientos");
        btnCategorias = new JButton("Gestionar categorias");
        btnEstadisticas = new JButton("Estadisticas");
        btnCalcularPrecio = new JButton("Calcular precio");
        btnResumenAlertas = new JButton("Resumen y alertas");
        btnLotes = new JButton("Gestionar lotes");

        panelAcciones.add(btnRegistrar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnDesactivar);
        panelAcciones.add(btnMovimientos);
        panelAcciones.add(btnHistorial);
        panelAcciones.add(btnCategorias);
        panelAcciones.add(btnEstadisticas);
        panelAcciones.add(btnCalcularPrecio);
        panelAcciones.add(btnResumenAlertas);
        panelAcciones.add(btnLotes);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    // Carga los productos en la tabla y determina su estado de stock.
    public void mostrarProductos(List<Producto> productos, Map<Integer, Integer> stocks) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            int stockActual = stocks.getOrDefault(p.getId(), 0);
            String estado;
            if (stockActual == 0) {
                estado = "Sin stock";
            } else if (stockActual <= p.getStockMinimo()) {
                estado = "Stock bajo";
            } else {
                estado = "Normal";
            }
            modeloTabla.addRow(new Object[]{
                    p.getCodigo(), p.getNombre(), p.getMarca(), p.getPresentacion(),
                    String.format("S/ %.2f", p.getPrecioCentimos() / 100.0),
                    stockActual, p.getStockMinimo(), estado
            });
        }
    }

    // Muestra el formulario para registrar o editar un producto.
    public Producto solicitarProducto(List<Categoria> categorias, Producto producto) {
        boolean edicion = producto != null;
        JTextField txtCodigo = new JTextField(edicion ? producto.getCodigo() : "");
        JTextField txtNombre = new JTextField(edicion ? producto.getNombre() : "");
        JTextField txtMarca = new JTextField(edicion ? producto.getMarca() : "");
        JTextField txtPresentacion = new JTextField(edicion ? producto.getPresentacion() : "");
        JTextField txtPrecio = new JTextField(edicion ? String.format("%.2f", producto.getPrecioCentimos() / 100.0) : "");
        JTextField txtCosto = new JTextField(edicion ? String.format("%.2f", producto.getCostoCentimos() / 100.0) : "");
        JTextField txtStockMinimo = new JTextField(edicion ? String.valueOf(producto.getStockMinimo()) : "");
        txtCodigo.setEditable(!edicion);

        // Carga las categorias y selecciona la actual cuando se edita.
        JComboBox<Categoria> cbCategoria = new JComboBox<>(categorias.toArray(new Categoria[0]));
        if (edicion) {
            for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                if (cbCategoria.getItemAt(i).getId() == producto.getCategoriaId()) {
                    cbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }

        JCheckBox chkVencimiento = new JCheckBox("Requiere vencimiento", edicion && producto.isRequiereVencimiento());
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Codigo:"));
        panel.add(txtCodigo);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Marca:"));
        panel.add(txtMarca);
        panel.add(new JLabel("Categoria:"));
        panel.add(cbCategoria);
        panel.add(new JLabel("Presentacion:"));
        panel.add(txtPresentacion);
        panel.add(new JLabel("Precio de venta (S/):"));
        panel.add(txtPrecio);
        panel.add(new JLabel("Costo (S/):"));
        panel.add(txtCosto);
        panel.add(new JLabel("Stock minimo:"));
        panel.add(txtStockMinimo);
        panel.add(chkVencimiento);
        panel.add(new JLabel(""));

        int opcion = JOptionPane.showConfirmDialog(this, panel,
                edicion ? "Editar producto" : "Registrar producto",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return null;

        // Valida los datos y construye el objeto Producto.
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String marca = txtMarca.getText().trim();
            String presentacion = txtPresentacion.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty() || marca.isEmpty() || presentacion.isEmpty()
                    || txtPrecio.getText().trim().isEmpty() || txtCosto.getText().trim().isEmpty()
                    || txtStockMinimo.getText().trim().isEmpty()) {
                mostrarMensaje("Complete todos los campos.");
                return null;
            }

            Categoria categoria = (Categoria) cbCategoria.getSelectedItem();
            if (categoria == null) {
                mostrarMensaje("Seleccione una categoria.");
                return null;
            }

            int precio = (int) Math.round(Double.parseDouble(txtPrecio.getText().trim().replace(",", ".")) * 100);
            int costo = (int) Math.round(Double.parseDouble(txtCosto.getText().trim().replace(",", ".")) * 100);
            int stockMinimo = Integer.parseInt(txtStockMinimo.getText().trim());

            if (precio < 0 || costo < 0 || stockMinimo < 0) {
                mostrarMensaje("Los valores no pueden ser negativos.");
                return null;
            }

            return new Producto(edicion ? producto.getId() : 0, codigo, nombre, marca,
                    categoria.getId(), presentacion, precio, costo, stockMinimo,
                    chkVencimiento.isSelected(), edicion ? producto.isActivo() : true);
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese valores numericos validos.");
            return null;
        }
    }

    // Solicita los datos para registrar una entrada o salida de stock.
    public Object[] solicitarMovimiento() {
        String[] tipos = {"ENTRADA", "SALIDA"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);
        JTextField txtCantidad = new JTextField();
        JTextField txtReferencia = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));

        panel.add(new JLabel("Tipo de movimiento:"));
        panel.add(cbTipo);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        panel.add(new JLabel("Referencia:"));
        panel.add(txtReferencia);

        int opcion = JOptionPane.showConfirmDialog(this, panel,
                "Registrar movimiento de stock",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return null;

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());
            if (cantidad <= 0) {
                mostrarMensaje("La cantidad debe ser mayor a cero.");
                return null;
            }
            return new Object[]{cbTipo.getSelectedItem().toString(), cantidad, txtReferencia.getText().trim()};
        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese una cantidad valida.");
            return null;
        }
    }

    // Devuelve los valores seleccionados en la interfaz.
    public String getCodigoBuscar() {
        return txtBuscar.getText().trim();
    }

    public String getTipoBusqueda() {
        return cbTipoBusqueda.getSelectedItem().toString();
    }

    public String getFiltroStock() {
        return cbFiltroStock.getSelectedItem().toString();
    }

    public String getOrden() {
        return cbOrden.getSelectedItem().toString();
    }

    // Obtiene el codigo del producto seleccionado en la tabla.
    public String getCodigoSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return null;
        int filaModelo = tabla.convertRowIndexToModel(fila);
        return modeloTabla.getValueAt(filaModelo, 0).toString();
    }

    // Conecta los botones y filtros con sus eventos del controlador.
    public void eventoBuscar(ActionListener evento) { btnBuscar.addActionListener(evento); }
    public void eventoListar(ActionListener evento) { btnListar.addActionListener(evento); }
    public void eventoRegistrar(ActionListener evento) { btnRegistrar.addActionListener(evento); }
    public void eventoEditar(ActionListener evento) { btnEditar.addActionListener(evento); }
    public void eventoDesactivar(ActionListener evento) { btnDesactivar.addActionListener(evento); }
    public void eventoMovimientos(ActionListener evento) { btnMovimientos.addActionListener(evento); }
    public void eventoHistorial(ActionListener evento) { btnHistorial.addActionListener(evento); }
    public void eventoCategorias(ActionListener evento) { btnCategorias.addActionListener(evento); }
    public void eventoFiltro(ActionListener evento) { cbFiltroStock.addActionListener(evento); }
    public void eventoOrden(ActionListener evento) { cbOrden.addActionListener(evento); }
    public void eventoEstadisticas(ActionListener evento) { btnEstadisticas.addActionListener(evento); }
    public void eventoCalcularPrecio(ActionListener evento) { btnCalcularPrecio.addActionListener(evento); }
    public void eventoResumenAlertas(ActionListener evento) { btnResumenAlertas.addActionListener(evento); }
    public void eventoLotes(ActionListener evento) { btnLotes.addActionListener(evento); }

    // Actualiza el contador y muestra las alertas en una ventana independiente.
    public void mostrarNotificaciones(List<Lote> lotes, Map<Integer, String> nombresProductos, Consumer<Lote> accion) {
        btnNotificaciones.setText("🔔 " + lotes.size());
        btnNotificaciones.setForeground(lotes.isEmpty() ? Color.BLACK : Color.RED);

        // Evita agregar varias veces el mismo evento.
        for (ActionListener listener : btnNotificaciones.getActionListeners()) {
            btnNotificaciones.removeActionListener(listener);
        }

        btnNotificaciones.addActionListener(e -> {
            // Crea una ventana que no desaparece al mover el raton.
            JDialog dialogo = new JDialog(SwingUtilities.getWindowAncestor(this));
            dialogo.setTitle("Notificaciones de vencimiento");
            dialogo.setSize(380, 320);
            dialogo.setModal(false);
            dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(Color.WHITE);

            if (lotes.isEmpty()) {
                JLabel mensaje = new JLabel("No hay alertas de vencimiento.");
                mensaje.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panel.add(mensaje);
            } else {
                // Crea una notificacion para cada lote vencido o proximo a vencer.
                for (Lote lote : lotes) {
                    String nombre = nombresProductos.getOrDefault(lote.getProductoId(), "Producto desconocido");
                    if (nombre.length() > 28) nombre = nombre.substring(0, 28) + "...";

                    boolean vencido = lote.getFechaVencimiento().isBefore(LocalDate.now());
                    String estado = vencido ? "VENCIDO" : "POR VENCER";
                    String texto = "<html><b>" + nombre + "</b><br>Lote: "
                            + lote.getNumeroLote() + " | " + lote.getFechaVencimiento()
                            + " | <font color='red'>" + estado + "</font></html>";

                    JButton item = new JButton(texto);
                    item.setHorizontalAlignment(SwingConstants.LEFT);
                    item.setMaximumSize(new Dimension(360, 55));
                    item.setPreferredSize(new Dimension(360, 55));
                    item.setBackground(Color.WHITE);
                    item.setFocusPainted(false);

                    // Abre la gestion de lotes del producto seleccionado.
                    item.addActionListener(ev -> {
                        dialogo.dispose();
                        accion.accept(lote);
                    });
                    panel.add(item);
                }
            }

            // Agrega desplazamiento para consultar todas las notificaciones.
            JScrollPane scroll = new JScrollPane(panel);
            scroll.setPreferredSize(new Dimension(360, 270));
            scroll.getVerticalScrollBar().setUnitIncrement(35);
            dialogo.add(scroll);
            dialogo.setLocationRelativeTo(btnNotificaciones);
            dialogo.setVisible(true);
        });
    }

    // Muestra mensajes informativos o errores.
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
