package com.bnkc.assetsystembackend.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class Search<T> implements Specification<T> {

    private Map<String, Object> filters;
    private List<String> fields;

    @Override
    public Predicate toPredicate(Root<T> root,
                                 CriteriaQuery<?> query,
                                 CriteriaBuilder cb) {

        if (filters == null || fields == null || fields.isEmpty()) {
            return cb.conjunction();
        }

        Object raw = filters.get("keyword");
        if (raw == null) {
            return cb.conjunction();
        }

        String term = raw.toString().trim().toLowerCase();
        if (term.isEmpty()) {
            return cb.conjunction();
        }

        List<Predicate> predicates = fields.stream()
                .map(f -> cb.like(cb.lower(root.get(f)), "%" + term + "%"))
                .toList();

        return cb.or(predicates.toArray(Predicate[]::new));
    }
}
