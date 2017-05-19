package com.laamware.ejercito.doc.web.serv;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoEnConsulta;
import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoEnConsultaRepository;

/**
 * Servicio para las operaciones de documento en consulta.
 * 
 * @author jgarcia@controltechcg.com
 * @since Abril 20, 2017
 *
 */
// 2017-04-20 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
@Service
public class DocumentoEnConsultaService {

	@Autowired
	private DocumentoEnConsultaRepository documentoEnConsultaRepository;

	/**
	 * Busca el registro en consulta activo para un documento.
	 * 
	 * @param documento
	 *            Documento.
	 * @return Registro en consulta activo del documento, o {@code null} si no
	 *         hay un registro correspondiente.
	 */
	public DocumentoEnConsulta buscarActivo(final Documento documento) {
		return documentoEnConsultaRepository.findByDocumentoAndActivoTrue(documento);
	}

	/**
	 * Valida si el usuario puede enviar el documento a la bandeja de consulta.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 * @return {@code true} si el usuario puede enviar el documento a la bandeja
	 *         de consulta; de lo contrario, {@code false}
	 */
	public boolean validarEnviarABandejaConsulta(final Usuario usuario, final Documento documento) {
		final Instancia instancia = documento.getInstancia();
		final Proceso proceso = instancia.getProceso();

		if (proceso.getId().equals(Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
			return validarEnviarABandejaConsultaParaProcesoRegistroDocumentos(usuario, documento);
		}

		/*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
		 * hotfix-81 -> Corrección para que los documentos del proceso externo
		 * no tengan acciones ni transiciones después de enviados.
		 */
		if (proceso.getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
			return false;
		}

		return validarEnviarABandejaConsultaParaProcesoGeneracionDocumentos(usuario, documento);

	}

	/**
	 * Valida si el usuario puede extraer el documento de la bandeja de consulta
	 * y enviarlo a la bandeja de entrada nuevamente.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 * @return {@code true} si el usuario puede extraer el documento de la
	 *         bandeja de consulta; de lo contrario, {@code false}.
	 */
	public boolean validarExtraerDeBandejaConsulta(final Usuario usuario, final Documento documento) {
		final Instancia instancia = documento.getInstancia();
		final Proceso proceso = instancia.getProceso();

		if (proceso.getId() == Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS) {
			return validarExtraerDeBandejaConsultaParaProcesoRegistroDocumentos(usuario, documento);
		}

		/*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #81 (SICDI-Controltech):
		 * hotfix-81 -> Corrección para que los documentos del proceso externo
		 * no tengan acciones ni transiciones después de enviados.
		 */
		if (proceso.getId().equals(Proceso.ID_TIPO_PROCESO_GENERAR_DOCUMENTOS_PARA_ENTES_EXTERNOS_O_PERSONAS)) {
			return false;
		}

		return validarExtraerDeBandejaConsultaParaProcesoGeneracionDocumentos(usuario, documento);

	}

	/**
	 * Envia el documento a la bandeja de apoyo y consulta.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 */
	public void enviarABandejaConsulta(final Usuario usuario, final Documento documento) {
		final Date fechaSistema = new Date(System.currentTimeMillis());
		final Integer usuarioID = usuario.getId();

		DocumentoEnConsulta documentoEnConsulta = documentoEnConsultaRepository.findByDocumento(documento);

		if (documentoEnConsulta == null) {
			documentoEnConsulta = new DocumentoEnConsulta();
			documentoEnConsulta.setDocumento(documento);
		}

		documentoEnConsulta.setActivo(true);
		documentoEnConsulta.setCuando(fechaSistema);
		documentoEnConsulta.setCuandoMod(fechaSistema);
		documentoEnConsulta.setQuien(usuarioID);
		documentoEnConsulta.setQuienMod(usuarioID);

		documentoEnConsultaRepository.saveAndFlush(documentoEnConsulta);
	}

	/**
	 * Extrae el documento de la bandeja de apoyo y consulta y lo coloca de
	 * nuevo en la bandeja de entrada.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 */
	public void extraerDeBandejaConsulta(final Usuario usuario, final Documento documento) {
		final Date fechaSistema = new Date(System.currentTimeMillis());
		final Integer usuarioID = usuario.getId();

		DocumentoEnConsulta documentoEnConsulta = documentoEnConsultaRepository.findByDocumento(documento);

		documentoEnConsulta.setActivo(false);
		documentoEnConsulta.setCuandoMod(fechaSistema);
		documentoEnConsulta.setQuienMod(usuarioID);

		documentoEnConsultaRepository.saveAndFlush(documentoEnConsulta);
	}

	/**
	 * Valida si el usuario puede enviar el documento a la bandeja de consulta
	 * para el proceso de registro de documentos.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 * @return {@code true} si el usuario puede enviar el documento a la bandeja
	 *         de consulta; de lo contrario, {@code false}
	 */
	private boolean validarEnviarABandejaConsultaParaProcesoRegistroDocumentos(Usuario usuario, Documento documento) {
		final Instancia instancia = documento.getInstancia();
		final Estado estado = instancia.getEstado();

		/*
		 * 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
		 * feature-73: Modificación en la validación de la acción de envío a la
		 * bandeja de consulta para el proceso de registro de documentos,
		 * teniendo como estado eje "Revisión Jefe Jefatura".
		 */
		if (!estado.getId().equals(Estado.REVISIÓN_JEFE_JEFATURA)) {
			return false;
		}

		final Usuario asignado = instancia.getAsignado();
		if (!usuario.getId().equals(asignado.getId())) {
			return false;
		}

		final DocumentoEnConsulta documentoEnConsulta = buscarActivo(documento);
		if (documentoEnConsulta != null) {
			return false;
		}

		return true;
	}

	/**
	 * Valida si el usuario puede enviar el documento a la bandeja de consulta
	 * para los procesos de generación de documentos internos y externos.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 * @return {@code true} si el usuario puede enviar el documento a la bandeja
	 *         de consulta; de lo contrario, {@code false}
	 */
	private boolean validarEnviarABandejaConsultaParaProcesoGeneracionDocumentos(Usuario usuario, Documento documento) {
		final Instancia instancia = documento.getInstancia();
		final Estado estado = instancia.getEstado();

		if (!estado.getId().equals(Estado.ENVIADO)) {
			return false;
		}

		final Usuario asignado = instancia.getAsignado();
		if (!usuario.getId().equals(asignado.getId())) {
			return false;
		}

		final DocumentoEnConsulta documentoEnConsulta = buscarActivo(documento);
		if (documentoEnConsulta != null) {
			return false;
		}

		return true;
	}

	/**
	 * Valida si el usuario puede extraer el documento de la bandeja de consulta
	 * y enviarlo a la bandeja de entrada nuevamente, para el proceso de
	 * registro de documentos.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 * @return {@code true} si el usuario puede extraer el documento de la
	 *         bandeja de consulta; de lo contrario, {@code false}.
	 */
	private boolean validarExtraerDeBandejaConsultaParaProcesoRegistroDocumentos(Usuario usuario, Documento documento) {
		final Instancia instancia = documento.getInstancia();
		final Estado estado = instancia.getEstado();

		/*
		 * 2017-05-19 jgarcia@controltechcg.com Issue #73 (SICDI-Controltech)
		 * feature-73: Modificación en la validación de la acción de extraccción
		 * de la bandeja de consulta para el proceso de registro de documentos,
		 * teniendo como estado eje "Revisión Jefe Jefatura".
		 */
		if (!estado.getId().equals(Estado.REVISIÓN_JEFE_JEFATURA)) {
			return false;
		}

		final Usuario asignado = instancia.getAsignado();
		if (!usuario.getId().equals(asignado.getId())) {
			return false;
		}

		final DocumentoEnConsulta documentoEnConsulta = buscarActivo(documento);
		if (documentoEnConsulta == null) {
			return false;
		}

		if (!usuario.getId().equals(documentoEnConsulta.getQuien())) {
			return false;
		}

		return true;
	}

	/**
	 * Valida si el usuario puede extraer el documento de la bandeja de consulta
	 * y enviarlo a la bandeja de entrada nuevamente, para los procesos de
	 * generación de documentos internos y externos.
	 * 
	 * @param usuario
	 *            Usuario.
	 * @param documento
	 *            Documento.
	 * @return {@code true} si el usuario puede extraer el documento de la
	 *         bandeja de consulta; de lo contrario, {@code false}.
	 */
	private boolean validarExtraerDeBandejaConsultaParaProcesoGeneracionDocumentos(Usuario usuario,
			Documento documento) {
		final Instancia instancia = documento.getInstancia();
		final Estado estado = instancia.getEstado();

		if (!estado.getId().equals(Estado.ENVIADO)) {
			return false;
		}

		final Usuario asignado = instancia.getAsignado();
		if (!usuario.getId().equals(asignado.getId())) {
			return false;
		}

		final DocumentoEnConsulta documentoEnConsulta = buscarActivo(documento);
		if (documentoEnConsulta == null) {
			return false;
		}

		if (!usuario.getId().equals(documentoEnConsulta.getQuien())) {
			return false;
		}

		return true;
	}

}
