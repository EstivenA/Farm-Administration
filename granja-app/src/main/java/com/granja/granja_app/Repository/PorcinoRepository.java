package com.granja.granja_app.Repository;

import com.granja.granja_app.model.Cliente;
import com.granja.granja_app.model.Porcino;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PorcinoRepository extends JpaRepository<Porcino, Long> {
    List<Porcino> findByCliente(Cliente cliente);
    List<Porcino> findByClienteId(Long clienteId);
}
