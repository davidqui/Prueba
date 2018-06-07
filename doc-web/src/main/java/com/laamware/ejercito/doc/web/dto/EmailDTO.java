package com.laamware.ejercito.doc.web.dto;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * DTO para el envío de los correos electrónico de notificación
 * @author Samuel Delgado Muñoz samuel.delgado@controltechcg.com
 * @since 1.8
 * @version 04/25/2018 (SICDI-Controltech Issue #169 feature-169)
 */
public class EmailDTO implements Serializable{

    private static final long serialVersionUID = 5125740897829353319L;
    
    private String remitente;
    private String destino;
    private List<String> copiaDestino;
    
    private String asunto;
    
    private String cabecera;
    private String cuerpo;
    private String piePagina;
    
    private File adjunto;

    public EmailDTO(String remitente, String destino, List<String> copiaDestino, String asunto, String cabecera, String cuerpo, String piePagina, File adjunto) {
        this.remitente = remitente;
        this.destino = destino;
        this.copiaDestino = copiaDestino;
        this.asunto = asunto;
        this.cabecera = cabecera;
        this.cuerpo = cuerpo;
        this.piePagina = piePagina;
        this.adjunto = adjunto;
    }
    

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public List<String> getCopiaDestino() {
        return copiaDestino;
    }

    public void setCopiaDestino(List<String> copiaDestino) {
        this.copiaDestino = copiaDestino;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCabecera() {
        return cabecera;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setPiePagina(String piePagina) {
        this.piePagina = piePagina;
    }

    public File getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(File adjunto) {
        this.adjunto = adjunto;
    }

    @Override
    public String toString() {
        return "EmailDTO{" + "remitente=" + remitente + ", destino=" + destino + ", copiaDestino=" + copiaDestino + ", asunto=" + asunto + ", cabecera=" + cabecera + ", cuerpo=" + cuerpo + ", piePagina=" + piePagina + ", adjunto=" + adjunto + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.remitente);
        hash = 23 * hash + Objects.hashCode(this.destino);
        hash = 23 * hash + Objects.hashCode(this.copiaDestino);
        hash = 23 * hash + Objects.hashCode(this.asunto);
        hash = 23 * hash + Objects.hashCode(this.cabecera);
        hash = 23 * hash + Objects.hashCode(this.cuerpo);
        hash = 23 * hash + Objects.hashCode(this.piePagina);
        hash = 23 * hash + Objects.hashCode(this.adjunto);
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
        final EmailDTO other = (EmailDTO) obj;
        if (!Objects.equals(this.remitente, other.remitente)) {
            return false;
        }
        if (!Objects.equals(this.destino, other.destino)) {
            return false;
        }
        if (!Objects.equals(this.asunto, other.asunto)) {
            return false;
        }
        if (!Objects.equals(this.cabecera, other.cabecera)) {
            return false;
        }
        if (!Objects.equals(this.cuerpo, other.cuerpo)) {
            return false;
        }
        if (!Objects.equals(this.piePagina, other.piePagina)) {
            return false;
        }
        if (!Objects.equals(this.copiaDestino, other.copiaDestino)) {
            return false;
        }
        if (!Objects.equals(this.adjunto, other.adjunto)) {
            return false;
        }
        return true;
    }
    
}
