package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.ProcesoReinicioContDetalle;
import com.laamware.ejercito.doc.web.entity.Radicacion;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.RadicacionRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.util.Date;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Servicio con las reglas de negocio para el proceso de generacion del numero
 * de radicacion de los documentos.
 *
 * @author edison.gonzalez@controltechcg.com
 * @since Nov 14, 2017
 * @version 1.0.0 (feature-138).
 */
@Service
public class RadicadoService {

    /**
     * Log del servicio.
     */
    private static final Logger log = LoggerFactory.getLogger(RadicadoService.class);

    /**
     * Fecha en la cual la funcion FN_DOCUMENTO_RADICADO, dejara de generar el
     * número de radicación de los documentos.
     */
    final Date FECHA_CAMBIO = DateUtil.setDateTime(2018, 0, 1, 0, 0, 0);

    /**
     * Repositorio de maestro de documentos.
     */
    @Autowired
    DocumentoRepository documentRepository;

    /**
     * Contexto para acceder a la base de datos.
     */
    @Autowired
    private DataSource dataSource;

    /*
     * 2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    @Autowired
    private RadicacionRepository radicacionRepository;

    /**
     * Obtiene el numero de radicado segun la dependencia y el proceso.
     *
     * @param radId Identificador del tipo de radicado
     * @param dep_id Identificador de la dependencia.
     * @return Número de radicado del documento.
     */
    public String retornaNumeroRadicado(Integer dep_id, Integer radId) {
        Date fechaActual = new Date();

        if (fechaActual.compareTo(FECHA_CAMBIO) >= 0) {
            return documentRepository.getNumeroRadicado(dep_id, radId);
        } else {
            return documentRepository.getRadicado(dep_id);
        }
    }

    /**
     * Metodo que permite reiniciar la secuencia utilizada por el numero de
     * radicacion.
     *
     * @param nombreSecuencia Nombre de la secuencia
     * @param prcd Detalle del proceso de reinicio
     */
    public void reiniciarsecuencia(String nombreSecuencia, ProcesoReinicioContDetalle prcd) {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            String nextval = "SELECT " + nombreSecuencia + ".NEXTVAL FROM dual";
            Integer secuencia = jdbcTemplate.queryForObject(nextval, Integer.class);
            log.info("verificando valor actual de la secuencia [" + (secuencia - 1) + "]...");
            prcd.setUltimoValorSeq(secuencia - 1);
            log.info("Alterando la secuencia [" + nombreSecuencia + "]...");
            jdbcTemplate.execute("ALTER SEQUENCE " + nombreSecuencia + " INCREMENT BY -" + secuencia + " MINVALUE 0");
            jdbcTemplate.queryForObject(nextval, Integer.class);
            jdbcTemplate.execute("ALTER SEQUENCE " + nombreSecuencia + " INCREMENT BY 1");
            prcd.setNuevoValorSeq(1);
            log.info("Secuencia alterada [" + nombreSecuencia + "]...");
        } catch (DataAccessException e) {
            log.error("Error reiniciando la secuencia[" + nombreSecuencia + "]", e);
        }
    }

    /**
     * Busca el proceso de radicación correspondiente al proceso documental.
     *
     * @param proceso Proceso.
     * @return Instancia del proceso de radicación, o {@code null} si no hay
     * correspondencia.
     */
    /*
     * 2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Radicacion findByProceso(Proceso proceso) {
        return radicacionRepository.findByProceso(proceso);
    }

    /**
     * Enum de los tipos de radicación del sistema.
     */
    public enum EnumRadicacion {
        REGISTRO_DOCUMENTO(1),
        REGISTRO_DOCUMENTO_INTERNO(2),
        REGISTRO_DOCUMENTO_EXTERNO(3),
        TRANSFERENCIA_ARCHIVO(4),
        /*
         * 2018-05-18 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Nueva secuencia de registro de actas.
         */
        REGISTRO_ACTAS(5);

        private final Integer radicacion;

        EnumRadicacion(Integer s) {
            radicacion = s;
        }

        public Integer getValue() {
            return radicacion;
        }
    }
}
