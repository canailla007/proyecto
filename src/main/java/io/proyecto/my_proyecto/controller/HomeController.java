package io.proyecto.my_proyecto.controller;




import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;









@Controller
public class HomeController {
    

    
    
    @GetMapping("/admin")
    public String admin() {
        return "home/admin";
    }

   

    @GetMapping("/")
    public String list(final Model model) {
       
        
        return "home/index";
    }
   

}
