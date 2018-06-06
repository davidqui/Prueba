package com.laamware.ejercito.doc.web.dto;

import java.io.File;
import java.io.Serializable;
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
    private String copiaDestino;
    
    private String asunto;
    
    private String cabecera;
    private String cuerpo;
    private String piePagina;
    
    private File adjunto;

    public EmailDTO(String remitente, String destino, String copiaDestino, String asunto, String cabecera, String cuerpo, String piePagina) {
        this.remitente = remitente;
        this.destino = destino;
        this.copiaDestino = copiaDestino;
        this.asunto = asunto;
        this.cabecera = cabecera;
        this.cuerpo = cuerpo;
        this.piePagina = piePagina;
    }

    public String getRemitente() {
        return remitente;
    }

    public String getDestino() {
        return destino;
    }

    public String getCopiaDestino() {
        return copiaDestino;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getCabecera() {
        return cabecera;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public String getPiePagina() {
        return piePagina;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public void setCopiaDestino(String copiaDestino) {
        this.copiaDestino = copiaDestino;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setCabecera(String cabecera) {
        this.cabecera = cabecera;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
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
        return "EmailDTO{" + "remitente=" + remitente + ", destino=" + destino + ", copiaDestino=" + copiaDestino + ", asunto=" + asunto + ", cabecera=" + cabecera + ", cuerpo=" + cuerpo + ", piePagina=" + piePagina + '}';
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
        if (!Objects.equals(this.copiaDestino, other.copiaDestino)) {
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
        return Objects.equals(this.piePagina, other.piePagina);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.remitente);
        hash = 79 * hash + Objects.hashCode(this.destino);
        hash = 79 * hash + Objects.hashCode(this.copiaDestino);
        hash = 79 * hash + Objects.hashCode(this.asunto);
        hash = 79 * hash + Objects.hashCode(this.cabecera);
        hash = 79 * hash + Objects.hashCode(this.cuerpo);
        hash = 79 * hash + Objects.hashCode(this.piePagina);
        return hash;
    }
    
    
}
