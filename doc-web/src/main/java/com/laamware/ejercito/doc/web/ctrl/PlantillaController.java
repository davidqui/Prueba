package com.laamware.ejercito.doc.web.ctrl;

import com.aspose.words.Bookmark;
import com.aspose.words.BookmarkCollection;
import com.aspose.words.Document;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Plantilla;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.entity.WildcardPlantilla;
import com.laamware.ejercito.doc.web.repo.PlantillaRepository;
import com.laamware.ejercito.doc.web.serv.OFS;
import com.laamware.ejercito.doc.web.serv.WildcardPlantillaService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping(value = "/admin/plantilla")
public class PlantillaController extends UtilController {

	private static final Logger logger = LoggerFactory.getLogger(PlantillaController.class);

        private static final String PLANTILLA_SELECCIONAR = "plantilla-seleccionar";
        
	@Autowired
	PlantillaRepository rep;

	@Autowired
	OFS ofs;
        /*
            2018-06-27 samuel.delgado@controltechcg.com feature #176 : servicio para los wildcards 
            de plantillas validadas
        */
        @Autowired
        private WildcardPlantillaService wildcardPlantillaService;
        /*
            2018-06-21 samuel.delgado@controltechcg.com feature #176 : valida si se verifica
            los bookmarks y los wildcars para el control de versiones.
        */
        @Value("${com.mil.imi.sicdi.plantillas.validar}")
        private Boolean VALIDAR_PLANTILLAS;
        
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String list(Model model,
			@RequestParam(value = "all", required = false, defaultValue = "false") Boolean all) {
		model.addAttribute("all", all);
		List<Plantilla> plantillas = null;
		if (all) {
			plantillas = rep.findAll(new Sort(Direction.ASC, "codigo"));
		} else {
			plantillas = rep.findByActivo(true, new Sort(Direction.ASC, "codigo"));
		}
		model.addAttribute("list", plantillas);
		return "admin-plantilla-list";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String create(Model model) {
		Plantilla plantilla = new Plantilla();
		model.addAttribute("plantilla", plantilla);
		return "admin-plantilla-edit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, @RequestParam(value = "id") Integer id) {
		Plantilla plantilla = rep.getOne(id);
		model.addAttribute("plantilla", plantilla);
                model.addAttribute("wildCardsPlantilla", plantilla.getWildCards());
		return "admin-plantilla-edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model, @Valid Plantilla plantilla, BindingResult errors,
			@RequestParam(value = "file", required = false) MultipartFile file, RedirectAttributes redirect) {
		model.addAttribute("plantilla", plantilla);
		/*
		 * 2017-02-13 jgarcia@controltechcg.com Issue #116: Se busca si ya
		 * existe una plantilla previa (determinar tiempo de edición) para
		 * comparar si hay cambio de plantilla Word. En caso que el usuario no
		 * realice dicho cambio, se mantiene la información previamente
		 * ingresada.
		 */
		Plantilla plantillaActual = null;
		if (plantilla.getId() != null) {
			plantillaActual = rep.getOne(plantilla.getId());
		}

		boolean toEdit = (plantillaActual != null);

		if (plantilla.getNombre() == null || plantilla.getNombre().trim().length() == 0) {
			model.addAttribute(AppConstants.FLASH_ERROR, "Debe ingresar el nombre de la plantilla");
			return "admin-plantilla-edit";
		}

                String[] fieldNames = null;
		try {
                        /*
                            2018-06-21 samuel.delgado@controltechcg.com feature #176 : se 
                            conprueba que el archivo ingrasado contenga los bookmarks requeridos
                        */
                        if (VALIDAR_PLANTILLAS) {
                            Document documentAspose = new Document(file.getInputStream());
                            BookmarkCollection bookmarks = documentAspose.getRange().getBookmarks();
                            String nombrePlantilla = null;
                            String versionPlantilla = null;
                            for (Bookmark bookmark : bookmarks) {
                                try {
                                    String key = bookmark.getName().split("_")[0];
                                    String value = bookmark.getName().split("_")[1];
                                    if (key.equals("nombre")) {
                                        nombrePlantilla = value;
                                    }
                                    if (key.equals("version")) {
                                        versionPlantilla = value;
                                    }
                                } catch (Exception ex) {}
                            }
                            if (nombrePlantilla == null || versionPlantilla == null){
                                model.addAttribute(AppConstants.FLASH_ERROR, "Existen errores en el versionamiento del documento");
                                return "admin-plantilla-edit";
                            }

                            System.out.println("NOMBRE PLANTILLA = "+nombrePlantilla+" Version = "+versionPlantilla);

                            plantilla.setBookmarkName(nombrePlantilla);
                            plantilla.setBookmarkValue(versionPlantilla);
                            
                            fieldNames = documentAspose.getMailMerge().getFieldNames();
                            
                        }else{
                            plantilla.setBookmarkName(null);
                            plantilla.setBookmarkValue(null);
                        }
                        
			// Issue #116
			boolean defaultFileContentType = (file.getContentType() != null)
					&& (file.getContentType().equalsIgnoreCase("application/octet-stream"));
			boolean validFileContentType = (file.getContentType() != null) && (file.getContentType()
					.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));

			// Issue #116
			if (!validFileContentType && (!toEdit || (!defaultFileContentType))) {
				model.addAttribute(AppConstants.FLASH_ERROR, "El formato de la plantilla debe ser Office .DOCX");
				return "admin-plantilla-edit";
			}

			// Issue #116
			if (validFileContentType && !defaultFileContentType) {
				String fileId = ofs.saveAsIs(file.getBytes(), file.getContentType());
				plantilla.setDocx4jDocumento(fileId + ".docx");
			} else if (toEdit) {
				plantilla.setDocx4jDocumento(plantillaActual.getDocx4jDocumento());
			}

		} catch (Exception e) {
			e.printStackTrace();
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
			model.addAttribute(AppConstants.FLASH_ERROR, "Ocurrió un error inesperado: " + e.getMessage());
			return "admin-plantilla-edit";
		}

		try {

			plantilla.setTipo("DOCX");
			plantilla.setCodigo("PLANTILLA_DOCX");
			rep.save(plantilla);

		} catch (Exception e) {
			model.addAttribute("plantilla", plantilla);
			model.addAttribute(AppConstants.FLASH_ERROR, "Ha ocurrido un error cuando se guardaba");
			logger.error("Guardando una plantilla: " + e.getMessage(), e);
			return "admin-plantilla-edit";
		}
                
                if (!VALIDAR_PLANTILLAS || (VALIDAR_PLANTILLAS && fieldNames != null && fieldNames.length < 1)) {
                    redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "La plantilla se ha guardado correctamente");
                    return "redirect:/admin/plantilla";
                }else{
                    model.addAttribute(AppConstants.FLASH_SUCCESS, "La plantilla se ha guardado correctamente");
                    model.addAttribute("fieldNames", fieldNames);
                    model.addAttribute("plantilla", plantilla);
                    model.addAttribute("controller", this);
                    return PLANTILLA_SELECCIONAR;
                }
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(@RequestParam("id") Integer id, RedirectAttributes redirect) {
		try {
			Plantilla p = rep.getOne(id);
			p.setActivo(Boolean.FALSE);
			rep.save(p);
			redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Plantilla eliminada: " + p.getNombre());
		} catch (Exception e) {
			redirect.addFlashAttribute(AppConstants.FLASH_ERROR, "No se pudo eliminar la plantilla");
			logger.error("Eliminando plantilla", e);
		}
		return "redirect:/admin/plantilla";
	}
        
