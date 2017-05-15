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

	public CustomAuthenticationProvider(
			ActiveDirectoryLdapAuthenticationProvider ldap,
			UsuarioRepository usuarioRepository) {
		this.ldap = ldap;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		try {
			ldap.authenticate(authentication);
		} catch (Exception e) {
			throw e;
		}

		List<String> roles = usuarioRepository.allByLogin(authentication.getName().toLowerCase());
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
		return ldap.supports(authentication);
	}

}
