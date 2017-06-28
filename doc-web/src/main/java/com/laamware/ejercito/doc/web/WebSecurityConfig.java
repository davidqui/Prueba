package com.laamware.ejercito.doc.web;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.event.LoggerListener;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

import com.laamware.ejercito.doc.web.entity.Log;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.LogRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.serv.LdapService;

@Configuration
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Value("${docweb.ldap.connection}")
	private String ldapConnectionString;

	@Value("${docweb.ldap.domain}")
	private String ldapDomainString;

	@Value("${docweb.authMode}")
	private String authMode;

	@Autowired
	AuthenticationSuccessHandler successHandler;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	LdapService ldapService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				// Permite iframe del mismo origen
				.headers().frameOptions().disable()
				// Permite el acceso a css
				.authorizeRequests().antMatchers("/").permitAll().antMatchers("/scanner/**").permitAll()
				.antMatchers("/css/**").permitAll()
				// Permite el acceso a js
				.antMatchers("/js/**").permitAll()
				// Permite el acceso a fonts
				.antMatchers("/fonts/**").permitAll()
				// Permite el acceso a java
				.antMatchers("/java/**").permitAll()
				// Permite el acceso a login
				.antMatchers("/login").permitAll()
				// .antMatchers("/invitacion").permitAll()
				// Permite el acceso a ofs
				.antMatchers("/ofs/**").permitAll()
				// Permite el acceso a la página de NoUser
				.antMatchers("/security/nouser").permitAll()
				// Permite el acceso a la página de NODependencia
				.antMatchers("/security/nodependencia").permitAll()
				// Cualquier solicitud debe ser autenticada
				.anyRequest().authenticated().and()
				// La página de login debe ser pública
				.formLogin().loginPage("/login").successHandler(successHandler).permitAll().and()
				// .formLogin().loginPage("/invitacion").successHandler(successHandler).permitAll().and()
				// El recurso de logout debe ser público
				.logout().logoutUrl("/logout").permitAll().and().exceptionHandling().accessDeniedPage("/access-denied");
	}

	@Bean
	public LoggerListener loggerListener() {
		return new LoggerListener();
	}

	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandler() {

			RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

			@Autowired
			UsuarioRepository usuR;

			@Autowired
			LogRepository logRepository;

			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				/*
				 * 2017-06-15 jgarcia@controltechcg.com Issue #23
				 * (SICDI-Controltech) hotfix-23: Banderas en proceso de login
				 * para soporte y depuración.
				 */
				// System.out.println(
				// "WebSecurityConfig.authenticationSuccessHandler().new
				// AuthenticationSuccessHandler()
				// {...}.onAuthenticationSuccess()");
				// System.out.println("authentication.isAuthenticated()=" +
				// authentication.isAuthenticated());

				if (authentication.isAuthenticated()) {

					String login = authentication.getName();
					Usuario u = usuR.findByLoginAndActivo(login.toLowerCase(), Boolean.TRUE);

					Log log = new Log();
					log.setQuien(login);
					log.setCuando(new Date());
					StringBuilder b = new StringBuilder(request.getRequestURI());
					b.append("|login_success|").append(request.getRemoteAddr()).append("|").append(log.getQuien())
							.append("|").append(log.getCuando());
					log.setContenido(b.toString());

					logRepository.save(log);

					if (u == null) {
						redirectStrategy.sendRedirect(request, response, "/security/nouser");

						new SecurityContextLogoutHandler().logout(request, response, authentication);
						SecurityContextHolder.getContext().setAuthentication(null);
						return;
					} else if (!actualizarUsuario(login)) {
						redirectStrategy.sendRedirect(request, response, "/security/nodependencia");

						new SecurityContextLogoutHandler().logout(request, response, authentication);
						SecurityContextHolder.getContext().setAuthentication(null);
						return;

					}
				}
				HttpSession session = request.getSession();
				Object defultSavedRequest = session.getAttribute("SPRING_SECURITY_SAVED_REQUEST");
				if (defultSavedRequest != null && defultSavedRequest instanceof DefaultSavedRequest) {
					DefaultSavedRequest defreq = (DefaultSavedRequest) defultSavedRequest;
					redirectStrategy.sendRedirect(request, response, defreq.getRedirectUrl());
				} else {
					redirectStrategy.sendRedirect(request, response, "/");
				}
			}

		};
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		/*
		 * 2017-06-15 jgarcia@controltechcg.com Issue #23 (SICDI-Controltech)
		 * hotfix-23: Banderas en proceso de login para soporte y depuración.
		 */
		// System.out.println("WebSecurityConfig.configureGlobal()");
		// System.out.println("authMode=" + authMode);

		if (authMode.equals("ad")) {

			ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
					ldapDomainString, ldapConnectionString);
			provider.setConvertSubErrorCodesToExceptions(true);
			provider.setUseAuthenticationRequestCredentials(true);
			auth.authenticationProvider(new CustomAuthenticationProvider(provider, usuarioRepository));

		} else if (authMode.equals("jdbc")) {
			/*
			 * 2017-06-22 jgarcia@controltechcg.com Issue #111
			 * (SICDI-Controltech) hotfix-111: Corrección de consulta SQL que
			 * obtiene las autoridades (roles) para cada usuario, con el fin que
			 * ahora únicamente obtenga aquellas autoridades activas en el
			 * sistema.
			 */
			auth.jdbcAuthentication().dataSource(dataSource)
					.authoritiesByUsernameQuery(
							" SELECT DISTINCT                                                                            "
									+ " USUARIO.USU_LOGIN,                                                               "
									+ " ROL.ROL_ID                                                                       "
									+ " FROM                                                                             "
									+ " USUARIO                                                                          "
									+ " JOIN PERFIL     ON (PERFIL.PER_ID     = USUARIO.PER_ID)                          "
									+ " JOIN PERFIL_ROL ON (PERFIL_ROL.PER_ID = PERFIL.PER_ID)                           "
									+ " JOIN ROL        ON (ROL.ROL_ID        = PERFIL_ROL.ROL_ID)                       "
									+ " WHERE                                                                            "
									+ " USUARIO.USU_LOGIN     = ?                                                        "
									+ " AND PERFIL.ACTIVO     = 1                                                        "
									+ " AND PERFIL_ROL.ACTIVO = 1                                                        "
									+ " AND ROL.ACTIVO        = 1                                                        ")
					.usersByUsernameQuery(
							"select USU_LOGIN,USU_PASSWORD,ACTIVO from USUARIO where lower(USU_LOGIN) = lower(?)");
		}
	}

	@Transactional
	private Boolean actualizarUsuario(String login) {
		try {
			if (!authMode.equals("ad")) {
				return Boolean.TRUE;
			}

			Usuario usuarioLdap = ldapService.getUsuarioFromLdapByAccountName(login.toLowerCase());
			if (usuarioLdap != null) {
				Usuario usuario = usuarioRepository.findByLoginAndActivo(login.toLowerCase(), Boolean.TRUE);
				usuario.setGrado(usuarioLdap.getGrado());
				usuario.setNombre(usuarioLdap.getNombre());
				usuario.setDocumento(usuarioLdap.getDocumento());
				usuario.setTelefono(usuarioLdap.getTelefono());
				usuario.setDependencia(usuarioLdap.getDependencia());
				usuario.setEmail(usuarioLdap.getEmail());
				if (usuarioLdap.getDependencia() == null) {
					return Boolean.FALSE;
				}
				usuarioRepository.save(usuario);
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.FALSE;

	}

}
