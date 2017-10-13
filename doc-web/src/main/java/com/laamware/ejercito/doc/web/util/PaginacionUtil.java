package com.laamware.ejercito.doc.web.util;

import com.laamware.ejercito.doc.web.dto.PaginacionDTO;

/**
 *
 * @author Usuario
 */
public class PaginacionUtil {

    /**
     * Tamaño de la lista de presentación en la página de búsqueda de usuarios.
     */
    private static final int BUSQUEDA_PAGE_SIZE = 10;

    public static PaginacionDTO retornaParametros(int count, int pageIndex) {
        if (count > 0) {
            int inicio = 0;
            int totalPages = (int) Math.ceil((double) count / BUSQUEDA_PAGE_SIZE) - 1;
            int fin = inicio + BUSQUEDA_PAGE_SIZE;

            if (pageIndex > 0) {
                inicio = (pageIndex * BUSQUEDA_PAGE_SIZE) + 1;
                fin = (inicio + BUSQUEDA_PAGE_SIZE) - 1;
            }
            if (count < BUSQUEDA_PAGE_SIZE) {
                fin = count;
            }
            return new PaginacionDTO(totalPages, inicio, fin);
        }
        return null;
    }
}
