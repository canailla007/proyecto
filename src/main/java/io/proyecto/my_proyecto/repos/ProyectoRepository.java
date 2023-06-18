package io.proyecto.my_proyecto.repos;

import io.proyecto.my_proyecto.domain.Estado;
import io.proyecto.my_proyecto.domain.Proyecto;
import io.proyecto.my_proyecto.domain.Servidor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    Proyecto findFirstByEstado(Estado estado);

    Proyecto findFirstByServidor(Servidor servidor);

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByUrlIgnoreCase(String url);

}
