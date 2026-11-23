
package com.tambo.inventario.controlador;

import com.tambo.inventario.repositorio.MovimientoInventarioSQLiteRepository;
import com.tambo.inventario.vista.MovimientoInventarioVista;

import javax.swing.JOptionPane;
import java.sql.SQLException;

public class MovimientoInventarioControlador {

    private final MovimientoInventarioVista vista;
    private final MovimientoInventarioSQLiteRepository repositorio;

    public MovimientoInventarioControlador(MovimientoInventarioVista vista) {
        this.vista = vista;
        this.repositorio = new MovimientoInventarioSQLiteRepository();

        vista.eventoBuscar(e -> buscarMovimientos());
        vista.eventoActualizar(() -> cargarMovimientos());

        cargarMovimientos();
    }

    private void cargarMovimientos() {
        cargarMovimientos("", "Todos");
    }

    private void buscarMovimientos() {
        String codigo = vista.getCodigoFiltro();
        String tipo = vista.getTipoFiltro();

        cargarMovimientos(codigo, tipo);
    }

    private void cargarMovimientos(String codigo, String tipo) {
        try {
            vista.mostrarMovimientos(
                    repositorio.listarMovimientos(codigo, tipo)
            );
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    vista,
                    "Error al cargar el historial: " + e.getMessage()
            );
        }
    }
}
