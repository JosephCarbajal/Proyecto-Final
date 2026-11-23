package com.tambo.abastecimiento.controlador;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.vista.DetalleOrdenCompraTablaModelo;
import com.tambo.abastecimiento.Formulario;
import com.tambo.abastecimiento.servicio.OrdenCompraServicio;
import com.tambo.abastecimiento.modelo.OrdenCompra;

import java.sql.SQLException;

public final class OrdenCompraControlador {
    private final OrdenCompraServicio servicio = new OrdenCompraServicio();

    public int guardar(String referencia, int proveedorId,
                       DetalleOrdenCompraTablaModelo modelo) throws SQLException {

        String numero = AccesoDatos.requerido(referencia, "el número de orden");

        OrdenCompra orden = new OrdenCompra(
                numero, proveedorId, modelo.detalles());

        return servicio.crear(orden, Formulario.usuario());
    }
}
