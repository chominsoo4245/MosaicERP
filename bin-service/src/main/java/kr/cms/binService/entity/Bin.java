package kr.cms.binService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_bin")
public class Bin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bin_id")
    private Long binId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
