package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.DependenciaCopiaMultidestino;
import com.laamware.ejercito.doc.web.repo.DependenciaCopiaMultidestinoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DependenciaCopiaMultidestino}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/10/2018 (SICDI-Controltech Issue #156)
 */
@Service
public class DependenciaCopiaMultidestinoService {

    @Autowired
    private DependenciaCopiaMultidestinoRepository multidestinoRepository;
}
