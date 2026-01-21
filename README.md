# Multi-Module Microservices Application

A professional-grade Spring Boot microservices architecture demonstrating best practices for building cloud-native applications.

## 🏗️ Architecture Overview

```
                                    ┌─────────────────┐
                                    │   API Gateway   │
                                    │   (Port 8080)   │
                                    └────────┬────────┘
                                             │
                    ┌────────────────────────┼────────────────────────┐
                    │                        │                        │
           ┌────────▼────────┐      ┌────────▼────────┐      ┌────────▼────────┐
           │  User Service   │      │  Order Service  │      │  Other Services │
           │   (Port 8081)   │◄────►│   (Port 8082)   │      │      (...)      │
           └────────┬────────┘      └────────┬───��────┘      └─────────────────┘
                    │                        │
                    └────────────┬───────────┘
                                 │
                    ┌────────────▼────────────┐
                    │    Discovery Server     │
                    │      (Port 8761)        │
                    └────────────┬────────────┘
                                 │
                    ┌────────────▼────────────┐
                    │     Config Server       │
                    │      (Port 8888)        │
                    └───────────��─────────────┘
```

## 🚀 Features

### Core Features
- **Service Discovery** - Netflix Eureka for service registration and discovery
- **Centralized Configuration** - Spring Cloud Config Server with native profile support
- **API Gateway** - Spring Cloud Gateway with routing, load balancing, and circuit breaker
- **Inter-Service Communication** - OpenFeign with circuit breaker (Resilience4j)

### Professional Features
- **Global Exception Handling** - Consistent error responses across all services
- **API Documentation** - OpenAPI 3.0 (Swagger UI) for all services
- **Health Checks** - Spring Boot Actuator with Prometheus metrics
- **Circuit Breaker Pattern** - Resilience4j for fault tolerance
- **Request Correlation** - Correlation ID tracking across services
- **Pagination & Sorting** - Standardized paginated responses
- **Validation** - Bean validation with detailed error messages
- **MapStruct** - Type-safe object mapping
- **Docker Support** - Multi-service Docker Compose setup with health checks

## 📦 Modules

| Module | Description | Port |
|--------|-------------|------|
| `discovery-server` | Eureka Service Discovery | 8761 |
| `config-server` | Centralized Configuration | 8888 |
| `api-gateway` | API Gateway & Routing | 8080 |
| `common-lib` | Shared DTOs, Exceptions, Utilities | - |
| `user-service` | User Management Microservice | 8081 |
| `order-service` | Order Management Microservice | 8082 |

## 🛠️ Technology Stack

- **Java 21** - Latest LTS version
- **Spring Boot 3.2.2** - Latest stable version
- **Spring Cloud 2023.0.0** - Cloud-native features
- **Spring Security** - Authentication & Authorization
- **JWT (JJWT 0.12.3)** - Token-based authentication
- **Maven** - Build tool
- **H2 Database** - In-memory database (demo)
- **Lombok** - Boilerplate reduction
- **MapStruct** - Object mapping
- **SpringDoc OpenAPI** - API documentation
- **Resilience4j** - Circuit breaker
- **Docker & Docker Compose** - Containerization

## 🔐 Security & Authentication

### Roles & Permissions

| Role | Description | Permissions |
|------|-------------|-------------|
| `ROLE_ADMIN` | Full system access | All operations including delete |
| `ROLE_MANAGER` | Management access | Create/Update users, manage orders |
| `ROLE_USER` | Basic user access | View own data, create orders |

### Protected Endpoints

#### User Service
| Endpoint | ADMIN | MANAGER | USER |
|----------|-------|---------|------|
| `GET /users` | ✅ | ✅ | ❌ |
| `GET /users/{id}` | ✅ | ✅ | ✅ |
| `POST /users` | ✅ | ✅ | ❌ |
| `PUT /users/{id}` | ✅ | ✅ | ❌ |
| `DELETE /users/{id}` | ✅ | ❌ | ❌ |

#### Order Service
| Endpoint | ADMIN | MANAGER | USER |
|----------|-------|---------|------|
| `GET /orders` | ✅ | ✅ | ✅ |
| `POST /orders` | ✅ | ✅ | ✅ |
| `PUT /orders/{id}` | ✅ | ✅ | ✅ |
| `PATCH /orders/{id}/status` | ✅ | ✅ | ❌ |
| `DELETE /orders/{id}` | ✅ | ❌ | ❌ |
| `GET /orders/statistics` | ✅ | ✅ | ❌ |

### Test Users

