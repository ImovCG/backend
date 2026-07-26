package com.imovcg.back.specification;

import com.imovcg.back.model.Imovel;
import com.imovcg.back.dto.ImoveisFiltrosDTO;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.util.*;

public class ImovelSpecification {

    public static Specification<Imovel> filtros(ImoveisFiltrosDTO filtros) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filtros.getPrecoMin() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        root.get("preco"),
                        filtros.getPrecoMin()
                    )
                );
            }

            if (filtros.getPrecoMax() != null) {
                predicates.add(
                    cb.lessThanOrEqualTo(
                        root.get("preco"),
                        filtros.getPrecoMax()
                    )
                );
            }


            if (filtros.getCidade() != null && !filtros.getCidade().isBlank()) {
                predicates.add(
                    cb.equal(
                        cb.lower(root.get("cidade")),
                        filtros.getCidade().toLowerCase()
                    )
                );
            }

            if (filtros.getBairro() != null && !filtros.getBairro().isBlank()) {
                predicates.add(
                    cb.like(
                        cb.lower(root.get("bairro")),
                        "%" + filtros.getBairro().toLowerCase() + "%"
                    )
                );
            }

            if (filtros.getQuartos() != null) {
                predicates.add(
                    cb.equal(root.get("quartos"), filtros.getQuartos())
                );
            }

            if (filtros.getQuartosMin() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(root.get("quartos"), filtros.getQuartosMin())
                );
            }

            if (filtros.getBanheirosMin() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(root.get("banheiros"), filtros.getBanheirosMin())
                );
            }

            if (filtros.getAreaMin() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(root.get("areaM2"), filtros.getAreaMin())
                );
            }

            if (filtros.getCategoria() != null && !filtros.getCategoria().isBlank()) {
                predicates.add(
                    cb.equal(
                        cb.lower(root.get("categoria")),
                        filtros.getCategoria().toLowerCase()
                    )
                );
            }

            if (filtros.getFonte() != null && !filtros.getFonte().isBlank()) {
                predicates.add(
                    cb.equal(
                        cb.lower(root.get("fonte")),
                        filtros.getFonte().toLowerCase()
                    )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}