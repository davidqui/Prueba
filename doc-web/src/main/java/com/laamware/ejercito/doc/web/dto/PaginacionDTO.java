package com.laamware.ejercito.doc.web.dto;

/**
 *
 * @author Usuario
 */
public class PaginacionDTO {
    private int totalPages;
    private int inicio;
    private int fin;

    public PaginacionDTO(int totalPages, int inicio, int fin) {
        this.totalPages = totalPages;
        this.inicio = inicio;
        this.fin = fin;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getInicio() {
        return inicio;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }
}
