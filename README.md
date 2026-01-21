# Multi-Module Microservices Application

A professional-grade Spring Boot microservices architecture demonstrating best practices for building cloud-native applications with **JWT-based authentication**, **role-based authorization**, and **clean architecture** principles.

## 🏗️ Architecture Overview

```
                                    ┌─────────────────────┐
                                    │     API Gateway     │
                                    │     (Port 8080)     │
                                    │   JWT Validation    │
                                    └──────────┬──────────┘
                                               │
       ┌───────────────┬───────────────────────┼───────────────────────┬───────────────┐
       │               │                       │                       │               │
┌──────▼──────┐ ┌──────▼──────┐ ┌──────────────▼──────────────┐ ┌──────▼──────┐ ┌──────▼──────┐
│Auth Service │ │User Service │ │       Order Service         │ │Config Server│ │  Discovery  │
│ (Port 8090) │ │ (Port 8081) │ │       (Port 8082)           │ │ (Port 8888) │ │ (Port 8761) │
│  JWT Issue  │ │   Secured   │ │         Secured             │ └─────────────┘ └─────���───────┘
└─────────────┘ └──────┬──────┘ └──────────────┬──────────────┘
                       │                       │
                       │    ┌─────────────┐    │
                       └────►  common-lib  ◄───┘
                            │  (Shared)   │
                            └─────────────┘
```

## 🚀 Features

### Core Features
- **Service Discovery** - Netflix Eureka for service registration and discovery
- **Centralized Configuration** - Spring Cloud Config Server with native profile support
- **API Gateway** - Spring Cloud Gateway with routing, load balancing, and JWT authentication
- **Inter-Service Communication** - OpenFeign with circuit breaker (Resilience4j)

### Security Features
- **JWT Authentication** - Stateless token-based authentication
- **Role-Based Access Control (RBAC)** - ADMIN, MANAGER, USER roles
- **Gateway-Level Security** - JWT validation at API Gateway
- **Method-Level Security** - @PreAuthorize annotations for fine-grained access control
- **Secure Password Storage** - BCrypt password encoding
- **Refresh Token Support** - Token refresh without re-authentication
- **Shared Security Components** - Centralized in common-lib (DRY principle)

### Professional Features
- **Clean Architecture** - Shared code in common-lib, no duplication
- **Global Exception Handling** - Consistent error responses across all services
- **API Documentation** - OpenAPI 3.0 (Swagger UI) for all services
- **Health Checks** - Spring Boot Actuator with Prometheus metrics
- **Circuit Breaker Pattern** - Resilience4j for fault tolerance
- **Pagination & Sorting** - Standardized paginated responses
- **Validation** - Bean validation with detailed error messages
- **MapStruct** - Type-safe object mapping
- **Docker Support** - Multi-service Docker Compose setup with health checks

## 📦 Modules

| Module | Description | Port | Dependencies |
|--------|-------------|------|--------------|
| `discovery-server` | Eureka Service Discovery | 8761 | - |
| `config-server` | Centralized Configuration | 8888 | discovery-server |
| `api-gateway` | API Gateway, JWT Validation & Routing | 8080 | discovery-server, config-server |
| `auth-service` | Authentication & Authorization (JWT) | 8090 | common-lib, user-service |
| `common-lib` | Shared DTOs, Exceptions, Security, Utilities | - | - |
| `user-service` | User Management Microservice | 8081 | common-lib |
| `order-service` | Order Management Microservice | 8082 | common-lib, user-service |

## 🛠️ Technology Stack

| Category | Technology | Version |
|----------|------------|---------|
| **Language** | Java | 21 (LTS) |
| **Framework** | Spring Boot | 3.2.2 |
| **Cloud** | Spring Cloud | 2023.0.0 |
| **Security** | Spring Security | 6.x |
| **JWT** | JJWT | 0.12.3 |
| **Database** | PostgreSQL | 16 |
| **Database (Dev)** | H2 (In-memory) | - |
| **ORM** | Spring Data JPA | - |
| **Build** | Maven | 3.9+ |
| **Mapping** | MapStruct | 1.5.5 |
| **Documentation** | SpringDoc OpenAPI | 2.3.0 |
| **Resilience** | Resilience4j | - |
| **Containerization** | Docker & Docker Compose | - |

## 🔐 Security & Authentication

### Architecture: Separation of Concerns

