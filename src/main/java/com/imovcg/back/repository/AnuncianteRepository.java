package com.imovcg.back.repository;

import com.imovcg.back.model.Anunciante;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnuncianteRepository extends JpaRepository<Anunciante, Long> {

    Optional<Anunciante> findByGoogleSub(String googleSub);

    Optional<Anunciante> findByEmail(String email);
}
