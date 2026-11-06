package com.tambo.inventario.vista;

import com.tambo.compartido.modelo.Producto;

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
    private final DefaultTableModel modeloTabla;

    public ProductoVista() {
        setLayout(new BorderLayout());

        // Barra de búsqueda
        JPanel panelBuscar = new JPanel(new FlowLayout());

        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Mostrar todos");

        panelBuscar.add(new JLabel("Código:"));
        panelBuscar.add(txtBuscar);
        panelBuscar.add(btnBuscar);
        panelBuscar.add(btnListar);

        add(panelBuscar, BorderLayout.NORTH);

        // Tabla de productos
        String[] columnas = {
                "Código", "Nombre", "Marca", "Presentación",
                "Precio", "Stock actual", "Stock mínimo", "Estado"
        };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    // Mostrar productos con su stock
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

    // Obtener el código escrito
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

    // Mostrar mensajes
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}
