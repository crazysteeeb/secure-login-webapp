package edu.missouristate.controller;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
@Controller
public class IndexController {
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }
    @GetMapping("/admin")
    public String getAdmin(Model model, Principal principal) {
// Modern way to get user info: Inject the Principal object
        model.addAttribute("username", principal.getName());
        return "admindashboard";
    }
    @GetMapping("/student")
    public String getStudent() {
        return "studentdashboard";
    }
    @GetMapping("/dashboard")
    public String getDashboard(Authentication authentication) {
// A powerful way to get user details and roles
        for (GrantedAuthority auth : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(auth.getAuthority())) {
                return "redirect:/admin";
            }
        }
// Default to student if not admin
        return "redirect:/student";
    }
}