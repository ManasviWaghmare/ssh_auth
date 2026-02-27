package com.org.sshauth.core.ssh_core_library;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.org.sshauth.core.exception.SSHAuthenticationException;
import com.org.sshauth.core.exception.SSHConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for managing SSH connections.
 */
@Slf4j
@Service
public class SSHConnectionManager {

    private final JSch jsch;

    public SSHConnectionManager() {
        this.jsch = new JSch();
    }

    /**
     * Test SSH connection to a remote host.
     */
    public boolean testConnection(String host, Integer port, String username, String keyPairId) {
        Session session = null;

        try {
            log.info("Testing SSH connection to {}:{} with username: {}", host, port, username);

            session = createSession(host, port, username, keyPairId);
            session.connect(10000); // 10 second timeout

            log.info("SSH connection successful to {}:{}", host, port);
            return true;

        } catch (JSchException e) {
            log.error("Failed to establish SSH connection to {}:{}", host, port, e);
            throw new SSHConnectionException("Failed to connect to " + host + ":" + port, e);

        } finally {
            if (session != null) {
                session.disconnect();
            }
        }
    }

    /**
     * Create an SSH session.
     */
    public Session createSession(String host, Integer port, String username, String keyPairId) {
        try {
            int sshPort = (port != null) ? port : 22;

            Session session = jsch.getSession(username, host, sshPort);

            // Configure session properties
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            log.debug("Session created for {}@{}:{}", username, host, sshPort);
            return session;

        } catch (JSchException e) {
            log.error("Error creating SSH session", e);
            throw new SSHConnectionException("Failed to create SSH session", e);
        }
    }

    /**
     * Authenticate using key pair.
     */
    public void authenticateWithKeyPair(Session session, String privateKeyPath) {
        try {
            log.debug("Authenticating with private key: {}", privateKeyPath);
            jsch.addIdentity(privateKeyPath);
            session.setUserInfo(new SSHUserInfo());

        } catch (JSchException e) {
            log.error("Error authenticating with key pair", e);
            throw new SSHAuthenticationException("Failed to authenticate with key pair", e);
        }
    }

    /**
     * Close an SSH session.
     */
    public void closeSession(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
            log.debug("SSH session closed");
        }
    }

    /**
     * Inner class for handling SSH user info (for authentication).
     */
    private static class SSHUserInfo implements com.jcraft.jsch.UserInfo {

        @Override
        public String getPassphrase() {
            return null;
        }

        @Override
        public String getPassword() {
            return null;
        }

        @Override
        public boolean promptPassword(String message) {
            return false;
        }

        @Override
        public boolean promptPassphrase(String message) {
            return false;
        }

        @Override
        public boolean promptYesNo(String message) {
            return true;
        }

        @Override
        public void showMessage(String message) {
            log.debug("SSH Message: {}", message);
        }
    }
}
