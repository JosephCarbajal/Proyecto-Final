package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.AccesoDatos;
import com.tambo.abastecimiento.validacion.OrdenCompraValidador;
import com.tambo.abastecimiento.modelo.DetalleOrdenCompra;
import com.tambo.abastecimiento.modelo.OrdenCompra;
import com.tambo.abastecimiento.repositorio.DetalleOrdenCompraSQLiteRepository;
import com.tambo.abastecimiento.repositorio.OrdenCompraSQLiteRepository;
import com.tambo.abastecimiento.repositorio.ProductoProveedorSQLiteRepository;

import java.sql.SQLException;

public final class OrdenCompraServicio {
    private final OrdenCompraValidador validador =
            new OrdenCompraValidador();

    private final OrdenCompraSQLiteRepository ordenes =
            new OrdenCompraSQLiteRepository();

    private final DetalleOrdenCompraSQLiteRepository detalles =
            new DetalleOrdenCompraSQLiteRepository();

    private final ProductoProveedorSQLiteRepository catalogo =
            new ProductoProveedorSQLiteRepository();

    private final AuditoriaAbastecimientoServicio auditoria =
            new AuditoriaAbastecimientoServicio();

    public int crear(OrdenCompra orden, String usuario) throws SQLException {
        validador.validar(orden);

        return AccesoDatos.operar(conexion -> {
            int ordenId = ordenes.crear(conexion, orden);
            long totalCentimos = 0;

            for (DetalleOrdenCompra detalle : orden.detalles()) {
                long costoUnitario = catalogo.costo(
                        conexion, orden.proveedorId(), detalle.productoId());

                long subtotal = detalles.insertar(
                        conexion, ordenId, detalle, costoUnitario);

                totalCentimos = Math.addExact(totalCentimos, subtotal);
            }

            ordenes.fijarTotal(conexion, ordenId, totalCentimos);

            auditoria.registrar(conexion, "ordenes_compra", ordenId,
                    "CREAR", usuario, orden.numero());

            return ordenId;
        });
    }
}