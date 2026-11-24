package com.tambo.inventario.controlador;

import com.tambo.inventario.modelo.Lote;
import com.tambo.inventario.vista.LoteVista;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoteControladorVista {

    private final LoteControlador controlador;
    private final LoteVista vista;
    private final int productoId;

    public LoteControladorVista(int productoId) {
        this.productoId = productoId;
        this.controlador = new LoteControlador();
        this.vista = new LoteVista();

        vista.eventoRegistrar(this::registrarLote);
        vista.eventoActualizar(this::cargarLotes);
        vista.eventoVencidos(this::mostrarVencidos);
        vista.eventoProximos(this::mostrarProximos);

        cargarLotes();
        vista.setVisible(true);
    }

    private void cargarLotes() {
        try {
            List<Lote> lotes = controlador.listarLotes(productoId);
            vista.mostrarLotes(lotes);
        } catch (SQLException e) {
            vista.mostrarMensaje("Error al cargar los lotes: " + e.getMessage());
        }
    }

    private void registrarLote() {
        Object[] datos = vista.solicitarLote();

        if (datos == null) {
            return;
        }

        try {
            controlador.registrarLote(
                    productoId,
                    (String) datos[0],
                    (LocalDate) datos[1],
                    (int) datos[2]
            );

            vista.mostrarMensaje("Lote registrado correctamente.");
            cargarLotes();

        } catch (Exception e) {
            vista.mostrarMensaje("No se pudo registrar el lote: " + e.getMessage());
        }
    }

    private void mostrarVencidos() {
        try {
            vista.mostrarLotes(controlador.listarLotesVencidos());
        } catch (SQLException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }

    private void mostrarProximos() {
        try {
            vista.mostrarLotes(controlador.listarProximosAVencer(30));
        } catch (SQLException e) {
            vista.mostrarMensaje("Error: " + e.getMessage());
        }
    }
}
