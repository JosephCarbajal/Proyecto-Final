package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.controlador.CatalogoProveedorControlador;

import java.awt.BorderLayout;
import javax.swing.*;

public final class CatalogoProveedorVista extends JPanel {
    private final CatalogoProveedorControlador controlador =
            new CatalogoProveedorControlador();

    private final JTextField campoProveedor = new JTextField(6);
    private final JTable tabla = new JTable();

    public CatalogoProveedorVista() {
        super(new BorderLayout());

        JPanel barra = Formulario.barra();
        barra.add(new JLabel("ID del proveedor"));
        barra.add(campoProveedor);

        barra.add(Formulario.boton("Ver catálogo", () -> {
            int id = leerProveedor();
            tabla.setModel(controlador.consultar(id));
        }));

        barra.add(Formulario.boton("Vincular o actualizar producto", () -> {
            int id = leerProveedor();
            controlador.vincular(id);
            tabla.setModel(controlador.consultar(id));
        }));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private int leerProveedor() {
        String texto = AccesoDatos.requerido(
                campoProveedor.getText(), "el ID del proveedor");

        try {
            int id = Integer.parseInt(texto);
            if (id > 0) {
                return id;
            }
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException(
                    "El ID del proveedor debe ser un número entero.");
        }

        throw new IllegalArgumentException(
                "El ID del proveedor debe ser mayor a cero.");
    }
}