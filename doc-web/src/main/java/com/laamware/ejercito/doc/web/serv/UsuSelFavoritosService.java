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
    
    /**
     * Metodo que permite insertar o actualizar un usuario de asignación favorito
     * @param usuario Usuario en sesión
     * @param seleccionado Usuario favorito
     */
    public void addUsuarioSelected(Usuario usuario, Usuario seleccionado){
        UsuSelFavoritos registro = favoritosRepository.findByUsuIdAndUsuFav(usuario, seleccionado);
        if(registro != null){
            registro.setContador(registro.getContador()+1);
        }else{
            registro = new UsuSelFavoritos();
            registro.setUsuId(usuario);
            registro.setUsuFav(seleccionado);
            registro.setContador(1);
        }
        favoritosRepository.saveAndFlush(registro);
    }
    
    /**
     * Retorna la lista de usuarios favoritos por usuario
     * @param usuario Usuario de origen
     * @return Lista de usuarios favoritos
     */
    public List<UsuSelFavoritos> listarUsuariosFavoritos(Usuario usuario){
        List<UsuSelFavoritos> findUsersFavorites = favoritosRepository.findUsersFavorites(usuario.getId());
        for (int i = 0; i < findUsersFavorites.size(); i++) {
            for (int j = 0; j < findUsersFavorites.size()-1; j++) {
                Integer p1 = findUsersFavorites.get(j).getUsuFav().getUsuGrado().getPesoOrden();
                Integer p2 = findUsersFavorites.get(j+1).getUsuFav().getUsuGrado().getPesoOrden();
                if (p1 < p2) {
                    UsuSelFavoritos aux = findUsersFavorites.get(j);
                    findUsersFavorites.set(j, findUsersFavorites.get(j+1));
                    findUsersFavorites.set(j+1, aux);
                }
            }
        }
        return findUsersFavorites;
    }
}
