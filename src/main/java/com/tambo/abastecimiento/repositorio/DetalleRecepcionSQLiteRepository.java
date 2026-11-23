package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.DetalleRecepcion;

import java.sql.Connection;
import java.sql.SQLException;

public final class DetalleRecepcionSQLiteRepository {

    public void registrar(Connection conexion, int recepcionId,
                          DetalleRecepcion detalle, long costo) throws SQLException {

        if (detalle.aceptadas() == 0) {
            return;
        }

        String sql = """
                INSERT INTO detalle_recepciones
                    (recepcion_id, producto_id, cantidad, costo_unitario_centimos)
                VALUES (?, ?, ?, ?)
                """;

        AccesoDatos.escribir(conexion, sql,
                recepcionId,
                detalle.productoId(),
                detalle.aceptadas(),
                costo);
    }
}