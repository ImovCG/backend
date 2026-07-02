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

            if (filtros.getTipo() != null && !filtros.getTipo().isBlank()) {
                predicates.add(
                    cb.equal(
                        cb.lower(root.get("tipo")),
                        filtros.getTipo().toLowerCase()
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

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}