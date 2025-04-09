package kr.cms.inventoryService.repository;

import kr.cms.inventoryService.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByItemIdAndWarehouseIdAndBinIdAndLotNumber(Long itemId, Integer warehouseId, Integer binId, String lotNumber);
}
