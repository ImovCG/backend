package com.imovcg.back.specification;

import com.imovcg.back.model.Imovel;
import com.imovcg.back.dto.ImoveisFiltrosDTO;
import com.imovcg.back.util.UniversidadeCampinaGrande;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


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

            if (filtros.getDistanciaMaximaKm() != null) {
                UniversidadeCampinaGrande universidade =
                        UniversidadeCampinaGrande.from(filtros.getUniversidade());
                predicates.add(cb.isNotNull(root.get("latitude")));
                predicates.add(cb.isNotNull(root.get("longitude")));
                predicates.add(cb.lessThanOrEqualTo(
                        distanciaEmKm(root, cb, universidade),
                        filtros.getDistanciaMaximaKm()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Expression<Double> distanciaEmKm(
            jakarta.persistence.criteria.Root<Imovel> root,
            jakarta.persistence.criteria.CriteriaBuilder cb,
            UniversidadeCampinaGrande universidade) {
        Expression<Double> latitude = cb.function(
                "radians", Double.class, root.get("latitude"));
        Expression<Double> longitude = cb.function(
                "radians", Double.class, root.get("longitude"));
        Expression<Double> latitudeUniversidade = cb.function(
                "radians", Double.class, cb.literal(universidade.getLatitude()));
        Expression<Double> longitudeUniversidade = cb.function(
                "radians", Double.class, cb.literal(universidade.getLongitude()));

        Expression<Double> diferencaLatitude = cb.diff(latitude, latitudeUniversidade);
        Expression<Double> diferencaLongitude = cb.diff(longitude, longitudeUniversidade);
        Expression<Double> senoLatitude = cb.function(
                "sin", Double.class, cb.quot(diferencaLatitude, cb.literal(2.0)));
        Expression<Double> senoLongitude = cb.function(
                "sin", Double.class, cb.quot(diferencaLongitude, cb.literal(2.0)));
        Expression<Double> parteLatitude = cb.prod(senoLatitude, senoLatitude);
        Expression<Double> parteLongitude = cb.prod(
                cb.prod(
                        cb.function("cos", Double.class, latitude),
                        cb.function("cos", Double.class, latitudeUniversidade)),
                cb.prod(senoLongitude, senoLongitude));
        Expression<Double> haversine = cb.sum(parteLatitude, parteLongitude);
        Expression<Double> haversineLimitado = cb.<Double>selectCase()
                .when(cb.greaterThan(haversine, 1.0), 1.0)
                .otherwise(haversine);

        Expression<Double> angulo = cb.prod(
                cb.literal(2.0),
                cb.function("asin", Double.class, cb.sqrt(haversineLimitado)));
        return cb.prod(cb.literal(6371.0088), angulo);
    }
}