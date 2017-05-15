package com.laamware.ejercito.doc.web.ctrl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.laamware.ejercito.doc.web.entity.Adjunto;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Estado;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Tipologia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepositoryCustom;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.InstanciaRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.OFS;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.util.GeneralUtils;

/**
 * 
 * @author rafar
 *
 */
@Controller
@RequestMapping(DocumentoScannerSwingController.PATH)
public class DocumentoScannerSwingController {
	
	static final String PATH = "/scanner";
	
	@Autowired
	DocumentoRepository documentRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ProcesoService procesoService;
	
	@Autowired
	OFS ofs;
	
	@Autowired
	AdjuntoRepositoryCustom ajuntoRcustom;
	
	@Autowired
	DocumentoGeneradorVariables documentoGeneradorVariables;
	
	@Autowired
	InstanciaRepository instanciaRepository;
	
	/*
	 * 
	 */
	@RequestMapping(value = "/validarcodigo/{codigo}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Map<String, String>> validarCodigoScanner(@PathVariable("codigo") String codigo) {
	
		Map<String,String> respuesta = new HashMap<>();
		try {
			
			List<Documento> listaDocumentos = documentRepository.findByCodigoValidaScannerAndEstadoCodigoValidaScanner(codigo, 1);
			
			respuesta.put("CODIGO", codigo);	
			if( listaDocumentos.isEmpty() ){
				//NO EXISTE CODIGO VALIDO
				respuesta.put("RESPUESTA", "FALLIDO");
			}else if( listaDocumentos.size() > 1 ){
				//ENCONTROS VARIOS CODIGOS ACTIVOS, SE TOMA COMO ERROR
				respuesta.put("RESPUESTA", "FALLIDO");
			}else{
				respuesta.put("tid", "2" );
				respuesta.put("ID_DOCUMENTO", listaDocumentos.get( 0 ).getId() );
				respuesta.put("F_CREACION_DOCUMENTO", listaDocumentos.get( 0 ).getCuando().toString() );
				respuesta.put("ASUNTO_DOCUMENTO", listaDocumentos.get( 0 ).getAsunto() );
				respuesta.put("PROCESO_DOCUMENTO", listaDocumentos.get( 0 ).getInstancia().getProceso().getNombre() );
				respuesta.put("ESTADO_DOCUMENTO", listaDocumentos.get( 0 ).getInstancia().getEstado().getNombre() );
				Usuario quien = usuarioRepository.getOne( listaDocumentos.get( 0 ).getQuien() );
				respuesta.put("USUARIO_CREA_DOCUMENTO", quien.getNombre() );
				respuesta.put("USUARIO_ENVIADO_POR", listaDocumentos.get( 0 ).getUsuarioUltimaAccion() != null ? listaDocumentos.get( 0 ).getUsuarioUltimaAccion().getNombre() : "" );
				respuesta.put("USUARIO_ASIGNADO_DOCUMENTO", listaDocumentos.get( 0 ).getInstancia().getAsignado() != null ? listaDocumentos.get( 0 ).getInstancia().getAsignado().getNombre() : "" );
				respuesta.put("USUARIO_ELABORA_DOCUMENTO", listaDocumentos.get( 0 ).getElabora() != null ? listaDocumentos.get( 0 ).getElabora().getNombre() : "" );
				respuesta.put("USUARIO_APRUEBA_DOCUMENTO", listaDocumentos.get( 0 ).getAprueba() != null ? listaDocumentos.get( 0 ).getAprueba().getNombre() : "" );
				respuesta.put("USUARIO_VISTO_BUENO_DOCUMENTO", listaDocumentos.get( 0 ).getVistoBueno() != null ? listaDocumentos.get( 0 ).getVistoBueno().getNombre() : "" );
				respuesta.put("USUARIO_FIRMA_DOCUMENTO", listaDocumentos.get( 0 ).getFirma() != null ? listaDocumentos.get( 0 ).getFirma().getNombre() : "" );
				respuesta.put("RESPUESTA", "OK");		
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.put("RESPUESTA", "ERROR");
		}
		
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
		
	}
	
	/**
	 * 
	 * @param map
	 * @param codigo
	 * @return
	 */
	@RequestMapping(value = "/informaciondocumentoscanner/{codigo}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, String>> informacionDocumentoScanner( @RequestBody Map<String, Object> map, @PathVariable("codigo") String codigo) {
	
		Map<String,String> respuesta = new HashMap<>();
		try {
			
			byte[] archivoBase64 = Base64.decodeBase64( (String)map.get("archivoBase64") );
			String docId = (String)map.get("ID_DOCUMENTO"); 
			Integer tid = Integer.parseInt( (String)map.get("tid"));	
			
			// Obtiene el documento asociado a la instancia de proceso
			Documento doc = documentRepository.getOne(docId);
			
			Instancia i = procesoService.instancia(doc.getInstancia().getId());

			String fileId = null;
			fileId = ofs.save( archivoBase64, "application/pdf");

			Adjunto adjunto = new Adjunto();
			adjunto.setContenido(fileId);
			adjunto.setDocumento(doc);
			adjunto.setId(GeneralUtils.newId());
			adjunto.setOriginal("DocumentoEscaneado");
			adjunto.setTipologia(Tipologia.DOCUMENTO_LLEGADO);
			adjunto.setQuien( doc.getQuien() );
			
			//DAD_ID,TDO_ID,CUANDO,QUIEN,DOC_ID,DAD_CONTENT,DAD_ORIGINAL,DAD_DESCRIPCION,ACTIVO
			ajuntoRcustom.insertIntoDocumentoAdjunto(
					GeneralUtils.newId(),
					9999,
					new Date(),
					doc.getQuien(),
					doc.getId(),
					fileId,
					"DocumentoEscaneado_swing",
					"DocumentoEscaneado_Swing",
					1
					);

			// Aplica el generador de variables
			documentoGeneradorVariables.generar(i);

			// Cambia el modo de visualización
			i.setVariable(Documento.DOC_MODE, DocumentoMode.NAME_DIGITALIZANDO );

			// Intenta avanzar en el proceso
			i.forward(tid);
						
			doc.setEstadoCodigoValidaScanner( 0 );
			
			Estado estadoDigitacion = new Estado();
			estadoDigitacion.setId( Estado.DIGITALIZACION );
			i.setEstado( estadoDigitacion );
			
			instanciaRepository.save( i );
			documentRepository.save( doc );
			
			
			respuesta.put("RESPUESTA", "OK");
			
			
		} catch (Exception e) {
			e.printStackTrace();
			respuesta.put("RESPUESTA", "ERROR");
		}
		
		return new ResponseEntity<>(respuesta, HttpStatus.OK);
		
	}
}
