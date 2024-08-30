package com.giftandgo.assessment.repository;

import com.giftandgo.assessment.data.LogRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRecordRepository extends JpaRepository<LogRecord, String> {
    LogRecord findByUuid(String uuid);
}
