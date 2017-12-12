package com.laamware.ejercito.doc.web.dto;

import java.util.Objects;

/**
 *
 * @author egonzalezm
 */
public class ReporteDependenciaDTO {
 
    private String siglaDependencia;
    private String nombreDependencia;
    private Integer cantidad;

    public ReporteDependenciaDTO(String siglaDependencia, String nombreDependencia, Integer cantidad) {
        this.siglaDependencia = siglaDependencia;
        this.nombreDependencia = nombreDependencia;
        this.cantidad = cantidad;
    }

    
    public String getSiglaDependencia() {
        return siglaDependencia;
    }

    public void setSiglaDependencia(String siglaDependencia) {
        this.siglaDependencia = siglaDependencia;
    }

    public String getNombreDependencia() {
        return nombreDependencia;
    }

    public void setNombreDependencia(String nombreDependencia) {
        this.nombreDependencia = nombreDependencia;
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
        hash = 89 * hash + Objects.hashCode(this.siglaDependencia);
        hash = 89 * hash + Objects.hashCode(this.nombreDependencia);
        hash = 89 * hash + Objects.hashCode(this.cantidad);
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
        final ReporteDependenciaDTO other = (ReporteDependenciaDTO) obj;
        if (!Objects.equals(this.siglaDependencia, other.siglaDependencia)) {
            return false;
        }
        if (!Objects.equals(this.nombreDependencia, other.nombreDependencia)) {
            return false;
        }
        if (!Objects.equals(this.cantidad, other.cantidad)) {
            return false;
        }
        return true;
    }
    
    
    
}
