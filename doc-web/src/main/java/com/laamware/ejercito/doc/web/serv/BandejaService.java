package com.laamware.ejercito.doc.web.serv;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;

/**
 * Servicio para funciones asociadas a las bandejas.
 *
 * @author jgarcia@controltechcg.com
 * @since Jul 05, 2017
 */
/*
 * 2017-07-05 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
 * feature-115.
 */
@Service
public final class BandejaService {

    @Value(value = "${co.mil.imi.sicdi.bandejas.dias}")
    private int numeroDias;

    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Obtiene el número de días por defecto para la búsqueda de documentos en
     * las bandejas.
     *
     * @return Número de días.
     */
    public int getNumeroDias() {
        return numeroDias;
    }

    /**
     * Obtiene los documentos de la bandeja de apoyo y consulta de un usuario
     * para el rango de fechas indicado.
     *
     * @param login Login del usuario.
     * @param fechaInicial Fecha inicial del rango de búsqueda.
     * @param fechaFinal Fecha final del rango de búsqueda.
     * @return Lista de documentos.
     */
    public List<Documento> obtenerDocumentosBandejaApoyoConsulta(String login, Date fechaInicial, Date fechaFinal) {
        return documentoRepository.findBandejaConsulta(login, fechaInicial, fechaFinal);
    }

    /**
     * Obtiene el numero de registros de las bandejas de entrada por usuario
     *
     * @param login Login del usuario
     * @return Número de registros
     */
    public int obtenerCountBandejaEntrada(String login) {
        return documentoRepository.findBandejaEntradaCount(login);
    }

    /**
     * Obtiene los registros de las bandejas de entrada por usuario, de acuerdo
     * a la fila inicial y final.
     *
     * @param login Login del usuario
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<Documento> obtenerDocumentosBandejaEntrada(String login, int inicio, int fin) {
        return documentoRepository.findBandejaEntradaPaginado(login, inicio, fin);
    }

    /**
     * Obtiene los documentos de la bandeja de enviados de un usuario para el
     * rango de fechas indicado.
     *
     * @param login Login del usuario.
     * @param fechaInicial Fecha inicial del rango de búsqueda.
     * @param fechaFinal Fecha final del rango de búsqueda.
     * @return Lista de documentos.
     */
    public List<Documento> obtenerDocumentosBandejaEnviados(final String login, final Date fechaInicial,
            final Date fechaFinal) {
        /*
		 * 2017-07-25 jgarcia@controltechcg.com Issue #118 (SICDI-Controltech)
		 * hotfix-118: Corrección en la sentencia SQL de la bandeja de enviados,
		 * para que no presente documentos cuyo usuario asignado actual
		 * corresponda al usuario en sesión.
         */
        return documentoRepository.findBandejaEnviados(login, login, fechaInicial, fechaFinal);
    }

    /**
     * Obtiene los documentos de la bandeja en trámite de un usuario para el
     * rango de fechas indicado.
     *
     * @param login Login del usuario.
     * @param fechaInicial Fecha inicial del rango de búsqueda.
     * @param fechaFinal Fecha final del rango de búsqueda.
     * @return Lista de documentos.
     */
    public List<Documento> obtenerDocumentosBandejaTramite(String login, Date fechaInicial, Date fechaFinal) {
        return documentoRepository.findBandejaTramite(login, fechaInicial, fechaFinal);
    }

    /**
     * Obtiene el numero de registros de las bandejas de enviados por usuario y
     * fechas
     *
     * @param login Login del usuario
     * @param fechaInicial Fecha inicial del filtro
     * @param fechaFinal Fecha final del filtro
     * @return Número de registros
     */
    public int obtenerCountBandejaEnviados(String login, Date fechaInicial, Date fechaFinal) {
        /*
		 * 2017-07-25 jgarcia@controltechcg.com Issue #118 (SICDI-Controltech)
		 * hotfix-118: Corrección en la sentencia SQL de la bandeja de enviados,
		 * para que no presente documentos cuyo usuario asignado actual
		 * corresponda al usuario en sesión.
         */
        return documentoRepository.findBandejaEnviadosCount(login, fechaInicial, fechaFinal);
    }

    /**
     * btiene la lista de registros de las bandejas de enviados por usuario y
     * fechas paginado.
     *
     * @param login Login del usuario
     * @param fechaInicial Fecha inicial del filtro
     * @param fechaFinal Fecha final del filtro
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<Documento> obtenerDocumentosBandejaEnviados(String login, Date fechaInicial, Date fechaFinal, int inicio, int fin) {
        /*
		 * 2017-07-25 jgarcia@controltechcg.com Issue #118 (SICDI-Controltech)
		 * hotfix-118: Corrección en la sentencia SQL de la bandeja de enviados,
		 * para que no presente documentos cuyo usuario asignado actual
		 * corresponda al usuario en sesión.
         */

        return documentoRepository.findBandejaEnviadosPaginado(login, fechaInicial, fechaFinal, inicio, fin);
    }

    /**
     * Obtiene el numero de registros de las bandejas de tramites por usuario y
     * fechas
     *
     * @param login Login del usuario
     * @param fechaInicial Fecha inicial del filtro
     * @param fechaFinal Fecha final del filtro
     * @return Número de registros
     */
    public int obtenerCountBandejaTramite(String login, Date fechaInicial, Date fechaFinal) {
        return documentoRepository.findBandejaTramiteCount(login, fechaInicial, fechaFinal);
    }

    /**
     * btiene la lista de registros de las bandejas en tramite por usuario y 
     * fechas paginado.
     *
     * @param login Login del usuario
     * @param fechaInicial Fecha inicial del filtro
     * @param fechaFinal Fecha final del filtro
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<Documento> obtenerDocumentosBandejaTramite(String login, Date fechaInicial, Date fechaFinal, int inicio, int fin) {
        return documentoRepository.findBandejaTramitePaginado(login, fechaInicial, fechaFinal, inicio, fin);
    }
    
    /**
     * Obtiene el numero de registros de las bandejas de consulta por usuario y
     * fechas
     *
     * @param login Login del usuario
     * @param fechaInicial Fecha inicial del filtro
     * @param fechaFinal Fecha final del filtro
     * @return Número de registros
     */
    public int obtenerCountBandejaConsulta(String login, Date fechaInicial, Date fechaFinal) {
        return documentoRepository.findBandejaConsultaCount(login, fechaInicial, fechaFinal);
    }
    
    /**
     * btiene la lista de registros de las bandejas en consulta por usuario y 
     * fechas paginado.
     *
     * @param login Login del usuario
     * @param fechaInicial Fecha inicial del filtro
     * @param fechaFinal Fecha final del filtro
     * @param inicio Numero de registro inicial
     * @param fin Numero de registro final
     * @return Lista de documentos
     */
    public List<Documento> obtenerDocumentosBandejaConsulta(String login, Date fechaInicial, Date fechaFinal, int inicio, int fin) {
        return documentoRepository.findBandejaConsultaPaginado(login, fechaInicial, fechaFinal, inicio, fin);
    }
}
