package com.laamware.ejercito.doc.web.ctrl;

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
import com.laamware.ejercito.doc.web.repo.PlantillaRepository;
import com.laamware.ejercito.doc.web.serv.OFS;

@Controller
@RequestMapping(value = "/admin/plantilla")
public class PlantillaController extends UtilController {

	private static final Logger logger = LoggerFactory.getLogger(PlantillaController.class);

	@Autowired
	PlantillaRepository rep;

	@Autowired
	OFS ofs;

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

		try {
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

		redirect.addFlashAttribute(AppConstants.FLASH_SUCCESS, "La plantilla se ha guardado correctamente");
		return "redirect:/admin/plantilla";
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

}