        @RequestMapping(value = "/seleccionar", method = RequestMethod.POST)
        public String seleccionarWildcards(@RequestParam(value="wildcards", required = false) String[] wildcards, @RequestParam("id") String idPlantilla,
                Principal principal, RedirectAttributes redirect){
            List<WildcardPlantilla> wildcardsPlantilla = new ArrayList<>();
            Usuario logueado = getUsuario(principal);
            if (wildcards != null) {
                for (String fieldName : wildcards) {
                    List<WildcardPlantilla> wildcardAsociado = wildcardPlantillaService.findByText(fieldName);
                    if (wildcardAsociado.isEmpty()) {
                        WildcardPlantilla w = new WildcardPlantilla();
                        w.setTexto(fieldName);
                        w.setQuien(logueado);
                        w.setCuando(new Date());
                        w.setQuienMod(logueado);
                        w.setCuandoMod(new Date());
                        w = wildcardPlantillaService.crearWildcardPlantilla(w);
                        wildcardsPlantilla.add(w);
                    }else{
                        wildcardsPlantilla.add(wildcardAsociado.get(0));
                    }
                }
            }
            Plantilla plantilla = rep.findOne(Integer.parseInt(idPlantilla));
            plantilla.setWildCards(wildcardsPlantilla);
            rep.saveAndFlush(plantilla);
            redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "Wildcards agregados a plantilla.");
            return "redirect:/admin/plantilla";
        }
}
