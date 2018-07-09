package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.dto.DocumentoDTO;
import com.laamware.ejercito.doc.web.dto.PaginacionDTO;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.BandejaRepository;
import com.laamware.ejercito.doc.web.repo.ClasificacionRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.serv.ConsultaService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;
import com.laamware.ejercito.doc.web.util.PaginacionUtil;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(value = ConsultaController.PATH)
public class ConsultaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(ConsultaController.class.getName());

    public static final String PATH = "/consulta";

    @Autowired
    BandejaRepository banR;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    ProcesoService procesoService;

    @Autowired
    DocumentoRepository documentoRepository;

    @Autowired
    ExpedienteRepository expedienteRepository;

    @Autowired
    ClasificacionRepository clasificacionRepository;

    @Autowired
    ConsultaService consultaService;

    @Autowired
    DependenciaRepository dependenciaRepository;

    /**
     * Ejecuta la búsqueda de documentos cuyo asunto o contenido contenga el
     * texto ingresado como términos de búsqueda
     *
     * @param term Texto a buscar
     * @param model
     * @param principal
     * @return
     */
    @PreAuthorize("hasRole('BANDEJAS')")
    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String consulta(@RequestParam(value = "term") String term, Model model, Principal principal) {

        // 2018-01-31 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
        List<Dependencia> listaDependencias = depsHierarchy();
        model.addAttribute("dependencias", listaDependencias);

        if (StringUtils.isBlank(term)) {
            return "consulta";
        }

        model.addAttribute("term", term);

        /*
         * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
         * feature-160: Filtro para firma UUID.
         */
        return buscar(model, term, term, null, null, term, term, null, null, null, term, principal, 1, 10, null, null, null, term, null);
    }

    /**
     * *
     *
     * @param model
     * @param asignado
     * @param asunto
     * @param fechaInicio
     * @param fechaFin
     * @param radicado
     * @param destinatario
     * @param clasificacion
     * @param dependenciaDestino
     * @param dependenciaOrigen
     * @param term
     * @param principal
     * @param pageIndex
     * @param pageSize
     * @param clasificacionNombre
     * @param dependenciaDestinoDescripcion
     * @param dependenciaOrigenDescripcion
     * @param firmaUUID
     * @param tipoProceso
     * @return
     */
    /*
     * 2017-02-10 jgarcia@controltechcg.com Issue #105: Se modifica todo el
     * cuerpo del método buscar() para construir una sola sentencia SQL a partir
     * de los valores ingresados desde el formulario.
     *
     * 2017-02-13 jgarcia@controltechcf.com Issue #77: Se amplían los valores de
     * búsqueda, para obtener el campo de la dependencia.
     *
     * 2017-10-31 edison.gonzalez@controltechcg.com Issue #136: Se agrega la
     * variable dependencia de origen para aplicarla al filtro.
     *
     * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
     * feature-160: Filtro para firma UUID.
     *
     * 2018-07-09 samuel.delgado@controltechcg.com Issue #177 (SICDI-Controltech)
     * feature-160: Filtro para tipo de proceso.
     */
    @RequestMapping(value = "/parametros", method = {RequestMethod.GET, RequestMethod.POST})
    public String buscar(Model model, @RequestParam(value = "asignado", required = false) String asignado,
            @RequestParam(value = "asunto", required = false) String asunto,
            @RequestParam(value = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(value = "fechaFin", required = false) String fechaFin,
            @RequestParam(value = "radicado", required = false) String radicado,
            @RequestParam(value = "destinatario", required = false) String destinatario,
            @RequestParam(value = "clasificacion", required = false) Integer clasificacion,
            @RequestParam(value = "dependenciaDestino", required = false) Integer dependenciaDestino,
            @RequestParam(value = "dependenciaOrigen", required = false) Integer dependenciaOrigen,
            @RequestParam(value = "term", required = false) String term,
            Principal principal,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "clasificacionNombre", required = false) String clasificacionNombre,
            @RequestParam(value = "dependenciaDestinoDescripcion", required = false) String dependenciaDestinoDescripcion,
            @RequestParam(value = "dependenciaOrigenDescripcion", required = false) String dependenciaOrigenDescripcion,
            @RequestParam(value = "firmaUUID", required = false) String firmaUUID,
            @RequestParam(value = "tipoProceso", required = false) Integer tipoProceso) {

        boolean sameValue = term != null && term.trim().length() > 0;

        // 2018-01-31 edison.gonzalez@controltechcg.com Issue #147 (SICDI-Controltech)
        List<Dependencia> listaDependencias = depsHierarchy();
        model.addAttribute("dependencias", listaDependencias);

        /*
         * 2018-05-08 jgarcia@controltechcg.com Issue #160 (SICDI-Controltech)
         * feature-160: Indicador en el modelo UI que permite realizar búsqueda
         * por UUID de firma y envío.
         */
        final boolean puedeBuscarXDocFirmaEnvioUUID = isAuthorizedRol(AppConstants.BUSCAR_X_DOC_FIRMA_ENVIO_UUID);
        model.addAttribute("puedeBuscarXDocFirmaEnvioUUID", puedeBuscarXDocFirmaEnvioUUID);

        if (sameValue) {
            asignado = term;
            asunto = term;
            radicado = term;
            destinatario = term;
            firmaUUID = term; // Issue #160
        }

        // Issue #105, Issue #128, Issue #160, Issue #177 
        Object[] args = {asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino, dependenciaOrigen, firmaUUID, tipoProceso};

        boolean parametrosVacios = true;
        for (Object arg : args) {
            if (arg != null) {
                if (arg instanceof String) {
                    if (((String) arg).trim().length() > 0) {
                        parametrosVacios = false;
                    }
                } else {
                    parametrosVacios = false;
                }
            }
        }

        if (parametrosVacios) {
            return "consulta-parametros";
        }

        /*
         * 2017-02-06 jgarcia@controltechcg.com Issue #128: Modificación en los
	 * procesos de búsqueda, para que únicamente presente la información de
	 * los documentos asociados al usuario en los pasos de creación y firma.
         */
        final Usuario usuarioSesion = getUsuario(principal);
        final Integer usuarioID = usuarioSesion.getId();
        expedientes(model, principal);

        /*
         * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Arreglo de IDs de cargos del usuario en sesión para
         * pasar como parámetro al constructor de la sentencia SQL.
         */
        final Integer[] cargosIDs = buildCargosIDsArray(usuarioSesion);

        List<DocumentoDTO> documentos = null;
        int count = consultaService.retornaCountConsultaMotorBusqueda(asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino,
                dependenciaOrigen, sameValue, usuarioID, firmaUUID, puedeBuscarXDocFirmaEnvioUUID, cargosIDs);
        // Issue #177 se agrega parametro tipoProceso
        int count = consultaService.retornaCountConsultaMotorBusqueda(asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino, dependenciaOrigen, sameValue, usuarioID, firmaUUID, puedeBuscarXDocFirmaEnvioUUID, tipoProceso);
        LOG.log(Level.INFO, "verificando count ]= {0}", count);
        int totalPages = 0;
        String labelInformacion = "";

        if (count > 0) {
            PaginacionDTO paginacionDTO = PaginacionUtil.retornaParametros(count, pageIndex, pageSize);
            totalPages = paginacionDTO.getTotalPages();
            documentos = consultaService.retornaConsultaMotorBusqueda(asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino,
                    dependenciaOrigen, sameValue, usuarioID, firmaUUID, puedeBuscarXDocFirmaEnvioUUID, cargosIDs, paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin());
            LOG.log(Level.INFO, "consulta completa");
            // Issue #177 se agrega parametro tipoProceso
            documentos = consultaService.retornaConsultaMotorBusqueda(asignado, asunto, fechaInicio, fechaFin, radicado, destinatario, clasificacion, dependenciaDestino, dependenciaOrigen, sameValue, usuarioID, firmaUUID, puedeBuscarXDocFirmaEnvioUUID, paginacionDTO.getRegistroInicio(), paginacionDTO.getRegistroFin(), tipoProceso);
            labelInformacion = paginacionDTO.getLabelInformacion();
        }

        model.addAttribute("totalResultados", documentos != null ? documentos.size() : 0);
        model.addAttribute("documentos", documentos);
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("labelInformacion", labelInformacion);
        model.addAttribute("pageSize", pageSize);

        if (!sameValue) {
            model.addAttribute("asignado", asignado);
            model.addAttribute("asunto", asunto);
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
            model.addAttribute("radicado", radicado);
            model.addAttribute("destinatario", destinatario);
            model.addAttribute("clasificacion", clasificacion);
            model.addAttribute("dependenciaDestino", dependenciaDestino);
            model.addAttribute("dependenciaOrigen", dependenciaOrigen);
            model.addAttribute("clasificacionNombre", clasificacionNombre);
            model.addAttribute("dependenciaDestinoDescripcion", dependenciaDestinoDescripcion);
            model.addAttribute("dependenciaOrigenDescripcion", dependenciaOrigenDescripcion);
            model.addAttribute("firmaUUID", firmaUUID);
            model.addAttribute("tipoProceso", tipoProceso);
        }

        model.addAttribute("term", term);

        return "consulta-parametros";
    }

    /**
     * Carga el listado de expedientes al modelo
     *
     * @param model
     * @param principal
     * @return
     */
    public List<Expediente> expedientesAdicional(Model model, Principal principal) {

        Dependencia dependencia = getUsuario(principal).getDependencia();

        if (dependencia == null) {
            return new ArrayList<>();
        }

        Integer dependenciaId = dependencia.getId();
        List<Expediente> list = expedienteRepository.findByActivoAndDependenciaId(true, dependenciaId,
                new Sort(Direction.ASC, "dato"));
        model.addAttribute("id", list);
        return list;
    }

    /**
     * Carga el listado de expedientes al modelo
     *
     * @param model
     * @param principal
     * @return
     */
    public List<Expediente> expedientes(Model model, Principal principal) {
        final Dependencia dependencia = getUsuario(principal).getDependencia();

        if (dependencia == null) {
            return new ArrayList<>();
        }

        final Integer dependenciaId = dependencia.getId();
        final List<Expediente> expedientes = expedienteRepository.findByActivoAndDependenciaId(true, dependenciaId, new Sort(Direction.ASC, "nombre"));
        model.addAttribute("expedientes", expedientes);
        return expedientes;
    }

    /**
     * Carga el listado de clasificaciones al modelo
     *
     * @return
     */
    @ModelAttribute("clasificaciones")
    public List<Clasificacion> clasificaciones() {
        return clasificacionRepository.findByActivo(true, new Sort(Direction.ASC, "orden"));
    }

    // 2017-11-10 edison.gonzalez@controltechcg.com Issue #136 
    //Lista desplegable para la paginación.
    @ModelAttribute("pageSizes")
    public List<Integer> pageSizes(Model model) {
        List<Integer> list = Arrays.asList(10, 30, 50);
        model.addAttribute("pageSizes", list);
        return list;
    }

    private List<Dependencia> depsHierarchy() {
        List<Dependencia> root = dependenciaRepository.findByActivoAndPadreIsNull(true,
                new Sort(Direction.ASC, "pesoOrden", "nombre"));
        for (Dependencia d : root) {
            depsHierarchy(d);
        }
        return root;
    }

    private void depsHierarchy(Dependencia d) {
        List<Dependencia> subs = dependenciaRepository.findByActivoAndPadre(true, d.getId(),
                new Sort(Direction.ASC, "pesoOrden", "nombre"));
        d.setSubs(subs);
        for (Dependencia x : subs) {
            depsHierarchy(x);
        }
    }

    /**
     * Construye un arreglo con los IDs de los cargos asignados al usuario.
     *
     * @param usuario Usuario.
     * @return Arreglo de IDs de cargos del usuario.
     */
    /*
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    private Integer[] buildCargosIDsArray(final Usuario usuario) {
        final Set<Integer> set = new LinkedHashSet<>();

        final Cargo[] cargos = {
            usuario.getUsuCargoPrincipalId(),
            usuario.getUsuCargo1Id(),
            usuario.getUsuCargo2Id(),
            usuario.getUsuCargo3Id(),
            usuario.getUsuCargo4Id(),
            usuario.getUsuCargo5Id(),
            usuario.getUsuCargo6Id(),
            usuario.getUsuCargo7Id(),
            usuario.getUsuCargo8Id(),
            usuario.getUsuCargo9Id(),
            usuario.getUsuCargo10Id()
        };

        for (final Cargo cargo : cargos) {
            if (cargo != null && cargo.getId() != null && !cargo.getId().equals(0)) {
                set.add(cargo.getId());
            }
        }

        return set.toArray(new Integer[set.size()]);
    }
    
    /*
    * 2018-07-09 samuel.delgado@controltechcg.com Issue #177: se agrega el atributo
    * de tipos de proceso para el select de busqueda por parametro
    */
    @ModelAttribute("tiposProcesos")
    public List<Map<String, Object>> tiposProcesos() {
        return procesoService.procesos();
    }

}
