package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.UsuSelFavoritos;
import com.laamware.ejercito.doc.web.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author sdelgadom
 */
public interface UsuSelFavoritosRepository extends JpaRepository<UsuSelFavoritos, Integer> {

    /**
     * Metodo que permite listar los 8 primeros usuarios favoritos por el orden 
     * de asignacion
     * @param usuId Usuario de origen
     * @return Lista de usuarios favorito
     */
    @Query(value = ""
            + "select USU_FAVORITO.USU_SEL_ID, USU_FAVORITO.USU_ID, USU_FAVORITO.USU_FAV, USU_FAVORITO.CONTADOR\n"
            + "from(\n"
            + "    select USU_FAVORITO.*, rownum num_registros\n"
            + "    from USU_SEL_FAVORITOS USU_FAVORITO,\n"
            + "         USUARIO USU\n"
            + "    where USU.USU_ID     = USU_FAVORITO.USU_FAV\n"
            + "    AND USU.ACTIVO       = 1\n"
            + "    AND USU_FAVORITO.USU_ID = :usuId\n"
            + "    ORDER BY CONTADOR DESC\n"
            + ") USU_FAVORITO\n"
            + "where USU_FAVORITO.num_registros >= 1 and USU_FAVORITO.num_registros <= 8", nativeQuery = true)
    List<UsuSelFavoritos> findUsersFavorites(@Param("usuId") Integer usuId);
    
    /**
     * metodo que permite encontrar el registro por usuario de origen y usuario
     * favorito.
     * @param u1 Usuario de origen
     * @param u2 Usuario favorito
     * @return Registro
     */
    UsuSelFavoritos findByUsuIdAndUsuFav(Usuario u1, Usuario u2);
}
