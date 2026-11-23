package com.tambo.abastecimiento.modelo;

public final class EventoRecepcionConfirmada {
    private final int recepcionId;
    private final int ordenId;
    private final String numero;

    public EventoRecepcionConfirmada(int recepcionId, int ordenId, String numero) {
        this.recepcionId = recepcionId;
        this.ordenId = ordenId;
        this.numero = numero;
    }

    public int recepcionId() {
        return recepcionId;
    }

    public int ordenId() {
        return ordenId;
    }

    public String numero() {
        return numero;
    }
}
