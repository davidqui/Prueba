package com.laamware.ejercito.doc.web.serv;

import com.laamware.ejercito.doc.web.entity.UsuSelFavoritos;
import com.laamware.ejercito.doc.web.entity.Usuario;
import com.laamware.ejercito.doc.web.repo.UsuSelFavoritosRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio para almacenar los usuarios favoritos dentro de la selección
 * @author Samuel Delgado Muñoz
 * @since 1.8
 * @version 11/07/2018 Issue #179 (SICDI-Controltech) feature-179
 */
@Service
public class UsuSelFavoritosService {
    
    @Autowired
    private UsuSelFavoritosRepository favoritosRepository;
    
    
    public void addUsuarioSelected(Usuario usuario, Usuario seleccionado){
        
    }
    
    
    public List<UsuSelFavoritos> listarUsuariosFavoritos(Usuario usuario){
        return null;
    }
}
