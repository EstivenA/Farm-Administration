package com.granja.granja_app.Controllers;

import com.granja.granja_app.Repository.PorcinoRepository;
import com.granja.granja_app.model.Porcino;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/porcinos")
public class PorcinoController {

    private final PorcinoRepository porcinoRepository;

    public PorcinoController(PorcinoRepository porcinoRepository) {
        this.porcinoRepository = porcinoRepository;
    }

    @GetMapping
    public List<Porcino> listar() {
        return porcinoRepository.findAll();
    }

    @PostMapping
    public Porcino crear(@RequestBody Porcino porcino) {
        return porcinoRepository.save(porcino);
    }

    @GetMapping("/{id}")
    public Porcino obtener(@PathVariable Long id) {
        return porcinoRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Porcino actualizar(@PathVariable Long id, @RequestBody Porcino porcino) {
        porcino.setId(id);
        return porcinoRepository.save(porcino);
    } 

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        porcinoRepository.deleteById(id);
    }
}
