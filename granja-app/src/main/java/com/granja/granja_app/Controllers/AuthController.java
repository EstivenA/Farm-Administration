package com.granja.granja_app.Controllers;

import com.granja.granja_app.Repository.ClienteRepository;
import com.granja.granja_app.model.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final ClienteRepository clienteRepository;

    public AuthController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        Optional<Cliente> cliente = clienteRepository.findByUsername(username);

        if (cliente.isPresent() && cliente.get().getPassword().equals(password)) {
            String rol = cliente.get().getRol();

            if ("ADMIN".equalsIgnoreCase(rol)) {
                return "redirect:/admin/home";
            } else if ("CLIENTE".equalsIgnoreCase(rol)) {
                Long ide= cliente.get().getId();
                return "redirect:/cliente/home/" + ide;
            } else {
                model.addAttribute("error", "Rol no reconocido");
                return "login";
            }
        } else {
            model.addAttribute("error", "Credenciales inválidas");
            return "login";
        }
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String cedula,
                           @RequestParam String nombre,
                           @RequestParam String apellido,
                           @RequestParam String direccion,
                           Model model) {

        if (clienteRepository.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El usuario ya existe");
            return "register";
        }

        Cliente nuevoCliente = new Cliente();
        nuevoCliente.setUsername(username);
        nuevoCliente.setPassword(password);
        nuevoCliente.setCedula(cedula);
        nuevoCliente.setNombre(nombre);
        nuevoCliente.setApellido(apellido);
        nuevoCliente.setDireccion(direccion);
        nuevoCliente.setRol("CLIENTE"); 

        clienteRepository.save(nuevoCliente);

        model.addAttribute("mensaje", "Registro exitoso, ahora puedes iniciar sesión");
        return "login";
    }
}
