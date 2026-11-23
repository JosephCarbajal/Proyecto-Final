package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.repositorio.ConsultaOrdenesPendientes;
import com.tambo.abastecimiento.Formulario;

import java.awt.BorderLayout;
import java.sql.SQLException;
import javax.swing.*;

public final class OrdenesPendientesVista extends JPanel {
    private final ConsultaOrdenesPendientes consulta = new ConsultaOrdenesPendientes();
    private final JTable tabla = new JTable();

    public OrdenesPendientesVista() throws SQLException {
        super(new BorderLayout());

        JPanel barra = Formulario.barra();
        barra.add(Formulario.boton("Actualizar pendientes", this::actualizar));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        actualizar();
    }

    public void actualizar() throws SQLException {
        tabla.setModel(consulta.listar());
    }
}