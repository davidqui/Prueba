package com.laamware.ejercito.doc.web.util;

import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.entity.Usuario;
import java.util.Comparator;

/**
 * Clase que se encarga de organizar una lista de usuarios, segun el peso del
 * grado
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 (SICDI-Controltech Issue #156 feature-156)
 */
public final class UsuarioGradoComparator implements Comparator<Usuario> {

    private final class GradoComparator implements Comparator<Grados> {

        @Override
        public int compare(Grados grado1, Grados grado2) {
            return grado2.getPesoOrden().compareTo(grado1.getPesoOrden());
        }
    }

    private final GradoComparator gradoComparator = new GradoComparator();

    @Override
    public int compare(Usuario usuario1, Usuario usuario2) {
        return gradoComparator.compare(usuario1.getUsuGrado(), usuario2.getUsuGrado());
    }

}
