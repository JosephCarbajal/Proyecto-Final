package com.tambo.inventario.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class MovimientoInventarioVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtCodigo;
    private JComboBox<String> cmbTipo;
    private JButton btnBuscar;
    private JButton btnActualizar;
    private JButton btnCerrar;

    public MovimientoInventarioVista() {
        setTitle("Historial de movimientos de inventario");
        setSize(1100, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        txtCodigo = new JTextField(12);
        cmbTipo = new JComboBox<>(new String[]{"Todos", "ENTRADA", "SALIDA"});
        btnBuscar = new JButton("Buscar");

        panelFiltros.add(new JLabel("Código:"));
        panelFiltros.add(txtCodigo);
        panelFiltros.add(new JLabel("Tipo:"));
        panelFiltros.add(cmbTipo);
        panelFiltros.add(btnBuscar);

        String[] columnas = {
                "ID", "Código", "Producto", "Tipo", "Cantidad",
                "Stock anterior", "Stock posterior", "Referencia",
                "Fecha", "Usuario"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        btnActualizar = new JButton("Actualizar");
        btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnActualizar);
        panelBotones.add(btnCerrar);

        add(panelFiltros, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> dispose());
    }

    public void mostrarMovimientos(List<Map<String, Object>> movimientos) {
        modelo.setRowCount(0);

        for (Map<String, Object> movimiento : movimientos) {
            modelo.addRow(new Object[]{
                    movimiento.get("id"),
                    movimiento.get("codigo"),
                    movimiento.get("nombre"),
                    movimiento.get("tipo"),
                    movimiento.get("cantidad"),
                    movimiento.get("stockAnterior"),
                    movimiento.get("stockPosterior"),
                    movimiento.get("referencia"),
                    movimiento.get("fecha"),
                    movimiento.get("usuario")
            });
        }
    }

    public String getCodigoFiltro() {
        return txtCodigo.getText().trim();
    }

    public String getTipoFiltro() {
        return cmbTipo.getSelectedItem().toString();
    }

    public void eventoBuscar(ActionListener evento) {
        btnBuscar.addActionListener(evento);
    }

    public void eventoActualizar(Runnable evento) {
        btnActualizar.addActionListener(e -> evento.run());
    }
}