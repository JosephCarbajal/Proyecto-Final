package com.tambo.inventario.vista;

import com.tambo.compartido.modelo.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoVista extends JPanel {
    private JTextField txtBuscar;
    private JButton btnBuscar, btnListar;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    public ProductoVista() {
        setLayout(new BorderLayout(10, 10));

        // Parte de arriba para buscar productos
        JPanel panelBuscar = new JPanel();
        txtBuscar = new JTextField(15);
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Mostrar todos");

        panelBuscar.add(new JLabel("Codigo:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnListar);
        add(panelBuscar, BorderLayout.NORTH);

        // Tabla de productos
        String[] columnas = {"Codigo", "Nombre", "Marca", "Presentacion", "Precio", "Stock minimo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    // Mostrar los productos en la tabla
    public void mostrarProductos(List<Producto> productos) {
        modeloTabla.setRowCount(0);

        for (Producto p : productos) {
            Object[] fila = {
                    p.getCodigo(),
                    p.getNombre(),
                    p.getMarca(),
                    p.getPresentacion(),
                    "S/ " + String.format("%.2f", p.getPrecioCentimos() / 100.0),
                    p.getStockMinimo()
            };
            modeloTabla.addRow(fila);
        }
    }

    // Obtener el codigo que se quiere buscar
    public String getCodigoBuscar() {
        return txtBuscar.getText().trim();
    }

    // Eventos de los botones
    public void eventoBuscar(ActionListener evento) {
        btnBuscar.addActionListener(evento);
    }

    public void eventoListar(ActionListener evento) {
        btnListar.addActionListener(evento);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
