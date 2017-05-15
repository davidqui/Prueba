package com.laamware.ejercito.doc.web.ctrl;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.laamware.ejercito.doc.web.entity.Formato;
import com.laamware.ejercito.doc.web.repo.FormatoRepository;
import com.laamware.ejercito.doc.web.serv.OFS;
import com.laamware.ejercito.doc.web.serv.OFSEntry;

@Controller
@RequestMapping(value = "/formato")
public class FormatoController extends UtilController {

	@Autowired
	FormatoRepository formatoRepository;

	@Autowired
	OFS ofs;

	/**
	 * Obtiene el listado de formatos de una subserie
	 * 
	 * @param subId
	 *            Es el identificador de la subserie
	 * 
	 * @return
	 */
	@RequestMapping(value = "/get-list", method = RequestMethod.GET)
	public @ResponseBody List<Formato> InfoDoc(
			@RequestParam("subId") Integer subId) {

		List<Formato> formatos = formatoRepository.findByTrdId(subId);

		return formatos;

	}

	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public void download(@PathVariable("id") String id, HttpServletResponse resp) {

		ServletOutputStream os = null;
		ByteArrayInputStream is = null;
		try {
			OFSEntry entry = ofs.read(id);
			byte[] content = entry.getContent();
			resp.setContentLength((int) content.length);
			resp.setContentType(entry.getContentType());

			Formato fmt = formatoRepository.findByContenido(id);

			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					fmt.getOriginal());
			resp.setHeader(headerKey, headerValue);

			// Write response
			os = resp.getOutputStream();
			is = new ByteArrayInputStream(content);
			IOUtils.copy(is, os);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
