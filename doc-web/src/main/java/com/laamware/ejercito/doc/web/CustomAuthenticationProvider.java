package com.laamware.ejercito.doc.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import com.laamware.ejercito.doc.web.repo.UsuarioRepository;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	ActiveDirectoryLdapAuthenticationProvider ldap;

	UsuarioRepository usuarioRepository;

	public CustomAuthenticationProvider(ActiveDirectoryLdapAuthenticationProvider ldap,
			UsuarioRepository usuarioRepository) {
		this.ldap = ldap;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			/*
			 * 2017-06-15 jgarcia@controltechcg.com Issue #23
			 * (SICDI-Controltech) hotfix-23: Banderas en proceso de login para
			 * soporte y depuración.
			 */
			// System.out.println("CustomAuthenticationProvider.authenticate()
			// >>>");
			// System.out.println("authentication.getName()=" +
			// authentication.getName());
			// System.out.println("authentication.getCredentials()=" +
			// authentication.getCredentials());
			// System.out.println("authentication.getDetails()=" +
			// authentication.getDetails());
			// System.out.println("authentication.getPrincipal()=" +
			// authentication.getPrincipal());

			ldap.authenticate(authentication);
		} catch (Exception e) {
			throw e;
		}

		/*
		 * 2017-06-22 jgarcia@controltechcg.com Issue #111 (SICDI-Controltech)
		 * hotfix-111: Cambio en el proveedor de autenticación para invocar
		 * nueva función que únicamente obtenga la lista de roles activos
		 * asignados a un usuario.
		 */
		List<String> roles = usuarioRepository.findAllActiveRolByLogin(authentication.getName());

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		for (String rol : roles) {
			authorities.add(new SimpleGrantedAuthority(rol));
		}

		User user = new User(authentication.getName(), "", authorities);
		return new UsernamePasswordAuthenticationToken(user, "", authorities);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		/*
		 * 2017-06-15 jgarcia@controltechcg.com Issue #23 (SICDI-Controltech)
		 * hotfix-23: Banderas en proceso de login para soporte y depuración.
		 */
		// System.out.println("CustomAuthenticationProvider.supports()");
		// System.out.println("authentication=" + authentication);
		// System.out.println("ldap.supports(authentication)" +
		// ldap.supports(authentication));

		return ldap.supports(authentication);
	}

}
