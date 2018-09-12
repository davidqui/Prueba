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
     * Se realiza el calculo de los atributos del objeto {@link PaginacionDTO}
     *
     * @param count
     * @param pageIndex
     * @param pageSize
     * @return PaginacionDTO
     */
    public static PaginacionDTO retornaParametros(int count, int pageIndex, int pageSize) {
        int inicio = 1;
        int fin = pageSize;
        int totalPages = 0;
        if (pageIndex > 1) {
            inicio = ((pageIndex - 1) * pageSize) + 1;
            fin = (inicio + pageSize) - 1;
        }
        if (count > 0) {
            totalPages = (int) Math.ceil((double) count / pageSize);


            if (fin > count) {
                fin = count;
            }
        }
        return new PaginacionDTO(totalPages, inicio, fin, "Mostrando " + inicio + " a " + fin + " de " + count + " registros");
    }
    
}
