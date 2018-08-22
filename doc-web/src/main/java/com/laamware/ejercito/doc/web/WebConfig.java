package com.laamware.ejercito.doc.web;

import com.laamware.ejercito.doc.web.entity.Dominio;
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
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.LogRepository;
import com.laamware.ejercito.doc.web.repo.UsuarioRepository;
import com.laamware.ejercito.doc.web.util.GeneralUtils;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableWebMvc
@EnableSpringDataWebSupport
public class WebConfig extends WebMvcAutoConfigurationAdapter {

    /**
     * 2018-05-02 jgarcia@controltechcg.com Issue #159 (SICDI-Controltech)
     * feature-159: Constantes para los atributos a colocar en el HTTP Request.
     */
    private static final String USERNAME_ATTR = "username";
    private static final String USERPROFILE_ATTR = "userprofile";
    private static final String USER_DOMINIO_ATTR = "user_dominio";
    private static final String USER_CAN_USE_OWA_LINK_ATTR = "user_can_use_owa_link";
    private static final String OWA_LINK_URL_ATTR = "owa_link_url";
    private static final String USU_ACTIVO_ATT = "usuActivo";

    @Autowired
    LogRepository logRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    /*
     * 2018-05-02 jgarcia@controltechcg.com Issue #159 (SICDI-Controltech)
     * feature-159: URL al OWA.
     */
    @Value("${com.mil.imi.sicdi.owa.url}")
    private String owaURL;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        super.addInterceptors(registry);

        /**
         * Interceptor para nombre de usuario y perfil
         */
        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                    ModelAndView modelAndView) throws Exception {
                /*
                 * 2018-05-02 jgarcia@controltechcg.com Issue #159
                 * (SICDI-Controltech) feature-159: Mejora en la implementación
                 * del código del postHandle. Adición de información del dominio
                 * del usuario como atributos del HTTP Request.
                 */
                if (modelAndView == null) {
                    return;
                }

                final String remoteUser = request.getRemoteUser();
                if (StringUtils.isBlank(remoteUser)) {
                    setNoUserInformation(request);
                    return;
                }

                /*
                 * 2017-03-08 jgarcia@controltechcg.com Issue #9
                 * (SIGDI-Incidencias01): Corrección en búsqueda de información
                 * del usuario que realiza el login.
                 */
                final Usuario usuario = usuarioRepository.findByLoginAndActivo(remoteUser.trim().toLowerCase(), Boolean.TRUE);
                if (usuario == null) {
                    setNoUserInformation(request);
                    return;
                }

                final Dominio dominio = usuario.getDominio();
                final Boolean visualizaLinkOWA = dominio.getActivo() && dominio.getVisualizaLinkOWA();

                setUserInformation(request, usuario.toString(), usuario.getPerfil().toString(), dominio.getNombre(), visualizaLinkOWA, usuario.getUsuActivo());
            }
        });

        /**
         * Interceptor para registrar el log del sistema
         */
        registry.addInterceptor(new HandlerInterceptorAdapter() {

            @Override
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

    /**
     * Establece sobre el HTTP Request los datos por defecto cuando no hay
     * información del usuario en sesión.
     *
     * @param request HTTP Request.
     */
    /*
     * 2018-05-02 jgarcia@controltechcg.com Issue #159 (SICDI-Controltech)
     * feature-159.
     */
    private void setNoUserInformation(final HttpServletRequest request) {
        setUserInformation(request, request.getRemoteUser(), "No profile", null, false, null);
    }

    /**
     * Establece sobre el HTTP Request los datos del usuario en sesión,
     * necesarios para la presentación de información en el header de la UI del
     * sistema.
     *
     * @param request HTTP Request.
     * @param userName Nombre del usuario.
     * @param profileName Nombre del perfil del usuario.
     * @param dominioNombre Nombre del dominio del usuario.
     * @param canUseOWALink Indica si el usuario puede utilizar el enlace para
     * acceder al OWA.
     */
    /*
     * 2018-05-02 jgarcia@controltechcg.com Issue #159 (SICDI-Controltech)
     * feature-159.
     */
    /*
    * 2018-08-15 samuel.delgado@controltechcg.com Issue #7 (SICDI-Controltech)
    * feature-gogs-7: se setea variable si el usuario esta activo.
    */
    private void setUserInformation(final HttpServletRequest request, final String userName, final String profileName, final String dominioNombre,
            final boolean canUseOWALink, final Boolean usuarioActivo) {
        request.setAttribute(USERNAME_ATTR, userName);
        request.setAttribute(USERPROFILE_ATTR, profileName);
        request.setAttribute(USER_DOMINIO_ATTR, dominioNombre);
        request.setAttribute(USER_CAN_USE_OWA_LINK_ATTR, canUseOWALink);
        request.setAttribute(USU_ACTIVO_ATT, usuarioActivo);
        request.setAttribute(OWA_LINK_URL_ATTR, owaURL);
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
