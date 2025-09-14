package com.granja.granja_app.Controllers;

import com.granja.granja_app.Repository.AlimentacionRepository;
import com.granja.granja_app.Repository.ClienteRepository;
import com.granja.granja_app.Repository.PorcinoRepository;
import com.granja.granja_app.model.Alimentacion;
import com.granja.granja_app.model.Cliente;
import com.granja.granja_app.model.Porcino;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cliente")
public class ClienteHomeC {

    private final ClienteRepository clienteRepository;
    private final PorcinoRepository porcinoRepository;
    private final AlimentacionRepository alimentacionRepository;

    public ClienteHomeC(ClienteRepository clienteRepository, PorcinoRepository porcinoRepository,
            AlimentacionRepository alimentacionRepository) {
        this.clienteRepository = clienteRepository;
        this.porcinoRepository = porcinoRepository;
        this.alimentacionRepository = alimentacionRepository;
    }

    @GetMapping("/home/{id}")
    public String home(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);

        List<Alimentacion> alimentaciones = alimentacionRepository.findAll();
        model.addAttribute("alimentaciones", alimentaciones);

        return "clientehome";
    }

    @GetMapping("/{id}/porcinos")
    public String verPorcinos(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Porcino> porcinos = porcinoRepository.findByCliente(cliente);
        model.addAttribute("cliente", cliente);
        model.addAttribute("porcinos", porcinos);

        return "clienteporcinos";
    }

    @GetMapping("/{id}/porcinos/nuevo")
    public String nuevoPorcino(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        ;
        model.addAttribute("cliente", cliente);
        model.addAttribute("porcino", new Porcino());
        model.addAttribute("alimentaciones", alimentacionRepository.findAll());
        return "clienteporcinosform";
    }

    @GetMapping("/{ide}/porcinos/editar/{id}")
    public String editarPorcino(@PathVariable Long id, @PathVariable Long ide, Model model) {
        Porcino porcino = porcinoRepository.findById(id).orElseThrow();
        model.addAttribute("porcino", porcino);
        Cliente cliente = clienteRepository.findById(ide).orElseThrow();
        model.addAttribute("cliente", cliente);
        model.addAttribute("alimentaciones", alimentacionRepository.findAll());
        return "clienteporcinosform";
    }

    @GetMapping("/{ide}/porcinos/eliminar/{id}")
    public String eliminarPorcino(@PathVariable Long id, @PathVariable Long ide) {
        porcinoRepository.deleteById(id);
        return "redirect:/cliente/" + ide + "/porcinos";
    }

    @PostMapping("/{id}/porcinos/guardar")
    public String guardarPorcino(@PathVariable Long id, @ModelAttribute Porcino porcino, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        ;
        porcino.setCliente(cliente);
        porcinoRepository.save(porcino);
        return "redirect:/cliente/" + id + "/porcinos";
    }

    @GetMapping("/{id}/alimentaciones")
    public String listarAlimentaciones(@PathVariable Long id, Model model) {
        model.addAttribute("alimentaciones", alimentacionRepository.findAll());
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientealimentaciones";
    }

    @GetMapping("/{id}/alimentaciones/nueva")
    public String nuevaAlimentacion(@PathVariable Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        ;
        model.addAttribute("cliente", cliente);
        model.addAttribute("alimentacion", new Alimentacion());
        return "clientealimentacionform";
    }

    @PostMapping("/{id}/alimentaciones/guardar")
    public String guardarAlimentacion(@PathVariable Long id, @ModelAttribute Alimentacion alimentacion, Model model) {
        alimentacionRepository.save(alimentacion);
        return "redirect:/cliente/" + id + "/alimentaciones";
    }

    @GetMapping("/{ide}/alimentaciones/editar/{id}")
    public String editarAlimentacion(@PathVariable Long id, @PathVariable Long ide, Model model) {
        Alimentacion ali = alimentacionRepository.findById(id).orElseThrow();
        model.addAttribute("alimentacion", ali);
        Cliente cliente = clienteRepository.findById(ide).orElseThrow();
        model.addAttribute("cliente", cliente);
        return "clientealimentacionform";
    }
}



