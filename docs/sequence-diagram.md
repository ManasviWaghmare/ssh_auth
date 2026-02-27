# SSH Authentication Module - Sequence Diagrams

## 1. SSH Connection Test Sequence Diagram

```
Client                  API Layer           SSH Manager         Remote Server
  │                        │                    │                    │
  │  POST /ssh/connect      │                    │                    │
  ├─────────────────────────>                    │                    │
  │                        │                    │                    │
  │                    Validate Request        │                    │
  │                        │                    │                    │
  │                        │  createSession()   │                    │
  │                        ├───────────────────>│                    │
  │                        │                    │                    │
  │                        │             authenticateWithKeyPair()  │
  │                        │                    │                    │
  │                        │                    │  SSH Connection   │
  │                        │                    ├───────────────────>│
  │                        │                    │  (Port 22)        │
  │                        │                    │<───────────────────┤
  │                        │              Connected               │
  │                        │                    │                    │
  │                        │              closeSession()            │
  │                        │                    │                    │
  │                        │  Return Success    │                    │
  │                        │<───────────────────┤                    │
  │  200 OK Response       │                    │                    │
  │<─────────────────────────                    │                    │
  │  {success: true}        │                    │                    │
```

## 2. Command Execution Sequence Diagram

```
Client                  API Layer           SSH Manager          Remote Server
  │                        │                    │                    │
  │  POST /ssh/execute      │                    │                    │
  ├─────────────────────────>                    │                    │
  │                        │                    │                    │
  │                    Validate Request        │                    │
  │                        │                    │                    │
  │                        │  createSession()   │                    │
  │                        ├───────────────────>│                    │
  │                        │                    │                    │
  │                        │  authenticate()    │                    │
  │                        │<───────────────────┤                    │
  │                        │                    │                    │
  │                        │             executeCommand()            │
  │                        │                    │                    │
  │                        │                    │  Open exec Channel │
  │                        │                    ├───────────────────>│
  │                        │                    │                    │
  │                        │                    │  Execute: ls -la   │
  │                        │                    ├───────────────────>│
  │                        │                    │                    │
  │                        │            Read Output Stream           │
  │                        │                    │<───────────────────┤
  │                        │                    │                    │
  │                        │              closeSession()            │
  │                        │                    │                    │
  │                        │  Return Output     │                    │
  │                        │<───────────────────┤                    │
  │  200 OK Response       │                    │                    │
  │<─────────────────────────                    │                    │
  │  {data: "...output..."} │                    │                    │
```

## 3. SSH Key Upload & Validation Sequence Diagram

```
Client                Key Manager          Encryption             Database
  │                        │                    │                    │
  │  POST /keys/upload      │                    │                    │
  ├─────────────────────────>                    │                    │
  │                        │                    │                    │
  │                   validateKeyFormat()       │                    │
  │                        │                    │                    │
  │                        │  encrypt(privateKey)                    │
  │                        ├───────────────────>│                    │
  │                        │                    │                    │
  │                        │ Base64 Encoded     │                    │
  │                        │<───────────────────┤                    │
  │                        │                    │                    │
  │                   generateFingerprint()     │                    │
  │                        │                    │                    │
  │                        │              saveToDatabase()          │
  │                        ├─────────────────────────────────────────>│
  │                        │                    │  SSHKeyPair       │
  │                        │                    │  (Encrypted)      │
  │                        │                    │<─────────────────────
  │                        │                    │                    │
  │  201 Created Response   │                    │                    │
  │<─────────────────────────                    │                    │
  │  {data: "key-id"}       │                    │                    │
```

## 4. Key Validation Sequence Diagram

```
Client              Key Manager           Encryption         Database
  │                      │                    │                 │
  │  POST /keys/{id}/validate               │                 │
  ├──────────────────────>                    │                 │
  │                      │                    │                 │
  │                      │         getKeyById()               │
  │                      ├─────────────────────────────────────>│
  │                      │                    │  Encrypted    │
  │                      │<──────────────────────────────────────
  │                      │                    │                 │
  │                   validateKeyFormat()     │                 │
  │                      │                    │                 │
  │                   checkPublicKey()        │                 │
  │                      │                    │                 │
  │                   checkPrivateKey()       │                 │
  │                      │                    │                 │
  │                   validateStructure()     │                 │
  │                      │                    │                 │
  │  200 OK Response      │                    │                 │
  │<──────────────────────                    │                 │
  │  {success: true}      │                    │                 │
```

## 5. Error Handling Sequence Diagram

```
Client              API Controller          Exception Handler    Client
  │                      │                        │                 │
  │  POST /ssh/connect    │                        │                 │
  ├─────────────────────>  │                        │                 │
  │                      │                        │                 │
  │                  Throw Exception               │                 │
  │                      │──────────────────────>  │                 │
  │                      │                    Map Exception         │
  │                      │                        │                 │
  │                      │              Build SSHResponse          │
  │                      │                        │                 │
  │  500 Error Response   │                        │                 │
  │<─────────────────────────────────────────────────────          │
  │  {success: false,     │                        │                 │
  │   errorDetails: "..."} │                        │                 │
```

## 6. Audit Logging Sequence Diagram

```
Client          API Controller        Audit Logger           Database
  │                   │                    │                   │
  │  Request          │                    │                   │
  ├────────────────────>                   │                   │
  │                   │                    │                   │
  │              Process Request           │                   │
  │                   │                    │                   │
  │                   │  logAuditEvent()   │                   │
  │                   ├───────────────────>│                   │
  │                   │                    │                   │
  │                   │              Log Details               │
  │                   │                    ├──────────────────>│
  │                   │                    │  AuditLog Entry  │
  │                   │                    │<──────────────────
  │                   │                    │                   │
  │              Response                  │                   │
  │                   │                    │                   │
  │  200 OK           │                    │                   │
  │<──────────────────────────────────────────────────────────│
```

## 7. Integration Service - Attendance Sync Sequence

```
Client              Attendance Service     SSH Manager      Remote Server
  │                        │                    │                 │
  │  Request Sync          │                    │                 │
  ├────────────────────────>                    │                 │
  │                        │                    │                 │
  │                   Build Sync Command        │                 │
  │                        │                    │                 │
  │                        │  executeCommand()  │                 │
  │                        ├───────────────────>│                 │
  │                        │                    │                 │
  │                        │              Execute Sync Script      │
  │                        │                    ├────────────────>│
  │                        │                    │                 │
  │                        │              Response (Data)         │
  │                        │                    │<────────────────┤
  │                        │         Return Output                │
  │                        │<───────────────────┤                 │
  │              Process Attendance Data       │                 │
  │                        │                    │                 │
  │  200 OK                │                    │                 │
  │<──────────────────────────────────────────────────────────────│
```

