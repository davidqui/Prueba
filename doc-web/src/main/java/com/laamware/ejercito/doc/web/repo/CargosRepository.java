package com.laamware.ejercito.doc.web.repo;

import com.laamware.ejercito.doc.web.entity.Cargo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author egonzalezm
 */
public interface CargosRepository extends JpaRepository<Cargo, Integer> {

    String CONSULTACARGOSXUSUARIO = ""
            + "select   a.usu_id, \n"
            + "         b.car_id,\n"
            + "         b.car_nombre,\n"
            + "         1 prioridad\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.usu_cargo_principal_id\n"
            + "union\n"
            + "select usu_id,\n"
            + "       car_id,\n"
            + "       car_nombre,\n"
            + "       2 prioridad\n"
            + "from(\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO1_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO2_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO3_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO4_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO5_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO6_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO7_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO8_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO9_ID\n"
            + "union\n"
            + "select  a.usu_id,\n"
            + "        b.car_id,\n"
            + "        b.car_nombre\n"
            + "from USUARIO a,\n"
            + "     CARGO b\n"
            + "where b.car_id = a.USU_CARGO10_ID)\n";

    @Query(nativeQuery = true, value = ""
            + "SELECT count(1) "
            + "FROM CARGO "
            + "WHERE UPPER(CAR_NOMBRE) = UPPER(:carNombre) "
            + "AND CAR_ID != :carId")
    public Integer findregistrosNombreRepetido(@Param("carNombre") String carNombre, @Param("carId") Integer carId);

    @Query(nativeQuery = true, value = ""
            + "SELECT count(1) "
            + "FROM CARGO "
            + "WHERE CAR_NOMBRE = :carNombre ")
    public Integer findregistrosByNombre(@Param("carNombre") String carNombre);

    public Cargo findBycarNombre(String carNombre);

    @Query(value = ""
            + "select car_id, car_nombre\n"
            + "from(\n"
            + CONSULTACARGOSXUSUARIO
            + ")\n"
            + "where usu_id = :usuId\n"
            + "order by prioridad ASC", nativeQuery = true)
    public List<Object[]> findCargosXusuario(@Param("usuId") Integer usuID);
}
