package kr.cms.warehouseService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_warehouse")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "name")
    private String name;
    @Column(name = "address_line1")
    private String addressLine1;
    @Column(name = "address_lien2")
    private String addressLine2;

    @Column(name = "city")
    private String city;
    @Column(name = "state")
    private String state;
    @Column(name = "zipcode")
    private String zipcode;
    @Column(name = "country")
    private String country;
    @Column(name = "capacity")
    private Integer capacity;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
