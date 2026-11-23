package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.servicio.PublicadorEventosRecepcion;
import com.tambo.abastecimiento.controlador.RecepcionControlador;
import com.tambo.abastecimiento.modelo.DetalleRecepcion;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.*;

public final class RecepcionVista extends JPanel {
    private final RecepcionControlador controlador;
    private final DetalleRecepcionTablaModelo modelo =
            new DetalleRecepcionTablaModelo();

    private final JTable tabla = new JTable(modelo);
    private final JTextField campoNumero = new JTextField(10);
    private final JTextField campoOrden = new JTextField(6);

    public RecepcionVista(PublicadorEventosRecepcion eventos) {
        super(new BorderLayout());
        controlador = new RecepcionControlador(eventos);

        JPanel barra = Formulario.barra();
        barra.add(new JLabel("Número de recepción"));
        barra.add(campoNumero);
        barra.add(new JLabel("ID de la orden"));
        barra.add(campoOrden);

        barra.add(Formulario.boton("Agregar línea", this::agregarLinea));
        barra.add(Formulario.boton("Quitar línea", this::quitarLinea));
        barra.add(Formulario.boton("Confirmar recepción", this::confirmar));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void agregarLinea() {
        int productoId = Formulario.entero("ID del producto de la orden");
        int recibidas = Formulario.entero("¿Cuántas unidades llegaron?");
        int aceptadas = Formulario.entero("¿Cuántas unidades se aceptaron?");

        if (productoId < 1 || recibidas < 1) {
            throw new IllegalArgumentException(
                    "El ID del producto y las unidades recibidas deben ser mayores a cero.");
        }

        if (aceptadas < 0 || aceptadas > recibidas) {
            throw new IllegalArgumentException(
                    "Las unidades aceptadas deben estar entre cero y las recibidas.");
        }

        modelo.agregar(new DetalleRecepcion(productoId, recibidas, aceptadas));
    }

    private void quitarLinea() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            throw new IllegalArgumentException("Selecciona la fila que deseas quitar.");
        }

        modelo.removeRow(tabla.convertRowIndexToModel(fila));
    }

    private void confirmar() throws SQLException {
        String texto = AccesoDatos.requerido(campoOrden.getText(), "el ID de la orden");

        int ordenId;
        try {
            ordenId = Integer.parseInt(texto);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("El ID de la orden debe ser un número entero.");
        }

        int id = controlador.guardar(campoNumero.getText(), ordenId, modelo);

        JOptionPane.showMessageDialog(this, "Recepción guardada. ID: " + id);
        modelo.setRowCount(0);
        campoNumero.setText("");
        campoOrden.setText("");
    }
}