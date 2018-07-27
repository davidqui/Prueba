package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.dto.CargoDTO;
import com.laamware.ejercito.doc.web.dto.DocumentoDependenciaArchivoDTO;
import com.laamware.ejercito.doc.web.dto.TrdArchivoDocumentosDTO;
import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.EstadoExpediente;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.ExpedienteEstado;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.AdjuntoRepository;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteEstadoRepository;
import com.laamware.ejercito.doc.web.repo.ExpedienteRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.serv.CargoService;
import com.laamware.ejercito.doc.web.serv.DocumentoDependenciaService;
import com.laamware.ejercito.doc.web.serv.TRDService;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mcr
 *
 */
@Controller
@RequestMapping(value = "/expediente")
public class ExpedienteController extends UtilController {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoController.class);

    public static final String PATH = "/expediente";

    /*
     * 2018-05-03 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
     * feature-157: Año mínimo para los selectores de filtro por año.
     */
    @Value("${com.mil.imi.sicdi.minFilterSelectorYear}")
    private Integer minFilterSelectorYear;

    @Autowired
    ExpedienteRepository repo;

    @Autowired
    DocumentoRepository documentoRepository;

    @Autowired
    TrdRepository trdRepository;

    @Autowired
    AdjuntoRepository adjuntoRepository;

    @Autowired
    DocumentoDependenciaRepository documentoDependenciaRepository;

    @Autowired
    ExpedienteEstadoRepository expedienteEstadoRepository;

    @Autowired
    CargosRepository cargosRepository;
    
    @Autowired
    UsuarioService usuarioService;
    
    @Autowired
    CargoService cargoService;

    // 2017-05-15 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech)
    // feature-80
    @Autowired
    TRDService trdService;

    /*
     * 2018-04-26 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151.
     */
    @Autowired
    private DocumentoDependenciaService documentoDependenciaService;

    @Autowired
    DataSource ds;
    
    Map<Usuario, Cargo> lectura;
    Map<Usuario, Cargo> escritura = new HashMap<Usuario, Cargo>();
    
    
    @RequestMapping(value = "/crear", method = RequestMethod.GET)
    @Transactional
    public String crearExpediente(Model model, Principal principal){
        final Usuario usuarioSesion = getUsuario(principal);
        Expediente expediente = new Expediente();
        model.addAttribute("expediente", expediente);
        List<Trd> trds = trdService.buildTrdsHierarchy(usuarioSesion);
        System.out.println("list trds"+ trds.toString());
        model.addAttribute("trds", trds);
        return "expediente-crear";
    }
    
    @RequestMapping(value = "/crear", method = RequestMethod.POST)
    @Transactional
    public String guardarExpediente(Expediente expediente, Model model, Principal principal, HttpServletRequest req){
        final Usuario usuarioSesion = getUsuario(principal);
        String texp = req.getParameter("expTipo");
        if (texp.equals("simple")) {
            return "expediente-seleccionar-usuarios";
        }else{
            return "expediente-seleccionar-trds";
        }
//        model.addAttribute("expediente", expediente);
//        List<Trd> trds = trdService.buildTrdsHierarchy(usuarioSesion);
//        System.out.println("list trds"+ trds.toString());
//        model.addAttribute("trds", trds);
    }
    
    
    @RequestMapping(value = "/asignar-usuario-expediente", method = RequestMethod.GET)
    public String listUsuarioExpediente(Expediente expediente, Model model, Principal principal, HttpServletRequest req){
       model.addAttribute("usuario1", lectura);
       model.addAttribute("usuario2", escritura);
       return "expediente-seleccionar-usuarios";
    } 
    
    @ResponseBody
    @RequestMapping(value = "/asignar-usuario-expediente/{exp}/{permiso}/{usuarioID}/{cargoID}", method = RequestMethod.POST)
    public ResponseEntity<?> asignarUsuarioExpediente(@PathVariable("exp") Integer expId, @PathVariable("permiso") Integer permiso, @PathVariable("usuarioID") Integer usuarioID,
            @PathVariable("cargoID") Integer cargoID, Principal principal){
        final Usuario usuario = usuarioService.findOne(usuarioID);
        final Cargo cargoUsu = cargoService.findOne(cargoID);
//        final Expediente expediente = ex
        if (lectura == null)
           lectura = new HashMap<Usuario, Cargo>();
        if (escritura == null)
           escritura = new HashMap<Usuario, Cargo>();
        if (permiso.equals(1))
            lectura.put(usuario, cargoUsu);
        if (permiso.equals(2))
            escritura.put(usuario, cargoUsu);
        System.out.println("permiso "+permiso+" - "+usuario.toString()+" - "+cargoUsu.toString());
        return ResponseEntity.ok(usuarioID);
    }
    
    @ResponseBody
    @RequestMapping(value = "/eliminar-usuario-expediente/{exp}/{usuarioID}", method = RequestMethod.POST)
    public ResponseEntity<?> eliminarUsuarioExpediente(@PathVariable("exp") Integer expId, @PathVariable("usuarioID") Integer usuarioID, Principal principal){
        Iterator<Map.Entry<Usuario, Cargo>> iter = lectura.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Usuario, Cargo> entry = iter.next();
            
            if(entry.getKey().getId().equals(usuarioID)){
                iter.remove();
            }
        }
        Iterator<Map.Entry<Usuario, Cargo>> iter2 = escritura.entrySet().iterator();
        while (iter2.hasNext()) {
            Map.Entry<Usuario, Cargo> entry = iter2.next();
            if(entry.getKey().getId().equals(usuarioID)){
                iter2.remove();
            }
        }
        return ResponseEntity.ok(usuarioID);
    }
    
    @ResponseBody
    @RequestMapping(value = "/cargos-usuario/{usuarioID}", method = RequestMethod.POST)
    public ResponseEntity<?> cargosUsuario(@PathVariable("usuarioID") Integer usuarioID, Principal principal){
        List<Object[]> cargos = cargoService.findCargosXusuario(usuarioID);
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        for (Object[] os : cargos) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        return ResponseEntity.ok(cargoDTOs);
    }

    /**
     * @param model
     * @param principal
     * @return exediente-list: La lista de expedientes que contienen documentos
     * en estado final.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, Principal principal) {

        Dependencia dependencia = getUsuario(principal).getDependencia();

        try {
            // Si es jefe de una dependencia padre muestra todos los expedientes
            if (dependencia != null && dependencia.getPadre() == null) {
                if (dependencia.getJefe().getId() == getUsuario(principal).getId()) {

                    JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
                    StringBuilder sbQuery = new StringBuilder();
                    sbQuery.append("SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
                            + " count(doc.doc_id), sum(PES.PES_FINAL) " + " FROM EXPEDIENTE EXP "
                            + " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
                            + " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID "
                            + " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
                            + " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
                            + " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " + " WHERE EXP.ACTIVO=1 ");
                    sbQuery.append(" AND (EE.ESEX_NOMBRE='Abierto' OR EE.ESEX_NOMBRE='Cerrado') ");
                    sbQuery.append(
                            " GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID ");
                    sbQuery.append("having count(doc.doc_id) = sum(PES.PES_FINAL) ");
                    sbQuery.append(" ORDER BY EXP.EXP_NOMBRE desc");

                    model.addAttribute("uid", getUsuario(principal).getId());

                    return "expediente-list";

                }
            }

        } catch (Exception e) {

        }
        // Si no es jefe de una dependencia padre solo ve los expedientes de su
        // dependencia

        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("SELECT EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID, "
                + " count(doc.doc_id), sum(PES.PES_FINAL) " + " FROM EXPEDIENTE EXP "
                + " JOIN ESTADO_EXPEDIENTE EE ON EE.ESEX_ID = EXP.ESEX_ID "
                + " JOIN DEPENDENCIA DEP ON EXP.DEP_ID=DEP.DEP_ID " + " JOIN DOCUMENTO DOC ON EXP.EXP_ID = DOC.EXP_ID "
                + " JOIN PROCESO_INSTANCIA PIN ON DOC.PIN_ID = PIN.PIN_ID "
                + " JOIN PROCESO_ESTADO PES ON PIN.PES_ID = PES.PES_ID " + " WHERE DEP.DEP_ID=");
        sbQuery.append(dependencia.getId());
        sbQuery.append(" AND  EXP.ACTIVO=1 ");
        sbQuery.append(" AND (EE.ESEX_NOMBRE='Abierto' OR EE.ESEX_NOMBRE='Cerrado') ");
        sbQuery.append(" GROUP BY EXP.EXP_ID, EXP.EXP_NOMBRE, EXP.CUANDO,EXP.DEP_ID, EXP.TRD_ID, EXP.ESEX_ID ");
        sbQuery.append("having count(doc.doc_id) = sum(PES.PES_FINAL) ");
        sbQuery.append(" ORDER BY EXP.EXP_NOMBRE desc");

        model.addAttribute("uid", getUsuario(principal).getId());

        return "expediente-list";

    }

    /**
     * Presenta la información del archivo del usuario, según los criterios
     * indicados.
     *
     * @param serieID ID de la serie documental. No obligatorio.
     * @param subserieID ID de la subserie documental. No obligatorio.
     * @param cargoID ID del cargo seleccionado como filtro de búsqueda. No
     * obligatorio.
     * @param anyo Año de filtro. No obligatorio. En caso de ser {@code null} o
     * cero, se presenta la información de todos los años.
     * @param model Modelo de información entre vista y controlador.
     * @param principal Objeto de información de usuario en sesión.
     * @return URL de la pantalla de búsqueda.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Modificación de nombre de parámetros en el método para mayor
     * entendimiento.
     *
     * 2018-05-03 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
     * feature-157: Adición de parámetros del filtro de año seleccionado.
     */
    @RequestMapping(value = "/carpeta", method = {RequestMethod.GET, RequestMethod.POST})
    public String carpeta(@RequestParam(value = "ser", required = false) Integer serieID, @RequestParam(value = "sub", required = false) Integer subserieID,
            @RequestParam(value = "cargoFiltro", required = false) Integer cargoID, @RequestParam(value = "anyo", required = false) Integer anyo,
            Model model, Principal principal) {
        model.addAttribute("ser", serieID);
        model.addAttribute("sub", subserieID);
        model.addAttribute("cargoFiltro", cargoID == null ? Integer.valueOf(0) : cargoID);
        /*
         * 2018-05-03 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Establecimiento del atributo de modelo correspondiente
         * al año seleccionado por el usuario y la lista de años desde el mínimo
         * configurado en las propiedades del sistema hasta el año actual de
         * forma descendente.
         */
        model.addAttribute("anyo", anyo == null ? Integer.valueOf(0) : anyo);
        model.addAttribute("filterYears", DateUtil.createListOfYears(minFilterSelectorYear, GregorianCalendar.getInstance().get(Calendar.YEAR), Boolean.FALSE));

        final Usuario usuarioSesion = getUsuario(principal);

        if (subserieID != null) {
            /*
             * 2017-05-05 jgarcia@controltechcg.com Issue #63
             * (SICDI-Controltech)
             *
             * 2017-05-11 jgarcia@controltechcg.com Issue #79
             * (SICDI-Controltech): Limitar la presentación de documentos
             * archivados por usuario en sesión y TRD.
             *
             * 2018-04-26 jgarcia@controltechcg.com Issue #151
             * (SICDI-Controltech) feature-151: Obtención de la información de
             * documentos archivados, a través de DTO. Retiro del mapa
             * "registrosArchivoMapa", ya que no es necesario por el uso de los
             * DTO.
             *
             * 2018-05-03 jgarcia@controltechcg.com Issue #157
             * (SICDI-Controltech) feature-157: Adición de filtro por año.
             */
            final Trd subserie = trdService.findOne(subserieID);
            final Cargo cargo = (cargoID == null) ? null : cargosRepository.findOne(cargoID);
            final List<DocumentoDependenciaArchivoDTO> documentos = documentoDependenciaService.findAllBySubserieAndUsuarioAndCargoAndAnyo(subserie, usuarioSesion, cargo, anyo);

            model.addAttribute("docs", documentos);

            model.addAttribute("subserie", subserie);
            model.addAttribute("retornaSerie", subserie.getSerie());
        } else if (serieID != null && serieID > 0) {
            /*
             * 2017-05-17 jgarcia@controltechcg.com Issue #86
             * (SICDI-Controltech) hotfix-86: Corrección para presentar
             * únicamente las subseries asociadas a la dependencia del usuario
             * en sesión en las pantallas de Archivo.
             *
             * 2018-04-25 jgarcia@controltechcg.com Issue #151
             * (SICDI-Controltech) feature-151: Uso de DTO de TRD y reducción de
             * proceso de búsquedas para mejora del rendimiento del proceso de
             * consulta.
             *
             * 2018-05-03 jgarcia@controltechcg.com Issue #157
             * (SICDI-Controltech) feature-157: Adición de filtro por año.
             */
            final Trd serie = trdService.findOne(serieID);
            final Cargo cargo = (cargoID == null) ? null : cargosRepository.findOne(cargoID);
            final List<TrdArchivoDocumentosDTO> subseries = trdService.findAllSubseriesWithArchivoBySerieAndUsuarioAndCargoAndAnyo(serie, usuarioSesion, cargo, anyo);


            /*
             * 2017-05-15 jgarcia@controltechcg.com Issue #80
             * (SICDI-Controltech) feature-80: Ordenamiento tipo número de
             * versión de las TRD por código.
             */
            trdService.ordenarPorCodigo(subseries);
            model.addAttribute("subseries", subseries);
            model.addAttribute("serie", serie);
            model.addAttribute("retornaSerie", 0);
        } else {
            /*
             * 2018-04-24 jgarcia@controltechcg.com Issue #151
             * (SICDI-Controltech) feature-151: Cambio en la presentación de la
             * pantalla de archivo para que únicamente presente las series TRD
             * en las que el usuario tiene documentos archivados.
             *
             * 2018-05-03 jgarcia@controltechcg.com Issue #157
             * (SICDI-Controltech) feature-157: Adición de filtro por año.
             */
            final Cargo cargo = (cargoID == null) ? null : cargosRepository.findOne(cargoID);
            final List<TrdArchivoDocumentosDTO> series = trdService.findAllSeriesWithArchivoByUsuarioAndCargoAndAnyo(usuarioSesion, cargo, anyo);

            /*
             * 2017-05-15 jgarcia@controltechcg.com Issue #80
             * (SICDI-Controltech) feature-80: Ordenamiento tipo número de
             * versión de las TRD por código.
             */
            trdService.ordenarPorCodigo(series);

            model.addAttribute("series", series);
        }

        return "expediente-carpeta";
    }

    /**
     * Construye un mapara de los registros de archivo, utilizando como llave el
     * ID del documento asociado.
     *
     * @param registros Lista de registros de archivo.
     * @return Mapa de registros de archivo.
     */
    // 2017-05-15 jgarcia@controltechcg.com Issue #82 (SICDI-Controltech)
    // feature-82
    private Map<String, DocumentoDependencia> buildMapaRegistrosArchivo(List<DocumentoDependencia> registros) {
        Map<String, DocumentoDependencia> map = new LinkedHashMap<>();

        for (DocumentoDependencia registro : registros) {
            map.put(registro.getDocumento().getId(), registro);
        }

        return map;
    }



    public Trd getTrd(String cod) {
        return trdRepository.findByCodigo(cod);
    }

    @Override
    public String nombre(Integer id) {
        return super.nombre(id);
    }



    
    @RequestMapping(value = "/expediente-vacio", method = RequestMethod.GET)
    public String expedienteVacio(Model model, Principal principal) {

        return "expediente-vacio";
    }

    /**
     * Carga el listado de cargos del usuario en sesión.
     *
     * @param principal
     * @return
     */
    @ModelAttribute("cargosXusuario")
    public List<CargoDTO> cargosXusuario(Principal principal) {
        Usuario usuarioSesion = getUsuario(principal);
        List<Object[]> list = cargosRepository.findCargosXusuario(usuarioSesion.getId());
        List<CargoDTO> cargoDTOs = new ArrayList<>();
        cargoDTOs.add(new CargoDTO(0, "TODOS"));
        for (Object[] os : list) {
            CargoDTO cargoDTO = new CargoDTO(((BigDecimal) os[0]).intValue(), (String) os[1]);
            cargoDTOs.add(cargoDTO);
        }
        return cargoDTOs;
    }
}
