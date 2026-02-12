# RevNext: The Master Catalog Manager

RevNext is a powerful system designed for end-to-end management of product configuration, pricing, and quotation (CPQ). This document serves as the primary technical and functional reference for the system.

---

## 🏛️ System Architecture

RevNext is built on a modern, layered Spring Boot architecture designed for scalability and maintainability.

### The 5-Layer Pattern
1.  **Web Layer (REST)**: JSON APIs handled by Spring MVC Controllers.
2.  **DTO Layer**: Decouples the API contract from the internal domain model.
3.  **Mapper Layer**: Explicit converters (Static or Component-based) for DTO-Entity transformations.
4.  **Service Layer**: Orchestrates business logic and transactional consistency.
5.  **Persistence Layer (JPA)**: Repository interfaces interacting with **Aiven PostgreSQL**.

---

## 🛠️ Functional Features

### 1. Authentication & Identity
Uses stateless **JWT** for security. `UserService` manages user identities and credentials, while `JwtService` handles the token lifecycle (generation and refresh).

### 2. Catalog & Product Hierarchy
- **Product Management**: Supports individual items with dynamic attributes and image associations.
- **Complex Bundles (Suites)**: Supports `Suites` defined by a dynamic `SuiteFormula` (Ingredients + Quantities).

### 3. Advanced Pricing & Discounting
- **Pricing Engine**: Tiered pricing calculation (min/max quantity ranges) and price validity management (dates, locations, currencies).
- **Discount Rules**: Hierarchical evaluation (Customer ➔ Segment ➔ Entity) to apply targeted reductions.

### 4. Dynamic Menu Management
Multi-level nested menus with role-based visibility and caching for optimal performance.

### 5. Centralized Approval System
A decoupled state management system using a central `ENTITY_STATUS` table to track the lifecycle (`DRAFT` ➔ `PENDING` ➔ `APPROVED`) of any approvable entity.

---

## ⚙️ Non-Functional Features

- **🔐 Security**: Dynamic Role-Based Access Control (RBAC) and path-based authorization.
- **📦 Data Integrity**: Automatic JPA Auditing (`BaseData`) for all entities.
- **🚀 Performance**: JOIN-based status filtering and Level-2 caching for menus.
- **🛡️ Reliability**: Global exception handling with structured JSON error responses.

---

## 🧪 Technical Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Java 17+ |
| **Framework** | Spring Boot 3.x |
| **Security** | Spring Security, JWT |
| **Persistence** | Spring Data JPA, Hibernate |
| **Database** | PostgreSQL (Aiven Managed) |
| **Build Tool** | Maven |

---
