package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.serv.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador para operaciones de usuario a través de finder.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/29/2018 Issue #162 (SICDI-Controltech) feature-162.
 */
@Controller
@RequestMapping("/finder/usuario")
public class UsuarioFinderController {

    /**
     * Tamaño de la lista de presentación en la página de búsqueda de usuarios.
     */
    private static final int BUSQUEDA_PAGE_SIZE = 10;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Presenta el formulario de búsqueda de usuario destino, para el finder
     * correspondiente.
     *
     * @param criteria Criteria de búsqueda.
     * @param pageIndex Índice de la página a presentar.
     * @param model Modelo de UI.
     * @return Nombre del template Freemarker del formulario.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Cambio de controlador para que queda unificado con las
     * operaciones de usuario. Cambia path de /formulario-buscar-usuario a
     * /finder-buscar-usuario. Cambio del nombre del template de
     * "transferencia-archivo-buscar-usuario" a "finder-buscar-usuario".
     */
    @RequestMapping(value = "/finder-buscar-usuario", method = {RequestMethod.GET, RequestMethod.POST})
    public String presentarFormularioBusquedaUsuarioPOST(@RequestParam(value = "criteria", required = false) String criteria,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") Integer pageIndex, Model model) {
        final Page<Usuario> page = usuarioService.findAllByCriteriaSpecification(criteria, pageIndex, BUSQUEDA_PAGE_SIZE);

        model.addAttribute("criteria", criteria);
        model.addAttribute("usuarios", page.getContent());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("totalPages", page.getTotalPages());

        return "finder-buscar-usuario";
    }

}
