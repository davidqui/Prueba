package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Cargo;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.util.NumeroVersionComparator;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Servicio para las operaciones de las TRD.
 *
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #80 (SICDI-Controltech) feature-80
@Service
public class TRDService {

    @Autowired
    private TrdRepository trdRepository;

    /**
     * Comparador de código de TRD.
     *
     * @author jgarcia@controltechcg.com
     * @since May 15, 2017
     */
    private class TrdCodigoComparator implements Comparator<Trd> {

        private final NumeroVersionComparator numeroVersionComparator = new NumeroVersionComparator();

        @Override
        public int compare(Trd trd1, Trd trd2) {
            final String codigo1 = trd1.getCodigo();
            final String codigo2 = trd2.getCodigo();
            return numeroVersionComparator.compare(codigo1, codigo2);
        }
    }

    /**
     * Ordena la lista de TRD por el código, en ordenamiento tipo número de
     * versión.
     *
     * @param trds Lista de TRD.
     */
    public void ordenarPorCodigo(List<Trd> trds) {
        Collections.sort(trds, new TrdCodigoComparator());
    }

    /**
     * Obtiene la lista de series TRD que contienen documentos archivados para
     * el usuario, según el cargo indicado.
     *
     * @param usuario Usuario. Obligatorio.
     * @param cargo Cargo. Opcional.
     * @return Lista de series TRD que para el usuario (y el cargo, en caso de
     * ser indicado) tienen registros de documentos archivados.
     */
    /*
     * 2018-04-24 jgarcia@controltechcg.com Issue #151 (SICDI-Controltech)
     * feature-151: Definición de los métodos para presentación de la pantalla
     * de consulta de archivo a través del servicio de TRD.
     */
    public List<Trd> findAllSeriesWithArchivoByUsuarioAndCargo(final Usuario usuario, final Cargo cargo) {
        if (cargo == null) {
            return trdRepository.findAllSeriesWithArchivoByUsuario(usuario.getId());
        }

        return trdRepository.findAllSeriesWithArchivoByUsuarioAndCargo(usuario.getId(), cargo.getId());
    }
}
