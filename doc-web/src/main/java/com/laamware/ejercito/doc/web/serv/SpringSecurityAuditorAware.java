package com.laamware.ejercito.doc.web.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

public class SpringSecurityAuditorAware implements AuditorAware<Integer> {

    @Autowired
    UsuarioRepository usuarioRepository;

    public Integer getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object userObject = authentication.getPrincipal();
        String username = null;
        if (User.class.isAssignableFrom(userObject.getClass())) {
            username = ((User) userObject).getUsername();
        } else if (LdapUserDetails.class
                .isAssignableFrom(userObject.getClass())) {
            username = ((LdapUserDetails) userObject).getUsername();
        } else if (String.class.isAssignableFrom(userObject.getClass())) {
            username = (String) userObject;
        } else {
            throw new RuntimeException(
                    "El tipo de usuario no se reconoce para la auditoría: "
                    + userObject.getClass().getName());
        }
        /*
         * Issue #123 (SICDI-Controltech) hotfix-123: Corrección en búsqueda de 
         * usuarios para que únicamente presente información de usuarios activos.
         */
        Usuario usuario = usuarioRepository.getByLoginAndActivoTrue(username);
        if (usuario == null) {
            return null;
        }
        return usuario.getId();
    }
}
