package com.example.scaapi.model.repository;

import com.example.scaapi.model.entity.Relacionado;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RelacionadoRepository extends JpaRepository<Relacionado, Long> {
    List<Relacionado> findByPartidaId(Long partidaId);
}
