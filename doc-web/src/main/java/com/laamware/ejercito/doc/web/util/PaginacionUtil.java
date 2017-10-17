package com.laamware.ejercito.doc.web.util;

import com.laamware.ejercito.doc.web.dto.PaginacionDTO;

/**
 * Util para ayuda de paginacion de las bandejas.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Oct 17, 2017
 * @version 1.0.0 (feature-132).
 */
public class PaginacionUtil {

    /**
     * Tamaño de la lista de presentación en la página de búsqueda de usuarios.
     */
    private static final int BUSQUEDA_PAGE_SIZE = 10;

    /**
     * Se realiza el calculo de los atributos del objeto {@link PaginacionDTO}
     *
     * @param count
     * @param pageIndex
     * @return PaginacionDTO
     */
    public static PaginacionDTO retornaParametros(int count, int pageIndex) {
        if (count > 0) {
            int inicio = 1;
            int fin = BUSQUEDA_PAGE_SIZE;
            int totalPages = (int) Math.ceil((double) count / BUSQUEDA_PAGE_SIZE);

            if (pageIndex > 1) {
                inicio = ((pageIndex - 1) * BUSQUEDA_PAGE_SIZE) + 1;
                fin = (inicio + BUSQUEDA_PAGE_SIZE) - 1;
            }
            if (count < BUSQUEDA_PAGE_SIZE) {
                fin = count;
            }
            return new PaginacionDTO(totalPages, inicio, fin, "Mostrando " + inicio + " a " + fin + " de " + count + " registros");
        }
        return null;
    }
}