| Username | Password | Roles |
|----------|----------|-------|
| `admin` | `password123` | ADMIN, MANAGER, USER |
| `manager` | `password123` | MANAGER, USER |
| `user` | `password123` | USER |

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
   mvn clean install
   ```

3. **Start services in order**
   ```bash
   # Terminal 1 - Discovery Server
   cd discovery-server && mvn spring-boot:run

   # Terminal 2 - Config Server (after discovery is up)
   cd config-server && mvn spring-boot:run

   # Terminal 3 - Auth Service
   cd auth-service && mvn spring-boot:run

   # Terminal 4 - API Gateway
   cd api-gateway && mvn spring-boot:run

   # Terminal 5 - User Service
   cd user-service && mvn spring-boot:run

   # Terminal 6 - Order Service
   cd order-service && mvn spring-boot:run
   ```

### Running with Docker Compose

```bash
# Build all modules first
mvn clean package -DskipTests

# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down
```

## 📖 API Documentation

Once services are running, access Swagger UI:

- **Auth Service**: http://localhost:8090/swagger-ui.html
- **User Service**: http://localhost:8081/swagger-ui.html
- **Order Service**: http://localhost:8082/swagger-ui.html
- **API Gateway (Aggregated)**: http://localhost:8080/swagger-ui.html

## 🔗 Service Endpoints

### Eureka Dashboard
- http://localhost:8761

### Auth Service API (Public - No Token Required)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and get JWT tokens |
| POST | `/auth/refresh` | Refresh access token |
| POST | `/auth/logout` | Logout and invalidate tokens |
| GET | `/auth/validate` | Validate JWT token |

### User Service API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/users` | Get all users (paginated) |
| GET | `/users/{id}` | Get user by ID |
| GET | `/users/email/{email}` | Get user by email |
| GET | `/users/search?q=&status=` | Search users |
| POST | `/users` | Create new user |
| PUT | `/users/{id}` | Update user |
| DELETE | `/users/{id}` | Delete user |

### Order Service API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/orders` | Get all orders (paginated) |
| GET | `/orders/{id}` | Get order by ID |
| GET | `/orders/user/{userId}` | Get orders by user |
| GET | `/orders/search?q=&status=&userId=` | Search orders |
| POST | `/orders` | Create new order |
| PUT | `/orders/{id}` | Update order |
| PATCH | `/orders/{id}/status?status=` | Update order status |
| POST | `/orders/{id}/cancel` | Cancel order |
| DELETE | `/orders/{id}` | Delete order |
| GET | `/orders/statistics` | Get order statistics |

### Via API Gateway
All endpoints are accessible through the gateway with `/api` prefix:
- Auth Service: `http://localhost:8080/api/auth/**` (No token required)
- User Service: `http://localhost:8080/api/users/**` (Token required)
- Order Service: `http://localhost:8080/api/orders/**` (Token required)

## 📊 Health & Metrics

### Actuator Endpoints
- Health: `http://localhost:{port}/actuator/health`
- Info: `http://localhost:{port}/actuator/info`
- Metrics: `http://localhost:{port}/actuator/metrics`
- Prometheus: `http://localhost:{port}/actuator/prometheus`

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

Response:
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
      "roles": ["ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"]
    }
  }
}
```

### 2. Create User (with Token)
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <access_token>" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phone": "+1-555-123-4567",
    "address": "123 Main St, City"
  }'
```

### 3. Create Order (with Token)
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

### 4. Refresh Token
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<refresh_token>"
  }'
```

## 🔧 Configuration

### Environment Variables
| Variable | Description | Default |
|----------|-------------|---------|
| `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` | Eureka server URL | `http://localhost:8761/eureka/` |
| `SPRING_CLOUD_CONFIG_URI` | Config server URL | `http://localhost:8888` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `default` |

## 📁 Project Structure

```
multi-module-app/
├── pom.xml                    # Parent POM
├── docker-compose.yml         # Docker orchestration
├── README.md
├── common-lib/                # Shared library
���   └── src/main/java/com/actora/common/
│       ├── dto/               # Common DTOs
│       ├── exception/         # Global exceptions
│       ├── constants/         # Constants
│       └── util/              # Utilities
├── discovery-server/          # Eureka Server
├── config-server/             # Config Server
│   └── src/main/resources/configs/  # Service configs
├── api-gateway/               # API Gateway
│   └── src/main/java/.../
│       ├── config/            # Gateway config
│       └── controller/        # Fallback controllers
├── user-service/              # User microservice
│   └── src/main/java/.../
│       ├── controller/
│       ├── service/
│       ├── repository/
│       ├── entity/
│       ├── dto/
│       └── mapper/
└── order-service/             # Order microservice
    └── src/main/java/.../
        ├── controller/
        ├── service/
        ├── repository/
        ├── entity/
        ├── dto/
        ├── mapper/
        └── client/            # Feign clients
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📄 License

This project is licensed under the Apache 2.0 License.

## 👥 Authors

- Actora Team - Initial work

---

⭐ Star this repository if you find it helpful!
