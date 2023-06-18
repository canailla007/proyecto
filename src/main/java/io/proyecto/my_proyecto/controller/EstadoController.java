package io.proyecto.my_proyecto.controller;

import io.proyecto.my_proyecto.model.EstadoDTO;
import io.proyecto.my_proyecto.service.EstadoService;
import io.proyecto.my_proyecto.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/estados")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(final EstadoService estadoService) {
        this.estadoService = estadoService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("estados", estadoService.findAll());
        return "estado/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("estado") final EstadoDTO estadoDTO) {
        return "estado/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("estado") @Valid final EstadoDTO estadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("name") && estadoService.nameExists(estadoDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.estado.name");
        }
        if (bindingResult.hasErrors()) {
            return "estado/add";
        }
        estadoService.create(estadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("estado.create.success"));
        return "redirect:/estados";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("estado", estadoService.get(id));
        return "estado/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("estado") @Valid final EstadoDTO estadoDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final EstadoDTO currentEstadoDTO = estadoService.get(id);
        if (!bindingResult.hasFieldErrors("name") &&
                !estadoDTO.getName().equalsIgnoreCase(currentEstadoDTO.getName()) &&
                estadoService.nameExists(estadoDTO.getName())) {
            bindingResult.rejectValue("name", "Exists.estado.name");
        }
        if (bindingResult.hasErrors()) {
            return "estado/edit";
        }
        estadoService.update(id, estadoDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("estado.update.success"));
        return "redirect:/estados";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = estadoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            estadoService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("estado.delete.success"));
        }
        return "redirect:/estados";
    }

}
