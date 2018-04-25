package com.laamware.ejercito.doc.web.dto;

import java.io.Serializable;
import java.util.Objects;
import com.laamware.ejercito.doc.web.util.NumeroVersionIdentificable;

/**
 * DTO para la información de TRD (Series y Subseries documentales) con la
 * cuenta del número de documentos archivados.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 Issue #151 (SICDI-Controltech) feature-151
 */
public class TrdArchivoDocumentosDTO implements Serializable, NumeroVersionIdentificable {

    private static final long serialVersionUID = -580271577132760660L;

    private int id;
    private String codigo;
    private String nombre;
    private int numeroDocumentosArchivados;

    public TrdArchivoDocumentosDTO() {
    }

    public TrdArchivoDocumentosDTO(int id, String codigo, String nombre, int numeroDocumentosArchivados) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.numeroDocumentosArchivados = numeroDocumentosArchivados;
    }

    /*
     * 2018-04-25 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Identificación del campo tipo número de versión.
     */
    @Override
    public String numeroVersion() {
        return getCodigo();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumeroDocumentosArchivados() {
        return numeroDocumentosArchivados;
    }

    public void setNumeroDocumentosArchivados(int numeroDocumentosArchivados) {
        this.numeroDocumentosArchivados = numeroDocumentosArchivados;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.id;
        hash = 19 * hash + Objects.hashCode(this.codigo);
        hash = 19 * hash + Objects.hashCode(this.nombre);
        hash = 19 * hash + this.numeroDocumentosArchivados;
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
        final TrdArchivoDocumentosDTO other = (TrdArchivoDocumentosDTO) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.numeroDocumentosArchivados != other.numeroDocumentosArchivados) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return Objects.equals(this.nombre, other.nombre);
    }

    @Override
    public String toString() {
        return "TrdArchivoDocumentosDTO{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", numeroDocumentosArchivados=" + numeroDocumentosArchivados + '}';
    }
}
