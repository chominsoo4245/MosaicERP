package kr.cms.lotService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_lot")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lot_id")
    private Long lotId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "lot_number")
    private String lotNumber;

    @Column(name = "initial_stock")
    private Integer initialStock;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "location_info")
    private String locationInfo;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
