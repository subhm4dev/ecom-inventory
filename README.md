# 🔧 E-Commerce Inventory Service

Inventory and Stock Management Service.

## Port

**8085**

## Endpoints

- `POST /v1/inventory/adjust` - Adjust stock quantity

## Events

Listens to `ProductCreated` event from Kafka and automatically creates stock row with 0 quantity.

## Running Locally

```bash
mvn spring-boot:run
```

