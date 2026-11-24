package com.tambo.inventario.vista;

import com.tambo.compartido.modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaVista extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtDescripcion;

    private JButton btnRegistrar;
    private JButton btnEditar;
    private JButton btnEliminar;
    private JButton btnLimpiar;

    public CategoriaVista() {
        setTitle("Gestión de categorías");
        setSize(750, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 8, 8));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de categoría"));

        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextField();

        panelFormulario.add(new JLabel("Código:"));
        panelFormulario.add(txtCodigo);
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Descripción:"));
        panelFormulario.add(txtDescripcion);

        btnRegistrar = new JButton("Registrar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnEditar);

        String[] columnas = {"ID", "Código", "Nombre"};

        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        add(panelFormulario, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int fila = tabla.getSelectedRow();

                if (fila >= 0) {
                    txtCodigo.setText(modelo.getValueAt(fila, 1).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 2).toString());
                }
            }
        });

        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    public void mostrarCategorias(List<Categoria> categorias) {
        modelo.setRowCount(0);

        for (Categoria categoria : categorias) {
            modelo.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getCodigo(),
                    categoria.getNombre()
            });
        }
    }

    public void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        tabla.clearSelection();
    }

    public String getCodigo() {
        return txtCodigo.getText().trim();
    }

    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getDescripcion() {
        return txtDescripcion.getText().trim();
    }

    public int getIdSeleccionado() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            return -1;
        }

        return (int) modelo.getValueAt(fila, 0);
    }

    public void eventoRegistrar(Runnable evento) {
        btnRegistrar.addActionListener(e -> evento.run());
    }

    public void eventoEditar(Runnable evento) {
        btnEditar.addActionListener(e -> evento.run());
    }

    public void eventoEliminar(Runnable evento) {
        btnEliminar.addActionListener(e -> evento.run());
    }
}
