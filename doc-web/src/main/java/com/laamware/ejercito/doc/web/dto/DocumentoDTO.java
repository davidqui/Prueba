package com.laamware.ejercito.doc.web.dto;

/**
 * 
 * @author rafar
 *
 */
public class DocumentoDTO{

	private String id;
	
	public DocumentoDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if( obj == null ){
			return false;
		}
		
		return id.equals( ((DocumentoDTO)obj).getId() );
	}
	
}
