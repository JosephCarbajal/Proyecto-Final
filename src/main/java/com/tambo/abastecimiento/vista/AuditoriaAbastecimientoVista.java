package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.controlador.AuditoriaAbastecimientoControlador;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.*;

public final class AuditoriaAbastecimientoVista extends JPanel {
    private final AuditoriaAbastecimientoControlador controlador =
            new AuditoriaAbastecimientoControlador();

    private final JTable tabla = new JTable();

    public AuditoriaAbastecimientoVista() throws SQLException {
        super(new BorderLayout());

        JPanel barra = Formulario.barra();
        barra.add(Formulario.boton("Actualizar auditoría", this::actualizar));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        actualizar();
    }

    public void actualizar() throws SQLException {
        tabla.setModel(controlador.historial());
    }
}