# README - SSH Authentication Module

[![Maven CI](https://github.com/ManasviWaghmare/ssh_auth/actions/workflows/maven.yml/badge.svg)](https://github.com/ManasviWaghmare/ssh_auth/actions/workflows/maven.yml)

## Project Overview

The **SSH Authentication Module** is a comprehensive Spring Boot-based REST API that provides secure SSH connection management, remote command execution, and SSH key management capabilities. It integrates with various backend systems for attendance, certificates, and dashboard operations.

## Features

✅ **SSH Connection Management**
- Test SSH connections with remote servers
- Manage SSH sessions and timeouts
- Support for multiple SSH key formats

✅ **Remote Command Execution**
- Execute commands on remote servers via SSH
- Capture command output and exit codes
- Configurable execution timeouts

✅ **SSH Key Management**
- Upload and store SSH key pairs
- Encrypt private keys using AES-256
- Generate key fingerprints
- Validate key format and integrity
- Delete keys with proper cleanup

✅ **Security Features**
- Encrypted private key storage
- Spring Security integration
- CORS configuration
- Global exception handling
- Audit logging of all operations

✅ **Integration Services**
- Attendance system integration
- Certificate management integration
- Dashboard monitoring integration

✅ **Database Support**
- MySQL integration via Spring Data JPA
- Audit log storage
- SSH key pair persistence

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.2.0 | Framework |
| Spring Security | 5.x | Authentication/Authorization |
| Spring Data JPA | - | Database ORM |
| MySQL | 8.0 | Database |
| JSch | 0.1.55 | SSH Library |
| Lombok | - | Boilerplate Reduction |
| JWT | 0.12.3 | Token Authentication |
| Maven | 3.x | Build Tool |
| Docker | - | Containerization |

## Project Structure

```
ssh-authentication-module/
├── api-layer/                           # API Layer (Spring Boot)
│   └── src/main/java/com/org/sshauth/api/
│       ├── controller/
│       │   ├── SSHRestController.java
│       │   └── KeyManagementController.java
│       ├── dto/
│       │   ├── SSHConnectRequest.java
│       │   ├── CommandRequest.java
│       │   ├── KeyUploadRequest.java
│       │   └── SSHResponse.java
│       ├── config/
│       │   └── SecurityConfig.java
│       └── SshAuthApplication.java
│
├── ssh-authentication-module/           # Core Module
│   └── src/main/java/com/org/sshauth/core/
│       ├── ssh-core-library/
│       │   ├── SSHConnectionManager.java
│       │   ├── KeyAuthenticationService.java
│       │   └── CommandExecutor.java
│       ├── integration-services/
│       │   ├── AttendanceIntegrationService.java
│       │   ├── CertificateIntegrationService.java
│       │   └── DashboardIntegrationService.java
│       ├── utilities-security/
│       │   ├── EncryptionUtil.java
│       │   ├── AuditLogger.java
│       │   ├── ExceptionHandlerUtil.java
│       │   └── FileUtil.java
│       ├── model/
│       │   ├── SSHRequest.java
│       │   ├── SSHKeyPair.java
│       │   └── AuditLog.java
│       └── exception/
│           ├── SSHAuthenticationException.java
│           ├── SSHConnectionException.java
│           └── KeyValidationException.java
│
├── resources/
│   ├── application.yml                  # Spring Configuration
│   ├── logback.xml                      # Logging Configuration
│   └── keys/                            # SSH Keys Storage
│
├── docker/
│   ├── Dockerfile                       # Docker Image
│   └── docker-compose.yml               # Docker Compose Setup
│
├── docs/
│   ├── api-documentation.md             # API Documentation
│   ├── architecture-diagram.md          # Architecture Diagrams
│   └── sequence-diagram.md              # Sequence Diagrams
│
└── pom.xml                              # Maven Configuration
```

## Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker & Docker Compose (optional)

### Local Development Setup

1. **Clone the repository**
```bash
git clone <repository-url>
cd ssh-authentication-module
```

2. **Create MySQL Database**
```bash
mysql -u root -p
CREATE DATABASE ssh_auth_db;
CREATE USER 'ssh_user'@'localhost' IDENTIFIED BY 'ssh_password';
GRANT ALL PRIVILEGES ON ssh_auth_db.* TO 'ssh_user'@'localhost';
FLUSH PRIVILEGES;
```

3. **Build the project**
```bash
mvn clean install
```

4. **Run the application**
```bash
mvn spring-boot:run
```

5. **Access the application**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/v3/api-docs

### Docker Setup

1. **Build and run with Docker Compose**
```bash
docker-compose up -d
```

2. **Verify services**
```bash
docker-compose ps
```

3. **View logs**
```bash
docker-compose logs -f ssh-auth-api
```

4. **Access services**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- PHPMyAdmin: http://localhost:8081

5. **Stop services**
```bash
docker-compose down
```

## API Usage

### Example 1: Test SSH Connection
```bash
curl -X POST http://localhost:8080/api/v1/ssh/connect \
  -H "Content-Type: application/json" \
  -d '{
    "host": "192.168.1.100",
    "port": 22,
    "username": "admin",
    "keyPairId": "my-key-id"
  }'
```

### Example 2: Execute Command
```bash
curl -X POST http://localhost:8080/api/v1/ssh/execute \
  -H "Content-Type: application/json" \
  -d '{
    "host": "192.168.1.100",
    "username": "admin",
    "command": "ls -la /home",
    "keyPairId": "my-key-id"
  }'
```

### Example 3: Upload SSH Key
```bash
curl -X POST http://localhost:8080/api/v1/keys/upload \
  -H "Content-Type: application/json" \
  -d '{
    "keyName": "my-production-key",
    "publicKey": "ssh-rsa AAAAB3NzaC1yc2E...",
    "privateKey": "-----BEGIN RSA PRIVATE KEY-----\n...\n-----END RSA PRIVATE KEY-----",
    "keyAlgorithm": "RSA",
    "keySize": 2048,
    "description": "Production server key"
  }'
```

## Configuration

### application.yml
Key configurations:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ssh_auth_db
    username: ssh_user
    password: ssh_password
  jpa:
    hibernate:
      ddl-auto: update

server:
  port: 8080
```

### logback.xml
Logging configuration:
- Console and file appenders
- Rolling file policy (10MB per file, 30 days retention)
- Custom log levels per package

## Security Considerations

1. **Encryption**: Private keys are encrypted using AES-256 before storage
2. **Key Storage**: Keys should be stored in a secure vault in production
3. **Authentication**: Implement JWT/OAuth2 for API authentication
4. **HTTPS**: Use HTTPS in production environments
5. **Audit Logging**: All operations are logged for compliance

## Monitoring & Logging

- **Log Location**: `logs/ssh-auth.log`
- **Log Level**: DEBUG for SSH operations, INFO for general
- **Audit Logs**: Stored in MySQL database
- **Health Check**: http://localhost:8080/health
- **Metrics**: http://localhost:8080/metrics

## Troubleshooting

### Connection Issues
- Verify SSH credentials and key pair
- Check host availability and firewall rules
- Review application logs for detailed errors

### Database Connection Errors
- Ensure MySQL is running
- Verify database credentials in application.yml
- Check MySQL port accessibility

### Key Validation Errors
- Verify key format (RSA or ED25519)
- Ensure private key has proper headers
- Check for invalid characters or formatting

## Performance Optimization

1. **Connection Pooling**: Configured in Spring Data JPA
2. **Async Logging**: Non-blocking log writes
3. **Caching**: Implement for frequently accessed keys
4. **Load Balancing**: Use multiple instances behind a load balancer

## Contributing

1. Create a feature branch
2. Make your changes
3. Add tests for new functionality
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues, questions, or contributions, please open an issue on the repository.

## Version History

- **v1.0.0** (2026-02-25) - Initial release with SSH connections, command execution, and key management

---

**Last Updated**: February 25, 2026
**Maintained by**: Development Team

