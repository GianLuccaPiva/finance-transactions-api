# Finance Transactions API

REST API developed with Java 17 and Spring Boot 3 for account and transaction management.

## Tech Stack

- Java 17
- Spring Boot 3.3
- PostgreSQL 15
- Docker / Docker Compose

## Requirements

- Docker
- Docker Compose

## Running the project

**1. Clone the repository and navigate to the project folder**

**2. Create the `.env` file based on the example:**
```bash
cp .env.example .env
```

**3. Start the containers:**
```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/accounts` | Create a new account |
| GET | `/accounts/{id}` | Get account by ID |
| DELETE | `/accounts/{id}` | Deactivate account (soft delete) |
| GET | `/accounts/{id}/transactions` | List all transactions for an account |
| GET | `/accounts/{id}/balance` | Get current balance for an account |
| POST | `/transactions` | Create a new transaction |
| GET | `/transactions?page=0&size=10` | List all transactions (paginated) |

## Operation Types

| ID | Description | Accepted Amount |
|----|-------------|-----------------|
| 1 | PURCHASE | Negative only |
| 2 | INSTALLMENT PURCHASE | Negative only |
| 3 | WITHDRAWAL | Negative only |
| 4 | PAYMENT | Positive only |

## Testing

A shell script is provided for quick endpoint testing:

```bash
chmod +x test.sh
./test.sh
```

The script presents an interactive menu:

```
1) POST   /accounts
2) GET    /accounts/{id}
3) POST   /transactions
4) GET    /accounts/{id}/transactions
5) GET    /accounts/{id}/balance
6) GET    /transactions?page=0&size=10
7) DELETE /accounts/{id}
8) Rodar todos
```

Each option prompts only for the values it needs. Option 8 runs all endpoints in sequence.

### Test cases

> **Important:** before testing any request that uses `accountId`, make sure to create an account first and use the returned `accountId`.

**Valid account creation**
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"documentNumber": "12345678901"}'
```

**Invalid document number (not 11 digits)**
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"documentNumber": "123"}'
```

**Duplicate document number**
```bash
curl -X POST http://localhost:8080/accounts \
  -H "Content-Type: application/json" \
  -d '{"documentNumber": "12345678901"}'
```

**Account not found**
```bash
curl http://localhost:8080/accounts/999
```

**Deactivate account (soft delete)**
```bash
curl -X DELETE http://localhost:8080/accounts/1
```

**Deactivate already inactive account**
```bash
curl -X DELETE http://localhost:8080/accounts/1
```

**List transactions for an account**
```bash
curl http://localhost:8080/accounts/1/transactions
```

**Get balance for an account**
```bash
curl http://localhost:8080/accounts/1/balance
```

**List all transactions (paginated)**
```bash
curl "http://localhost:8080/transactions?page=0&size=10"
```

**Valid transaction (PURCHASE — negative amount)**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId": 1, "operationTypeId": 1, "amount": -50.00}'
```

**Valid transaction (PAYMENT — positive amount)**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId": 1, "operationTypeId": 4, "amount": 50.00}'
```

**Transaction with invalid account**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId": 999, "operationTypeId": 1, "amount": -50.00}'
```

**Transaction with invalid operation type**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId": 1, "operationTypeId": 99, "amount": -50.00}'
```

**Transaction with invalid amount for operation type (types 1, 2, 3 require negative)**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId": 1, "operationTypeId": 1, "amount": 50.00}'
```

**Transaction with invalid amount for operation type (type 4 requires positive)**
```bash
curl -X POST http://localhost:8080/transactions \
  -H "Content-Type: application/json" \
  -d '{"accountId": 1, "operationTypeId": 4, "amount": -50.00}'
```

## Unit Tests

Unit tests are written with JUnit 5 + Mockito and cover both service layers.

```bash
mvn test
```

| Test class | Tests | Coverage |
|------------|-------|----------|
| `ClientAccountServiceTest` | 8 | Create account, get account, duplicate document, account not found, deactivate account, deactivate already inactive |
| `TransactionTest` | 15 | Create transaction, amount validations, account/operation not found, get transactions by account, get balance, paginated listing |

## API Documentation

Swagger UI available at: `http://localhost:8080/swagger-ui.html`
