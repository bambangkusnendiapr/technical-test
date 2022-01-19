package com.enigma.technicaltest.specification;

import com.enigma.technicaltest.dto.MerchantDTO;
import com.enigma.technicaltest.entity.Merchant;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class MerchantSpecification {
  public static Specification<Merchant> getSpecification(MerchantDTO merchantDTO) {
    return new Specification<Merchant>() {
      @Override
      public Predicate toPredicate(Root<Merchant> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();
        if (merchantDTO.getSearchName() != null){
          Predicate predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + merchantDTO.getSearchName().toLowerCase() + "%");
          predicates.add(predicate);
        }
        Predicate[] arrayPredicates = predicates.toArray(new Predicate[predicates.size()]);
        return criteriaBuilder.and(arrayPredicates);
      }
    };
  }
}
