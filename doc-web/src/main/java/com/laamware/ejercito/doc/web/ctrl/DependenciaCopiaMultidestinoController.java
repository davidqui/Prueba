package com.laamware.ejercito.doc.web.ctrl;

import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.serv.DependenciaCopiaMultidestinoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156)
 */
@Controller
@RequestMapping(DependenciaCopiaMultidestinoController.PATH)
public class DependenciaCopiaMultidestinoController {

    static final String PATH = "/dependencia-copia-multidestino";

    @Autowired
    private DependenciaCopiaMultidestinoService multidestinoService;

}
