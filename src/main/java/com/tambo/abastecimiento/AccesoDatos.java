package com.tambo.abastecimiento;

import com.tambo.compartido.ConexionSQLite;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public interface AccesoDatos {

    interface Operacion<T> {
        T ejecutar(Connection conexion) throws SQLException;
    }

    static <T> T operar(Operacion<T> operacion) throws SQLException {
        try (Connection conexion = ConexionSQLite.conectar();
             Statement sentencia = conexion.createStatement()) {

            sentencia.execute("PRAGMA foreign_keys = ON");
            conexion.setAutoCommit(false);

            try {
                T resultado = operacion.ejecutar(conexion);
                conexion.commit();
                return resultado;
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        }
    }

    static PreparedStatement preparar(Connection conexion, String sql,
                                      Object... valores) throws SQLException {
        PreparedStatement consulta = conexion.prepareStatement(sql);

        try {
            for (int i = 0; i < valores.length; i++) {
                consulta.setObject(i + 1, valores[i]);
            }
            return consulta;
        } catch (SQLException error) {
            consulta.close();
            throw error;
        }
    }

    static int escribir(Connection conexion, String sql,
                        Object... valores) throws SQLException {
        try (var consulta = preparar(conexion, sql, valores)) {
            return consulta.executeUpdate();
        }
    }

    static long numero(Connection conexion, String sql,
                       Object... valores) throws SQLException {
        try (var consulta = preparar(conexion, sql, valores);
             var resultado = consulta.executeQuery()) {
            if (!resultado.next()) {
                throw new SQLException("No se encontró el registro solicitado.");
            }
            return resultado.getLong(1);
        }
    }

    static int nuevoId(Connection conexion) throws SQLException {
        return Math.toIntExact(numero(conexion, "SELECT last_insert_rowid()"));
    }

    static DefaultTableModel tabla(Connection conexion, String sql,
                                   Object... valores) throws SQLException {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        try (var consulta = preparar(conexion, sql, valores);
             var resultado = consulta.executeQuery()) {

            var columnas = resultado.getMetaData();
            int cantidad = columnas.getColumnCount();

            for (int i = 1; i <= cantidad; i++) {
                modelo.addColumn(columnas.getColumnLabel(i));
            }

            while (resultado.next()) {
                Object[] fila = new Object[cantidad];
                for (int i = 0; i < cantidad; i++) {
                    fila[i] = resultado.getObject(i + 1);
                }
                modelo.addRow(fila);
            }
        }
        return modelo;
    }

    static String requerido(String texto, String campo) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Completa " + campo);
        }
        return texto.strip();
    }
}