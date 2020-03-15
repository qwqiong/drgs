package com.dchealth.drgs.db.repository;

import com.dchealth.drgs.db.entity.DrgIcd9Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrgIcd9Repository extends JpaRepository<DrgIcd9Info, Integer> {
}
