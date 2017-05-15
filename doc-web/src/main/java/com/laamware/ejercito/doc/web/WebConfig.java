package com.laamware.ejercito.doc.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.laamware.ejercito.doc.web.entity.Log;
import com.laamware.ejercito.doc.web.entity.Perfil;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.LogRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.util.GeneralUtils;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcAutoConfigurationAdapter {

	@Autowired
	LogRepository logRepository;

	@Autowired
	UsuarioRepository usuarioRepository;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {

		super.addInterceptors(registry);

		/**
		 * Interceptor para nombre de usuario y perfil
		 */
		registry.addInterceptor(new HandlerInterceptorAdapter() {

			public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
					ModelAndView modelAndView) throws Exception {

				if (modelAndView != null) {

					String username = request.getRemoteUser();
					String profile = "No profile";
					if (StringUtils.isNotBlank(username)) {

						/*
						 * 2017-03-08 jgarcia@controltechcg.com Issue #9
						 * (SIGDI-Incidencias01): Corrección en búsqueda de
						 * información del usuario que realiza el login.
						 */
						Usuario usuario = usuarioRepository.findByLoginAndActivo(username.trim().toLowerCase(),
								Boolean.TRUE);

						if (usuario != null) {
							username = usuario.toString();
							Perfil perfil = usuario.getPerfil();
							if (perfil != null) {
								profile = perfil.toString();
							}
						}
					}
					request.setAttribute("username", username);
					request.setAttribute("userprofile", profile);
				}
			}
		});

		/**
		 * Interceptor para registrar el log del sistema
		 */
		registry.addInterceptor(new HandlerInterceptorAdapter() {

			public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
					throws Exception {

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				String username = "anonymous";
				if (auth != null) {
					username = auth.getName();
				}
				Log log = new Log();
				log.setQuien(username);
				log.setCuando(new Date());
				StringBuilder b = new StringBuilder(request.getRequestURI());
				b.append("|").append(request.getQueryString()).append("|")
						.append(GeneralUtils.mapToString(request.getParameterMap())).append("|")
						.append(request.getRemoteAddr()).append("|").append(log.getQuien()).append("|")
						.append(log.getCuando());
				log.setContenido(b.toString());

				logRepository.save(log);

				return true;
			}

		});

	}

	@Bean
	EmbeddedServletContainerCustomizer containerCustomizer(ConfigurableEmbeddedServletContainer container)
			throws Exception {

		return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				if (container instanceof TomcatEmbeddedServletContainerFactory) {
					TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
					tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {

						@Override
						public void customize(Connector connector) {
							connector.setMaxPostSize(101 * 1024 * 1024);// 101MB
						}
					});
				}

			}
		};

	}
}
