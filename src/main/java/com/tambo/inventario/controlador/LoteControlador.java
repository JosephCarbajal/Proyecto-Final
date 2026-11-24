
package com.tambo.inventario.controlador;

import com.tambo.inventario.modelo.Lote;
import com.tambo.inventario.repositorio.LoteSQLiteRepository;
import com.tambo.inventario.util.FechaValidador;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class LoteControlador {

    private final LoteSQLiteRepository repositorio;
    private final FechaValidador fechaValidador;

    public LoteControlador() {
        this.repositorio = new LoteSQLiteRepository();
        this.fechaValidador = new FechaValidador();
    }

    public List<Lote> listarLotes(int productoId)
            throws SQLException {

        return repositorio.listarPorProducto(productoId);
    }

    public void registrarLote(
            int productoId,
            String numeroLote,
            LocalDate fechaVencimiento,
            int cantidad) throws SQLException {

        if (!fechaValidador.validarFecha(fechaVencimiento)
                || !fechaValidador.fechaEsFutura(fechaVencimiento)) {
            throw new IllegalArgumentException(
                    "La fecha de vencimiento debe ser futura."
            );
        }

        repositorio.registrarLote(
                productoId,
                numeroLote,
                fechaVencimiento,
                cantidad
        );
    }

    public List<Lote> listarLotesDisponibles(int productoId)
            throws SQLException {

        return repositorio.listarDisponiblesFEFO(productoId);
    }

    public List<Lote> listarLotesVencidos() throws SQLException {
        return repositorio.listarVencidos();
    }

    public List<Lote> listarProximosAVencer(int dias)
            throws SQLException {

        return repositorio.listarProximosAVencer(dias);
    }
}
