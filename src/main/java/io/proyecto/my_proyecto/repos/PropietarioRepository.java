package io.proyecto.my_proyecto.repos;

import io.proyecto.my_proyecto.domain.Propietario;
import io.proyecto.my_proyecto.domain.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PropietarioRepository extends JpaRepository<Propietario, Long> {

    Propietario findFirstByProyecto(Proyecto proyecto);

    boolean existsByEmailIgnoreCase(String email);

}
