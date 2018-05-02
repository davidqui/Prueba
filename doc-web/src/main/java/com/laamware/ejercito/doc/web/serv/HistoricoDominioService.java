package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.HistoricoDominio;
import com.laamware.ejercito.doc.web.repo.HistoricoDominioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para {@link HistoricoDominio}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Service
public class HistoricoDominioService {

    @Autowired
    private HistoricoDominioRepository historicoDominioRepository;
}
