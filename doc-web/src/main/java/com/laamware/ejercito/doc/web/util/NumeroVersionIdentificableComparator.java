package com.laamware.ejercito.doc.web.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparador para objetos que implementen {@link NumeroVersionIdentificable}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 Issue #151 (SICDI-Controltech) feature-151
 */
public class NumeroVersionIdentificableComparator implements Comparator<NumeroVersionIdentificable>, Serializable {

    private static final long serialVersionUID = -7555008255207703877L;

    private final NumeroVersionComparator numeroVersionComparator = new NumeroVersionComparator();

    @Override
    public int compare(NumeroVersionIdentificable identificable1, NumeroVersionIdentificable identificable2) {
        final String codigo1 = identificable1.numeroVersion();
        final String codigo2 = identificable2.numeroVersion();
        return numeroVersionComparator.compare(codigo1, codigo2);
    }
}
