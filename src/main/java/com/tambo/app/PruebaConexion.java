package com.tambo.app;

import com.tambo.compartido.ConexionSQLite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PruebaConexion {

    public static void main(String[] args) {

        try (
                Connection conexion = ConexionSQLite.conectar();
                Statement stmt = conexion.createStatement();
                ResultSet resultado = stmt.executeQuery(
                        "SELECT COUNT(*) FROM productos"
                )
        ) {

            System.out.println("Conexión exitosa con SQLite");

            if (resultado.next()) {
                int cantidad = resultado.getInt(1);

                System.out.println(
                        "Productos registrados: " + cantidad
                );
            }

        } catch (Exception e) {
            System.out.println(
                    "Error de conexión: " + e.getMessage()
            );
        }
    }
}