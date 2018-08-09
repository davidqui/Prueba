package com.laamware.ejercito.doc.web.ctrl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.ExpedienteTransicion;
import com.laamware.ejercito.doc.web.entity.HProcesoInstancia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.DatabaseException;
import com.laamware.ejercito.doc.web.serv.DocumentoService;
import com.laamware.ejercito.doc.web.serv.ExpedienteTransicionService;
import com.laamware.ejercito.doc.web.serv.ProcesoService;

@Controller
@RequestMapping(value = ProcesoController.PATH)
public class ProcesoController extends UtilController {

    public static final String PATH = "/proceso";

    @Autowired
    ProcesoService procesoService;

    @Autowired
    UsuarioRepository usuarioRepository;
    
    /**
     *  2018-08-09 samuel.delgado@controltechcg.com Issue #181 (SIGDI-Controltech):
     *  servicio de transiciones del expediente.
     */
    @Autowired
    ExpedienteTransicionService expedienteTransicionService;
    /**
     *  2018-08-09 samuel.delgado@controltechcg.com Issue #181 (SIGDI-Controltech):
     *  servicio de documento
     */
    @Autowired
    DocumentoService documentoService;

    /**
     * Muestra el listado de procesos para seleccionar
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {
        /*
         * 2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Cambio a lista de procesos.
         */
        final List<Proceso> procesos = procesoService.getProcesosAutorizados();
        model.addAttribute("procesos", procesos);
        return "proceso-select";
    }

    /**
     * Crea una nueva instancia de proceso según la selección
     *
     * @param model
     * @param proId
     * @return
     */
    @RequestMapping(value = "/instancia/nueva", method = RequestMethod.GET)
    public String instanciaNueva(Model model, @RequestParam("proId") Integer proId, RedirectAttributes redirect,
            Principal principal) {
        String pin = null;
        try {
            pin = procesoService.instancia(proId, getUsuario(principal));
        } catch (DatabaseException e) {
            e.printStackTrace();
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error creando al instancia");
            return String.format("redirect:%s/list", PATH);
        }
        return String.format("redirect:%s/instancia?pin=%s", PATH, pin);
    }

    /**
     * Dependiendo de las características de la instancia se enruta el navegador
     * a una página personalizada o a la página genérica de gestión de instancia
     *
     * @param model
     * @param proId
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/instancia", method = RequestMethod.GET)
    public String instancia(Model model, @RequestParam("pin") String pin, RedirectAttributes redirect) {
        byPassFlassAttributes(redirect, model);
        Instancia i = procesoService.instancia(pin);
        return String.format("redirect:%s", i.url());
    }

    @RequestMapping(value = "/instancia/detalle", method = RequestMethod.GET)
    public String instanciaDetalle(Model model, @RequestParam("pin") String pin, RedirectAttributes redirect) {
        Instancia i = procesoService.instancia(pin);
        model.addAttribute("instancia", i);

        /*
		 * 2017-03-14 jgarcia@controltechcg.com Issue #19 (SIGDI-Incidencias01):
		 * Carga de mapa de usuarios para búsqueda de usuario modificación para
		 * la presentación.
         */
        Map<String, Usuario> quienModMap = new LinkedHashMap<>();
        List<HProcesoInstancia> historias = i.historia();
        for (HProcesoInstancia historia : historias) {
            Integer quienModID = historia.getQuienMod();
            Usuario quienMod = usuarioRepository.findOne(quienModID);
            quienModMap.put(quienModID.toString(), quienMod);
        }
        model.addAttribute("quienModMap", quienModMap);

        /*
		 * 2017-03-15 jgarcia@controltechcg.com Issue #19 (SIGDI-Incidencias01):
		 * Se modifica el proceso de presentación del histórico de la instancia
		 * del proceso, para únicamente mostrar aquellos registros que
		 * corresponden a transiciones entre estados.
         */
        List<HProcesoInstancia> historiasTransiciones = buildHistoriasTransiciones(historias);
        model.addAttribute("historiasTransiciones", historiasTransiciones);
        
        /**
         *  2018-08-09 samuel.delgado@controltechcg.com Issue #181 (SIGDI-Controltech):
         *  se agregan transiciones del expediente.
         */
        // Obtiene el documento registrado en las variables de la instancia
        String docId = i.getVariable(Documento.DOC_ID);
        Documento doc = documentoService.buscarDocumento(docId);
        List<ExpedienteTransicion> expedienteTransiciones =  expedienteTransicionService.listaTrasicionesXdocumento(doc);
        model.addAttribute("expedienteTransiciones", expedienteTransiciones);
        
        return "proceso-instancia-detalle";

    }

    /**
     * Construye una lista de los registros históricos de la instancia del
     * proceso, únicamente con aquellos que corresponden a transiciones entre
     * estados.
     *
     * @param historias Lista completa de historias de la instancia de un
     * proceso.
     * @return Lista con las historias correspondientes a transiciones entre
     * estados, ordenadas de forma descendente.
     */
    // 2017-03-15 jgarcia@controltechcg.com Issue #19 (SIGDI-Incidencias01)
    private List<HProcesoInstancia> buildHistoriasTransiciones(List<HProcesoInstancia> historias) {
        List<HProcesoInstancia> historiasTransiciones = new ArrayList<>();

        Map<Integer, List<HProcesoInstancia>> mapaHistoriasPorEstado = buildMapaHistoriasPorEstado(historias);
        Set<Integer> estadoIDs = mapaHistoriasPorEstado.keySet();

        for (Integer estadoID : estadoIDs) {
            List<HProcesoInstancia> list = mapaHistoriasPorEstado.get(estadoID);
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    HProcesoInstancia historia = list.get(0);
                    historiasTransiciones.add(historia);
                } else {
                    for (HProcesoInstancia historia : list) {
                        if (!historia.getQuienMod().equals(historia.getAsignado().getId())) {
                            historiasTransiciones.add(historia);
                        }
                    }
                }
            }
        }

        Collections.sort(historiasTransiciones, new Comparator<HProcesoInstancia>() {

            @Override
            public int compare(HProcesoInstancia o1, HProcesoInstancia o2) {
                return o2.getCuandoMod().compareTo(o1.getCuandoMod());
            }

        });

        return historiasTransiciones;
    }

    /**
     * Construye un mapa de las historias, agrupadas por el estado.
     *
     * @param historias Lista completa de historias de la instancia de un
     * proceso.
     * @return Mapa con la lista completa de historias, pero agrupadas por el
     * estado.
     */
    // 2017-03-15 jgarcia@controltechcg.com Issue #19 (SIGDI-Incidencias01)
    private Map<Integer, List<HProcesoInstancia>> buildMapaHistoriasPorEstado(List<HProcesoInstancia> historias) {
        Map<Integer, List<HProcesoInstancia>> map = new LinkedHashMap<Integer, List<HProcesoInstancia>>();

        for (HProcesoInstancia historia : historias) {
            Integer estadoID = historia.getEstado().getId();
            List<HProcesoInstancia> list = map.get(estadoID);
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(historia);
            map.put(estadoID, list);
        }

        return map;
    }
}