```
┌���────────────────────┐          ┌─────────────────────┐
│    auth-service     │          │   user-service      │
├─────────────────────┤  Feign   ├─────────────────────┤
│ auth_users table:   │ ──────►  │ users table:        │
│ - id                │          │ - id ◄──────────────┤
│ - user_id ──────────┼──────────┼──(FK reference)     │
│ - username          │          │ - first_name        │
│ - email             │          │ - last_name         │
│ - password (hashed) │          │ - email             │
│ - roles             │          │ - phone             │
└─────────────────────┘          │ - address           │
         ↓                       │ - status            │
  Auth concerns ONLY             └─────────────────────┘
  - Credentials                          ↓
  - Roles/Permissions           Profile data ONLY
  - JWT Tokens                  - Personal info
                                - Contact details
```

### Roles & Permissions

| Role | Description | Permissions |
|------|-------------|-------------|
| `ROLE_ADMIN` | Full system access | All operations including delete |
| `ROLE_MANAGER` | Management access | Create/Update users, manage orders |
| `ROLE_USER` | Basic user access | View own data, create orders |

### Protected Endpoints

#### User Service
| Endpoint | Method | ADMIN | MANAGER | USER | Description |
|----------|--------|:-----:|:-------:|:----:|-------------|
| `/users` | GET | ✅ | ✅ | ❌ | Get all users |
| `/users/{id}` | GET | ✅ | ✅ | ✅ | Get user by ID |
| `/users/email/{email}` | GET | ✅ | ✅ | ❌ | Get user by email |
| `/users/search` | GET | ✅ | ✅ | ❌ | Search users |
| `/users/status/{status}` | GET | ✅ | ✅ | ❌ | Get users by status |
| `/users` | POST | ✅ | ✅ | ❌ | Create user |
| `/users/{id}` | PUT | ✅ | ✅ | ❌ | Update user |
| `/users/{id}` | DELETE | ✅ | ❌ | ❌ | Delete user |
| `/users/health` | GET | 🔓 | 🔓 | 🔓 | Health check (public) |
| `/users/exists/{id}` | GET | 🔓 | 🔓 | 🔓 | Check exists (public) |

#### Order Service
| Endpoint | Method | ADMIN | MANAGER | USER | Description |
|----------|--------|:-----:|:-------:|:----:|-------------|
| `/orders` | GET | ✅ | ✅ | ✅ | Get all orders |
| `/orders/{id}` | GET | ✅ | ✅ | ✅ | Get order by ID |
| `/orders/user/{userId}` | GET | ✅ | ✅ | ✅ | Get orders by user |
| `/orders/search` | GET | ✅ | ✅ | ✅ | Search orders |
| `/orders` | POST | ✅ | ✅ | ✅ | Create order |
| `/orders/{id}` | PUT | ✅ | ✅ | ✅ | Update order |
| `/orders/{id}/status` | PATCH | ✅ | ✅ | ❌ | Update status |
| `/orders/{id}/cancel` | POST | ✅ | ✅ | ✅ | Cancel order |
| `/orders/{id}` | DELETE | ✅ | ❌ | ❌ | Delete order |
| `/orders/statistics` | GET | ✅ | ✅ | ❌ | Get statistics |
| `/orders/health` | GET | 🔓 | 🔓 | 🔓 | Health check (public) |

✅ = Allowed | ❌ = Denied | 🔓 = Public (no auth required)

### Test Users (Pre-configured)

| Username | Password | Roles | User ID |
|----------|----------|-------|---------|
| `admin` | `password123` | ADMIN, MANAGER, USER | 1 |
| `manager` | `password123` | MANAGER, USER | 2 |
| `user` | `password123` | USER | 3 |

## 🚦 Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker & Docker Compose (optional)

### Running Locally

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd multi-module-app
   ```

2. **Build all modules**
   ```bash
   mvn clean install -DskipTests
   ```

3. **Start services in order** (each in a separate terminal)
   ```bash
   # 1. Discovery Server (wait until started)
   cd discovery-server && mvn spring-boot:run

   # 2. Config Server
   cd config-server && mvn spring-boot:run

   # 3. Auth Service
   cd auth-service && mvn spring-boot:run

   # 4. User Service
   cd user-service && mvn spring-boot:run

   # 5. Order Service
   cd order-service && mvn spring-boot:run

   # 6. API Gateway
   cd api-gateway && mvn spring-boot:run
   ```

4. **Verify services**
   - Eureka Dashboard: http://localhost:8761
   - All services should be registered

### Running with Docker Compose

```bash
# Build all modules first
mvn clean package -DskipTests

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Check status
docker-compose ps

