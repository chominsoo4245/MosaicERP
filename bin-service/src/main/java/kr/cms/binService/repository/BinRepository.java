package kr.cms.binService.repository;

import kr.cms.binService.entity.Bin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BinRepository extends JpaRepository<Bin, Long>, JpaSpecificationExecutor<Bin> {

}
