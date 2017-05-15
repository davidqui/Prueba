package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Informe;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.InformeRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.JasperService;

@Controller
@PreAuthorize("hasRole('ADMIN_INFORMES')")
@RequestMapping(AdminInformeController.PATH)
public class AdminInformeController extends AdminGenController<Informe, Integer, InformeRepository> {

	static final String PATH = "/admin/informes";

	@Autowired
	DependenciaRepository dependenciaRepository;
	
	@Autowired
	ClasificacionRepository clasificacionRepository;
	
	@Autowired
	InformeRepository repo;
	
	@Autowired
	DocumentoRepository documentRepository;
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	JasperService jasperService;
	
	@Value("${docweb.images.root}")
	private String imagesRoot;	
	
	@Value("${docweb.archivos.jasper}")
	private String dirJaspersRoot;
	
	private static final SimpleDateFormat FORMATEADOR = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	InformeRepository getRepository() {
		return repo;
	}
	
	@RequestMapping(value = "/init", method = RequestMethod.GET)
	public String init(
			@RequestParam(value = "seleccion_tipo_reporte", required = false, defaultValue = "") String seleccionTipoReporte,
			@RequestParam(value = "finicio", required = false, defaultValue = "") String finicio,
			@RequestParam(value = "ffin", required = false, defaultValue = "") String ffin,
			@RequestParam(value = "usuario", required = false, defaultValue = "") String usuario,
			@RequestParam(value = "asunto", required = false, defaultValue = "") String asunto,
			@RequestParam(value = "nivelclasifica", required = false, defaultValue = "") String nivelclasifica,
			@RequestParam(value = "destinatario", required = false, defaultValue = "") String destinatario,
			Model model, Principal principal,
			HttpServletResponse resp,
			 HttpServletRequest req,
			RedirectAttributes redirect){		
					
		try {
		
			model.addAttribute("seleccionTipoReporte", seleccionTipoReporte);
			model.addAttribute("finicio", finicio);
			model.addAttribute("ffin", ffin);
			model.addAttribute("usuario", usuario);
			model.addAttribute("asunto", asunto);
			model.addAttribute("nivelclasifica", nivelclasifica);
			model.addAttribute("destinatario", destinatario);
			
			boolean btnConsultarReporte = req.getParameter("btn_consultar_datos") != null;
			boolean btnGenerarReporte = req.getParameter("btn_generar_reporte") != null;
			
			Usuario usuarioModelo = null;
			boolean validadorFechaInicioFin = false;
			boolean validadorNivelClasifica = false;
			Date fechaInicio = null, fechaFin = null;
			int idClasificacion = 0;
			
			if( usuario.trim().length() > 0 ){
				
				usuarioModelo = usuarioRepository.getByLogin(usuario.trim());
				
			}else if( finicio != null && ffin != null && finicio.trim().length() > 0 && ffin.trim().length() > 0 ){
				
				try {
					
					fechaInicio = FORMATEADOR.parse( finicio.trim() );
					fechaFin = FORMATEADOR.parse( ffin.trim() );
					validadorFechaInicioFin = true;
					
				} catch (Exception e) {
					model.addAttribute(AppConstants.FLASH_ERROR,"Debe ingresar las fechas de forma correcta.");
				}
			}else if( nivelclasifica != null && nivelclasifica.trim().length() > 0 ){
				try {

					idClasificacion = Integer.parseInt( nivelclasifica.trim() );
					validadorNivelClasifica = true;

				} catch (Exception e) {
					model.addAttribute(AppConstants.FLASH_ERROR,"La clasificación del documento selecciona es incorrecta.");
				}
			}
			
			if( btnGenerarReporte ){
				
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("imagesRoot", imagesRoot);
				params.put("SUBREPORT_DIR", dirJaspersRoot);
				
				byte[] reporteGenerado = null;
				String nombreRporte = null;
				if( "REPORTE_POR_USUARIO".equals( seleccionTipoReporte ) && usuarioModelo != null ){
					
					nombreRporte = "DocumentosElaboradosPorUsuario";
					
					if( usuarioModelo != null ){
						
						params.put("p_usu_id", usuarioModelo.getId());
						params.put("p_nombre_usuario", usuarioModelo.getNombre());		
						
					}
				}else if( "REPORTE_POR_FECHAS".equals( seleccionTipoReporte ) && validadorFechaInicioFin ){
					
					if( finicio == null || ffin == null || finicio.trim().length() == 0 || ffin.trim().length() == 0 ){
						model.addAttribute(AppConstants.FLASH_ERROR,"Debe ingresar las fechas ( Fecha inicio y Fecha fin)");
					}else{
						try {
							
							if( fechaInicio.compareTo( fechaFin ) > 0 ){
								model.addAttribute(AppConstants.FLASH_ERROR,"La \"Fecha de inicio\" no puede ser mayor a la \"Fecha fin\"");
							}else{							
								validadorFechaInicioFin = true;
								nombreRporte = "DocumentosElaboradosPorFechaInicioFin";
								params.put("finicio", fechaInicio);
								params.put("ffin", fechaFin );	
							}						
						} catch (Exception e) {
							model.addAttribute(AppConstants.FLASH_ERROR,"Debe ingresar las fechas de forma correcta.");
						}
					}
				}else if( "REPORTE_POR_CLASIFICACION_DEL_DOCUMENTO".equals( seleccionTipoReporte ) && validadorNivelClasifica ){
					
					nombreRporte = "DocumentosElaboradosClasificacion";
					params.put("p_clasificacion", idClasificacion );
					
				}
				
				if( !params.isEmpty() && nombreRporte != null ){
					try {
						reporteGenerado = jasperService.pdf( nombreRporte, params, null, dataSource.getConnection());
					} catch (Exception e) {
						model.addAttribute(AppConstants.FLASH_ERROR,"Error general el reporte: "+ e.getMessage());
						e.printStackTrace();
					}				
				}			
				
				if( reporteGenerado != null ){
					
					try {
						resp.setContentLength((int) reporteGenerado.length);
						resp.setContentType("application/pdf");
	
						String headerKey = "Content-Disposition";
						String headerValue = String.format("attachment; filename=\"%s\"", "SalidaReporte.pdf");
						resp.setHeader(headerKey, headerValue);
	
						// Write response
						ServletOutputStream outStream = resp.getOutputStream();
						IOUtils.write(reporteGenerado, outStream);
					} catch (Exception e) {
						model.addAttribute(AppConstants.FLASH_ERROR,"Error general enviar reporte al navegador: "+ e.getMessage());
						e.printStackTrace();
					}						
				}
			}
			
			List<Documento> lDocumento = null;
			
			if( btnConsultarReporte && "REPORTE_POR_USUARIO".equals( seleccionTipoReporte ) && usuarioModelo != null ){
				
				lDocumento = documentRepository.findDocumentoByUsuario( usuarioModelo.getId() );
				model.addAttribute("documentos", lDocumento );
				
			}else if( btnConsultarReporte && "REPORTE_POR_FECHAS".equals( seleccionTipoReporte ) && validadorFechaInicioFin ){
				
				lDocumento = documentRepository.findDocumentoByFechaInicioYfin( fechaInicio, fechaFin );
				model.addAttribute("documentos", lDocumento );
				
			}else if( btnConsultarReporte && "REPORTE_POR_CLASIFICACION_DEL_DOCUMENTO".equals( seleccionTipoReporte ) && validadorNivelClasifica ){
				
				lDocumento = documentRepository.findDocumentoByClasificacion( idClasificacion );
				model.addAttribute("documentos", lDocumento );
				
			}
			if( btnConsultarReporte && ( lDocumento == null || lDocumento.size() == 0 )){
				model.addAttribute(AppConstants.FLASH_ERROR,"No se han encontrado documentos para los filtros ingresados");
			}
			
		} catch (Exception e) {
			model.addAttribute(AppConstants.FLASH_ERROR,"Error general del sistema");
			e.printStackTrace();
		}
		
		return "informe-list";
	}
	
	@Override
	String getPath() {
		return AdminInformeController.PATH;
	}

	@Override
	GenDescriptor getDescriptor() {
		GenDescriptor d = reflectDescriptor(Informe.class);
		return d;
	}

	@Override
	@ModelAttribute("activePill")
	public String getActivePill() {
		return "informes";
	}
	
	/**
	 * Carga el listado de clasificaciones al modelo
	 * 
	 * @return
	 */
	@ModelAttribute("clasificaciones_informe")
	public List<Clasificacion> clasificaciones() {
		return clasificacionRepository.findByActivo(true, new Sort(
				Direction.ASC, "orden"));
	}	
	
}
