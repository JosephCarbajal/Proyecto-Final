package com.tambo.compartido;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConexionSQLite {

    private static final Path RUTA =
            Path.of("database", "tambo_demo.db")
                    .toAbsolutePath();

    public static Connection conectar() throws SQLException {

        if (!Files.isRegularFile(RUTA)) {
            throw new SQLException(
                    "No se encontró la base de datos: " + RUTA
            );
        }

        String url = "jdbc:sqlite:" + RUTA;

        return DriverManager.getConnection(url);
    }
}