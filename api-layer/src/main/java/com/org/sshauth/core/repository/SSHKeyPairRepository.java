package com.org.sshauth.core.repository;

import com.org.sshauth.core.model.SSHKeyPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for SSHKeyPair entities.
 */
@Repository
public interface SSHKeyPairRepository extends JpaRepository<SSHKeyPair, Long> {
    Optional<SSHKeyPair> findByKeyName(String keyName);
}
