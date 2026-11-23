package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.modelo.Proveedor;

import java.util.concurrent.CancellationException;
import javax.swing.*;

public final class ProveedorEdicionDialogo {

    public Proveedor editar(Proveedor proveedor) {
        JTextField codigo = new JTextField(proveedor.codigo());
        JTextField nombre = new JTextField(proveedor.nombre());
        JTextField correo = new JTextField(proveedor.correo());
        JTextField dias = new JTextField(String.valueOf(proveedor.plazoDias()));

        Object[] campos = {
                "Código", codigo,
                "Nombre", nombre,
                "Correo", correo,
                "Días de entrega", dias
        };

        String titulo = proveedor.id() == 0
                ? "Registrar proveedor"
                : "Editar proveedor";

        int opcion = JOptionPane.showConfirmDialog(
                null, campos, titulo,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (opcion != JOptionPane.OK_OPTION) {
            throw new CancellationException();
        }

        int plazo;
        try {
            plazo = Integer.parseInt(dias.getText().strip());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(
                    "Escribe los días de entrega como un número entero.");
        }

        return new Proveedor(
                proveedor.id(),
                codigo.getText().strip(),
                nombre.getText().strip(),
                correo.getText().strip(),
                plazo);
    }
}