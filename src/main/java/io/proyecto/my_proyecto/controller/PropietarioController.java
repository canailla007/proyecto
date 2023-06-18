package io.proyecto.my_proyecto.controller;

import io.proyecto.my_proyecto.domain.Proyecto;
import io.proyecto.my_proyecto.model.PropietarioDTO;
import io.proyecto.my_proyecto.repos.ProyectoRepository;
import io.proyecto.my_proyecto.service.PropietarioService;
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
@RequestMapping("/propietarios")
public class PropietarioController {

    private final PropietarioService propietarioService;
    private final ProyectoRepository proyectoRepository;

    public PropietarioController(final PropietarioService propietarioService,
            final ProyectoRepository proyectoRepository) {
        this.propietarioService = propietarioService;
        this.proyectoRepository = proyectoRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("proyectoValues", proyectoRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Proyecto::getId, Proyecto::getNombre)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("propietarios", propietarioService.findAll());
        return "propietario/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("propietario") final PropietarioDTO propietarioDTO) {
        return "propietario/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("propietario") @Valid final PropietarioDTO propietarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("email") && propietarioDTO.getEmail() != null && propietarioService.emailExists(propietarioDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.propietario.email");
        }
        if (bindingResult.hasErrors()) {
            return "propietario/add";
        }
        propietarioService.create(propietarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("propietario.create.success"));
        return "redirect:/propietarios";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("propietario", propietarioService.get(id));
        return "propietario/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("propietario") @Valid final PropietarioDTO propietarioDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final PropietarioDTO currentPropietarioDTO = propietarioService.get(id);
        if (!bindingResult.hasFieldErrors("email") && propietarioDTO.getEmail() != null &&
                !propietarioDTO.getEmail().equalsIgnoreCase(currentPropietarioDTO.getEmail()) &&
                propietarioService.emailExists(propietarioDTO.getEmail())) {
            bindingResult.rejectValue("email", "Exists.propietario.email");
        }
        if (bindingResult.hasErrors()) {
            return "propietario/edit";
        }
        propietarioService.update(id, propietarioDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("propietario.update.success"));
        return "redirect:/propietarios";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        propietarioService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("propietario.delete.success"));
        return "redirect:/propietarios";
    }

}
