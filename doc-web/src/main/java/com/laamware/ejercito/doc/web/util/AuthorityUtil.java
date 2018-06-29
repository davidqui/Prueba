package com.laamware.ejercito.doc.web.util;

import java.util.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilería para validaciones de autoridades (roles) del usuario en sesión.
 *
 * @author jgarcia@controltechcg.com
 * @since 1.8
 * @version 05/11/2018 Issue #162 (SICDI-Controltech) feature-162
 */
public final class AuthorityUtil {

    /**
     * Indica si el usuario en sesión tiene asignada la autoridad (rol).
     *
     * @param authority Autoridad (id del rol).
     * @return {@code true} si el usuario tiene asignada la autoridad; de lo
     * contrario, {@code  false}.
     */
    public static boolean hasAuthority(final String authority) {
        return hasAuthority((Collection<SimpleGrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities(), authority);
    }

    /**
     * Indica si la colección de autoridades contiene la autoridad (rol).
     *
     * @param authorities Colección de autoridades.
     * @param authority Autoridad (id del rol).
     * @return {@code true} si la colección contiene la autoridad; de lo
     * contrario, {@code  false}.
     */
    public static boolean hasAuthority(final Collection<SimpleGrantedAuthority> authorities, final String authority) {
        for (final SimpleGrantedAuthority grantedAuthority : authorities) {
            if (grantedAuthority.getAuthority().equals(authority)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Constructor privado.
     */
    private AuthorityUtil() {
    }

}
