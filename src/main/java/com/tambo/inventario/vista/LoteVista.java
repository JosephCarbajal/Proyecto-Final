
package com.tambo.inventario.vista;

import com.tambo.inventario.modelo.Lote;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LoteVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnRegistrar;
    private JButton btnActualizar;
    private JButton btnVencidos;
    private JButton btnProximos;
    private JButton btnCerrar;

    public LoteVista() {
        setTitle("Gestion de lotes y vencimientos");
        setSize(850, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnas = {
                "ID", "Numero de lote", "Vencimiento", "Cantidad", "Estado"
        };

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        btnRegistrar = new JButton("Registrar lote");
        btnActualizar = new JButton("Actualizar");
        btnVencidos = new JButton("Vencidos");
        btnProximos = new JButton("Proximos a vencer");
        btnCerrar = new JButton("Cerrar");

        JPanel panelBotones = new JPanel(new FlowLayout());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnVencidos);
        panelBotones.add(btnProximos);
        panelBotones.add(btnCerrar);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnCerrar.addActionListener(e -> dispose());
    }

    public void mostrarLotes(List<Lote> lotes) {
        modelo.setRowCount(0);

        for (Lote lote : lotes) {
            String estado;

            if (lote.getFechaVencimiento().isBefore(LocalDate.now())) {
                estado = "Vencido";
            } else if (!lote.getFechaVencimiento().isAfter(
                    LocalDate.now().plusDays(30))) {
                estado = "Proximo a vencer";
            } else {
                estado = "Vigente";
            }

            modelo.addRow(new Object[]{
                    lote.getId(),
                    lote.getNumeroLote(),
                    lote.getFechaVencimiento(),
                    lote.getCantidadActual(),
                    estado
            });
        }
    }

    public Object[] solicitarLote() {
        JTextField txtNumero = new JTextField();
        JTextField txtFecha = new JTextField();
        JTextField txtCantidad = new JTextField();

        Object[] campos = {
                "Numero de lote:", txtNumero,
                "Fecha de vencimiento (AAAA-MM-DD):", txtFecha,
                "Cantidad:", txtCantidad
        };

        int opcion = JOptionPane.showConfirmDialog(
                this,
                campos,
                "Registrar lote",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return null;
        }

        try {
            String numero = txtNumero.getText().trim();
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (numero.isEmpty() || cantidad <= 0) {
                throw new IllegalArgumentException();
            }

            return new Object[]{numero, fecha, cantidad};

        } catch (Exception e) {
            mostrarMensaje("Ingrese datos validos.");
            return null;
        }
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void eventoRegistrar(Runnable evento) {
        btnRegistrar.addActionListener(e -> evento.run());
    }

    public void eventoActualizar(Runnable evento) {
        btnActualizar.addActionListener(e -> evento.run());
    }

    public void eventoVencidos(Runnable evento) {
        btnVencidos.addActionListener(e -> evento.run());
    }

    public void eventoProximos(Runnable evento) {
        btnProximos.addActionListener(e -> evento.run());
    }
}
