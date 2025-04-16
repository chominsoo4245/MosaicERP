package kr.cms.serialService.repository;

import kr.cms.serialService.entity.Serial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SerialRepository extends JpaRepository<Serial, Long>, JpaSpecificationExecutor<Serial> {

}
