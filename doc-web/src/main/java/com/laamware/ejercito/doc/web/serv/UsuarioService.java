package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.Clasificacion;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Documento;
import com.laamware.ejercito.doc.web.entity.Instancia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.enums.UsuarioFinderTipo;
import com.laamware.ejercito.doc.web.repo.DocumentoRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioSpecificationRepository;
import com.laamware.ejercito.doc.web.serv.spec.UsuarioSpecifications;
import java.math.BigDecimal;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;

/**
 * Servicio para las operaciones de negocio de usuario.
 *
 * @author jgarcia@controltechcg.com
 * @since May 15, 2017 Issue #78 (SICDI-Controltech) feature-78
 */
@Service
public class UsuarioService {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(UsuarioService.class.getName());

    /**
     * Repositorio de usuarios.
     */
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Servicios de dependencias.
     */
    @Autowired
    private DependenciaService dependenciaService;

    /**
     * Mapa de unidades por dependencia.
     */
    private Map<Integer, Dependencia> unidadesMap = new LinkedHashMap<>();

    /**
     * Repositorio de especificación de usuarios.
     */
    @Autowired
    private UsuarioSpecificationRepository repository;

    /**
     * Repositorio de documentos.
     */
    @Autowired
    private DocumentoRepository documentoRepository;

    /**
     * Repositorio de procesos.
     */
    @Autowired
    ProcesoService procesoService;

    /**
     * Obtiene una página de usuarios correspondientes a la criteria de búsqueda
     * asignada, según las reglas de búsqueda para el tipo de finder de usuario.
     *
     * @param criteria Criteria de búsqueda.
     * @param pageIndex Índice de la página.
     * @param pageSize Tamaño de la página.
     * @param usuarioFinderTipo Tipo de finder de usuario. En caso de ser
     * {@link UsuarioFinderTipo#ACTA} únicamente se presentan los usuarios con
     * igual o mayor grado de clasificación que el usuario en sesión.
     * @param usuarioSesion Usuario en sesión.
     * @param criteriaParametersMap Mapa de otros parámetros para la criteria de
     * búsqueda.
     * @return Página de usuarios.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162: Adición de usuarioFinderTipo y usuarioSesion.
     */
    public Page<Usuario> findAllByCriteriaSpecification(final String criteria, final int pageIndex, final int pageSize, final UsuarioFinderTipo usuarioFinderTipo,
            final Usuario usuarioSesion, final Map<String, ?> criteriaParametersMap) {

        Specifications<Usuario> where = Specifications.where(UsuarioSpecifications.inicio());

        /*
         * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Filtro por orden de clasificación para finder tipo ACTA.
         */
        if (usuarioFinderTipo.equals(UsuarioFinderTipo.ACTA)) {
            final String pin = (String) criteriaParametersMap.get("pin");
            final Documento documento = documentoRepository.findOneByInstanciaId(pin);
            final Clasificacion clasificacion = documento.getClasificacion();
            where = where.and(UsuarioSpecifications.condicionPorOrdenGradoClasificacion(clasificacion.getOrden()));
        }

        if (criteria != null) {
            final String[] tokens = criteria.replaceAll("'", " ").split(" ");
            for (String token : tokens) {
                where = where.and(UsuarioSpecifications.filtrosPorToken(token));
            }
        }
        /*
         * 2017-11-10 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Cambio en la entidad usuario, se
         * coloca llave foranea el grado.
         *
         * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
         * feature-162: Orden de los nombres cuando los usuarios tienen el mismo
         * grado.
         */
        final Sort sort = new Sort(
                new Sort.Order(Sort.Direction.DESC, "usuGrado.pesoOrden"),
                new Sort.Order(Sort.Direction.ASC, "nombre")
        );

        final PageRequest pageRequest = new PageRequest(pageIndex, pageSize, sort);
        final Page<Usuario> users = repository.findAll(where, pageRequest);
        return users;
    }

    /**
     * Busca un usuario.
     *
     * @param id ID.
     * @return Usuario.
     */
    public Usuario findOne(Integer id) {
        return usuarioRepository.findOne(id);
    }

