# SSH Authentication Module - Architecture

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     Client Applications                          │
│                   (Web, Mobile, Desktop)                         │
└──────────────────────┬──────────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API Layer (Spring Boot)                       │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  REST Controllers                                           ││
│  │  ├── SSHRestController (SSH Operations)                    ││
│  │  └── KeyManagementController (Key Management)              ││
│  └─────────────────────────────────────────────────────────────┘│
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  Security & Configuration                                  ││
│  │  ├── SecurityConfig (Spring Security)                      ││
│  │  ├── Exception Handlers (Global Error Handling)            ││
│  │  └── CORS Configuration                                    ││
│  └─────────────────────────────────────────────────────────────┘│
└──────────────────────┬──────────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────────┐
│              Core Authentication Module                         │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  SSH Core Library                                           ││
│  │  ├── SSHConnectionManager (Connection Handling)            ││
│  │  ├── KeyAuthenticationService (Key Management)             ││
│  │  └── CommandExecutor (Remote Command Execution)            ││
│  └─────────────────────────────────────────────────────────────┘│
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  Integration Services                                       ││
│  │  ├── AttendanceIntegrationService                          ││
│  │  ├── CertificateIntegrationService                         ││
│  │  └── DashboardIntegrationService                           ││
│  └─────────────────────────────────────────────────────────────┘│
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  Utilities & Security                                       ││
│  │  ├── EncryptionUtil (AES Encryption)                       ││
│  │  ├── AuditLogger (Audit Trail)                             ││
│  │  ├── FileUtil (File Operations)                            ││
│  │  └── ExceptionHandlerUtil (Error Handling)                 ││
│  └─────────────────────────────────────────────────────────────┘│
└──────────────────────┬──────────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                   Data Layer                                     │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │  SQLite Database (Spring Data JPA)                        ││
│  │  ├── SSH Keys Storage                                      ││
│  │  ├── SSH Requests Log                                      ││
│  │  ├── Audit Logs                                            ││
│  │  └── User Credentials                                      ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                 Remote SSH Servers                              │
│  ├── Production Servers                                         │
│  ├── Development Servers                                        │
│  ├── Database Servers                                           │
│  └── Infrastructure Servers                                     │
└─────────────────────────────────────────────────────────────────┘
```

## Component Details

### API Layer
- **REST Controllers**: Handle HTTP requests and responses
- **DTOs**: Data transfer objects for request/response serialization
- **Security Config**: Spring Security configuration for authentication/authorization

### Core Module
- **SSH Core Library**: JSch library for SSH connections and command execution
- **Services**: Business logic for SSH operations, key management, and integrations
- **Models**: Entity classes for SSH operations and audit logging
- **Utilities**: Encryption, file handling, audit logging, and error handling

### Data Layer
- **SQLite Database**: File-based storage for keys, logs, and configurations
- **JPA Repositories**: Data access objects for database operations

## Data Flow

### SSH Connection Flow
```
Client Request
    ↓
SSHRestController
    ↓
SSHConnectionManager.testConnection()
    ↓
JSch Session Creation
    ↓
SSH Key Authentication
    ↓
Connection Test
    ↓
Response (Success/Failure)
```

### Command Execution Flow
```
Client Request (CommandRequest)
    ↓
SSHRestController.executeCommand()
    ↓
CommandExecutor.executeCommand()
    ↓
SSHConnectionManager.createSession()
    ↓
JSch Channel (exec)
    ↓
Remote Command Execution
    ↓
Output Collection
    ↓
Response with Command Output
```

### Key Management Flow
```
Client Request (KeyUploadRequest)
    ↓
KeyManagementController.uploadKey()
    ↓
KeyAuthenticationService.uploadKey()
    ↓
EncryptionUtil.encrypt() (Private Key)
    ↓
Key Validation
    ↓
Fingerprint Generation
    ↓
Database Storage
    ↓
Response with Key ID
```

## Security Considerations

1. **Encryption**: Private keys are encrypted using AES-256 before storage
2. **CORS**: Configured to allow requests from specified origins
3. **Spring Security**: Can be extended with JWT/OAuth2 authentication
4. **Audit Logging**: All operations are logged with user info and timestamps
5. **Error Handling**: Sensitive error details are not exposed to clients

## Deployment Architecture

### Docker Deployment
```
Docker Host
├── SQLite file stored on host
│   └── ssh_auth_db
├── SSH Auth API Container (Port 8080)
│   └── Application
└── PHPMyAdmin Container (Port 8081)
    └── Database Management
```

### Network
- `ssh-auth-network`: Internal bridge network connecting all containers
- SQLite data file accessed locally by the application
- API and PHPMyAdmin can access MySQL

## Performance Considerations

1. **Connection Pooling**: Not required for SQLite; connections are lightweight
2. **Async Logging**: Asynchronous appender for non-blocking log writes
3. **Session Reuse**: SSH session management for frequently accessed servers
4. **Caching**: Consider caching frequently accessed SSH keys

## Scalability

1. **Horizontal Scaling**: Multiple API instances behind a load balancer
2. **Database Replication**: Engage file-based backup strategies for SQLite
3. **Connection Load Balancing**: Distribute SSH connections across servers
4. **Microservices**: Future splitting of integration services into separate services

