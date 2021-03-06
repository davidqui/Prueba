package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.PaginacionDTO;
import java.security.Principal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.HProcesoInstancia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.InstanciaBandeja;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaAdicionalRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.HProcesoInstanciaRepository;
import com.laamware.ejercito.doc.web.repo.InstanciaBandejaRepository;
import com.laamware.ejercito.doc.web.serv.BandejaService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import com.laamware.ejercito.doc.web.util.DateUtil;
import com.laamware.ejercito.doc.web.util.DateUtil.SetTimeType;
import com.laamware.ejercito.doc.web.util.PaginacionUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping(value = BandejaController.PATH)
public class BandejaController extends UtilController {

    public static final String PATH = "/bandeja";

    @Autowired
    ProcesoService procesoService;

    @Autowired
    DocumentoRepository docR;

    @Autowired
    InstanciaBandejaRepository instanciaBandejaR;

    // 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
    // feature-78
    @Autowired
    UsuarioService usuarioService;

    // 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de jefes de
    // dependencias adicionales a un documento.
    @Autowired
    DocumentoDependenciaAdicionalRepository documentoDependenciaAdicionalRepository;

    /*
	 * 2017-07-05 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Implementación de servicio para manejo de filtros por fecha
	 * para la presentación de las bandejas diferentes a entrada.
     */
    @Autowired
    private BandejaService bandejaService;
    
    /*
     * 2018-07-06 samuel.delgado@controltechcg.com release-20180628(SICDI-Controltech)
     * Implementación de servicio para manejo de historial de instancia.
     */
    @Autowired
    private HProcesoInstanciaRepository hProcesoInstanciaRepository;
    
    @PreAuthorize("hasRole('BANDEJAS')")
    @RequestMapping(value = "/entrada", method = RequestMethod.GET)
    public String entrada(Model model, Principal principal,
            @RequestParam(required = false, value = "action") String action,
            @RequestParam(required = false, value = "pin") String pin,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        if (StringUtils.isNotBlank(action)) {
            if ("quitar".equals(action)) {
                if (StringUtils.isNotBlank(pin)) {
                    InstanciaBandeja ib = instanciaBandejaR.findByInstanciaIdAndUsuarioLoginAndBandejaAndActivo(pin,
                            principal.getName(), "ENTRADA", true);
                    ib.setActivo(false);
                    instanciaBandejaR.save(ib);
                }
            }
        }

        // 2017-10-17 edison.gonzalez@controltechcg.com Issue #132 Paginacion de 
        // la bandeja de entrada.
        List<Documento> docs = null;
        int count = bandejaService.obtenerCountBandejaEntrada(principal.getName());
        int totalPages = 0;
        String labelInformacion = "";

        if (count > 0) {
            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
            totalPages = paginacionDTO.getTotalPages();
            docs = bandejaService.obtenerDocumentosBandejaEntrada(principal.getName(), paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
            labelInformacion = paginacionDTO.getLabelInformacion();
            if (docs != null) {
                for (Documento d : docs) {
                    d.getInstancia().getCuando();
                    d.getInstancia().setService(procesoService);
                }
            }
        }

        model.addAttribute("documentos", docs);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);

        /*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
         */
        model.addAttribute("usuarioService", usuarioService);

        return "bandeja-entrada";
    }