# Stop all services
docker-compose down
```

## 📖 API Documentation

### Swagger UI Access
| Service | URL |
|---------|-----|
| Auth Service | http://localhost:8090/swagger-ui.html |
| User Service | http://localhost:8081/swagger-ui.html |
| Order Service | http://localhost:8082/swagger-ui.html |
| API Gateway (Aggregated) | http://localhost:8080/swagger-ui.html |

### Service URLs

| Service | Direct URL | Via Gateway |
|---------|------------|-------------|
| Auth | http://localhost:8090/auth/** | http://localhost:8080/api/auth/** |
| Users | http://localhost:8081/users/** | http://localhost:8080/api/users/** |
| Orders | http://localhost:8082/orders/** | http://localhost:8080/api/orders/** |

## 🔗 API Endpoints

### Auth Service (Public - No Token Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and get JWT tokens |
| POST | `/auth/refresh` | Refresh access token |
| POST | `/auth/logout` | Logout and invalidate tokens |
| GET | `/auth/validate` | Validate JWT token |
| GET | `/auth/health` | Health check |

### User Service (Token Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users` | Get all users (paginated) |
| GET | `/users/{id}` | Get user by ID |
| GET | `/users/email/{email}` | Get user by email |
| GET | `/users/search?q=&status=` | Search users |
| GET | `/users/status/{status}` | Get users by status |
| POST | `/users` | Create new user |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user (Admin only) |

### Order Service (Token Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/orders` | Get all orders (paginated) |
| GET | `/orders/{id}` | Get order by ID |
| GET | `/orders/number/{orderNumber}` | Get order by number |
| GET | `/orders/user/{userId}` | Get orders by user |
| GET | `/orders/search?q=&status=&userId=` | Search orders |
| GET | `/orders/status/{status}` | Get orders by status |
| GET | `/orders/statistics` | Get order statistics |
| POST | `/orders` | Create new order |
| PUT | `/orders/{id}` | Update order |
| PATCH | `/orders/{id}/status?status=` | Update order status |
| POST | `/orders/{id}/cancel` | Cancel order |
| DELETE | `/orders/{id}` | Delete order (Admin only) |

## 🧪 Sample Requests

### 1. Login and Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@actora.com",
      "fullName": "Admin User",
      "roles": ["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"]
    }
  },
  "message": "Login successful"
}
```

### 2. Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "password": "securepass123",
    "firstName": "New",
    "lastName": "User",
    "phone": "+1-555-000-0000",
    "address": "123 New Street"
  }'
```

### 3. Get All Users (with Token)
```bash
curl -X GET "http://localhost:8080/api/users?page=0&size=10" \
  -H "Authorization: Bearer <access_token>"
```

### 4. Create Order (with Token)
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <access_token>" \
  -d '{
    "userId": 1,
    "description": "Test order",
    "shippingAddress": "123 Main St",
    "items": [
      {
        "productName": "Widget",
        "productCode": "WGT-001",
        "quantity": 2,
        "unitPrice": 29.99
      }
    ]
  }'
```

### 5. Refresh Token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<refresh_token>"
  }'
```

### 6. Update Order Status (Manager/Admin only)
```bash
curl -X PATCH "http://localhost:8080/api/orders/1/status?status=CONFIRMED" \
  -H "Authorization: Bearer <access_token>"
```

## 📊 Health & Metrics

### Actuator Endpoints
| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health status |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Application metrics |
| `/actuator/prometheus` | Prometheus format metrics |

### Example Health Check
```bash
curl http://localhost:8081/actuator/health
```

## 🔧 Configuration

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Eureka server URL | `http://localhost:8761/eureka/` |
| `SPRING_CLOUD_CONFIG_URI` | Config server URL | `http://localhost:8888` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `default` |
| `SPRING_DATASOURCE_URL` | Database JDBC URL | (H2 in-memory) |
| `SPRING_DATASOURCE_USERNAME` | Database username | `sa` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | (empty) |
| `JWT_SECRET` | JWT signing secret | (configured in properties) |
| `JWT_ACCESS_TOKEN_EXPIRATION` | Access token expiry (ms) | `3600000` (1 hour) |
| `JWT_REFRESH_TOKEN_EXPIRATION` | Refresh token expiry (ms) | `86400000` (24 hours) |

### Profiles
| Profile | Description | Database |
|---------|-------------|----------|
| `default` | Local development | H2 In-memory |
| `docker` | Docker deployment | PostgreSQL |

