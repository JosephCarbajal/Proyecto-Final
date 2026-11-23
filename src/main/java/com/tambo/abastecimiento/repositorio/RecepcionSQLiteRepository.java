package com.tambo.abastecimiento.repositorio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.Recepcion;
import com.tambo.compartido.ConexionSQLite;
import java.sql.Connection;
import java.sql.SQLException;

public final class RecepcionSQLiteRepository {

    public int crear(Connection conexion, Recepcion recepcion) throws SQLException {
        String sql = """
                INSERT INTO recepciones (orden_id, numero, fecha, estado)
                VALUES (?, ?, CURRENT_TIMESTAMP, 'PARCIAL')
                """;

        AccesoDatos.escribir(conexion, sql,
                recepcion.ordenId(), recepcion.numero());

        return AccesoDatos.nuevoId(conexion);
    }

    public void completar(Connection conexion, int recepcionId) throws SQLException {
        String sql = """
                UPDATE recepciones
                SET estado = 'COMPLETA'
                WHERE id = ?
                """;

        AccesoDatos.escribir(conexion, sql, recepcionId);
    }

    public void habilitarParciales() throws SQLException {
        try (Connection conexion = ConexionSQLite.conectar();
             var sentencia = conexion.createStatement()) {

            sentencia.execute("PRAGMA foreign_keys = OFF");
            sentencia.execute("BEGIN IMMEDIATE");

            try {
                String buscarRestriccion = """
                        SELECT COUNT(*)
                        FROM pragma_index_list('recepciones') i
                        WHERE i.[unique] = 1
                          AND (
                              SELECT group_concat(name)
                              FROM pragma_index_info(i.name)
                          ) = 'orden_id'
                        """;

                if (AccesoDatos.numero(conexion, buscarRestriccion) > 0) {
                    sentencia.execute("""
                            CREATE TABLE recepciones_parciales_nueva (
                                id INTEGER PRIMARY KEY,
                                orden_id INTEGER NOT NULL REFERENCES ordenes_compra(id),
                                numero TEXT NOT NULL UNIQUE,
                                fecha TEXT NOT NULL,
                                estado TEXT NOT NULL DEFAULT 'COMPLETA'
                            )
                            """);

                    sentencia.execute("""
                            INSERT INTO recepciones_parciales_nueva
                                (id, orden_id, numero, fecha, estado)
                            SELECT id, orden_id, numero, fecha, estado
                            FROM recepciones
                            """);

                    sentencia.execute("DROP TABLE recepciones");
                    sentencia.execute("""
                            ALTER TABLE recepciones_parciales_nueva
                            RENAME TO recepciones
                            """);
                    sentencia.execute("""
                            CREATE INDEX idx_recepciones_orden_ab
                            ON recepciones(orden_id)
                            """);

                    long errores = AccesoDatos.numero(conexion,
                            "SELECT COUNT(*) FROM pragma_foreign_key_check");

                    if (errores > 0) {
                        throw new SQLException(
                                "No se pudo conservar la integridad de las recepciones.");
                    }
                }

                sentencia.execute("COMMIT");
            } catch (SQLException | RuntimeException error) {
                sentencia.execute("ROLLBACK");
                throw error;
            }
        }
    }
}