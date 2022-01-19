package com.enigma.technicaltest.specification;

import com.enigma.technicaltest.dto.TransferDTO;
import com.enigma.technicaltest.entity.Transfer;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public class TransferSpecification {
  public static Specification<Transfer> getSpecification(TransferDTO transferDTO) {
    return new Specification<Transfer>() {
      @Override
      public Predicate toPredicate(Root<Transfer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (transferDTO.getFromDate() != null){
          Predicate customerNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("createdAt")), "%" + transferDTO.getFromDate().toLowerCase() + "%");
          predicates.add(customerNamePredicate);
        }

        predicates.add(criteriaBuilder.equal(root.get("fromCustomer"), transferDTO.getCustomerId()));
        Predicate[] arrayPredicates = predicates.toArray(new Predicate[predicates.size()]);
        return criteriaBuilder.and(arrayPredicates);
      }
    };
  }
}
