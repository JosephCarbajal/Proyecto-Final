package com.tambo.abastecimiento.validacion;

import com.tambo.abastecimiento.modelo.DetalleRecepcion;
import com.tambo.abastecimiento.modelo.ResultadoVerificacionRecepcion;
import com.tambo.abastecimiento.modelo.SaldoPendientePorProducto;

public final class VerificadorCantidadPendiente {

    public ResultadoVerificacionRecepcion verificar(
            DetalleRecepcion detalle, SaldoPendientePorProducto saldo) {

        if (detalle.productoId() != saldo.productoId()) {
            throw new IllegalArgumentException(
                    "El saldo corresponde a otro producto.");
        }

        if (detalle.recibidas() < 1) {
            throw new IllegalArgumentException(
                    "La cantidad recibida debe ser mayor a cero.");
        }

        if (detalle.aceptadas() < 0 || detalle.aceptadas() > detalle.recibidas()) {
            throw new IllegalArgumentException(
                    "La cantidad aceptada debe estar entre cero y la cantidad recibida.");
        }

        if (detalle.aceptadas() > saldo.pendiente()) {
            throw new IllegalArgumentException(
                    "Solo quedan " + saldo.pendiente() + " unidades pendientes.");
        }

        int rechazadas = detalle.recibidas() - detalle.aceptadas();
        int saldoRestante = saldo.pendiente() - detalle.aceptadas();

        return new ResultadoVerificacionRecepcion(
                detalle.aceptadas(), rechazadas, saldoRestante);
    }
}
