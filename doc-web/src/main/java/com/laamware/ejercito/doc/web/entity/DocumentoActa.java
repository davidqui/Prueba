package com.laamware.ejercito.doc.web.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidad que describe un acta, con los valores adicionales a documento.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/15/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Entity
@Table(name = "DOCUMENTO_ACTA")
@SuppressWarnings("PersistenceUnitPresent")
public class DocumentoActa implements Serializable {

    private static final long serialVersionUID = -4546342192282132810L;

    @Id
    @Column(name = "DOC_ID")
    private String documento;

    @Column(name = "LUGAR")
    @Basic(optional = false)
    private String lugar;

    /**
     * Constructor vacío.
     */
    public DocumentoActa() {
    }

    /**
     * Constructor.
     *
     * @param documento ID del Documento asociado.
     */
    public DocumentoActa(String documento) {
        this.documento = documento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.documento);
        hash = 17 * hash + Objects.hashCode(this.lugar);
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
        final DocumentoActa other = (DocumentoActa) obj;
        if (!Objects.equals(this.documento, other.documento)) {
            return false;
        }
        return Objects.equals(this.lugar, other.lugar);
    }

    @Override
    public String toString() {
        return "DocumentoActa{" + "documento=" + documento + ", lugar=" + lugar + '}';
    }

}
