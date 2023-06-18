package io.proyecto.my_proyecto.service;

import io.proyecto.my_proyecto.domain.Propietario;
import io.proyecto.my_proyecto.domain.Proyecto;
import io.proyecto.my_proyecto.model.PropietarioDTO;
import io.proyecto.my_proyecto.repos.PropietarioRepository;
import io.proyecto.my_proyecto.repos.ProyectoRepository;
import io.proyecto.my_proyecto.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PropietarioService {

    private final PropietarioRepository propietarioRepository;
    private final ProyectoRepository proyectoRepository;

    public PropietarioService(final PropietarioRepository propietarioRepository,
            final ProyectoRepository proyectoRepository) {
        this.propietarioRepository = propietarioRepository;
        this.proyectoRepository = proyectoRepository;
    }

    public List<PropietarioDTO> findAll() {
        final List<Propietario> propietarios = propietarioRepository.findAll(Sort.by("id"));
        return propietarios.stream()
                .map(propietario -> mapToDTO(propietario, new PropietarioDTO()))
                .toList();
    }

    public PropietarioDTO get(final Long id) {
        return propietarioRepository.findById(id)
                .map(propietario -> mapToDTO(propietario, new PropietarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PropietarioDTO propietarioDTO) {
        final Propietario propietario = new Propietario();
        mapToEntity(propietarioDTO, propietario);
        return propietarioRepository.save(propietario).getId();
    }

    public void update(final Long id, final PropietarioDTO propietarioDTO) {
        final Propietario propietario = propietarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(propietarioDTO, propietario);
        propietarioRepository.save(propietario);
    }

    public void delete(final Long id) {
        propietarioRepository.deleteById(id);
    }

    private PropietarioDTO mapToDTO(final Propietario propietario,
            final PropietarioDTO propietarioDTO) {
        propietarioDTO.setId(propietario.getId());
        propietarioDTO.setNombre(propietario.getNombre());
        propietarioDTO.setApellido1(propietario.getApellido1());
        propietarioDTO.setApellido2(propietario.getApellido2());
        propietarioDTO.setEmail(propietario.getEmail());
        propietarioDTO.setTelefono(propietario.getTelefono());
        propietarioDTO.setProyecto(propietario.getProyecto() == null ? null : propietario.getProyecto().getId());
        return propietarioDTO;
    }

    private Propietario mapToEntity(final PropietarioDTO propietarioDTO,
            final Propietario propietario) {
        propietario.setNombre(propietarioDTO.getNombre());
        propietario.setApellido1(propietarioDTO.getApellido1());
        propietario.setApellido2(propietarioDTO.getApellido2());
        propietario.setEmail(propietarioDTO.getEmail());
        propietario.setTelefono(propietarioDTO.getTelefono());
        final Proyecto proyecto = propietarioDTO.getProyecto() == null ? null : proyectoRepository.findById(propietarioDTO.getProyecto())
                .orElseThrow(() -> new NotFoundException("proyecto not found"));
        propietario.setProyecto(proyecto);
        return propietario;
    }

    public boolean emailExists(final String email) {
        return propietarioRepository.existsByEmailIgnoreCase(email);
    }

}
