package io.proyecto.my_proyecto.repos;

import io.proyecto.my_proyecto.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EstadoRepository extends JpaRepository<Estado, Long> {

    boolean existsByNameIgnoreCase(String name);

}
