package com.dchealth.drgs.db.repository;

import com.dchealth.drgs.db.entity.DrgIcdR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrgIcdRRepository extends JpaRepository<DrgIcdR, Integer> {
}
