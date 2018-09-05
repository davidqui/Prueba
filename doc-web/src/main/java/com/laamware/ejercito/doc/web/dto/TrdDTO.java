
package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author egonzalezm
 * @since 1.8
 * @version 08/09/2018
 */
public class TrdDTO implements Serializable{

    private static final long serialVersionUID = 3588742000993005171L;
    
    private int trdId;
    private String trdNombre;
    private String trdCodigo;
    private int cantidad;

    public TrdDTO() {
    }

    public TrdDTO(int trdId, String trdNombre, String trdCodigo, int cantidad) {
        this.trdId = trdId;
        this.trdNombre = trdNombre;
        this.trdCodigo = trdCodigo;
        this.cantidad = cantidad;
    }

    public int getTrdId() {
        return trdId;
    }

    public void setTrdId(int trdId) {
        this.trdId = trdId;
    }

    public String getTrdNombre() {
        return trdNombre;
    }

    public void setTrdNombre(String trdNombre) {
        this.trdNombre = trdNombre;
    }

    public String getTrdCodigo() {
        return trdCodigo;
    }

    public void setTrdCodigo(String trdCodigo) {
        this.trdCodigo = trdCodigo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "TrdDTO{" + "trdId=" + trdId + ", trdNombre=" + trdNombre + ", trdCodigo=" + trdCodigo + ", cantidad=" + cantidad + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.trdId;
        hash = 97 * hash + Objects.hashCode(this.trdNombre);
        hash = 97 * hash + Objects.hashCode(this.trdCodigo);
        hash = 97 * hash + this.cantidad;
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
        final TrdDTO other = (TrdDTO) obj;
        if (this.trdId != other.trdId) {
            return false;
        }
        if (this.cantidad != other.cantidad) {
            return false;
        }
        if (!Objects.equals(this.trdNombre, other.trdNombre)) {
            return false;
        }
        if (!Objects.equals(this.trdCodigo, other.trdCodigo)) {
            return false;
        }
        return true;
    }
    
    
}
