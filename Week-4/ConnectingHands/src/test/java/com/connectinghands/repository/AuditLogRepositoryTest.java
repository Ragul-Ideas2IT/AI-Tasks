package com.connectinghands.repository;

import com.connectinghands.entity.AuditLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void whenSaveAuditLog_thenFindById() {
        AuditLog log = new AuditLog();
        log.setAction("CREATE");
        log.setEntityType("User");
        log.setEntityId(1L);
        log.setOldValue(null);
        log.setNewValue("{\"email\":\"test@example.com\"}");
        log.setIpAddress("127.0.0.1");

        AuditLog saved = auditLogRepository.save(log);
        assertThat(saved.getId()).isNotNull();
        assertThat(auditLogRepository.findById(saved.getId())).isPresent();
    }
} 