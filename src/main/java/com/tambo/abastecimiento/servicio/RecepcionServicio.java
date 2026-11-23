package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.*;
import com.tambo.abastecimiento.modelo.DetalleRecepcion;
import com.tambo.abastecimiento.modelo.EventoRecepcionConfirmada;
import com.tambo.abastecimiento.modelo.Recepcion;
import com.tambo.abastecimiento.repositorio.ConsultaOrdenesPendientes;
import com.tambo.abastecimiento.repositorio.DetalleRecepcionSQLiteRepository;
import com.tambo.abastecimiento.repositorio.RecepcionSQLiteRepository;
import com.tambo.abastecimiento.validacion.VerificadorCantidadPendiente;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public final class RecepcionServicio {
    private final PublicadorEventosRecepcion eventos;
    private final RecepcionSQLiteRepository recepciones =
            new RecepcionSQLiteRepository();
    private final DetalleRecepcionSQLiteRepository detalles =
            new DetalleRecepcionSQLiteRepository();
    private final ConsultaOrdenesPendientes pendientes =
            new ConsultaOrdenesPendientes();
    private final VerificadorCantidadPendiente verificador =
            new VerificadorCantidadPendiente();
    private final AdaptadorEntradaInventario inventario =
            new AdaptadorEntradaInventario();
    private final ActualizadorEstadoOrdenCompra estadoOrden =
            new ActualizadorEstadoOrdenCompra();
    private final AuditoriaAbastecimientoServicio auditoria =
            new AuditoriaAbastecimientoServicio();

    public RecepcionServicio(PublicadorEventosRecepcion eventos) {
        this.eventos = eventos;
    }

    public int confirmar(Recepcion recepcion, String usuario) throws SQLException {
        AccesoDatos.requerido(recepcion.numero(), "el número de recepción");
        AccesoDatos.requerido(usuario, "el usuario");

        if (recepcion.detalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "Agrega por lo menos un producto recibido.");
        }

        recepciones.habilitarParciales();

        int recepcionId = AccesoDatos.operar(conexion -> {
            int id = recepciones.crear(conexion, recepcion);
            Set<Integer> productosRevisados = new HashSet<>();

            for (DetalleRecepcion detalle : recepcion.detalles()) {
                int productoId = detalle.productoId();

                if (!productosRevisados.add(productoId)) {
                    throw new IllegalArgumentException(
                            "El producto " + productoId + " está repetido en la recepción.");
                }

                var saldo = pendientes.buscar(
                        conexion, recepcion.ordenId(), productoId);
                var resultado = verificador.verificar(detalle, saldo);

                detalles.registrar(conexion, id, detalle, saldo.costo());

                if (resultado.aceptadas() > 0) {
                    inventario.ingresar(conexion, productoId,
                            resultado.aceptadas(), recepcion.numero(), usuario);
                }

                String resumen = "Producto " + productoId
                        + ", recibidas " + detalle.recibidas()
                        + ", aceptadas " + resultado.aceptadas()
                        + ", rechazadas " + resultado.rechazadas();

                auditoria.registrar(conexion, "recepciones", id,
                        "VERIFICAR", usuario, resumen);
            }

            if (estadoOrden.actualizar(conexion, recepcion.ordenId())) {
                recepciones.completar(conexion, id);
            }

            return id;
        });

        eventos.publicar(new EventoRecepcionConfirmada(
                recepcionId, recepcion.ordenId(), recepcion.numero()));

        return recepcionId;
    }
}