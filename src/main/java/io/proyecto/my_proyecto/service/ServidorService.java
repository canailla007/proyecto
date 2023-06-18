package io.proyecto.my_proyecto.service;

import io.proyecto.my_proyecto.domain.Proyecto;
import io.proyecto.my_proyecto.domain.Servidor;
import io.proyecto.my_proyecto.model.ServidorDTO;
import io.proyecto.my_proyecto.repos.ProyectoRepository;
import io.proyecto.my_proyecto.repos.ServidorRepository;
import io.proyecto.my_proyecto.util.NotFoundException;
import io.proyecto.my_proyecto.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ServidorService {

    private final ServidorRepository servidorRepository;
    private final ProyectoRepository proyectoRepository;

    public ServidorService(final ServidorRepository servidorRepository,
            final ProyectoRepository proyectoRepository) {
        this.servidorRepository = servidorRepository;
        this.proyectoRepository = proyectoRepository;
    }

    public List<ServidorDTO> findAll() {
        final List<Servidor> servidors = servidorRepository.findAll(Sort.by("id"));
        return servidors.stream()
                .map(servidor -> mapToDTO(servidor, new ServidorDTO()))
                .toList();
    }

    public ServidorDTO get(final Long id) {
        return servidorRepository.findById(id)
                .map(servidor -> mapToDTO(servidor, new ServidorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ServidorDTO servidorDTO) {
        final Servidor servidor = new Servidor();
        mapToEntity(servidorDTO, servidor);
        return servidorRepository.save(servidor).getId();
    }

    public void update(final Long id, final ServidorDTO servidorDTO) {
        final Servidor servidor = servidorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(servidorDTO, servidor);
        servidorRepository.save(servidor);
    }

    public void delete(final Long id) {
        servidorRepository.deleteById(id);
    }

    private ServidorDTO mapToDTO(final Servidor servidor, final ServidorDTO servidorDTO) {
        servidorDTO.setId(servidor.getId());
        servidorDTO.setName(servidor.getName());
        servidorDTO.setServidor(servidor.getServidor());
        return servidorDTO;
    }

    private Servidor mapToEntity(final ServidorDTO servidorDTO, final Servidor servidor) {
        servidor.setName(servidorDTO.getName());
        servidor.setServidor(servidorDTO.getServidor());
        return servidor;
    }

    public boolean servidorExists(final String servidor) {
        return servidorRepository.existsByServidorIgnoreCase(servidor);
    }

    public String getReferencedWarning(final Long id) {
        final Servidor servidor = servidorRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Proyecto servidorProyecto = proyectoRepository.findFirstByServidor(servidor);
        if (servidorProyecto != null) {
            return WebUtils.getMessage("servidor.proyecto.servidor.referenced", servidorProyecto.getId());
        }
        return null;
    }

}
