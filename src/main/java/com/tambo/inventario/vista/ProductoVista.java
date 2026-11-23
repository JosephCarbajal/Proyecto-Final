
package com.tambo.inventario.vista;

import com.tambo.compartido.modelo.Producto;
import com.tambo.compartido.modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ProductoVista extends JPanel {

    private final JTextField txtBuscar;
    private final JButton btnBuscar;
    private final JButton btnListar;
    private final JButton btnRegistrar;
    private final JButton btnEditar;
    private final JButton btnDesactivar;
    private final JButton btnMovimientos;
    private final JButton btnHistorial;
    private final JTable tabla;
    private final DefaultTableModel modeloTabla;

    public ProductoVista() {
        setLayout(new BorderLayout());

        JPanel panelBuscar = new JPanel(new FlowLayout());

        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Mostrar todos");

        panelBuscar.add(new JLabel("Codigo:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnListar);

        add(panelBuscar, BorderLayout.NORTH);

        String[] columnas = {
                "Codigo", "Nombre", "Marca", "Presentacion",
                "Precio", "Stock actual", "Stock minimo", "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout());

        btnRegistrar = new JButton("Registrar producto");
        btnEditar = new JButton("Editar producto");
        btnDesactivar = new JButton("Desactivar producto");
        btnMovimientos = new JButton("Movimientos de stock");
        btnHistorial = new JButton("Historial de movimientos");

        panelAcciones.add(btnRegistrar);
        panelAcciones.add(btnEditar);
        panelAcciones.add(btnDesactivar);
        panelAcciones.add(btnMovimientos);
        panelAcciones.add(btnHistorial);

        add(panelAcciones, BorderLayout.SOUTH);
    }

    public void mostrarProductos(List<Producto> productos,
                                 Map<Integer, Integer> stocks) {

        modeloTabla.setRowCount(0);

        for (Producto p : productos) {
            int stockActual = stocks.getOrDefault(p.getId(), 0);

            String estado = stockActual <= p.getStockMinimo()
                    ? "Stock bajo" : "Normal";

            modeloTabla.addRow(new Object[]{
                    p.getCodigo(),
                    p.getNombre(),
                    p.getMarca(),
                    p.getPresentacion(),
                    String.format("S/ %.2f", p.getPrecioCentimos() / 100.0),
                    stockActual,
                    p.getStockMinimo(),
                    estado
            });
        }
    }

    public Producto solicitarProducto(List<Categoria> categorias,
                                      Producto producto) {

        boolean edicion = producto != null;

        JTextField txtCodigo = new JTextField(
                edicion ? producto.getCodigo() : "");

        JTextField txtNombre = new JTextField(
                edicion ? producto.getNombre() : "");

        JTextField txtMarca = new JTextField(
                edicion ? producto.getMarca() : "");

        JTextField txtPresentacion = new JTextField(
                edicion ? producto.getPresentacion() : "");

        JTextField txtPrecio = new JTextField(
                edicion ? String.format("%.2f",
                        producto.getPrecioCentimos() / 100.0) : "");

        JTextField txtCosto = new JTextField(
                edicion ? String.format("%.2f",
                        producto.getCostoCentimos() / 100.0) : "");

        JTextField txtStockMinimo = new JTextField(
                edicion ? String.valueOf(producto.getStockMinimo()) : "");

        txtCodigo.setEditable(!edicion);

        JComboBox<Categoria> cbCategoria =
                new JComboBox<>(categorias.toArray(new Categoria[0]));

        if (edicion) {
            for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                if (cbCategoria.getItemAt(i).getId()
                        == producto.getCategoriaId()) {
                    cbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }

        JCheckBox chkVencimiento = new JCheckBox(
                "Requiere vencimiento",
                edicion && producto.isRequiereVencimiento()
        );

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

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                edicion ? "Editar producto" : "Registrar producto",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String marca = txtMarca.getText().trim();
            String presentacion = txtPresentacion.getText().trim();

            if (codigo.isEmpty() || nombre.isEmpty()
                    || marca.isEmpty() || presentacion.isEmpty()
                    || txtPrecio.getText().trim().isEmpty()
                    || txtCosto.getText().trim().isEmpty()
                    || txtStockMinimo.getText().trim().isEmpty()) {

                mostrarMensaje("Complete todos los campos.");
                return null;
            }

            Categoria categoria =
                    (Categoria) cbCategoria.getSelectedItem();

            if (categoria == null) {
                mostrarMensaje("Seleccione una categoria.");
                return null;
            }

            int precio = (int) Math.round(
                    Double.parseDouble(txtPrecio.getText().trim()
                            .replace(",", ".")) * 100
            );

            int costo = (int) Math.round(
                    Double.parseDouble(txtCosto.getText().trim()
                            .replace(",", ".")) * 100
            );

            int stockMinimo =
                    Integer.parseInt(txtStockMinimo.getText().trim());

            if (precio < 0 || costo < 0 || stockMinimo < 0) {
                mostrarMensaje("Los valores no pueden ser negativos.");
                return null;
            }

            return new Producto(
                    edicion ? producto.getId() : 0,
                    codigo,
                    nombre,
                    marca,
                    categoria.getId(),
                    presentacion,
                    precio,
                    costo,
                    stockMinimo,
                    chkVencimiento.isSelected(),
                    edicion ? producto.isActivo() : true
            );

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese valores numericos validos.");
            return null;
        }
    }

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

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Registrar movimiento de stock",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                mostrarMensaje("La cantidad debe ser mayor a cero.");
                return null;
            }

            return new Object[]{
                    cbTipo.getSelectedItem().toString(),
                    cantidad,
                    txtReferencia.getText().trim()
            };

        } catch (NumberFormatException e) {
            mostrarMensaje("Ingrese una cantidad valida.");
            return null;
        }
    }

    public String getCodigoBuscar() {
        return txtBuscar.getText().trim();
    }

    public String getCodigoSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            return null;
        }

        int filaModelo = tabla.convertRowIndexToModel(fila);
        return modeloTabla.getValueAt(filaModelo, 0).toString();
    }

    public void eventoBuscar(ActionListener evento) {
        btnBuscar.addActionListener(evento);
    }

    public void eventoListar(ActionListener evento) {
        btnListar.addActionListener(evento);
    }

    public void eventoRegistrar(ActionListener evento) {
        btnRegistrar.addActionListener(evento);
    }

    public void eventoEditar(ActionListener evento) {
        btnEditar.addActionListener(evento);
    }

    public void eventoDesactivar(ActionListener evento) {
        btnDesactivar.addActionListener(evento);
    }

    public void eventoMovimientos(ActionListener evento) {
        btnMovimientos.addActionListener(evento);
    }

    public void eventoHistorial(ActionListener evento) {
        btnHistorial.addActionListener(evento);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
