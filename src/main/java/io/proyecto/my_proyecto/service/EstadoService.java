package io.proyecto.my_proyecto.service;

import io.proyecto.my_proyecto.domain.Estado;
import io.proyecto.my_proyecto.domain.Proyecto;
import io.proyecto.my_proyecto.model.EstadoDTO;
import io.proyecto.my_proyecto.repos.EstadoRepository;
import io.proyecto.my_proyecto.repos.ProyectoRepository;
import io.proyecto.my_proyecto.util.NotFoundException;
import io.proyecto.my_proyecto.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;
    private final ProyectoRepository proyectoRepository;

    public EstadoService(final EstadoRepository estadoRepository,
            final ProyectoRepository proyectoRepository) {
        this.estadoRepository = estadoRepository;
        this.proyectoRepository = proyectoRepository;
    }

    public List<EstadoDTO> findAll() {
        final List<Estado> estados = estadoRepository.findAll(Sort.by("id"));
        return estados.stream()
                .map(estado -> mapToDTO(estado, new EstadoDTO()))
                .toList();
    }

    public EstadoDTO get(final Long id) {
        return estadoRepository.findById(id)
                .map(estado -> mapToDTO(estado, new EstadoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final EstadoDTO estadoDTO) {
        final Estado estado = new Estado();
        mapToEntity(estadoDTO, estado);
        return estadoRepository.save(estado).getId();
    }

    public void update(final Long id, final EstadoDTO estadoDTO) {
        final Estado estado = estadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(estadoDTO, estado);
        estadoRepository.save(estado);
    }

    public void delete(final Long id) {
        estadoRepository.deleteById(id);
    }

    private EstadoDTO mapToDTO(final Estado estado, final EstadoDTO estadoDTO) {
        estadoDTO.setId(estado.getId());
        estadoDTO.setName(estado.getName());
        return estadoDTO;
    }

    private Estado mapToEntity(final EstadoDTO estadoDTO, final Estado estado) {
        estado.setName(estadoDTO.getName());
        return estado;
    }

    public boolean nameExists(final String name) {
        return estadoRepository.existsByNameIgnoreCase(name);
    }

    public String getReferencedWarning(final Long id) {
        final Estado estado = estadoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Proyecto estadoProyecto = proyectoRepository.findFirstByEstado(estado);
        if (estadoProyecto != null) {
            return WebUtils.getMessage("estado.proyecto.estado.referenced", estadoProyecto.getId());
        }
        return null;
    }

}
