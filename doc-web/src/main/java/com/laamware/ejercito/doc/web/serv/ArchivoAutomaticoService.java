package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Proceso;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.CargosRepository;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Servicio que permite aplicar el archivo automático.
 *
 * @author jgarcia@controltechcg.com
 * @since Abril 18, 2017
 *
 */
/*
 * 2017-04-18 jgarcia@controltechcg.com Issue #50 (SICDI-Controltech)
 */
@Service
public class ArchivoAutomaticoService {

    private static final Logger LOG = LoggerFactory.getLogger(ArchivoAutomaticoService.class);

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DocumentoDependenciaRepository documentoDependenciaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CargosRepository cargosRepository;

    @Autowired
    DependenciaRepository dependenciaRepository;

    /**
     * Permite aplicar el archivo automático para los procesos de documento,
     * tras la transición de firma o entrega de documento.
     *
     * @param documento Documento.
     * @param usuarioSesion Usuario en sesión
     */
    public void archivarAutomaticamente(Documento documento, Usuario usuarioSesion) {

        /*
         * 2017-12-14 edison.gonzalez@controltechcg.com Issue #151
         * (SICDI-Controltech) feature-151: Ajuste de cargos en el archivo de
         * documentos.
         */
        final Usuario usuarioArchivador;
        final Cargo cargoArchivador;
        final Instancia instancia = documento.getInstancia();
        final Proceso proceso = instancia.getProceso();

        if (Objects.equals(proceso.getId(), Proceso.ID_TIPO_PROCESO_REGISTRAR_Y_CONSULTAR_DOCUMENTOS)) {
            usuarioArchivador = getUsuarioArchivadorAutomaticoRegistroDocumento(documento, usuarioSesion);
            cargoArchivador = usuarioArchivador.getUsuCargoPrincipalId();
        } else {
            usuarioArchivador = usuarioRepository.findOne(documento.getQuien());
            cargoArchivador = cargosRepository.findOne(documento.getCargoIdElabora().getId());
        }

        /*
         * 2017-12-14 edison.gonzalez@controltechcg.com Issue #144
         * (SICDI-Controltech) hotfix-144: Duplicidad de registros.
         */
        List<DocumentoDependencia> docDependencias = documentoDependenciaRepository.findAllActivoByUsuarioAndDocumento(usuarioArchivador.getId(), documento.getId());

        if (docDependencias.size() > 0) {
            for (DocumentoDependencia documentoDependencia : docDependencias) {
                LOG.info("Warning: Ya se encontro dato en la tabla DOCUMENTO_DEPENDENCIA con los siguientes atributos: DCDP_ID [" + documentoDependencia.getId() + "], TRD_ID [" + documentoDependencia.getTrd().getId() + "], DEP_ID [" + documentoDependencia.getDependencia().getId() + "]" + "], DOC_ID [" + documentoDependencia.getDocumento().getId() + "], QUIEN [" + documentoDependencia.getQuien() + "], CUANDO [" + documentoDependencia.getCuando() + "]");
            }
            return;
        }

        DocumentoDependencia documentoDependenciaArchivar = new DocumentoDependencia();
        documentoDependenciaArchivar.setDependencia(usuarioArchivador.getDependencia());
        documentoDependenciaArchivar.setDocumento(documento);
        documentoDependenciaArchivar.setTrd(documento.getTrd());
        documentoDependenciaArchivar.setCargo(cargoArchivador);
        documentoDependenciaRepository.save(documentoDependenciaArchivar);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "UPDATE DOCUMENTO_DEPENDENCIA SET DOCUMENTO_DEPENDENCIA.QUIEN = ? WHERE DOCUMENTO_DEPENDENCIA.DCDP_ID = ?";
        Object[] params = {usuarioArchivador.getId(), documentoDependenciaArchivar.getId()};
        jdbcTemplate.update(sql, params);
    }

    /**
     * Permite saber el usuario quien se le archivara el documento en el caso de
     * los procesos de registro de documentos.
     *
     * @param d Documento
     * @param usuarioSesion Usuario
     * @return Usuario archivador del proceso de registro de documentos
     */
    public Usuario getUsuarioArchivadorAutomaticoRegistroDocumento(Documento d, Usuario usuarioSesion) {

        /*
         * 2017-02-06 jgarcia@controltechcg.com Issue# 150: Para asignación de
         * Jefe Máximo, se busca la Super Dependencia Destino. Sobre esta se
         * consulta si tiene un Jefe Segundo (Encargado). Si lo tiene se compara
         * el rango de fechas asignado en la Super Dependencia. En caso que la
         * fecha actual corresponda al rango, se selecciona el Jefe Segundo
         * (Encargado). De lo contrario, a las reglas anteriormente descritas,
         * se selecciona el Jefe de la Super Dependencia.
         */
        Dependencia dependenciaDestino = d.getDependenciaDestino();
        Dependencia superDependencia = getSuperDependencia(dependenciaDestino);

        Usuario jefeActivo = getJefeActivoDependencia(superDependencia);
        /*
         * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01):
         * En caso que el usuario en sesión corresponda como Jefe Segundo de la
         * Dependencia Destino, se asigna el documento al Jefe principal de la
         * Dependencia.
         */
        if (jefeActivo != null && usuarioSesion.getId().equals(jefeActivo.getId())) {
            return superDependencia.getJefe();
        }

        return jefeActivo;
    }

    /**
     * Permite buscar la dependencia inmediatamente superior que es unidad padre
     * de la dependencia que entra como parametro.
     *
     * @param dep Dependencia
     * @return Dependencia inmediatamente superior que es unidad padre
     */
    protected Dependencia getSuperDependencia(Dependencia dep) {
        /*
         * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Validacion
         * para que tenga en cuenta el campo Indicador de envio documentos.
         */
        if (dep.getPadre() == null || (dep.getDepIndEnvioDocumentos() != null && dep.getDepIndEnvioDocumentos())) {
            return dep;
        }

        Dependencia jefatura = dep;
        Integer jefaturaId = dep.getPadre();
        while (jefaturaId != null) {
            jefatura = dependenciaRepository.getOne(jefaturaId);

            if (jefatura.getDepIndEnvioDocumentos() != null && jefatura.getDepIndEnvioDocumentos()) {
                return jefatura;
            }

            jefaturaId = jefatura.getPadre();
        }
        return jefatura;
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
     * 2017-02-09 jgarcia@controltechcg.com Issue #11 (SIGDI-Incidencias01):
     * Paso a static
     */
    private static Usuario getJefeActivoDependencia(final Dependencia dependencia) {

        Usuario jefe = dependencia.getJefe();

        Usuario jefeEncargado = dependencia.getJefeEncargado();
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

}
