package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.LinkOWARegistroUso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.LinkOWARegistroUsoRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio de negocio para {@link LinkOWARegistroUso}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 05/02/2018 Issue #159 (SICDI-Controltech) feature-159
 */
@Service
public class LinkOWARegistroUsoService {

    @Autowired
    private LinkOWARegistroUsoRepository repository;

    /**
     * Registra un acceso al enlace hacia OWA a través del sistema.
     *
     * @param usuario Usuario que aplicó la acción.
     * @param url URL a la que fue redirigido.
     * @return Instancia del registro realizado.
     */
    public LinkOWARegistroUso registrar(final Usuario usuario, final String url) {
        LinkOWARegistroUso registro = new LinkOWARegistroUso(usuario, new Date(), usuario.getDominio(), url);
        registro = repository.saveAndFlush(registro);
        return registro;
    }
}
