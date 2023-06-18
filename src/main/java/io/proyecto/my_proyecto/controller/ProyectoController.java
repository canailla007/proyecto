package io.proyecto.my_proyecto.controller;

import io.proyecto.my_proyecto.domain.Estado;
import io.proyecto.my_proyecto.domain.Servidor;
import io.proyecto.my_proyecto.model.ProyectoDTO;
import io.proyecto.my_proyecto.repos.EstadoRepository;
import io.proyecto.my_proyecto.repos.ServidorRepository;

import io.proyecto.my_proyecto.service.ProyectoService;
import io.proyecto.my_proyecto.util.CustomCollectors;
import io.proyecto.my_proyecto.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/proyectos")
public class ProyectoController {

    private final ProyectoService proyectoService;
    private final ServidorRepository servidorRepository;
    private final EstadoRepository estadoRepository;

    public ProyectoController(final ProyectoService proyectoService,
            final ServidorRepository servidorRepository, final EstadoRepository estadoRepository) {
        this.proyectoService = proyectoService;
        this.servidorRepository = servidorRepository;
        this.estadoRepository = estadoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("servidorValues", servidorRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Servidor::getId, Servidor::getName)));
        model.addAttribute("estadoValues", estadoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Estado::getId, Estado::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("proyectos", proyectoService.findAll());
      
       
        return "proyecto/list";
    }

  

    @GetMapping("/add")
    public String add(@ModelAttribute("proyecto") final ProyectoDTO proyectoDTO) {
        return "proyecto/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("proyecto") @Valid final ProyectoDTO proyectoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("nombre") && proyectoDTO.getNombre() != null && proyectoService.nombreExists(proyectoDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.proyecto.nombre");
        }
        if (!bindingResult.hasFieldErrors("url") && proyectoDTO.getUrl() != null && proyectoService.urlExists(proyectoDTO.getUrl())) {
            bindingResult.rejectValue("url", "Exists.proyecto.url");
        }
        if (bindingResult.hasErrors()) {
            return "proyecto/add";
        }
        proyectoService.create(proyectoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("proyecto.create.success"));
        return "redirect:/proyectos";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("proyecto", proyectoService.get(id));
        return "proyecto/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("proyecto") @Valid final ProyectoDTO proyectoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final ProyectoDTO currentProyectoDTO = proyectoService.get(id);
        if (!bindingResult.hasFieldErrors("nombre") && proyectoDTO.getNombre() != null &&
                !proyectoDTO.getNombre().equalsIgnoreCase(currentProyectoDTO.getNombre()) &&
                proyectoService.nombreExists(proyectoDTO.getNombre())) {
            bindingResult.rejectValue("nombre", "Exists.proyecto.nombre");
        }
        if (!bindingResult.hasFieldErrors("url") && proyectoDTO.getUrl() != null &&
                !proyectoDTO.getUrl().equalsIgnoreCase(currentProyectoDTO.getUrl()) &&
                proyectoService.urlExists(proyectoDTO.getUrl())) {
            bindingResult.rejectValue("url", "Exists.proyecto.url");
        }
        if (bindingResult.hasErrors()) {
            return "proyecto/edit";
        }
        proyectoService.update(id, proyectoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("proyecto.update.success"));
        return "redirect:/proyectos";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = proyectoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            proyectoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("proyecto.delete.success"));
        }
        return "redirect:/proyectos";
    }

}
