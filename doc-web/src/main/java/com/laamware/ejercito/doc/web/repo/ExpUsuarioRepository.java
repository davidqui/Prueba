package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.ExpUsuario;
import com.laamware.ejercito.doc.web.entity.Expediente;
import com.laamware.ejercito.doc.web.entity.Usuario;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpUsuarioRepository extends JpaRepository<ExpUsuario, Long> {
    
    /**
     * Lista los usuarios dado un expediente.
     * @param expediente
     * @return 
     */
    List<ExpUsuario> getByExpIdAndActivoTrue(Expediente expediente, Sort sort);
    
    /***
     * Lista los usuarios expediente dado un expediente y un usuario
     * @param expediente
     * @param usuario
     * @return 
     */
    List<ExpUsuario> getByExpIdAndUsuIdAndActivoTrue(Expediente expediente, Usuario usuario);
}