### JWT Configuration (auth-service)
```properties
jwt.secret=ActoraSecretKeyForJWTAuthenticationMustBeAtLeast256BitsLongForHS256Algorithm2024
jwt.access-token-expiration=3600000
jwt.refresh-token-expiration=86400000
```

## 📁 Project Structure

```
multi-module-app/
├── pom.xml                         # Parent POM with dependency management
├── docker-compose.yml              # Docker orchestration
├── README.md                       # This file
│
├── docker/                         # 🐳 DOCKER CONFIGURATION
│   └── postgres/
│       └── init-multi-db.sh        # Multi-database initialization script
│
├── common-lib/                     # 🔧 SHARED LIBRARY
│   └── src/main/java/com/actora/common/
│       ├── dto/                    # ApiResponse, PageResponse, ErrorDetails
│       ├── exception/              # Global exceptions & handler
│       ├── security/               # 🔐 SHARED SECURITY COMPONENTS
│       │   ├── UserPrincipal.java           # Auth principal
│       │   ├── GatewayAuthenticationFilter  # JWT filter for services
│       │   ├── SecurityConstants.java       # Header constants
│       │   └── SecurityUtils.java           # Security utilities
│       ├── constants/              # Application constants
│       └── util/                   # Utility classes
│
├── discovery-server/               # Eureka Server
│   └── src/main/java/.../
│       └── DiscoveryServerApplication.java
│
├── config-server/                  # Config Server
│   └── src/main/resources/configs/ # Service configurations
│
├── api-gateway/                    # 🚪 API GATEWAY
│   └── src/main/java/.../
│       ├── filter/
│       │   └── AuthenticationFilter.java  # JWT validation (WebFlux)
│       └── ApiGatewayApplication.java
│
├── auth-service/                   # 🔐 AUTHENTICATION SERVICE
│   └── src/main/java/.../
│       ├── controller/AuthController.java
│       ├── service/AuthService.java
│       ├── security/
│       │   ├── JwtService.java            # JWT generation/validation
│       │   └── CustomUserDetailsService.java
│       ├── entity/
│       │   ├── AuthUser.java              # Auth credentials only
│       │   ├── Role.java
│       │   └── RefreshToken.java
│       ├── repository/
│       ├── dto/
│       └── client/UserServiceClient.java  # Feign client
│
├── user-service/                   # 👤 USER SERVICE
│   └── src/main/java/.../
│       ├── controller/UserController.java # @PreAuthorize secured
│       ├── service/
│       ├── repository/
│       ├── entity/User.java               # User profile data
│       ├── dto/
│       ├── mapper/
│       └── config/SecurityConfig.java     # Uses common-lib
│
└── order-service/                  # 📦 ORDER SERVICE
    └── src/main/java/.../
        ├── controller/OrderController.java # @PreAuthorize secured
        ├── service/
        ├── repository/
        ├── entity/
        ├── dto/
        ├── mapper/
        ├── client/UserClient.java         # Feign client
        └── config/SecurityConfig.java     # Uses common-lib
```

## 🏛️ Architecture Principles

### 1. DRY (Don't Repeat Yourself)
- Shared security components in `common-lib`
- Common DTOs and exceptions centralized
- No duplicate code across services

### 2. Separation of Concerns
- `auth-service`: Only authentication/authorization (credentials, tokens, roles)
- `user-service`: Only user profile data (name, contact, address)
- Each service has a single responsibility

### 3. Loose Coupling
- Services communicate via Feign clients
- Circuit breaker pattern for resilience
- Gateway handles cross-cutting concerns

### 4. Security by Design
- JWT validation at gateway level
- Role-based access control
- Method-level security with @PreAuthorize

## 🐳 Docker Services

| Container | Image | Port | Health Check |
|-----------|-------|------|--------------|
| postgres | postgres:16-alpine | 5432 | pg_isready |
| discovery-server | Custom | 8761 | /actuator/health |
| config-server | Custom | 8888 | /actuator/health |
| auth-service | Custom | 8090 | /actuator/health |
| api-gateway | Custom | 8080 | /actuator/health |
| user-service | Custom | 8081 | /actuator/health |
| order-service | Custom | 8082 | /actuator/health |

### Database Configuration

| Service | Database | Credentials |
|---------|----------|-------------|
| auth-service | authdb | actora / actora123 |
| user-service | userdb | actora / actora123 |
| order-service | orderdb | actora / actora123 |

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache 2.0 License.

## 👥 Authors

- Actora Team - Initial work

---

⭐ **Star this repository if you find it helpful!**

## 📞 Support

For questions or issues, please open a GitHub issue.
