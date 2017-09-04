package com.laamware.ejercito.doc.web.serv.spec;

import com.laamware.ejercito.doc.web.entity.Clasificacion;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Usuario;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * Especificaciones de búsqueda para servicio de usuarios.
 *
 * @author jgarcia@controltechcg.com
 * @since Sep 04, 2017
 * @version 1.0.0 (feature-120).
 */
public class UsuarioSpecifications {

    /**
     * Función UPPER.
     */
    private static final String UPPER_FUNCTION = "UPPER";

    /**
     * Especificación de inicio, con las condiciones iniciales del filtro.
     * (Usuario activo).
     *
     * @return Especificación.
     */
    public static Specification<Usuario> inicio() {
        return new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cb.equal(root.<Boolean>get("activo"), Boolean.TRUE);
            }
        };
    }

    /**
     * Especificación con el conjunto de condiciones a evaluar por token. Todos
     * van unidos por OR.
     *
     * @param token Token.
     * @return Especificación.
     */
    public static Specification<Usuario> filtrosPorToken(final String token) {
        return new Specification<Usuario>() {
            @Override
            public Predicate toPredicate(Root<Usuario> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                final String upperCase = token.toUpperCase();
                return cb.or(cb.like(cb.function(UPPER_FUNCTION, String.class, root.<String>get("nombre")), "%" + upperCase + "%"),
                        cb.like(cb.function(UPPER_FUNCTION, String.class, root.<String>get("documento")), "%" + upperCase + "%"),
                        cb.like(cb.function(UPPER_FUNCTION, String.class, root.<Dependencia>get("dependencia").<String>get("nombre")), "%" + upperCase + "%"),
                        cb.like(cb.function(UPPER_FUNCTION, String.class, root.<Clasificacion>get("clasificacion").<String>get("nombre")), "%" + upperCase + "%")
                );
            }

        };
    }

}
