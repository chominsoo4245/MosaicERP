package kr.cms.inventoryService.repository;

import kr.cms.inventoryService.dto.InventorySearchParamDTO;
import kr.cms.inventoryService.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long>, InventoryRepositoryCustom{
    Optional<Inventory> findByItemIdAndWarehouseIdAndBinIdAndLotNumber(Long itemId, Integer warehouseId, Integer binId, String lotNumber);
}
