package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author egonzalezm
 */
public interface CargosRepository extends JpaRepository<Cargo, Integer> {

    @Query(nativeQuery = true, value = ""
            + "SELECT count(1) "
            + "FROM CARGO "
            + "WHERE UPPER(CAR_NOMBRE) = UPPER(:carNombre) "
            + "AND CAR_ID != :carId")
    public Integer findregistrosNombreRepetido(@Param("carNombre") String carNombre, @Param("carId") Integer carId);
}
