package com.laamware.ejercito.doc.web.serv;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Component;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.laamware.ejercito.doc.web.entity.AppConstants;
import com.laamware.ejercito.doc.web.entity.Dependencia;
import com.laamware.ejercito.doc.web.entity.Grados;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.DependenciaRepository;
import com.laamware.ejercito.doc.web.repo.GradosRepository;

@Component
public class LdapService {
    
	@Value("${docweb.ldap.path}")
	private String path;

	@Autowired
	private DependenciaRepository dependenciaRepository;
	
	@Autowired
	private GradosRepository gradosRepository;

	private static final String NAME_LOGIN = "sAMAccountName";
	private static final String NAME_INICIALES = "initials";
	private static final String NAME_APELLIDO = "sn";
	private static final String NAME_NOMBRE = "givenName";
	private static final String NAME_NOMBRE_COMPLETO = "displayName";
	private static final String NAME_IDENTIFICACION = "facsimileTelephoneNumber";
	private static final String NAME_TELEFONO = "telephoneNumber";
	private static final String NAME_DEPEDENCIA = "distinguishedName";//department
	private static final String NAME_CORREO = "mail";
	private static final String NAME_GRADO = "initials";
	private static final String NAME_CARGO = "title";	

	@Autowired
	LdapTemplate ldapTemplate;

	/**
	 * Obtiene los datos básicos de usuario desde el DA y los mapea a un objeto
	 * Usuario
	 * 
	 * @param accountName
	 *            sAMAccountName
	 * @return Usuario que contiene los datos obtenidos desde el DA
	 */
	public Usuario getUsuarioFromLdapByAccountName(String accountName) {
		LdapQuery query = query()
				.base(path).attributes(NAME_LOGIN, NAME_INICIALES, NAME_APELLIDO, NAME_NOMBRE, NAME_NOMBRE_COMPLETO,
						NAME_IDENTIFICACION, NAME_TELEFONO, NAME_DEPEDENCIA, NAME_CORREO, NAME_GRADO, NAME_CARGO)
				.where(NAME_LOGIN).is(accountName);

		List<Usuario> usuariosResult = ldapTemplate.search(query, new AttributesMapper<Usuario>() {
			public Usuario mapFromAttributes(Attributes attrs) throws NamingException {
				Usuario us = new Usuario();
				us.setLogin(attrs.get(NAME_LOGIN)!= null ? attrs.get(NAME_LOGIN).get().toString() : null);
				us.setDocumento(attrs.get(NAME_IDENTIFICACION)!=null ? attrs.get(NAME_IDENTIFICACION).get().toString() : " ");
				us.setNombre(attrs.get(NAME_NOMBRE_COMPLETO) != null ? attrs.get(NAME_NOMBRE_COMPLETO).get().toString() : null);
				us.setTelefono(attrs.get(NAME_TELEFONO) != null ? attrs.get(NAME_TELEFONO).get().toString() : null);
                                /*
                                    2017-11-10 edison.gonzalez@controltechcg.com Issue #133 (SICDI-Controltech) 
                                    feature-133: Cambio en la entidad usuario, se coloca llave foranea el grado.
                                */
                                Grados grados = getGradoLdap(attrs.get(NAME_GRADO)!= null ? attrs.get(NAME_GRADO).get().toString() : null);
				us.setUsuGrado(grados);
                                
				us.setEmail(attrs.get(NAME_CORREO) != null ? attrs.get(NAME_CORREO).get().toString() : null);
				
				String txtAdicional = "CN="+us.getNombre()+",";
				String dependenciaCODIGO = attrs.get(NAME_DEPEDENCIA) != null ? attrs.get(NAME_DEPEDENCIA).get().toString() : null;
				dependenciaCODIGO = dependenciaCODIGO.replaceFirst(txtAdicional, "");
				
				us.setDependencia( getDependenciaLdap( dependenciaCODIGO ) );
				us.setCargo(attrs.get(NAME_CARGO) != null ? attrs.get(NAME_CARGO).get().toString() : null);
				return us;
			}
		});

		if (usuariosResult != null && !usuariosResult.isEmpty()) {
			return usuariosResult.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Obtiene la dependencia del usuario a partir del código de la dependencia en el DA
	 * @param codigoDepLdap
	 * @return
	 */
	protected Dependencia getDependenciaLdap(String codigoDepLdap) {
		try {
			System.out.println("Buscando dependecia CODIGO_LDAP:"+codigoDepLdap+":");
			Dependencia dep = null;
			if (codigoDepLdap != null) {
				dep = dependenciaRepository.findByActivoAndDepCodigoLdap(Boolean.TRUE, codigoDepLdap);
				System.out.println("Resultado CODIGO_LDAP:"+dep);
			}
			return dep;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Obtiene el grado del usuario a partir del código del grado en el DA, si no encuentra un grado, establece un grado por defecto 
	 * @param codigoDepLdap
	 * @return
	 */
	protected Grados getGradoLdap(String siglaGradoLdap) {
		try {
			Grados grado = null;
                        
			if (siglaGradoLdap != null) {
				grado = gradosRepository.findByActivoAndId(Boolean.TRUE, siglaGradoLdap, new Sort(Direction.ASC, "id"));
			}
			if(grado==null){
				grado = gradosRepository.findByActivoAndId(Boolean.TRUE, AppConstants.SIN_GRADO, new Sort(Direction.ASC, "id"));
			}
			if(grado!=null){
				return grado;
			}else{
                                grado = gradosRepository.findOne(AppConstants.SIN_GRADO);
				return grado;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Obtiene los datos basicos de usuario desde el DA y los mapea a un objeto
	 * Usuario
	 * 
	 * @param accountName
	 *            sAMAccountName
	 * @return Usuario que contiene los datos obtenidos desde el DA
	 */
//	public Usuario getUsuarioFromLdapByAccountName(String accountName) {
//		Usuario us = new Usuario();
//		us.setLogin("adsan");
//		us.setDocumento("51515");
//		us.setNombre("Adriana");
//		us.setTelefono("5802121");
//		us.setGrado(getGradoLdap("SV."));
//		us.setEmail("adrigs21@gmail.com");
//		us.setDependencia(getDependenciaLdap("Departamento de Inteligencia"));
//		return us;
//	}
	

}
