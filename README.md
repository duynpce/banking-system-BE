# banking-system-BE

## Introduction

- A full-stack banking system built as a personal project to practise real-world software architecture
- Backend acts as both an **OAuth2 Authorization Server** and an **OAuth2 Resource Server** using Spring Security
- Frontend (React) communicates with the backend through a secured REST API protected by JWT tokens
- Features cover account management, card management, loans, loan fines, transactions, and reporting

## Features

- **Authentication & Authorization** — OAuth2 Authorization Server issues JWT access tokens; Resource Server validates them on every request
- **Account management** — Personal, Business, and Government account types with credit rating and verification status
- **Card management** — Personal and Business cards (DEBIT / CREDIT) with privilege tiers (STANDARD → DIAMOND), pin code, balance, and cashback
- **Transactions** — Deposit, withdrawal, and peer-to-peer transfers with sender/receiver posted-balance tracking
- **Loans** — Loan creation, repayment, and status tracking (CREDIT / MORTGAGE / AUTO) with interest calculation
- **Loan fines** — Fine policies (OVERDUE\_PAYMENT / EARLY\_PAYMENT) applied per loan
- **Reporting** — Loan report endpoint aggregating total amount, left amount, and monthly installment by status
- **OpenAPI docs** — Swagger UI available at `/swagger-ui.html`

## Project Details

### Workflow

**Backend**
```
client → controller → service (domain) → validator → service (query) → repository → database
```

**Frontend**
```
page → component → component hooks (local state & effects)
     → domain hooks (business logic, shared across components)
     → service layer (Axios / TanStack Query API calls)
```

### Tech Stack

| Layer | Technology |
|---|---|
| Backend language | Java 21 |
| Backend framework | Spring Boot 3.5.9 |
| Security | Spring Security — OAuth2 Authorization Server + Resource Server, JWT (JJWT 0.12.6) |
| Persistence | Spring Data JPA + Hibernate, PostgreSQL |
| Mapping | MapStruct 1.5.5 |
| Validation | Spring Validation (Bean Validation 3) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build tool | Maven |
| Testing | JUnit 5, Mockito, Testcontainers (PostgreSQL) |
| Frontend language | TypeScript |
| Frontend framework | React |
| State / data fetching | TanStack Query |
| Form validation | Zod |
| Build tool (FE) | Vite |

## How to Run

### Requirements

| Tool | Version |
|---|---|
| Java | 21 |
| Node.js | 18+ |
| PostgreSQL | 15+ |
| Maven | 3.9+ |
| npm | 9+ |

### Backend

1. Set up a PostgreSQL database and configure the environment variables below
2. create your own application.yaml or just rename the application-ci to application.yaml
3. Run the application:

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
./mvnw.cmd spring-boot:run
```

### Required Environment Variables

| Variable | Description |
|---|---|
| `DATABASE_URL` | JDBC URL of the PostgreSQL database |
| `DATABASE_USERNAME` | Database username |
| `DATABASE_PASSWORD` | Database password |
| `PORT` | Server port (default `8080`) |
| `APP_CONTEXT_PATH` | Servlet context path (e.g. `/api`) |
| `APP_ORIGIN_URL` | Base URL of this server |
| `OAUTH2_*` | OAuth2 client / auth-server config (see `application-prod.yaml`) |
| `CARD_BIN` | 6-digit BIN prefix for card number generation |
| `TRANSACTION_DEPOSIT_ACCOUNT_NUMBER` | System deposit account number |
| `TRANSACTION_WITHDRAWAL_ACCOUNT_NUMBER` | System withdrawal account number |

### Frontend

```bash
npm install
npm run dev
```

