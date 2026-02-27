package com.org.sshauth.core.repository;

import com.org.sshauth.core.model.SSHRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for SSHRequest entities.
 */
@Repository
public interface SSHRequestRepository extends JpaRepository<SSHRequest, Long> {
}
