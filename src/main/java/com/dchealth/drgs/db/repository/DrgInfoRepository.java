package com.dchealth.drgs.db.repository;

import com.dchealth.drgs.db.entity.DrgInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrgInfoRepository extends JpaRepository<DrgInfo, Integer> {
}
