package com.tambo.app;

import com.tambo.inventario.controlador.ProductoControlador;
import com.tambo.inventario.vista.ProductoVista;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Crear la ventana principal
            JFrame ventana = new JFrame("Tambo+ - Gestion de Inventarios");
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setSize(1000, 600);
            ventana.setLocationRelativeTo(null);

            // Mostrar los productos
            ProductoVista vista = new ProductoVista();
            new ProductoControlador(vista);

            ventana.add(vista);
            ventana.setVisible(true);
        });
    }
}