    /**
     * Obtiene la información básica del usuario (Grado, Nombre, Cargo,
     * Dependencia).
     *
     * @param usuario Usuario.
     * @return Información básica, o texto vacío en caso que el usuario sea
     * {@code null}.
     */
    public String mostrarInformacionBasica(Usuario usuario) {
        if (usuario == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        /*
         * 2017-11-10 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Cambio en la entidad usuario, se
         * coloca llave foranea el grado.
         */
        final String grado = usuario.getUsuGrado().getId();
        if (grado != null && !grado.trim().isEmpty()) {
            builder.append(grado).append(". ");
        }

        final String nombre = usuario.getNombre();
        builder.append(nombre).append(" ");

        /*
         * 2018-02-13 edison.gonzalez@controltechcg.com Issue #149
         * (SICDI-Controltech) feature-149: Se coloca en comentarios la columna
         * de cargo, la cual se remplaza por la columna usuCArgoPrincipalId.
         */
        final String cargo = (usuario.getUsuCargoPrincipalId() != null) ? usuario.getUsuCargoPrincipalId().getCarNombre() : null;
        if (cargo != null && !cargo.trim().isEmpty()) {
            builder.append(" - ").append(cargo).append(" ");
        }

        final Dependencia dependencia = usuario.getDependencia();
        if (dependencia != null) {
            final Dependencia unidad = buscarUnidad(dependencia);
            /*
             * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Se
             * realiza la validacion para que muestre el nombre de la
             * dependencia en caso de que la sigla sea nula.
             */
            if (unidad.getSigla() == null || unidad.getSigla().trim().length() == 0) {
                builder.append("(").append(unidad.getNombre()).append(")");
            } else {
                builder.append("(").append(unidad.getSigla()).append(")");
            }
        }

        return builder.toString().trim();
    }

    /**
     * Obtiene la información básica del usuario (Grado, Nombre, Cargo) en las
     * bandejas.
     *
     * @param usuario Usuario.
     * @return Información básica, o texto vacío en caso que el usuario sea
     * {@code null}.
     */
    public String mostrarInformacionBasicaBandejas(Usuario usuario) {
        if (usuario == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        /*
         * 2017-11-10 edison.gonzalez@controltechcg.com Issue #131
         * (SICDI-Controltech) feature-131: Cambio en la entidad usuario, se
         * coloca llave foranea el grado.
         *
         * 2017-10-23 edison.gonzalez@controltechcg.com Issue #132
         * (SICDI-Controltech) feature-132: Se quita la dependencia, para
         * mostrarla en otra columna
         */
        final String grado = usuario.getUsuGrado().getId();
        if (grado != null && !grado.trim().isEmpty()) {
            builder.append(grado).append(". ");
        }

        final String nombre = usuario.getNombre();
        builder.append(nombre).append(" ");

        /*
         * 2018-02-13 edison.gonzalez@controltechcg.com Issue #149
         * (SICDI-Controltech) feature-149: Se coloca en comentarios la columna
         * de cargo, la cual se remplaza por la columna usuCArgoPrincipalId.
         */
        final String cargo = (usuario.getUsuCargoPrincipalId() != null) ? usuario.getUsuCargoPrincipalId().getCarNombre() : null;
        if (cargo != null && !cargo.trim().isEmpty()) {
            builder.append("</br>").append(cargo);
        }
        return builder.toString().trim();
    }

    /**
     * Obtiene la unidad del usuario (Dependencia).
     *
     * @param usuario Usuario.
     * @return Información básica, o texto vacío en caso que el usuario sea
     * {@code null}.
     */
    public String mostrarInformacionUnidad(Usuario usuario) {
        if (usuario == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        /*
         * 2017-10-23 edison.gonzalez@controltechcg.com Issue #132
         * (SICDI-Controltech) feature-132: Se Agrega la dependencia, para
         * mostrarla en otra columna
         */
        final Dependencia dependencia = usuario.getDependencia();
        if (dependencia != null) {
            final Dependencia unidad = buscarUnidad(dependencia);
            /*
             * 2018-01-30 edison.gonzalez@controltechcg.com Issue #147: Se
             * realiza la validacion para que muestre el nombre de la
             * dependencia en caso de que la sigla sea nula.
             */
            if (unidad.getSigla() == null || unidad.getSigla().trim().length() == 0) {
                builder.append(unidad.getNombre());
            } else {
                builder.append(unidad.getSigla());
            }
        }

        return builder.toString().trim();
    }

    /**
     * Busca la unidad de la dependencia.
     *
     * @param dependencia Dependencia.
     * @return Dependencia unidad.
     */
    private Dependencia buscarUnidad(Dependencia dependencia) {
        final Integer dependenciaID = dependencia.getId();
        if (unidadesMap.containsKey(dependenciaID)) {
            return unidadesMap.get(dependenciaID);
        }

        final Dependencia unidad = dependenciaService.buscarUnidad(dependencia);
        unidadesMap.put(dependenciaID, unidad);
        return unidad;
    }

    public Boolean verificaAccesoDocumento(Integer usuId, String pinId) {
        Integer permiso = documentoRepository.verificaAccesoDocumento(usuId, pinId);
        if (permiso >= 1) {
            return true;
        } else {
            return verficaAccesoDocumentoCorrelacionado(usuId, pinId);
        }
    }

    /**
     * Verifica si el usuario tiene acceso al documento acta.
     *
     * @param usuario Usuario.
     * @param procesoInstanciaID ID de la instancia del proceso.
     * @return {@code true} si el usuario tiene acceso al documento acta; de lo
     * contrario, {@code false}.
     */
    /*
     * 2018-05-15 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public boolean verificaAccesoDocumentoActa(final Usuario usuario, final String procesoInstanciaID) {
        final BigDecimal acceso = documentoRepository.verificaAccesoDocumentoActa(usuario.getId(), procesoInstanciaID);
        return acceso.equals(BigDecimal.ONE);
    }

    /**
     * Busca un usuario activo por su número de documento.
     *
     * @param documento Número de documento.
     * @return Instancia del usuario activo del documento, o {@code null} en
     * caso de no existir correspondencia en el sistema.
     */
    /*
     * 2018-05-29 jgarcia@controltechcg.com Issue #162 (SICDI-Controltech)
     * feature-162.
     */
    public Usuario buscarActivoPorDocumento(final String documento) {
        return usuarioRepository.findByActivoTrueAndDocumento(documento);
    }

    private boolean verficaAccesoDocumentoCorrelacionado(Integer usuId, String pinId) {
        Instancia i = procesoService.instancia(pinId);
        // Obtiene el identificador de documento
        String docId = i.getVariable(Documento.DOC_ID);
        //Verifica si el documento existe, sino le permite seguir ya que se
        //esta creando
        if (!StringUtils.isBlank(docId)) {
            Documento doc = documentoRepository.getOne(docId);

            //Verifica que tenga documento relacionado
            if (doc != null && !StringUtils.isBlank(doc.getRelacionado())) {
                Documento relacionado = documentoRepository.getOne(doc.getRelacionado());
                if (relacionado != null) {
                    Integer permiso = documentoRepository.verificaAccesoDocumento(usuId, relacionado.getInstancia().getId());
                    if (permiso >= 1) {
                        return true;
                    }
                }
            }
            return false;

        } else {
            return true;
        }
    }

}