    /**
     * Presenta los documentos de la bandeja de enviados del usuario en sesión.
     *
     * @param model Modelo de presentación.
     * @param principal Atributos de autenticación.
     * @param fechaInicial Fecha inicial del rango de filtro (Opcional).
     * @param fechaFinal Fecha final del rango de filtro (Opcional).
     * @param pageIndex Indice de la pagina a mostrar (Opcional).
     * @param pageSize Numero de registros a visualizar (Opcional).
     * @return Lista de documentos enviados del usuario.
     */
    /*
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de controlador de bandejas para manejo de rango
	 * de fechas, utilizando un servicio del modelo de negocio.
     */
    @PreAuthorize("hasRole('BANDEJAS')")
    @RequestMapping(value = "/enviados", method = {RequestMethod.GET, RequestMethod.POST})
    public String enviados(Model model, Principal principal,
            @RequestParam(required = false, value = "fechaInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicial,
            @RequestParam(required = false, value = "fechaFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinal,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        // 2017-10-18 edison.gonzalez@controltechcg.com Issue #132 Ajuste para  
        // dejar las fechas de filtro no obligatorias.
        Date fechaInicialFiltro = fechaInicial;
        Date fechaFinFiltro = fechaFinal;
        if (fechaFinal == null) {
            fechaFinFiltro = new Date();
        }
        DateUtil.setTime(fechaFinFiltro, SetTimeType.END_TIME);

        if (fechaInicial == null) {
            fechaInicialFiltro = DateUtil.obtenerFechaInicialFiltroBandeja();
        }
        DateUtil.setTime(fechaInicialFiltro, SetTimeType.START_TIME);

        final String login = principal.getName();

        // 2017-10-18 edison.gonzalez@controltechcg.com Issue #132 Paginacion de 
        // la bandeja de enviados.
        List<Documento> documentos = null;
        int count = bandejaService.obtenerCountBandejaEnviados(login, fechaInicialFiltro, fechaFinFiltro);
        int totalPages = 0;
        String labelInformacion = "";

        if (count > 0) {
            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
            totalPages = paginacionDTO.getTotalPages();
            documentos = bandejaService.obtenerDocumentosBandejaEnviados(login, fechaInicialFiltro, fechaFinFiltro, paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
            labelInformacion = paginacionDTO.getLabelInformacion();
        }

        if (documentos != null) {
            for (Documento documento : documentos) {
                documento.getInstancia().getCuando();
                documento.getInstancia().setService(procesoService);

                /*
             * 2017-02-06 jgarcia@controltechcg.com Issue #118 Presentación de
             * jefes de dependencias adicionales a un documento.
             * 
             * 2017-05-15 jgarcia@controltechcg.com Issue #78
             * (SICDI-Controltech) feature-78: Presentar información básica de
             * los usuarios asignadores y asignados en las bandejas del sistema.
             * 
             * 2017-05-24 jgarcia@controltechcg.com Issue #73
             * (SICDI-Controltech) feature-73: Opción para indicar si la
             * construcción del texto de asignados debe manejar múltiples
             * destinos o no.
             *
             * 2017-10-24 edison.gonzalez@controltechcg.com Issue #132
             * (SICDI-Controltech) feature-132: Se pone en comentarios 
             * la signacion de la variable asignadosText al llamado del
             * metodo buildAsignadosText mientras se reimplementa el
             * multidestino.
                 */
 /*String asignadosText = DocumentoController.buildAsignadosText(documentoDependenciaAdicionalRepository,
                    usuarioService, documento.getInstancia(), null, true);*/
                String asignadosText = usuarioService.mostrarInformacionBasicaBandejas(documento.getInstancia().getAsignado());
                documento.setTextoAsignado(asignadosText);
            }
        }

        model.addAttribute("documentos", documentos);
        model.addAttribute("fechaInicial", fechaInicial);
        model.addAttribute("fechaFinal", fechaFinal);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);

        /*
            * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
            * feature-78: Presentar información básica de los usuarios asignadores
            * y asignados en las bandejas del sistema.
         */
        model.addAttribute("usuarioService", usuarioService);

        return "bandeja-enviados";
    }

    /**
     * Presenta los documentos de la bandeja en trámite del usuario en sesión.
     *
     * @param model Modelo de presentación.
     * @param principal Atributos de autenticación.
     * @param fechaInicial Fecha inicial del rango de filtro (Opcional).
     * @param fechaFinal Fecha final del rango de filtro (Opcional).
     * @param pageIndex Indice de la pagina a mostrar (Opcional).
     * @param pageSize Numero de registros a visualizar (Opcional).
     * @return Lista de documentos en trámite del usuario.
     */
    /*
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de controlador de bandejas para manejo de rango
	 * de fechas, utilizando un servicio del modelo de negocio.
     */
    @PreAuthorize("hasRole('BANDEJAS')")
    @RequestMapping(value = "/entramite", method = {RequestMethod.GET, RequestMethod.POST})
    public String entramite(Model model, Principal principal,
            @RequestParam(required = false, value = "fechaInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicial,
            @RequestParam(required = false, value = "fechaFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinal,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        // 2017-10-18 edison.gonzalez@controltechcg.com Issue #132 Ajuste para  
        // dejar las fechas de filtro no obligatorias.
        Date fechaInicialFiltro = fechaInicial;
        Date fechaFinFiltro = fechaFinal;
        if (fechaFinal == null) {
            fechaFinFiltro = new Date();
        }
        DateUtil.setTime(fechaFinFiltro, SetTimeType.END_TIME);

        if (fechaInicial == null) {
            fechaInicialFiltro = DateUtil.obtenerFechaInicialFiltroBandeja();
        }
        DateUtil.setTime(fechaInicialFiltro, SetTimeType.START_TIME);

        final String login = principal.getName();

        // 2017-10-25 edison.gonzalez@controltechcg.com Issue #132 Paginacion de 
        // la bandeja de tramites.
        List<Documento> documentos = null;
        int count = bandejaService.obtenerCountBandejaTramite(login, fechaInicialFiltro, fechaFinFiltro);
        int totalPages = 0;
        String labelInformacion = "";

        if (count > 0) {
            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
            totalPages = paginacionDTO.getTotalPages();
            documentos = bandejaService.obtenerDocumentosBandejaTramite(login, fechaInicialFiltro, fechaFinFiltro, paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
            labelInformacion = paginacionDTO.getLabelInformacion();
        }

        if (documentos != null) {
            for (Documento documento : documentos) {
                documento.getInstancia().getCuando();
                documento.getInstancia().setService(procesoService);
            }
        }

        model.addAttribute("documentos", documentos);
        model.addAttribute("fechaInicial", fechaInicial);
        model.addAttribute("fechaFinal", fechaFinal);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);

        /*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
         */
        model.addAttribute("usuarioService", usuarioService);

        return "bandeja-tramite";
    }

    @PreAuthorize("hasRole('BANDEJAS')")
    @RequestMapping(value = "/consulta", method = RequestMethod.GET)
    public String consulta(Model model) {

        return "bandeja-consulta";

    }

    /**
     * Obtiene la información a presentar para la bandeja de apoyo y consulta
     * del usuario, y carga la pantalla correspondiente.
     *
     * @param model Modelo.
     * @param principal Principal.
     * @param fechaInicial Fecha inicial del rango de filtro (Opcional).
     * @param fechaFinal Fecha final del rango de filtro (Opcional).
     * @param pageIndex Indice de la pagina a mostrar (Opcional).
     * @param pageSize Numero de registros a visualizar (Opcional).
     * @return Identificador del template de la bandeja.
     */
    @PreAuthorize("hasRole('BANDEJAS')")
    @RequestMapping(value = "/apoyo-consulta", method = {RequestMethod.GET, RequestMethod.POST})
    /*
	 * 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
	 * 
	 * 2017-07-10 jgarcia@controltechcg.com Issue #115 (SICDI-Controltech)
	 * feature-115: Modificación de controlador de bandejas para manejo de rango
	 * de fechas, utilizando un servicio del modelo de negocio.
     */
    public String apoyoConsulta(Model model, Principal principal,
            @RequestParam(required = false, value = "fechaInicial") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicial,
            @RequestParam(required = false, value = "fechaFinal") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFinal,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {

        // 2017-10-26 edison.gonzalez@controltechcg.com Issue #132 Ajuste para  
        // dejar las fechas de filtro no obligatorias.
        Date fechaInicialFiltro = fechaInicial;
        Date fechaFinFiltro = fechaFinal;
        if (fechaFinal == null) {
            fechaFinFiltro = new Date();
        }
        DateUtil.setTime(fechaFinFiltro, SetTimeType.END_TIME);

        if (fechaInicial == null) {
            fechaInicialFiltro = DateUtil.obtenerFechaInicialFiltroBandeja();
        }
        DateUtil.setTime(fechaInicialFiltro, SetTimeType.START_TIME);

        final String login = principal.getName();

        // 2017-10-25 edison.gonzalez@controltechcg.com Issue #132 Paginacion de 
        // la bandeja de tramites.
        List<Documento> documentos = null;
        int count = bandejaService.obtenerCountBandejaConsulta(login, fechaInicialFiltro, fechaFinFiltro);
        int totalPages = 0;
        String labelInformacion = "";

        if (count > 0) {
            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
            totalPages = paginacionDTO.getTotalPages();
            documentos = bandejaService.obtenerDocumentosBandejaConsulta(login, fechaInicialFiltro, fechaFinFiltro, paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
            labelInformacion = paginacionDTO.getLabelInformacion();
        }

        if (documentos != null) {
            for (Documento documento : documentos) {
                documento.getInstancia().getCuando();
                documento.getInstancia().setService(procesoService);
            }
        }

        model.addAttribute("documentos", documentos);
        model.addAttribute("fechaInicial", fechaInicial);
        model.addAttribute("fechaFinal", fechaFinal);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);

        /*
		 * 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech)
		 * feature-78: Presentar información básica de los usuarios asignadores
		 * y asignados en las bandejas del sistema.
         */
        model.addAttribute("usuarioService", usuarioService);

        return "bandeja-apoyo-consulta";

    }

    // 2017-10-17 edison.gonzalez@controltechcg.com Issue #132 
    //Lista desplegable para la paginación.
    @ModelAttribute("pageSizes")
    public List<Integer> pageSizes(Model model) {
        List<Integer> list = Arrays.asList(10, 30, 50);
        model.addAttribute("pageSizes", list);
        return list;
    }
    
    /**
     * Agrega el controlador
     *
     * @return controlador
     */
    @ModelAttribute("controller")
    public BandejaController controller() {
        return this;
    }
    
    
    public String tranInstancia(Instancia instancia){
        Map<Integer, String> transcripcionInstancias = transcripcionInstancias();
        if (instancia.getEstado().getId() != 61) {
            return transcripcionInstancias.get(instancia.getEstado().getId());
        }else{
            List<HProcesoInstancia> instanciasDoc = hProcesoInstanciaRepository.findById(instancia.getId(), new Sort(Sort.Direction.DESC, "cuandoMod"));
            if (!instanciasDoc.isEmpty() && instanciasDoc.size() > 1) {
                return transcripcionInstancias.get(instancia.getEstado().getId());
            }else{
                return "En progreso";
            }
        }
    }
    
    public Map<Integer, String> transcripcionInstancias(){
        Map<Integer, String> transcripcionInstancia = new HashMap<>();
        //En progreso
        transcripcionInstancia.putIfAbsent(42 , "En progreso");
        transcripcionInstancia.putIfAbsent(43 , "En progreso");
        transcripcionInstancia.putIfAbsent(44 , "En progreso");
        transcripcionInstancia.putIfAbsent(150 , "En progreso");
        transcripcionInstancia.putIfAbsent(151 , "En progreso");
        transcripcionInstancia.putIfAbsent(152 , "En progreso");
        transcripcionInstancia.putIfAbsent(153 , "En progreso");
        transcripcionInstancia.putIfAbsent(154 , "En progreso");
        transcripcionInstancia.putIfAbsent(155 , "En progreso");
        //Por revisión
        transcripcionInstancia.putIfAbsent(54 , "Por revisión");
        transcripcionInstancia.putIfAbsent(46 , "Por revisión");
        //Devuelto
        transcripcionInstancia.putIfAbsent(61 , "Devuelto");
        //Aprobado
        transcripcionInstancia.putIfAbsent(55 , "Aprobado");
        //Por visto bueno
        transcripcionInstancia.putIfAbsent(56 , "Por visto bueno");
        //Por firmar
        transcripcionInstancia.putIfAbsent(58 , "Por firmar");
        //Firmado y asignado
        transcripcionInstancia.putIfAbsent(49 , "Firmado y asignado");
        //Por digitalizar
        transcripcionInstancia.putIfAbsent(156 , "Por digitalizar");
        //Por abrobar
        transcripcionInstancia.putIfAbsent(157 , "Por abrobar");
        return transcripcionInstancia;
    }
}
