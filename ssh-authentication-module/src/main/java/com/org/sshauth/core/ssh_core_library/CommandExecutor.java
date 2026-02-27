package com.org.sshauth.core.ssh_core_library;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.org.sshauth.core.exception.SSHConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Service for executing commands on remote SSH servers.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommandExecutor {

    private final SSHConnectionManager sshConnectionManager;

    /**
     * Execute a command on a remote SSH server.
     */
    public String executeCommand(String host, Integer port, String username, 
                                 String command, String keyPairId, Long executionTimeout) {
        Session session = null;
        ChannelExec channelExec = null;

        try {
            log.info("Executing command on {}@{}:{}: {}", username, host, port, command);

            // Create session
            session = sshConnectionManager.createSession(host, port, username, keyPairId);
            sshConnectionManager.authenticateWithKeyPair(session, keyPairId);
            session.connect(10000);

            // Execute command
            channelExec = (ChannelExec) session.openChannel("exec");
            channelExec.setCommand(command);
            channelExec.connect();

            // Read output
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(channelExec.getInputStream())
            );

            String line;
            long startTime = System.currentTimeMillis();
            long timeout = executionTimeout != null ? executionTimeout : 30000; // 30 seconds default

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");

                // Check timeout
                if (System.currentTimeMillis() - startTime > timeout) {
                    log.warn("Command execution timeout");
                    channelExec.disconnect();
                    throw new SSHConnectionException("Command execution timeout");
                }
            }

            log.info("Command executed successfully");
            return output.toString();

        } catch (Exception e) {
            log.error("Error executing command", e);
            throw new SSHConnectionException("Failed to execute command: " + e.getMessage(), e);

        } finally {
            if (channelExec != null) {
                channelExec.disconnect();
            }
            if (session != null) {
                sshConnectionManager.closeSession(session);
            }
        }
    }

    /**
     * Execute a command with a specified channel type.
     */
    public String executeCommandWithChannel(String host, Integer port, String username,
                                           String command, String keyPairId, String channelType) {
        try {
            log.info("Executing command with channel type: {}", channelType);
            
            if ("exec".equals(channelType)) {
                return executeCommand(host, port, username, command, keyPairId, null);
            } else {
                throw new SSHConnectionException("Unsupported channel type: " + channelType);
            }

        } catch (Exception e) {
            log.error("Error executing command with channel", e);
            throw new SSHConnectionException("Failed to execute command with channel", e);
        }
    }
}
