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
	 * Obtiene los documentos de la bandeja de enviados de un usuario para el
	 * rango de fechas indicado.
	 * 
	 * @param login
	 *            Login del usuario.
	 * @param fechaInicial
	 *            Fecha inicial del rango de búsqueda.
	 * @param fechaFinal
	 *            Fecha final del rango de búsqueda.
	 * @return Lista de documentos.
	 */
	public List<Documento> obtenerDocumentosBandejaEnviados(final String login, final Date fechaInicial,
			final Date fechaFinal) {
		return documentoRepository.findBandejaEnviados(login, fechaInicial, fechaFinal);
	}

	/**
	 * Obtiene los documentos de la bandeja en trámite de un usuario para el
	 * rango de fechas indicado.
	 * 
	 * @param login
	 *            Login del usuario.
	 * @param fechaInicial
	 *            Fecha inicial del rango de búsqueda.
	 * @param fechaFinal
	 *            Fecha final del rango de búsqueda.
	 * @return Lista de documentos.
	 */
	public List<Documento> obtenerDocumentosBandejaTramite(String login, Date fechaInicial, Date fechaFinal) {
		return documentoRepository.findBandejaTramite(login, fechaInicial, fechaFinal);
	}

}
