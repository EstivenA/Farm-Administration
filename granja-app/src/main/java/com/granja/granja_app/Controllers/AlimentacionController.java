package com.granja.granja_app.Controllers;

import com.granja.granja_app.model.Alimentacion;
import com.granja.granja_app.Repository.AlimentacionRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alimentacion")
public class AlimentacionController {

    private final AlimentacionRepository alimentacionRepository;

    public AlimentacionController(AlimentacionRepository alimentacionRepository) {
        this.alimentacionRepository = alimentacionRepository;
    }

    @GetMapping
    public List<Alimentacion> listar() {
        return alimentacionRepository.findAll();
    }

    @PostMapping
    public Alimentacion crear(@RequestBody Alimentacion alimentacion) {
        return alimentacionRepository.save(alimentacion);
    }

    @GetMapping("/{id}")
    public Alimentacion obtener(@PathVariable Long id) {
        return alimentacionRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Alimentacion actualizar(@PathVariable Long id, @RequestBody Alimentacion alimentacion) {
        alimentacion.setId(id);
        return alimentacionRepository.save(alimentacion);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        alimentacionRepository.deleteById(id);
    }
}
