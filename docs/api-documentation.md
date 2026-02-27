# SSH Authentication Module - API Documentation

## Overview
The SSH Authentication Module is a Spring Boot-based REST API that provides secure SSH connection management, remote command execution, and SSH key management capabilities. It integrates with various backend systems for attendance, certificates, and dashboard operations.

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
Currently, all endpoints are open. In production, implement JWT or OAuth2 authentication.

---

## API Endpoints

### SSH Operations

#### 1. Test SSH Connection
**Endpoint:** `POST /ssh/connect`

**Description:** Tests SSH connection to a remote host.

**Request Body:**
```json
{
  "host": "192.168.1.100",
  "port": 22,
  "username": "admin",
  "keyPairId": "key-1",
  "connectionTimeout": 5000,
  "sessionTimeout": 30000,
  "strictHostKeyChecking": false
}
```

**Response:**
```json
{
  "success": true,
  "message": "Connection successful",
  "requestId": "uuid-string",
  "data": null,
  "errorDetails": null,
  "timestamp": "2026-02-25T10:30:00",
  "executionTimeMs": 1234,
  "exitCode": null
}
```

**Status Code:** 200 (Success), 500 (Error)

---

#### 2. Execute Command
**Endpoint:** `POST /ssh/execute`

**Description:** Executes a command on a remote SSH server.

**Request Body:**
```json
{
  "host": "192.168.1.100",
  "username": "admin",
  "command": "ls -la /home",
  "keyPairId": "key-1",
  "port": 22,
  "executionTimeout": 30000
}
```

**Response:**
```json
{
  "success": true,
  "message": "Command executed successfully",
  "requestId": "uuid-string",
  "data": "total 48\ndrwxr-xr-x 5 root root 4096 Feb 25 10:00 .\n...",
  "errorDetails": null,
  "timestamp": "2026-02-25T10:35:00",
  "executionTimeMs": 500,
  "exitCode": 0
}
```

**Status Code:** 200 (Success), 500 (Error)

---

#### 3. Check Connection Status
**Endpoint:** `GET /ssh/status/{host}`

**Description:** Retrieves the connection status for a specific host.

**Response:**
```json
{
  "success": true,
  "message": "Status retrieved successfully",
  "requestId": null,
  "data": "Connected",
  "errorDetails": null,
  "timestamp": "2026-02-25T10:40:00",
  "executionTimeMs": null,
  "exitCode": null
}
```

**Status Code:** 200 (Success), 500 (Error)

---

### Key Management

#### 1. Upload SSH Key
**Endpoint:** `POST /keys/upload`

**Description:** Uploads a new SSH key pair.

**Request Body:**
```json
{
  "keyName": "production-key",
  "publicKey": "ssh-rsa AAAAB3NzaC1yc2EAAAA...",
  "privateKey": "-----BEGIN RSA PRIVATE KEY-----\nMIIEpAIBAAKCAQEA...\n-----END RSA PRIVATE KEY-----",
  "keyAlgorithm": "RSA",
  "keySize": 2048,
  "description": "Production server SSH key",
  "setAsDefault": true
}
```

**Response:**
```json
{
  "success": true,
  "message": "Key uploaded successfully",
  "requestId": "uuid-string",
  "data": "1234567890",
  "errorDetails": null,
  "timestamp": "2026-02-25T10:45:00",
  "executionTimeMs": null,
  "exitCode": null
}
```

**Status Code:** 201 (Created), 500 (Error)

---

#### 2. Get All Keys
**Endpoint:** `GET /keys/all`

**Description:** Retrieves all SSH keys from the system.

**Response:**
```json
{
  "success": true,
  "message": "Keys retrieved successfully",
  "requestId": null,
  "data": "5 keys found",
  "errorDetails": null,
  "timestamp": "2026-02-25T10:50:00",
  "executionTimeMs": null,
  "exitCode": null
}
```

**Status Code:** 200 (Success), 500 (Error)

---

#### 3. Get Key by ID
**Endpoint:** `GET /keys/{keyId}`

**Description:** Retrieves a specific SSH key by its ID.

**Response:**
```json
{
  "success": true,
  "message": "Key retrieved successfully",
  "requestId": null,
  "data": "production-key",
  "errorDetails": null,
  "timestamp": "2026-02-25T10:55:00",
  "executionTimeMs": null,
  "exitCode": null
}
```

**Status Code:** 200 (Success), 404 (Not Found), 500 (Error)

---

#### 4. Delete Key
**Endpoint:** `DELETE /keys/{keyId}`

**Description:** Deletes a specific SSH key.

