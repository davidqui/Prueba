package com.laamware.ejercito.doc.web.serv;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para las operaciones de negocio de dependencias.
 *
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017
 */
// 2017-05-15 jgarcia@controltechcg.com Issue #78 (SICDI-Controltech) feature-78
@Service
public class DependenciaService {

    private static final Logger LOG = Logger.getLogger(DependenciaService.class.getName());

    @Autowired
    private DependenciaRepository dependenciaRepository;

    /**
     * Busca la unidad de una dependencia.
     *
     * @param dependencia Dependencia.
     * @return Dependencia unidad.
     */
    public Dependencia buscarUnidad(Dependencia dependencia) {
        Dependencia unidad = dependencia;
        Integer padreID = dependencia.getPadre();

        /*
        * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Se realiza la validacion
        * para que muestre la unidad segun el indicador de envio documentos.
         */
        if (dependencia.getDepIndEnvioDocumentos() != null && dependencia.getDepIndEnvioDocumentos()) {
            return dependencia;
        }

        while (padreID != null) {
            unidad = dependenciaRepository.findOne(padreID);
            if (unidad.getDepIndEnvioDocumentos() != null && unidad.getDepIndEnvioDocumentos()) {
                return unidad;
            }

            padreID = unidad.getPadre();
        }

        return unidad;
    }

    /**
     * Retira el usuario como jefe asignado (jefe principal o jefe encargado) de
     * las dependencias asociadas.
     *
     * @param usuario Usuario a retirar como jefe encargado.
     * @return Lista de las dependencias activas de las cuales el usuario se
     * retiró como jefe asignado.
     */
    // 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
    // hotfix-99
    public List<Dependencia> retirarUsuarioComoJefeAsignado(Usuario usuario) {
        final Integer usuarioId = usuario.getId();
        List<Dependencia> dependenciasAsignadas = dependenciaRepository.findActivoByJefeAsignado(usuarioId);

        for (Dependencia dependencia : dependenciasAsignadas) {
            try {
                retirarUsuarioComoJefeAsignado(usuarioId, dependencia);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "" + usuarioId, ex);
            }
        }

        return dependenciasAsignadas;
    }

    /**
     * Retira el usuario como jefe asignado (jefe principal o jefe encargado) de
     * la dependencia asociada.
     *
     * @param usuarioId ID del usuario a retirar como jefe encargado.
     * @param dependencia Dependencia.
     */
    // 2017-06-01 jgarcia@controltechcg.com Issue #99 (SICDI-Controltech)
    // hotfix-99
    private void retirarUsuarioComoJefeAsignado(final Integer usuarioId, Dependencia dependencia) {
        final Usuario jefe = dependencia.getJefe();
        if (jefe != null && jefe.getId().equals(usuarioId)) {
            dependencia.setJefe(null);
        }

        final Usuario jefeEncargado = dependencia.getJefeEncargado();
        if (jefeEncargado != null && jefeEncargado.getId().equals(usuarioId)) {
            dependencia.setJefeEncargado(null);
        }

        dependenciaRepository.saveAndFlush(dependencia);
    }

    /**
     * Obtiene el jefe activo de la dependencia.
     *
     * @param dependencia Dependencia.
     * @return En caso que la dependencia tenga un jefe encargado y la fecha del
     * sistema se encuentre dentro del rango de asignación del jefe encargado y
     * este se encuentre activo en el sistema, se retorna el jefe encargado; de
     * lo contrario, se retorna el jefe principal de la dependencia.
     */
    /*
     * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01)
     *
     * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01):
     * Paso a static.
     *
     * 2018-04-12 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Extracción desde DocumentoController para poder utilizar el
     * método desde otros componentes.
     */
    public Usuario getJefeActivoDependencia(final Dependencia dependencia) {
        final Usuario jefe = dependencia.getJefe();
        final Usuario jefeEncargado = dependencia.getJefeEncargado();

        if (jefeEncargado == null) {
            return jefe;
        }

        if (!jefeEncargado.getActivo()) {
            return jefe;
        }

        Date fechaInicioJefeEncargado = dependencia.getFchInicioJefeEncargado();
        if (fechaInicioJefeEncargado == null) {
            return jefe;
        }

        fechaInicioJefeEncargado = DateUtil.setTime(fechaInicioJefeEncargado, DateUtil.SetTimeType.START_TIME);

        Date fechaFinJefeEncargado = dependencia.getFchFinJefeEncargado();
        if (fechaFinJefeEncargado == null) {
            return jefe;
        }

        fechaFinJefeEncargado = DateUtil.setTime(fechaFinJefeEncargado, DateUtil.SetTimeType.END_TIME);

        Date fechaActual = new Date(System.currentTimeMillis());

        if (fechaInicioJefeEncargado.compareTo(fechaActual) <= 0 && fechaActual.compareTo(fechaFinJefeEncargado) <= 0) {
            return jefeEncargado;
        }

        return jefe;
    }

    /**
     * Busca una dependencia por su ID.
     *
     * @param id ID.
     * @return Instancia de la dependencia correspondiente al ID, o {@code null}
     * en caso que no exista en el sistema.
     */
    /*
     * 2018-04-12 jgarcia@controltechcg.com Issue #156 (SICDI-Controltech)
     * feature-156: Se crea esta función con el fin de poder utilizar el
     * servicio sin necesidad de invocar el repositorio.
     */
    public Dependencia findOne(final Integer id) {
        return dependenciaRepository.findOne(id);
    }

    /**
     * Busca la súper dependencia de una dependencia.
     *
     * @param dependencia Dependencia.
     * @return Súper dependencia.
     */
    /*
     * 2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Se mueve este método proveniente de DocumentoController.
     */
    public Dependencia getSuperDependencia(Dependencia dependencia) {
        /*
	 * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Validacion para que tenga en cuenta el
	 * campo Indicador de envio documentos.
         */
        if (dependencia.getPadre() == null || (dependencia.getDepIndEnvioDocumentos() != null && dependencia.getDepIndEnvioDocumentos())) {
            return dependencia;
        }

        Dependencia jefatura = dependencia;
        Integer jefaturaId = dependencia.getPadre();
        while (jefaturaId != null) {
            jefatura = dependenciaRepository.getOne(jefaturaId);

            if (jefatura.getDepIndEnvioDocumentos() != null && jefatura.getDepIndEnvioDocumentos()) {
                return jefatura;
            }

            jefaturaId = jefatura.getPadre();
        }
        return jefatura;
    }

}
