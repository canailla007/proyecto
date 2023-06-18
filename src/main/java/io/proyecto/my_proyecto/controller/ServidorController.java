package io.proyecto.my_proyecto.controller;

import io.proyecto.my_proyecto.model.ServidorDTO;
import io.proyecto.my_proyecto.service.ServidorService;
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
@RequestMapping("/servidors")
public class ServidorController {

    private final ServidorService servidorService;

    public ServidorController(final ServidorService servidorService) {
        this.servidorService = servidorService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("servidors", servidorService.findAll());
        return "servidor/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("servidor") final ServidorDTO servidorDTO) {
        return "servidor/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("servidor") @Valid final ServidorDTO servidorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (!bindingResult.hasFieldErrors("servidor") && servidorDTO.getServidor() != null && servidorService.servidorExists(servidorDTO.getServidor())) {
            bindingResult.rejectValue("servidor", "Exists.servidor.servidor");
        }
        if (bindingResult.hasErrors()) {
            return "servidor/add";
        }
        servidorService.create(servidorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("servidor.create.success"));
        return "redirect:/servidors";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("servidor", servidorService.get(id));
        return "servidor/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("servidor") @Valid final ServidorDTO servidorDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        final ServidorDTO currentServidorDTO = servidorService.get(id);
        if (!bindingResult.hasFieldErrors("servidor") && servidorDTO.getServidor() != null &&
                !servidorDTO.getServidor().equalsIgnoreCase(currentServidorDTO.getServidor()) &&
                servidorService.servidorExists(servidorDTO.getServidor())) {
            bindingResult.rejectValue("servidor", "Exists.servidor.servidor");
        }
        if (bindingResult.hasErrors()) {
            return "servidor/edit";
        }
        servidorService.update(id, servidorDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("servidor.update.success"));
        return "redirect:/servidors";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = servidorService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            servidorService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("servidor.delete.success"));
        }
        return "redirect:/servidors";
    }

}
