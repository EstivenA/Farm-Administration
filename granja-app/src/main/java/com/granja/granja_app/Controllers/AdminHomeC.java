package com.granja.granja_app.Controllers;

import com.granja.granja_app.Repository.AlimentacionRepository;
import com.granja.granja_app.Repository.ClienteRepository;
import com.granja.granja_app.Repository.PorcinoRepository;
import com.granja.granja_app.model.Alimentacion;
import com.granja.granja_app.model.Cliente;
import com.granja.granja_app.model.Porcino;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminHomeC {

    private final ClienteRepository clienteRepository;
    private final PorcinoRepository porcinoRepository;
    private final AlimentacionRepository alimentacionRepository;

    public AdminHomeC(ClienteRepository clienteRepository, PorcinoRepository porcinoRepository,
            AlimentacionRepository alimentacionRepository) {
        this.clienteRepository = clienteRepository;
        this.porcinoRepository = porcinoRepository;
        this.alimentacionRepository = alimentacionRepository;
    }

    @GetMapping("/home")
    public String home() {
        return "adminhome";
    }

    @GetMapping("/clientes")
    public String clientes(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        return "clienteslist";
    }

    @GetMapping("/clientes/nuevo")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "nuevocliente"; 
    }

    @PostMapping("/clientes/nuevo")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        clienteRepository.save(cliente);
        return "redirect:/admin/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Cliente> clienteOpt = clienteRepository.findById(id);
        if (clienteOpt.isPresent()) {
            model.addAttribute("cliente", clienteOpt.get());
            return "editarcliente";
        } else {
            return "redirect:/admin/clientes";
        }
    }

    @PostMapping("/clientes/editar/{id}")
    public String actualizarCliente(@PathVariable Long id, @ModelAttribute Cliente cliente) {
        cliente.setId(id);
        clienteRepository.save(cliente);
        return "redirect:/admin/clientes";
    }

    @PostMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        clienteRepository.deleteById(id);
        return "redirect:/admin/clientes";
    }

    @GetMapping("/porcinos")
    public String porcinos(Model model) {
        model.addAttribute("porcinos", porcinoRepository.findAll());
        return "porcinoslist";
    }

    @GetMapping("/porcinos/nuevo")
    public String mostrarFormularioNuevoPorcino(Model model) {
        model.addAttribute("porcino", new Porcino());
        model.addAttribute("clientes", clienteRepository.findAll());
        model.addAttribute("alimentaciones", alimentacionRepository.findAll());
        return "nuevoporcino";
    }

    @PostMapping("/porcinos/nuevo")
    public String guardarNuevoPorcino(@ModelAttribute Porcino porcino,
            @RequestParam("cliente_id") Long clienteId,
            @RequestParam("alimentacion_id") Long alimentacionId) {

        // Asignar dueño
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        porcino.setCliente(cliente);

        // Asignar alimentación
        Alimentacion alimentacion = alimentacionRepository.findById(alimentacionId)
                .orElseThrow(() -> new RuntimeException("Alimentación no encontrada"));
        porcino.setAlimentacion(alimentacion);

        porcinoRepository.save(porcino);
        return "redirect:/admin/porcinos";
    }

    @GetMapping("/porcinos/editar/{id}")
    public String mostrarFormularioEdicionPorcino(@PathVariable Long id, Model model) {
        Optional<Porcino> porcinoOpt = porcinoRepository.findById(id);
        if (porcinoOpt.isPresent()) {
            Porcino porcino = porcinoOpt.get();
            model.addAttribute("porcino", porcino);
            return "editarporcino";
        } else {
            return "redirect:/admin/porcinos";
        }
    }

    @PostMapping("/porcinos/editar/{id}")
    public String actualizarPorcino(
            @PathVariable("id") Long id,
            @ModelAttribute Porcino porcino,
            @RequestParam("alimentacion_id") Long alimentacion_id,
            @RequestParam("cliente_id") Long cliente_id) {

        System.out.println("ID en la URL: " + id);
        System.out.println("Objeto porcino recibido: " + porcino);

        // Buscar la alimentación
        Alimentacion alimentacion = alimentacionRepository.findById(alimentacion_id)
                .orElseThrow(() -> new RuntimeException("Alimentación no encontrada"));

        // Buscar el cliente
        Cliente cliente = clienteRepository.findById(cliente_id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        porcino.setAlimentacion(alimentacion);
        porcino.setCliente(cliente);
        porcino.setId(id);

        porcinoRepository.save(porcino);
        return "redirect:/admin/porcinos";
    }

    @GetMapping("/porcinos/eliminar/{id}")
    public String eliminarPorcino(@PathVariable Long id) {
        porcinoRepository.deleteById(id);
        return "redirect:/admin/porcinos";
    }

    @GetMapping("/alimentacion")
    public String alimentaciones(Model model) {
        model.addAttribute("alimentaciones", alimentacionRepository.findAll());
        return "alimentacionlist";
    }

    @GetMapping("/alimentacion/nuevo")
    public String añadirAlimentacion(Model model) {
        model.addAttribute("alimentacion", new Alimentacion());
        return "nuevaalimentacion";
    }

    @PostMapping("/alimentacion/nuevo")
    public String guardarNuevaAlimentacion(@ModelAttribute Alimentacion alimentacion) {
        alimentacionRepository.save(alimentacion);
        return "redirect:/admin/alimentacion";
    }

    @GetMapping("/alimentacion/editar/{id}")
    public String mostrarFormularioEdicionAlimentacion(@PathVariable Long id, Model model) {
        Optional<Alimentacion> alimentacionOpt = alimentacionRepository.findById(id);
        if (alimentacionOpt.isPresent()) {
            model.addAttribute("alimentacion", alimentacionOpt.get());
            return "editaralimentacion";
        } else {
            return "redirect:/admin/alimentacion";
        }
    }

    @PostMapping("/alimentacion/editar/{id}")
    public String actualizarAlimentacion(@PathVariable Long id, @ModelAttribute Alimentacion alimentacion) {
        alimentacion.setId(id); 
        alimentacionRepository.save(alimentacion);
        return "redirect:/admin/alimentacion";
    }

    @GetMapping("/informeclientes")
    public String informeClientes(Model model) {
        List<Cliente> clientes = clienteRepository.findAll();
        model.addAttribute("clientes", clientes);
        return "informeclientes"; 
    }

    @GetMapping("/clientes/informe")
    public void generarInformeClientes(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=clientes_informe.pdf");

        try {
            com.itextpdf.text.Document document = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
            document.open();
            document.add(new com.itextpdf.text.Paragraph("Informe de Clientes y Porcinos\n\n"));

            List<Cliente> clientes = clienteRepository.findAll();

            for (Cliente cliente : clientes) {
                document.add(new com.itextpdf.text.Paragraph(
                        "Cliente: " + cliente.getNombre() + " " + cliente.getApellido()
                                + " | Cedula: " + cliente.getCedula()
                                + " | Telefono: " + cliente.getTelefono()
                                + " | Direccion: " + cliente.getDireccion()));

                document.add(new com.itextpdf.text.Paragraph("Porcinos del cliente:"));

                if (cliente.getPorcinos() != null && !cliente.getPorcinos().isEmpty()) {
                    for (Porcino porcino : cliente.getPorcinos()) {
                        document.add(new com.itextpdf.text.Paragraph(
                                "   - Identificación: " + porcino.getIdentificacion()
                                        + ", Raza: " + porcino.getRaza()
                                        + ", Edad: " + porcino.getEdad()
                                        + ", Peso: " + porcino.getPeso()
                                        + ", Alimentación: "
                                        + (porcino.getAlimentacion() != null
                                                ? porcino.getAlimentacion().getDescripcion()
                                                : "N/A")));
                    }
                } else {
                    document.add(new com.itextpdf.text.Paragraph("   * No tiene porcinos registrados"));
                }

                document.add(
                        new com.itextpdf.text.Paragraph("\n-----------------------------------------------------\n"));
            }

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

