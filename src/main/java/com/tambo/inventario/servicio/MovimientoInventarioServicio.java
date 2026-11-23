package com.tambo.inventario.servicio;

import com.tambo.inventario.modelo.Inventario;
import com.tambo.inventario.modelo.TipoMovimiento;
import com.tambo.inventario.util.MovimientoValidador;
import com.tambo.inventario.repositorio.MovimientoInventarioSQLiteRepository;

import java.sql.SQLException;

public class MovimientoInventarioServicio {

    private final MovimientoValidador validador;
    private final MovimientoInventarioSQLiteRepository repositorio;

    public MovimientoInventarioServicio() {
        this.validador = new MovimientoValidador();
        this.repositorio = new MovimientoInventarioSQLiteRepository();
    }

    // Registrar movimiento en memoria
    public void registrarMovimiento(Inventario inventario, TipoMovimiento tipo, int cantidad) {

        if (inventario == null) {
            throw new IllegalArgumentException("El inventario no puede estar vacío.");
        }

        if (!validador.validarMovimiento(tipo, cantidad)) {
            throw new IllegalArgumentException("El movimiento no es válido.");
        }

        inventario.registrarMovimiento(tipo, cantidad);
    }

    // Registrar movimiento en SQLite
    public void registrarMovimientoSQLite(
            int productoId,
            TipoMovimiento tipo,
            int cantidad,
            String referencia,
            String usuario) throws SQLException {

        if (!validador.validarMovimiento(tipo, cantidad)) {
            throw new IllegalArgumentException("El movimiento no es válido.");
        }

        if (tipo != TipoMovimiento.ENTRADA && tipo != TipoMovimiento.SALIDA) {
            throw new IllegalArgumentException("Tipo de movimiento no permitido en SQLite.");
        }

        repositorio.registrarMovimiento(
                productoId,
                tipo.name(),
                cantidad,
                referencia,
                usuario
        );
    }

    public int consultarStock(Inventario inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("El inventario no puede estar vacío.");
        }

        return inventario.getStock();
    }

    public boolean necesitaReposicion(Inventario inventario) {
        if (inventario == null) {
            throw new IllegalArgumentException("El inventario no puede estar vacío.");
        }

        return inventario.requiereReposicion();
    }
}
