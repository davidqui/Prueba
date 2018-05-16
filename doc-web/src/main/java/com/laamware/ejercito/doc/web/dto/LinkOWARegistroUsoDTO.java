package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * DTO para la obtención de información para el registro de uso del enlace al
 * OWA.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
public class LinkOWARegistroUsoDTO implements Serializable {

    private static final long serialVersionUID = -7931177837496341261L;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.url);
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
        final LinkOWARegistroUsoDTO other = (LinkOWARegistroUsoDTO) obj;
        return Objects.equals(this.url, other.url);
    }

    @Override
    public String toString() {
        return "LinkOWARegistroUsoDTO{" + "url=" + url + '}';
    }

}
