package com.example.velotochka.specifications;

import com.example.velotochka.entities.Product;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ProductFeatureSpecification implements Specification<Product> {
    private final String name;
    private final String value;

    public ProductFeatureSpecification(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (null == name || null == value) {
            return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
        }

        List<String> joinNames = Arrays.stream(value.split("\\.")).collect(Collectors.toList());
        System.out.println("joinNames = " + joinNames);
        String lastValue = joinNames.remove(joinNames.size() - 1);
        System.out.println("lastValue(removed from joinNames) = " + lastValue);

        List<String> joinAliases = new ArrayList<>(joinNames.size());
        for (String joinName : joinNames) {
            if (joinAliases.isEmpty()) {
                joinAliases.add(joinName);
            } else {
                joinAliases.add(joinAliases.get(joinAliases.size() - 1) + "_" + joinName);
            }
        }
        System.out.println("joinAliases = " + joinAliases);

        int amountOfPairsOfJoin = joinNames.size() - 1;
        System.out.println("amountOfPairsOfJoin = " + amountOfPairsOfJoin);

        Join<Object, Object> join = root.join("features");
        join = join.on(criteriaBuilder.like(join.get("name"), joinNames.get(0)));

        for (int i = 0; i < amountOfPairsOfJoin; i++) {
            String joinName = joinNames.get(i + 1);
            String joinAlias = joinAliases.get(i);
            join = join.join("valueProduct");
            join = join.join("features");
            join = (Join<Object, Object>) join.on(criteriaBuilder.like(join.get("name"), joinName))
                    .alias(joinAlias + "_features");
        }

        Path<String> lastPathValue = join.get("value");

        Predicate predicateForValue = createPredicate(criteriaBuilder, lastPathValue, lastValue);

        return criteriaBuilder.and(predicateForValue);
    }

    private Predicate createPredicate(CriteriaBuilder criteriaBuilder, Expression<String> path, String value) {
        String operator = name.substring(name.indexOf('(') + 1, name.length() - 1);
        System.out.println("operator = " + operator);
        switch (operator){
            case "lte": {
                return criteriaBuilder.lessThanOrEqualTo(path, value);
            }
            case "gte": {
                return criteriaBuilder.greaterThanOrEqualTo(path, value);
            }
            case "le": {
                return criteriaBuilder.lessThan(path, value);
            }
            case "ge": {
                return criteriaBuilder.greaterThan(path, value);
            }
            case "in": {
                CriteriaBuilder.In<String> inCriteria = criteriaBuilder.in(path);
                String[] strings = value.split(",");
                for (String string : strings) {
                    inCriteria.value(string);
                }
                return inCriteria;
            }
            default: {
                return criteriaBuilder.like(path, value);
            }
        }
    }
}
