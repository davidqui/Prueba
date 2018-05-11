package com.laamware.ejercito.doc.web.repo;

import java.util.List;

import org.springframework.data.domain.Sort;

import com.laamware.ejercito.doc.web.entity.Proceso;

public interface ProcesoRepository extends GenJpaRepository<Proceso, Integer> {

    /**
     * Obtiene la lista de procesos activos.
     *
     * @return Lista de procesos activos.
     */
    /*
     * 2018-05-11 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public List<Proceso> findByActivoTrue();

    public List<Proceso> findByActivo(boolean b, Sort sort);

    /**
     * Obtiene la lista de procesos que no son alias de ninguno
     *
     * @param b indica si los procesos están o no activos
     * @param sort orden
     * @return lista de procesos con las características descritas
     */
    public List<Proceso> findByActivoAndAliasIsNull(boolean b, Sort sort);

}
