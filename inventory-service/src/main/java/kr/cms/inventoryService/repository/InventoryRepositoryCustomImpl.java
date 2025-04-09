package kr.cms.inventoryService.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import kr.cms.inventoryService.dto.InventorySearchParamDTO;
import kr.cms.inventoryService.entity.Inventory;

import java.util.ArrayList;
import java.util.List;

public class InventoryRepositoryCustomImpl implements InventoryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Inventory> searchByCriteria(InventorySearchParamDTO param) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Inventory> query = cb.createQuery(Inventory.class);
        Root<Inventory> inventory = query.from(Inventory.class);

        List<Predicate> predicates = new ArrayList<>();

        // itemId 조건
        if (param.getItemId() != null) {
            predicates.add(cb.equal(inventory.get("itemId"), param.getItemId()));
        }
        // warehouseId 조건
        if (param.getWarehouseId() != null) {
            predicates.add(cb.equal(inventory.get("warehouseId"), param.getWarehouseId()));
        }
        // keyword 조건 (여기서는 lotNumber에 대해 LIKE 검색)
        if (param.getKeyword() != null && !param.getKeyword().trim().isEmpty()) {
            String pattern = "%" + param.getKeyword().trim() + "%";
            predicates.add(cb.like(inventory.get("lotNumber"), pattern));
        }
        // fromCreatedAt 조건 (createdAt 필드 사용)
        if (param.getFromCreatedAt() != null) {
            predicates.add(cb.greaterThanOrEqualTo(inventory.get("createdAt"), param.getFromCreatedAt()));
        }
        // toCreatedAt 조건 (createdAt 필드 사용)
        if (param.getToCreatedAt() != null) {
            predicates.add(cb.lessThanOrEqualTo(inventory.get("createdAt"), param.getToCreatedAt()));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }
}
