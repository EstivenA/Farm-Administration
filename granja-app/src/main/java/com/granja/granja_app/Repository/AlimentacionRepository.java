package com.granja.granja_app.Repository;

import com.granja.granja_app.model.Alimentacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long> {
}
