package com.laamware.ejercito.doc.web.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.ctrl.GenController.MapWrapper;
import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.DependenciaTrd;
import com.laamware.ejercito.doc.web.entity.GenDescriptor;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaTrdRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.CacheService;
import com.laamware.ejercito.doc.web.serv.TRDService;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping(DependenciaController.PATH)
public class DependenciaController extends UtilController {

    private static final Logger LOG = Logger.getLogger(DependenciaController.class.getName());

    static final String PATH = "/dependencias";
    
    //issue-179 constante llave del cache dependencias
    static final String DEPENDENCIAS_CACHE_KEY = "dependencias";
    
    //issue-179 constante llave del cache trd
    public final static String TRD_CACHE_KEY = "trd";

    
    /*
     * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
     * feature-179: Servicio de cache.
     */
    @Autowired
    private CacheService cacheService;
    
    @Autowired
    DependenciaRepository dependenciaRepository;

    @Autowired
    DependenciaTrdRepository dependenciaTrdRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
     * feature-170: Ordenamiento de subseries TRD por códigos.
     */
    @Autowired
    private TRDService trdService;

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String list(@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all,
            Model model) {

        List<Dependencia> list = findAll(all);
        model.addAttribute("list", list);
        model.addAttribute("all", all);
        return "dependencia-list";
    }

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = {"/create"}, method = RequestMethod.GET)
    public String create(Model model) {
        return "dependencia-create";
    }

    protected List<Dependencia> findAll(boolean all) {
        if (!all) {
            return dependenciaRepository.findByActivo(true);
        } else {
            return dependenciaRepository.findAll();
        }
    }

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = {"/edit"}, method = RequestMethod.GET)
    public String edit(Model model, HttpServletRequest req) {
        Integer id = Integer.valueOf(req.getParameter("id"));
        Dependencia dependencia = dependenciaRepository.findOne(id);
        model.addAttribute("entity", dependencia);
        return "dependencia-edit";
    }

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = {"/save"}, method = RequestMethod.POST)
    public String save(@Valid Dependencia e, HttpServletRequest req, BindingResult eResult, Model model,
            RedirectAttributes redirect, MultipartFile archivo) {

        try {
            String idS = req.getParameter("id");
            if (idS != null && idS.trim().length() > 0) {
                e.setId(Integer.parseInt(idS.trim()));
            }

            model.addAttribute("entity", e);

            if (e.getCodigo() == null || e.getCodigo().trim().length() == 0) {
                model.addAttribute(AppConstants.FLASH_ERROR, "El código de la dependencia es requerido");
                if (e.getId() != null) {
                    return "dependencia-edit";
                } else {
                    return "dependencia-create";
                }
            }

            if (e.getNombre() == null || e.getNombre().trim().length() == 0) {
                model.addAttribute(AppConstants.FLASH_ERROR, "El nombre de la dependencia es requerido");
                if (e.getId() != null) {
                    return "dependencia-edit";
                } else {
                    return "dependencia-create";
                }
            }

            if (e.getJefeEncargado() != null && e.getJefeEncargado().getId() != null) {

                if (e.getFchInicioJefeEncargado() == null || e.getFchFinJefeEncargado() == null) {
                    model.addAttribute(AppConstants.FLASH_ERROR,
                            "La fecha de inicio segundo comandante, está vacía. Por favor ingrese una fecha válida");
                    return "dependencia-edit";
                }
                if (e.getFchInicioJefeEncargado().compareTo(e.getFchFinJefeEncargado()) >= 0) {
                    model.addAttribute(AppConstants.FLASH_ERROR,
                            "La fecha de inicio segundo comandante, no puede ser MAYOR a la fecha de fin segundo comandante");
                    return "dependencia-edit";
                }

            }

            try {
                
                dependenciaRepository.save(e);
                /*
                * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
                * feature-179: se elimina el cache de dependencias.
                */
                cacheService.deleteKeyCache(DEPENDENCIAS_CACHE_KEY);
            } catch (Exception e2) {

                if (e2 instanceof org.springframework.dao.DataIntegrityViolationException) {

                    model.addAttribute(AppConstants.FLASH_ERROR,
                            "Ya existe una dependencia con el Código LDAP ingresado. Un Código LDAP puede estar asociado a una única dependencia");

                    if (e.getId() != null) {
                        return "dependencia-edit";
                    } else {
                        return "dependencia-create";
                    }
                }
                throw e2;
            }

            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Registro guardado con éxito");
            return "redirect:" + PATH + "?" + model.asMap().get("queryString");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            redirect.addFlashAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
            if (e.getId() != null) {
                return "dependencia-edit";
            } else {
                return "dependencia-create";
            }
        }
    }

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, HttpServletRequest req, RedirectAttributes redirect) {
        Integer id = Integer.valueOf(req.getParameter("id"));
        try {
            Dependencia dep = dependenciaRepository.findOne(id);
            dep.setActivo(false);
            dependenciaRepository.save(dep);
            /*
            * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
            * feature-179: se elimina el cache de dependencias.
            */
            cacheService.deleteKeyCache(DEPENDENCIAS_CACHE_KEY);
            model.addAttribute(AppConstants.FLASH_SUCCESS, "Dependencia eliminada con éxito");
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            model.addAttribute(AppConstants.FLASH_ERROR, ex.getMessage());
        }
        return "redirect:" + PATH;
    }

    protected void registerLists(Map<String, Object> map) {
        /**
         * 2017-10-05 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Ajuste de orden segun el peso de los
         * grados.
         */
        map.put("usuarios", usuarioRepository.findAllByActivoTrueOrderByGradoDesc());
    }

    /**
     * Carga el listado de dependencias al modelo
     *
     * @param model Modelo.
     * @return Lista de dependencias.
     */
    /*
     * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
     * Ordenamiento por peso. Modificación: variable y orden en que se presentan
     * las dependencias.
     */
    @ModelAttribute("dependencias")
    public List<Dependencia> dependencias(Model model) {
        List<Dependencia> list = dependenciaRepository.findByActivo(true, new Sort(Direction.ASC, "pesoOrden", "nombre"));
        model.addAttribute("dependencias", list);
        return list;
    }

    @ModelAttribute("lists")
    protected MapWrapper<String, Object> getLists() {
        HashMap<String, Object> m = new HashMap<>();
        registerLists(m);
        MapWrapper<String, Object> w = new MapWrapper<>(m);
        return w;
    }

    @ModelAttribute("descriptor")
    GenDescriptor getDescriptor() {
        GenDescriptor d = GenDescriptor.find(Dependencia.class);
        d.addAction("Subseries", PATH + "/trds-subseries", new String[]{"id"}, new String[]{"depId"});
        return d;
    }

    @ModelAttribute("activePill")
    public String getActivePill() {
        return "dependencia";
    }

    @ModelAttribute("templatePrefix")
    protected String getTemplatePrefix() {
        return "admin";
    }

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = "/trds-subseries", method = RequestMethod.GET)
    public String dependenciaTrd(@RequestParam("depId") Integer depId, Model model) {
        final Dependencia dependencia = dependenciaRepository.findOne(depId);
        model.addAttribute("dependencia", dependencia);

        /*
         * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
         * feature-170: Funcionalidad por servicio.
         */
        model.addAttribute("trds", trdService.findAllSubseriesActivasOrdenPorNombre());
        model.addAttribute("controller", this);

        return "admin-dependencia-trds";
    }

    @PreAuthorize("hasRole('ADMIN_DEPENDENCIAS')")
    @RequestMapping(value = "/trds-subseries", method = RequestMethod.POST)
    public String roles(@RequestParam("depId") Integer depId, @RequestParam("trd") Integer[] trds, Model model) {

        Dependencia dep = dependenciaRepository.findOne(depId);

        for (DependenciaTrd dtrd : dep.getTrds()) {
            Integer current = dtrd.getTrd().getId();
            boolean found = false;
            for (Integer t : trds) {
                if (t.equals(current)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                dependenciaTrdRepository.delete(dtrd);
            }
        }

        for (Integer t : trds) {
            boolean found = false;
            for (DependenciaTrd dtrd : dep.getTrds()) {
                if (t.equals(dtrd.getTrd().getId())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                DependenciaTrd newdtrd = new DependenciaTrd();
                newdtrd.setDependencia(dep);
                Trd trd = new Trd();
                trd.setId(t);
                newdtrd.setTrd(trd);
                dependenciaTrdRepository.save(newdtrd);
            }
        }
        
        /*
        * 2018-07-11 samuel.delgado@controltechcg.com Issue #179 (SICDI-Controltech)
        * feature-179: se elimina el cache de dependencias.
        */
        /**
         * 26/07/2018 hotfix-183 se cambia de pocisión el metodo para que se realiza fuera del for
         */
        cacheService.deleteKeyCache(DEPENDENCIAS_CACHE_KEY);
        cacheService.deleteKeyCache(TRD_CACHE_KEY+"_did-"+dep.getId());

        return String.format("redirect:%s", PATH);
    }

    public boolean has(Integer trdId, List<DependenciaTrd> trdLinks) {
        for (DependenciaTrd dependenciaTrd : trdLinks) {
            if (dependenciaTrd.getTrd().getId().equals(trdId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si todas las subseries activas han sido asignadas a la
     * dependencia.
     *
     * @param dependencia Dependencia.
     * @param subseries Lista de subseries activas.
     * @return {@code true} si y solo si todas las subseries activas en el
     * sistema están asignadas a la dependencia; de lo contrario, {@code false}.
     */
    /*
     * 2018-05-21 jgarcia@controltechcg.com Issue #170 (SICDI-Controltech)
     * feature-170.
     */
    public boolean hasAllSubseriesSelected(final Dependencia dependencia, final List<Trd> subseries) {
        final List<DependenciaTrd> selectedTrds = dependencia.getTrds();
        if (subseries.size() != selectedTrds.size()) {
            return false;
        }

        for (Trd subserie : subseries) {
            if (!has(subserie.getId(), selectedTrds)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Agrega el controlador
     *
     * @return Controlador.
     */
    @ModelAttribute("controller")
    public DependenciaController controller() {
        return this;
    }

    /**
     * Retorna el nombre de la dependencia Padre
     *
     * @param idPadre
     * @return
     */
    public String nombreDependenciaPadre(String idPadre) {
        try {
            if (idPadre != null && idPadre.trim().length() > 0) {

                Dependencia depPadre = dependenciaRepository.findOne(Integer.valueOf(idPadre.replaceAll("\\.", "")));
                if (depPadre != null) {
                    return depPadre.getNombre();
                } else {
                    return "";
                }
            }
            return "";
        } catch (NumberFormatException ex) {
            LOG.log(Level.SEVERE, null, ex);
            return idPadre;
        }
    }

    @ResponseBody
    @RequestMapping(value = "/dependencias.json", method = RequestMethod.GET)
    public List<Map<String, String>> dependencias(@RequestParam(value = "id", required = false) Integer id) {
        List<Map<String, String>> mapList = new ArrayList<>();
        List<Dependencia> dependencias;

        if (id != null) {
            /*
             * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
             * Ordenamiento por peso. Modificación: variable y orden en que se
             * presentan las dependencias.
             */
            dependencias = dependenciaRepository.findByActivoAndPadre(true, id, new Sort(Direction.ASC, "pesoOrden", "nombre"));
        } else {
            /*
             * 2017-04-11 jvargas@controltechcg.com Issue #45: DEPENDENCIAS:
             * Ordenamiento por peso. Modificación: variable y orden en que se
             * presentan las dependencias.
             */
            dependencias = dependenciaRepository.findByActivoAndPadreIsNull(true, new Sort(Direction.ASC, "pesoOrden", "nombre"));
        }

        for (Dependencia dependencia : dependencias) {
            HashMap<String, String> map = new HashMap<>();
            map.put("id", dependencia.getId().toString());
            map.put("nombre", dependencia.getSiglaNombre());
            mapList.add(map);
        }

        return mapList;
    }
}
