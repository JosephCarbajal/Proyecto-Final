package com.tambo.abastecimiento.validacion;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.modelo.DetalleOrdenCompra;
import com.tambo.abastecimiento.modelo.OrdenCompra;

import java.util.HashSet;
import java.util.Set;

public final class OrdenCompraValidador {

    public void validar(OrdenCompra orden) {
        AccesoDatos.requerido(orden.numero(), "el número de la orden");

        if (orden.proveedorId() < 1) {
            throw new IllegalArgumentException("Ingresa un ID de proveedor válido.");
        }

        if (orden.detalles().isEmpty()) {
            throw new IllegalArgumentException("Agrega al menos un producto a la orden.");
        }

        Set<Integer> productos = new HashSet<>();

        for (DetalleOrdenCompra detalle : orden.detalles()) {
            if (detalle.productoId() < 1) {
                throw new IllegalArgumentException("Ingresa un ID de producto válido.");
            }

            if (detalle.cantidad() < 1) {
                throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
            }

            if (!productos.add(detalle.productoId())) {
                throw new IllegalArgumentException(
                        "El producto " + detalle.productoId() + " está repetido en la orden.");
            }
        }
    }
}