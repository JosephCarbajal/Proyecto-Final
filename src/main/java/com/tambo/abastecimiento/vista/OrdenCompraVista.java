package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.controlador.OrdenCompraControlador;
import com.tambo.abastecimiento.modelo.DetalleOrdenCompra;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.*;

public final class OrdenCompraVista extends JPanel {
    private final OrdenCompraControlador controlador = new OrdenCompraControlador();
    private final DetalleOrdenCompraTablaModelo modelo = new DetalleOrdenCompraTablaModelo();

    private final JTable tabla = new JTable(modelo);
    private final JTextField campoNumero = new JTextField(10);
    private final JTextField campoProveedor = new JTextField(6);

    public OrdenCompraVista() {
        super(new BorderLayout());

        JPanel barra = Formulario.barra();
        barra.add(new JLabel("Número de orden"));
        barra.add(campoNumero);
        barra.add(new JLabel("ID del proveedor"));
        barra.add(campoProveedor);

        barra.add(Formulario.boton("Agregar línea", this::agregarLinea));
        barra.add(Formulario.boton("Quitar línea", this::quitarLinea));
        barra.add(Formulario.boton("Confirmar compra", this::confirmarCompra));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void agregarLinea() {
        int productoId = Formulario.entero("ID del producto del catálogo del proveedor");
        int cantidad = Formulario.entero("Cantidad solicitada");

        if (productoId < 1 || cantidad < 1) {
            throw new IllegalArgumentException(
                    "El ID del producto y la cantidad deben ser mayores a cero.");
        }

        modelo.agregar(new DetalleOrdenCompra(productoId, cantidad));
    }

    private void quitarLinea() {
        int fila = tabla.getSelectedRow();

        if (fila < 0) {
            throw new IllegalArgumentException("Selecciona la fila que deseas quitar.");
        }

        modelo.removeRow(tabla.convertRowIndexToModel(fila));
    }

    private void confirmarCompra() throws SQLException {
        String texto = AccesoDatos.requerido(campoProveedor.getText(), "el ID del proveedor");

        int proveedorId;
        try {
            proveedorId = Integer.parseInt(texto);
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("El ID del proveedor debe ser un número entero.");
        }

        int ordenId = controlador.guardar(campoNumero.getText(), proveedorId, modelo);

        JOptionPane.showMessageDialog(this, "Orden de compra guardada. ID: " + ordenId);
        modelo.setRowCount(0);
        campoNumero.setText("");
        campoProveedor.setText("");
    }
}