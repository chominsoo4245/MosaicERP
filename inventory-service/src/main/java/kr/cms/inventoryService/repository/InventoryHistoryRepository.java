package kr.cms.inventoryService.repository;

import kr.cms.inventoryService.entity.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Long> {
    List<InventoryHistory> findByItemIdAndWarehouseIdAndBinIdAndLotId(Long itemId, Integer warehouseId, Integer binId, Integer lotId);
}
