package io.proyecto.my_proyecto.repos;

import io.proyecto.my_proyecto.domain.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServidorRepository extends JpaRepository<Servidor, Long> {

    boolean existsByServidorIgnoreCase(String servidor);

}