**Response:**
```json
{
  "success": true,
  "message": "Key deleted successfully",
  "requestId": null,
  "data": null,
  "errorDetails": null,
  "timestamp": "2026-02-25T11:00:00",
  "executionTimeMs": null,
  "exitCode": null
}
```

**Status Code:** 200 (Success), 404 (Not Found), 500 (Error)

---

#### 5. Validate Key
**Endpoint:** `POST /keys/{keyId}/validate`

**Description:** Validates an SSH key.

**Response:**
```json
{
  "success": true,
  "message": "Key is valid",
  "requestId": null,
  "data": null,
  "errorDetails": null,
  "timestamp": "2026-02-25T11:05:00",
  "executionTimeMs": null,
  "exitCode": null
}
```

**Status Code:** 200 (Success), 500 (Error)

---

## Error Handling

### Error Response Format
```json
{
  "success": false,
  "message": "Error message",
  "requestId": "uuid-string",
  "data": null,
  "errorDetails": "Detailed error information",
  "timestamp": "2026-02-25T11:10:00",
  "executionTimeMs": null,
  "exitCode": 1
}
```

### Common HTTP Status Codes
- **200 OK** - Successful request
- **201 Created** - Resource created successfully
- **400 Bad Request** - Invalid request parameters
- **401 Unauthorized** - Authentication required
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error
- **503 Service Unavailable** - SSH connection failed

---

## Exception Types

- **SSHAuthenticationException** - Authentication with SSH key failed → 401
- **SSHConnectionException** - Failed to establish SSH connection → 503
- **KeyValidationException** - SSH key validation failed → 400

---

## Models

### SSHKeyPair
```json
{
  "id": 1234567890,
  "keyName": "production-key",
  "publicKey": "ssh-rsa AAAAB3...",
  "privateKey": "-----BEGIN RSA PRIVATE KEY-----...",
  "keyFingerprint": "ssh-rsa-fingerprint",
  "keyAlgorithm": "RSA",
  "keySize": 2048,
  "owner": "admin",
  "createdAt": "2026-02-25T10:00:00",
  "updatedAt": "2026-02-25T10:00:00",
  "isActive": true,
  "description": "Production server key"
}
```

### AuditLog
```json
{
  "id": 1,
  "userId": "admin",
  "action": "SSH_COMMAND_EXECUTE",
  "resource": "server-prod-01",
  "resourceId": "192.168.1.100",
  "status": "SUCCESS",
  "details": "Executed 'ls -la'",
  "ipAddress": "192.168.1.50",
  "timestamp": "2026-02-25T11:00:00",
  "durationMs": 500,
  "changesSummary": "No changes"
}
```

---

## Integration Services

### Attendance Integration
- Sync attendance data from remote servers
- Fetch attendance records
- Update attendance status

### Certificate Integration
- Deploy SSL certificates
- Validate certificates on remote servers
- Fetch certificate details

### Dashboard Integration
- Get server status and health
- Retrieve system metrics
- Monitor service health
- Send alerts to dashboard

---

## Usage Examples

### Example 1: Test SSH Connection
```bash
curl -X POST http://localhost:8080/api/v1/ssh/connect \
  -H "Content-Type: application/json" \
  -d '{
    "host": "192.168.1.100",
    "port": 22,
    "username": "admin",
    "keyPairId": "key-1"
  }'
```

### Example 2: Execute Remote Command
```bash
curl -X POST http://localhost:8080/api/v1/ssh/execute \
  -H "Content-Type: application/json" \
  -d '{
    "host": "192.168.1.100",
    "username": "admin",
    "command": "whoami",
    "keyPairId": "key-1"
  }'
```

### Example 3: Upload SSH Key
```bash
curl -X POST http://localhost:8080/api/v1/keys/upload \
  -H "Content-Type: application/json" \
  -d '{
    "keyName": "my-key",
    "publicKey": "ssh-rsa AAAAB3NzaC1yc2EAAAA...",
    "privateKey": "-----BEGIN RSA PRIVATE KEY-----...",
    "description": "My SSH key"
  }'
```

---

## Environment Variables

- `SPRING_DATASOURCE_URL`: JDBC URL for SQLite file (e.g. `jdbc:sqlite:./data/ssh_auth.db`)
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SERVER_PORT`: Application port (default: 8080)

---

## Running the Application

### Using Maven
```bash
mvn clean install
mvn spring-boot:run
```

### Using Docker
```bash
docker-compose up -d
```

### Access Points
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs
- PHPMyAdmin: http://localhost:8081

---

## Version History
- **v1.0.0** (2026-02-25) - Initial release with SSH connection, command execution, and key management

