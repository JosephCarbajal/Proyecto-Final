package com.tambo.abastecimiento.vista;

import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.controlador.ReporteAbastecimientoControlador;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import javax.swing.*;

public final class ReporteAbastecimientoVista extends JPanel {
    private final ReporteAbastecimientoControlador controlador =
            new ReporteAbastecimientoControlador();

    private final JComboBox<String> tipoReporte = new JComboBox<>(new String[]{
            "Compras por proveedor",
            "Recepciones por periodo",
            "Cantidades pendientes"
    });

    private final JTextField campoDesde;
    private final JTextField campoHasta;
    private final JTable tabla = new JTable();

    public ReporteAbastecimientoVista() {
        super(new BorderLayout());

        LocalDate hoy = LocalDate.now();
        campoDesde = new JTextField(hoy.withDayOfYear(1).toString(), 10);
        campoHasta = new JTextField(hoy.toString(), 10);

        JPanel barra = Formulario.barra();
        barra.add(new JLabel("Reporte"));
        barra.add(tipoReporte);
        barra.add(new JLabel("Desde"));
        barra.add(campoDesde);
        barra.add(new JLabel("Hasta"));
        barra.add(campoHasta);
        barra.add(Formulario.boton("Generar reporte", this::generar));

        add(barra, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void generar() throws SQLException {
        tabla.setModel(controlador.generar(
                tipoReporte.getSelectedIndex(),
                campoDesde.getText(),
                campoHasta.getText()));
    }
}