package com.laamware.ejercito.doc.web.util;

import com.laamware.ejercito.doc.web.entity.UsuarioXDocumentoActa;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Comparador para {@link UsuarioXDocumentoActa}. Ordena la informción a
 * presentar a partir del grado del usuario y luego por el nombre del usuario.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/30/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
public final class UsuarioXDocumentoActaComparator implements Comparator<UsuarioXDocumentoActa>, Serializable {

    private static final long serialVersionUID = -1105648600276038021L;

    @Override
    public int compare(UsuarioXDocumentoActa usuarioXDocumentoActa1, UsuarioXDocumentoActa usuarioXDocumentoActa2) {
        final Integer pesoOrden1 = usuarioXDocumentoActa1.getUsuario().getUsuGrado().getPesoOrden();
        final Integer pesoOrden2 = usuarioXDocumentoActa2.getUsuario().getUsuGrado().getPesoOrden();

        int compare = pesoOrden2.compareTo(pesoOrden1);
        if (compare != 0) {
            return compare;
        }

        return usuarioXDocumentoActa1.getUsuario().getNombre().compareTo(usuarioXDocumentoActa1.getUsuario().getNombre());
    }

}
