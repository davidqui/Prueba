package com.laamware.ejercito.doc.web.dto;

/**
 * DTO para ayuda de paginacion de las bandejas.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Oct 17, 2017
 * @version 1.0.0 (feature-132).
 */
public class PaginacionDTO {
    
    /**
     * Numero total de paginas.
     */
    private int totalPages;
    /**
     * Numero del registro inicial.
     */
    private int registroInicio;
    /**
     * Numero del registro Final.
     */
    private int registroFin;

    /**
     * Constructor.
     * @param totalPages
     * @param registroInicio
     * @param registroFin
     */
    public PaginacionDTO(int totalPages, int registroInicio, int registroFin) {
        this.totalPages = totalPages;
        this.registroInicio = registroInicio;
        this.registroFin = registroFin;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRegistroInicio() {
        return registroInicio;
    }

    public void setRegistroInicio(int registroInicio) {
        this.registroInicio = registroInicio;
    }

    public int getRegistroFin() {
        return registroFin;
    }

    public void setRegistroFin(int registroFin) {
        this.registroFin = registroFin;
    }
}