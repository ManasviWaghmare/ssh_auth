package com.org.sshauth.core.ssh_core_library;

import com.org.sshauth.core.exception.KeyValidationException;
import com.org.sshauth.core.model.SSHKeyPair;
import com.org.sshauth.core.repository.SSHKeyPairRepository;
import com.org.sshauth.core.utilities_security.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing SSH key authentication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KeyAuthenticationService {

    private final EncryptionUtil encryptionUtil;
    private final SSHKeyPairRepository keyPairRepository;

    /**
     * Upload a new SSH key pair.
     */
    public SSHKeyPair uploadKey(SSHKeyPair keyPair) {
        try {
            log.info("Uploading SSH key: {}", keyPair.getKeyName());

            // Validate keys
            validateKeyFormat(keyPair.getPublicKey(), keyPair.getPrivateKey());

            // Encrypt private key
            String encryptedPrivateKey = encryptionUtil.encrypt(keyPair.getPrivateKey());
            keyPair.setPrivateKey(encryptedPrivateKey);

            // Generate fingerprint
            String fingerprint = generateFingerprint(keyPair.getPublicKey());
            keyPair.setKeyFingerprint(fingerprint);

            keyPair.setCreatedAt(LocalDateTime.now());
            keyPair.setUpdatedAt(LocalDateTime.now());
            keyPair.setIsActive(true);

            // Store in database
            SSHKeyPair savedKeyPair = keyPairRepository.save(keyPair);

            log.info("SSH key uploaded successfully with ID: {}", savedKeyPair.getId());
            return savedKeyPair;

        } catch (Exception e) {
            log.error("Error uploading SSH key", e);
            throw new KeyValidationException("Failed to upload key", e);
        }
    }

    /**
     * Retrieve all SSH keys.
     */
    public List<SSHKeyPair> getAllKeys() {
        log.info("Retrieving all SSH keys");
        return keyPairRepository.findAll();
    }

    /**
     * Get a specific SSH key by ID.
     */
    public SSHKeyPair getKeyById(Long keyId) {
        log.info("Retrieving SSH key with ID: {}", keyId);

        return keyPairRepository.findById(keyId)
                .orElseThrow(() -> new KeyValidationException("SSH key not found with ID: " + keyId));
    }

    /**
     * Delete a SSH key.
     */
    public void deleteKey(Long keyId) {
        log.info("Deleting SSH key with ID: {}", keyId);

        if (!keyPairRepository.existsById(keyId)) {
            throw new KeyValidationException("SSH key not found with ID: " + keyId);
        }

        keyPairRepository.deleteById(keyId);
        log.info("SSH key deleted successfully");
    }

    /**
     * Validate a SSH key.
     */
    public boolean validateKey(Long keyId) {
        try {
            log.info("Validating SSH key with ID: {}", keyId);

            SSHKeyPair keyPair = getKeyById(keyId);

            if (keyPair.getPublicKey() == null || keyPair.getPublicKey().isEmpty()) {
                throw new KeyValidationException("Invalid public key");
            }

            if (keyPair.getPrivateKey() == null || keyPair.getPrivateKey().isEmpty()) {
                throw new KeyValidationException("Invalid private key");
            }

            validateKeyFormat(keyPair.getPublicKey(), keyPair.getPrivateKey());

            log.info("SSH key validation successful");
            return true;

        } catch (Exception e) {
            log.error("Key validation failed", e);
            return false;
        }
    }

    /**
     * Validate key format.
     */
    private void validateKeyFormat(String publicKey, String privateKey) {
        if (!publicKey.contains("ssh-rsa") && !publicKey.contains("ssh-ed25519")) {
            throw new KeyValidationException("Invalid public key format");
        }

        if (!privateKey.contains("BEGIN") || !privateKey.contains("END")) {
            throw new KeyValidationException("Invalid private key format");
        }
    }

    /**
     * Generate key fingerprint.
     */
    private String generateFingerprint(String publicKey) {
        // Simplified fingerprint generation
        return publicKey.substring(0, Math.min(16, publicKey.length()));
    }
}
