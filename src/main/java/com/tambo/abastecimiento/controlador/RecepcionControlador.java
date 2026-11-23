package com.tambo.abastecimiento.controlador;

import com.tambo.abastecimiento.*;
import com.tambo.abastecimiento.modelo.Recepcion;
import com.tambo.abastecimiento.servicio.PublicadorEventosRecepcion;
import com.tambo.abastecimiento.servicio.RecepcionServicio;
import com.tambo.abastecimiento.vista.DetalleRecepcionTablaModelo;

import java.sql.SQLException;

public final class RecepcionControlador {
    private final RecepcionServicio servicio;

    public RecepcionControlador(PublicadorEventosRecepcion eventos) {
        servicio = new RecepcionServicio(eventos);
    }

    public int guardar(String referencia, int ordenId,
                       DetalleRecepcionTablaModelo modelo) throws SQLException {

        String numero = AccesoDatos.requerido(
                referencia, "el número de recepción");

        if (ordenId < 1) {
            throw new IllegalArgumentException(
                    "El ID de la orden debe ser mayor a cero.");
        }

        Recepcion recepcion = new Recepcion(numero, ordenId, modelo.detalles());
        return servicio.confirmar(recepcion, Formulario.usuario());
    }
}