package com.tambo.abastecimiento;

import java.awt.FlowLayout;
import java.sql.SQLException;
import java.util.concurrent.CancellationException;
import javax.swing.*;

public interface Formulario {
    interface Accion {
        void ejecutar() throws SQLException;
    }

    static JButton boton(String nombre, Accion accion) {
        JButton boton = new JButton(nombre);

        boton.addActionListener(evento -> {
            try {
                accion.ejecutar();
            } catch (CancellationException cancelada) {
                // El usuario cerró el diálogo.
            } catch (SQLException | IllegalArgumentException | ArithmeticException error) {
                JOptionPane.showMessageDialog(boton, error.getMessage(),
                        "Revisar operación", JOptionPane.WARNING_MESSAGE);
            }
        });

        return boton;
    }

    static String pedir(String campo) {
        String texto = JOptionPane.showInputDialog(null, campo);

        if (texto == null) {
            throw new CancellationException();
        }

        return AccesoDatos.requerido(texto, campo);
    }

    static int entero(String campo) {
        return Integer.parseInt(pedir(campo));
    }

    static String usuario() {
        return System.getProperty("user.name", "operador");
    }

    static JPanel barra() {
        return new JPanel(new FlowLayout(FlowLayout.LEFT));
    }
}