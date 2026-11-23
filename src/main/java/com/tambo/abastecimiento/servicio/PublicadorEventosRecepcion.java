package com.tambo.abastecimiento.servicio;

import com.tambo.abastecimiento.modelo.EventoRecepcionConfirmada;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.SwingUtilities;

public final class PublicadorEventosRecepcion {
    private final PropertyChangeSupport eventos = new PropertyChangeSupport(this);

    public void escuchar(PropertyChangeListener oyente) {
        eventos.addPropertyChangeListener(oyente);
    }

    public void publicar(EventoRecepcionConfirmada evento) {
        SwingUtilities.invokeLater(() ->
                eventos.firePropertyChange("recepcion", null, evento)
        );
    }
}