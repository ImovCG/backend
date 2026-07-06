package com.imovcg.back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.imovcg.back.model.Imovel;
import java.util.Optional;

@Repository
public interface ImovelRepository extends JpaRepository<Imovel, Long>, JpaSpecificationExecutor<Imovel> {

	Optional<Imovel> findByExternalId(String externalId);
	Optional<Imovel> findByHash(String hash);
	boolean existsByHash(String hash);

}
