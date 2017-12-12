package com.laamware.ejercito.doc.web.dto;

import java.util.Objects;

/**
 *
 * @author egonzalezm
 */
public class ReporteDependenciaTrdDTO {

    private String trdNombre;
    private Integer cantidad;

    public ReporteDependenciaTrdDTO(String trdNombre, Integer cantidad) {
        this.trdNombre = trdNombre;
        this.cantidad = cantidad;
    }

    public String getTrdNombre() {
        return trdNombre;
    }

    public void setTrdNombre(String trdNombre) {
        this.trdNombre = trdNombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.trdNombre);
        hash = 59 * hash + Objects.hashCode(this.cantidad);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ReporteDependenciaTrdDTO other = (ReporteDependenciaTrdDTO) obj;
        if (!Objects.equals(this.trdNombre, other.trdNombre)) {
            return false;
        }
        if (!Objects.equals(this.cantidad, other.cantidad)) {
            return false;
        }
        return true;
    }
}
