package com.dchealth.drgs.db.repository;

import com.dchealth.drgs.db.entity.MdcInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MDCInfoRepository extends JpaRepository<MdcInfo, Integer> {
}
