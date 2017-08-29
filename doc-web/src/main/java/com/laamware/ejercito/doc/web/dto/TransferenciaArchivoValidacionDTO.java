package com.laamware.ejercito.doc.web.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * DTO de validación para el proceso de transferencia de archivo.
 *
 * @author jgarcia@controltechcg.com
 * @since Ago 28, 2017
 * @version 1.0.0 (feature-120).
 */
public final class TransferenciaArchivoValidacionDTO implements Iterable<String> {

    /**
     * Lista de errores.
     */
    private final List<String> errors;

    /**
     * Constructor.
     */
    public TransferenciaArchivoValidacionDTO() {
        errors = new ArrayList<>();
    }

    @Override
    public Iterator<String> iterator() {
        return errors.iterator();
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        errors.forEach(action);
    }

    @Override
    public Spliterator<String> spliterator() {
        return errors.spliterator();
    }

    /**
     * Adiciona un nuevo error a la validación.
     *
     * @param error Mensaje del error.
     */
    public void addError(String error) {
        errors.add(error);
    }

    /**
     * Indica si el resultado de la validación es positivo o no.
     *
     * @return {@code true} si la validación tiene un resultado positivo, sin
     * errores; de lo contrario, {@code false}.
     */
    public boolean isOK() {
        return errors.isEmpty();
    }
}
