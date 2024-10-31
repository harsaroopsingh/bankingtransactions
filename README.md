# Banking API

## Build and run the application

Clone the Repository

```bash
git clone https://github.com/harsaroopsingh/bankingapi
```
 
Build the project

```bash
mvn clean install
```

Run the project

```bash
mvn spring-boot:run 
```

**Assumptions**

- Account balance cannot go negative
- Only USD, CAD, GBP, EUR currency codes and their fixed exchanges rates are supported 
- Account will be registered with 0 unit of the currency, if balance is not specified
- To prevent inconsistencie each fund transfer will be atomic
- Funds cannot be transferred from an account to itself
- Transfer funds amount must be at least 1 unit of the currency specified.

**API**

- Creating or Registering new account
  - /accounts/register
  - (POST) Input:
    - username (Required)
    - currency (Required)
    - balance (Optional)

- Transfering funds from issuer account to recipient account
  - /transactions/transfer/funds
  - (POST) Input:
    - issuerAccountId (Required)
    - recipientAccountId (Required)
    - amount (Required)

- Transaction history of an account
  - Input (POST):
      - accountId (Required)

