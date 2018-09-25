package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.dto.DocumentoDependenciaArchivoDTO;
import com.laamware.ejercito.doc.web.dto.TrdDTO;
import com.laamware.ejercito.doc.web.entity.Cargo;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.DocumentoDependencia;
import com.laamware.ejercito.doc.web.entity.TransferenciaArchivo;
import com.laamware.ejercito.doc.web.entity.Trd;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DocumentoDependenciaRepository;
import com.laamware.ejercito.doc.web.repo.TrdRepository;
import com.laamware.ejercito.doc.web.util.DateUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

/**
 * Servicio de lógica de negocio para {@link DocumentoDependencia}. NOTA: Las
 * operaciones de archivo automático se encuentran en el servicio
 * {@link ArchivoAutomaticoService}. Las operaciones de transferencia de archivo
 * se encuentran en el servicio {@link TransferenciaArchivoService}.
 *
 * @author Jorge Alfonso García Espinosa
 * @since 1.8
 * @version 04/25/2018 Issue #151 (SICDI-Controltech) feature-151
 */
@Service
public class DocumentoDependenciaService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    @Autowired
    private DocumentoDependenciaRepository documentoDependenciaRepository;

    @Autowired
    private TrdRepository trdRepository;
    
    /**
     * Obtiene la lista de registros de archivo (DTOs) para la pantalla de
     * archivo, según la subserie, el usuario (y su dependencia), y el cargo (si
     * este es indicado).
     *
     * @param subserie Subserie TRD. Obligatorio.
     * @param usuario Usuario. Obligatorio.
     * @param cargo Cargo. Opcional.
     * @param anyo Año. Opcional.
     * @return Lista de registros de archivo para los parámetros indicados.
     */
    public List<DocumentoDependenciaArchivoDTO> findAllBySubserieAndUsuarioAndCargoAndAnyo(final Trd subserie, final Usuario usuario, final Cargo cargo, final Integer anyo) {
        String sql = ""
                + "SELECT documento.doc_radicado AS \"numeroRadicado\",\n"
                + "       documento.pin_id AS \"procesoInstanciaID\",\n"
                + "       documento.doc_asunto AS \"documentoAsunto\",\n"
                + "       documento.cuando AS \"fechaCreacionDocumento\",\n"
                + "       documento_dependencia.cuando AS \"fechaCreacionArchivo\",\n"
                + "       cargo.car_nombre AS \"cargoNombre\"\n"
                + "FROM documento_dependencia\n"
                + "JOIN dependencia_trd ON (dependencia_trd.dep_id = documento_dependencia.dep_id\n"
                + "                         AND dependencia_trd.trd_id = documento_dependencia.trd_id)\n"
                + "JOIN documento ON (documento.doc_id = documento_dependencia.doc_id)\n"
                + "LEFT JOIN cargo ON (cargo.car_id = documento_dependencia.cargo_id)\n"
                + "WHERE documento_dependencia.activo = 1\n"
                + "  AND documento_dependencia.trd_id = ?\n"
                + "  AND documento_dependencia.quien = ?\n"
                + (cargo == null ? "" : "     AND documento_dependencia .cargo_id = ?\n");
        /*
         * 2018-05-04 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Adición del filtro de año.
         */
        if (anyo != null && anyo != 0) {
            sql += " AND documento_dependencia.cuando BETWEEN ? AND ? \n";
        }

        sql += "  AND dependencia_trd.activo = 1\n"
                + "  AND dependencia_trd.dep_id = ?\n"
                + "ORDER BY documento_dependencia.cuando DESC"
                + "";

        final List<Object> params = new ArrayList<>();
        params.add(subserie.getId());
        params.add(usuario.getId());

        if (cargo != null) {
            params.add(cargo.getId());
        }
        /*
         * 2018-05-04 jgarcia@controltechcg.com Issue #157 (SICDI-Controltech)
         * feature-157: Adición del filtro de año.
         */
        if (anyo != null && anyo != 0) {
            params.add(DateUtil.getMinDateOfMonth(Calendar.JANUARY, anyo));
            params.add(DateUtil.getMaxDateOfMonth(Calendar.DECEMBER, anyo));
        }

        params.add(usuario.getDependencia().getId());

        final RowMapper<DocumentoDependenciaArchivoDTO> rowMapper = new BeanPropertyRowMapper<>(DocumentoDependenciaArchivoDTO.class);
        return jdbcTemplate.query(sql, params.toArray(), rowMapper);
    }

    /**
     * Busca un registro activo para un documento y un usuario asociado.
     *
     * @param documento Documento.
     * @param usuario Usuario.
     * @return Instancia del registro activo de archivo para el documento y el
     * usuario. En caso de no existir correspondencia en el sistema, se retorna
     * {@code null}.
     */
    /*
     * 2018-06-05 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public DocumentoDependencia buscarRegistroActivo(final Documento documento, final Usuario usuario) {
        return documentoDependenciaRepository.findOneByDocumentoAndQuienAndActivoTrue(documento, usuario.getId());
    }
    
    /***
     * Busca un documento dependencia por identificador 
     * @param id identificador documento dependencia
     * @return  DocumentoDependencia
     */
    public DocumentoDependencia buscarPorId(Integer id){
        return documentoDependenciaRepository.findOne(id);
    }
    
    /***
     * lista los documentos dependencia por un usuario y los empaqueta por trd 
     * @param usuario usuario a buscar los documentos
     * @return lista de documentos. 
     */
    /*
     * 2018-08-23 jgarcia@controltechcg.com Issue gogs #4 (SICDI-Controltech)
     * feature-gogs-4.
     */
    public List<TrdDTO> documentoXtrdDadoUsuario(Usuario usuario, Integer cargo){
        List<DocumentoDependencia> documentosDependenciaXUsuario;
        documentosDependenciaXUsuario = documentoDependenciaRepository.documentosDependenciaXUsuario(usuario.getId(), cargo);
        Map<Trd, List<DocumentoDependencia>> hashMap = new HashMap<>();
        Calendar c = Calendar.getInstance();
        for (DocumentoDependencia documento : documentosDependenciaXUsuario) {
            Documento pdocumento = documento.getDocumento();
            c.setTime(pdocumento.getDocFecRadicado());
            c.add(Calendar.YEAR, pdocumento.getTrd().getRetArchivoGeneral());
            if (new Date().before(c.getTime()))
                if (!hashMap.containsKey(documento.getDocumento().getTrd())) {
                    List<DocumentoDependencia> list = new ArrayList<>();
                    list.add(documento);
                    hashMap.put(documento.getDocumento().getTrd(), list);
                } else {
                    hashMap.get(documento.getDocumento().getTrd()).add(documento);
                }
        }
        List<Trd> findByActivoAndSerieNull = trdRepository.findByActivoAndSerieNull(true);
        List<TrdDTO> documentosXtrd = new ArrayList<>();
        for (Trd trd : findByActivoAndSerieNull) {
            TrdDTO tdto = new TrdDTO(trd.getId(), trd.getNombre(), trd.getCodigo(), 0);
            documentosXtrd.add(tdto);
        }
        for (Map.Entry<Trd, List<DocumentoDependencia>> entry : hashMap.entrySet()) {
            for (TrdDTO trdDTO : documentosXtrd) {
                if (trdDTO.getTrdId() == entry.getKey().getSerie()) {
                    TrdDTO tdto = new TrdDTO(entry.getKey().getId(), entry.getKey().getNombre(), entry.getKey().getCodigo(), entry.getValue().size());
                    tdto.setDocumentosDependencia(entry.getValue());
                    if (trdDTO.getSubSeries() == null)
                        trdDTO.setSubSeries(new ArrayList<TrdDTO>());
                    trdDTO.getSubSeries().add(tdto);
                }
            }
        }
        return documentosXtrd;
    }
    
    /**
     * Lista los documentos que se encuentran en documentos en otras transferencias abiertas
     * @param usuario usuario a consultar documentos
     * @param transferenciaArchivo transferencia de archivo actual
     * @return  lista de documentos dependencia
     */
    /*
     * 2018-08-23 jgarcia@controltechcg.com Issue gogs #4 (SICDI-Controltech)
     * feature-gogs-4.
     */
    public List<DocumentoDependencia> listarDocumentosOtrasTransferencias(Usuario usuario, TransferenciaArchivo transferenciaArchivo){
        return documentoDependenciaRepository.documentosDependenciaXUsuarioxNotTransferencia(usuario.getId(), transferenciaArchivo.getId());
    }
    
    
    public void completarTransferencia(DocumentoDependencia documentoDependencia, Usuario usuario, Dependencia dependencia, Cargo cargo){
        documentoDependencia.setQuien(usuario.getId());
        documentoDependencia.setCargo(cargo);
        documentoDependencia.setDependencia(dependencia);
        documentoDependencia.setCuando(new Date());
        documentoDependenciaRepository.save(documentoDependencia);
    }
    
    
    /**
     * Retorna la cantidad de documentos posibles que tiene un usuario y un cargo
     * de su archivo, cuyos documentos no se encuentren en proceso de transferencia.
     * @param usuID Identificador del usuario
     * @param cargoId Identificador del cargo
     * @return Número de documentos
     */
    public int cantidadDocumentosPosibleTransferenciaXusuIdAndCargoId(Integer usuID,Integer cargoId){
        return documentoDependenciaRepository.cantidadDocumentosPosibleTransferenciaXusuIdAndCargoId(usuID, cargoId);
    }
}
