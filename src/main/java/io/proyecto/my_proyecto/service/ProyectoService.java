package io.proyecto.my_proyecto.service;

import io.proyecto.my_proyecto.domain.Estado;
import io.proyecto.my_proyecto.domain.Propietario;
import io.proyecto.my_proyecto.domain.Proyecto;
import io.proyecto.my_proyecto.domain.Servidor;
import io.proyecto.my_proyecto.model.ProyectoDTO;
import io.proyecto.my_proyecto.repos.EstadoRepository;
import io.proyecto.my_proyecto.repos.PropietarioRepository;
import io.proyecto.my_proyecto.repos.ProyectoRepository;
import io.proyecto.my_proyecto.repos.ServidorRepository;
import io.proyecto.my_proyecto.util.NotFoundException;
import io.proyecto.my_proyecto.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final ServidorRepository servidorRepository;
    private final EstadoRepository estadoRepository;
    private final PropietarioRepository propietarioRepository;

    public ProyectoService(final ProyectoRepository proyectoRepository,
            final ServidorRepository servidorRepository, final EstadoRepository estadoRepository,
            final PropietarioRepository propietarioRepository) {
        this.proyectoRepository = proyectoRepository;
        this.servidorRepository = servidorRepository;
        this.estadoRepository = estadoRepository;
        this.propietarioRepository = propietarioRepository;
    }

    public List<ProyectoDTO> findAll() {
        final List<Proyecto> proyectos = proyectoRepository.findAll(Sort.by("id"));
        return proyectos.stream()
                .map(proyecto -> mapToDTO(proyecto, new ProyectoDTO()))
                .toList();
    }

    public ProyectoDTO get(final Long id) {
        return proyectoRepository.findById(id)
                .map(proyecto -> mapToDTO(proyecto, new ProyectoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProyectoDTO proyectoDTO) {
        final Proyecto proyecto = new Proyecto();
        mapToEntity(proyectoDTO, proyecto);
        return proyectoRepository.save(proyecto).getId();
    }

    public void update(final Long id, final ProyectoDTO proyectoDTO) {
        final Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(proyectoDTO, proyecto);
        proyectoRepository.save(proyecto);
    }

    public void delete(final Long id) {
        proyectoRepository.deleteById(id);
    }

    private ProyectoDTO mapToDTO(final Proyecto proyecto, final ProyectoDTO proyectoDTO) {
        proyectoDTO.setId(proyecto.getId());
        proyectoDTO.setNombre(proyecto.getNombre());
        proyectoDTO.setUrl(proyecto.getUrl());
        proyectoDTO.setFechaCreada(proyecto.getFechaCreada());
        proyectoDTO.setFechaActualizado(proyecto.getFechaActualizado());
        proyectoDTO.setServidor(proyecto.getServidor() == null ? null : proyecto.getServidor().getId());
        proyectoDTO.setEstado(proyecto.getEstado() == null ? null : proyecto.getEstado().getId());
        return proyectoDTO;
    }

    private Proyecto mapToEntity(final ProyectoDTO proyectoDTO, final Proyecto proyecto) {
        proyecto.setNombre(proyectoDTO.getNombre());
        proyecto.setUrl(proyectoDTO.getUrl());
        proyecto.setFechaCreada(proyectoDTO.getFechaCreada());
        proyecto.setFechaActualizado(proyectoDTO.getFechaActualizado());
        final Servidor servidor = proyectoDTO.getServidor() == null ? null : servidorRepository.findById(proyectoDTO.getServidor())
                .orElseThrow(() -> new NotFoundException("servidor not found"));
        proyecto.setServidor(servidor);
        final Estado estado = proyectoDTO.getEstado() == null ? null : estadoRepository.findById(proyectoDTO.getEstado())
                .orElseThrow(() -> new NotFoundException("estado not found"));
        proyecto.setEstado(estado);
        return proyecto;
    }

    public boolean nombreExists(final String nombre) {
        return proyectoRepository.existsByNombreIgnoreCase(nombre);
    }

    public boolean urlExists(final String url) {
        return proyectoRepository.existsByUrlIgnoreCase(url);
    }

    public String getReferencedWarning(final Long id) {
        final Proyecto proyecto = proyectoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Propietario proyectoPropietario = propietarioRepository.findFirstByProyecto(proyecto);
        if (proyectoPropietario != null) {
            return WebUtils.getMessage("proyecto.propietario.proyecto.referenced", proyectoPropietario.getId());
        }
        return null;
    }

}
