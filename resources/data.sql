-- Test data for SSH Authentication Module

-- SSH Key Pairs
INSERT INTO ssh_key_pair (key_name, public_key, private_key, key_fingerprint, key_algorithm, key_size, owner, created_at, updated_at, is_active, description) 
VALUES ('dev-server-key', 'ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC...', 'protected-key-content', 'SHA256:fingerprint1', 'RSA', 2048, 'admin', '2026-02-27 10:00:00', '2026-02-27 10:00:00', 1, 'Main development server key');

INSERT INTO ssh_key_pair (key_name, public_key, private_key, key_fingerprint, key_algorithm, key_size, owner, created_at, updated_at, is_active, description) 
VALUES ('staging-key', 'ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAA...', 'protected-key-content', 'SHA256:fingerprint2', 'ED25519', 256, 'dev-ops', '2026-02-27 11:00:00', '2026-02-27 11:00:00', 1, 'Staging environment access key');

-- Audit Logs
INSERT INTO audit_log (user_id, action, resource, resource_id, status, details, ip_address, timestamp, duration_ms, changes_summary)
VALUES ('admin', 'LOGIN', 'SYSTEM', '1', 'SUCCESS', 'Admin login successful', '192.168.1.1', '2026-02-27 10:00:00', 150, 'User logged in');

INSERT INTO audit_log (user_id, action, resource, resource_id, status, details, ip_address, timestamp, duration_ms, changes_summary)
VALUES ('admin', 'KEY_UPLOAD', 'SSH_KEY', '1', 'SUCCESS', 'Uploaded dev-server-key', '192.168.1.1', '2026-02-27 10:05:00', 300, 'New key pair added');

-- SSH Requests
INSERT INTO ssh_request (request_id, host, port, username, command, key_pair_id, created_at, executed_at, status, result, error_message, execution_time_ms)
VALUES ('req-001', '10.0.0.5', 22, 'ubuntu', 'uptime', '1', '2026-02-27 10:10:00', '2026-02-27 10:10:01', 'COMPLETED', 'up 10 days, 2 hours', NULL, 1200);

INSERT INTO ssh_request (request_id, host, port, username, command, key_pair_id, created_at, executed_at, status, result, error_message, execution_time_ms)
VALUES ('req-002', '10.0.0.5', 22, 'ubuntu', 'ls -la', '1', '2026-02-27 10:15:00', '2026-02-27 10:15:02', 'FAILED', NULL, 'Connection timeout', 5000);
