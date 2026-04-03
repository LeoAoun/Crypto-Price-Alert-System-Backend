# 📈 Crypto Price Alert System (CPAS) - Backend

A robust, microservices-based backend system designed to provide real-time cryptocurrency price alerts. The system monitors market prices and asynchronously notifies users when their predefined custom conditions are met.

---

## 🛠 Tech Stack

| Layer | Technologies |
| :--- | :--- |
| **Core & Frameworks** | ![Java](https://img.shields.io/badge/Java_21-red?style=flat-square&logo=java&logoColor=white&labelColor=red) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-green?style=flat-square&logo=springboot&labelColor=green) ![Spring Security](https://img.shields.io/badge/Spring_Security-green?style=flat-square&logo=springsecurity&labelColor=green) |
| **Infrastructure** | ![Kafka](https://img.shields.io/badge/Apache_Kafka-black?style=flat-square&logo=apache-kafka&labelColor=black) ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-%23316192?style=flat-square&logo=postgresql&logoColor=white&labelColor=%23316192) ![Docker](https://img.shields.io/badge/Docker-blue?style=flat-square&logo=docker&logoColor=white&labelColor=blue) |
| **Gateway & Auth** | ![Kong](https://img.shields.io/badge/Kong_Gateway-teal?style=flat-square&logo=kong&labelColor=teal) ![JWT RSA Signed](https://img.shields.io/badge/JWT_RSA_Signed-purple?style=flat-square&logo=json-web-tokens&labelColor=purple) |

---

## 🏗 Architecture Overview

The system is built on a distributed microservices architecture, leveraging event-driven communication to ensure high scalability, decoupling, and resilience.

```mermaid
graph TD
    %% Layout Version: 1.1.0 - Enhanced Notification Flow
    User["User / Device"]
    App["Mobile / Web App"]
    
    User -->|"Interact"| App
    App -->|"1. Login & API"| Kong

    subgraph "CPAS Backend Architecture"
        direction TB
        Kong[Kong API Gateway]
        Kafka((Apache Kafka Broker))
        DB[(PostgreSQL)]

        Identity[Identity Service]
        UserPref[User Preference Service]
        PriceFetch[Price Fetcher Service]
        AlertEval[Alert Evaluator Service]
        Notification[Notification Service]
        
        Config{{"RSA Public Key"}}
        Config -.->|"Pre-configured"| Kong
    end

    %% Auth & Flow
    Kong -->|"Pass-thru"| Identity
    Identity -.->|"2. Issue JWT"| App

    App -->|"3. Client Request"| Kong
    Kong -->|"4. Validate JWT"| Kong
    Kong -->|"5. Route Request"| UserPref
    
    Identity <--> DB
    UserPref <--> DB

    %% Event Driven Messaging
    UserPref -- "Pub Preference" --> Kafka
    PriceFetch -- "Pub Price" --> Kafka
    
    Kafka -- "Stream Data" --> AlertEval
    
    AlertEval -- "Trigger Alert" --> Kafka
    Kafka -- "Deliver Alert" --> Notification
    
    %% Notifications Implementation
    Notification -.->|"HTTPS Request"| CallMeBot[CallMeBot API]
    CallMeBot -.->|"WhatsApp Message"| UserWA["User's WhatsApp"]
```

### 🧩 Microservices Ecosystem

- 🔐 **Identity Service**: Handles user registration, authentication, and authorization. Issues RSA-signed JWTs.
- ⚙️ **User Preference Service**: Manages user-defined price alerts and portfolio preferences.
- 📈 **Price Fetcher Service**: Dynamically fetches real-time cryptocurrency prices from external providers (e.g., Binance, CoinGecko).
- 🧠 **Alert Evaluator Service**: The core engine. Subscribes to both price streams and user preferences, evaluating conditions to trigger alerts.
- 🔔 **Notification Service**: Consumes triggered alert events and delivers them via **WhatsApp** (CallMeBot) and other channels.

---

## 📲 Notification Channels

### 🟢 WhatsApp Notifications (via CallMeBot)
The system supports real-time alerts directly to your WhatsApp using the **CallMeBot API**. This ensures you never miss a critical price movement, even when you're on the go.

<p align="center">
  <img src="assets/whatsapp_mockup.png" width="500" alt="WhatsApp Notification Mockup">
</p>

> **💡 Why WhatsApp?** Unlike standard push notifications that can be buried, WhatsApp messages provide a persistent, easy-to-read history of your alerts without requiring a dedicated mobile app for receiving.

#### ⚙️ How to Setup
To enable WhatsApp notifications, follow these steps:
1. Add **+34 644 10 55 33** (the CallMeBot bot) to your contacts.
2. Send the message: `I allow callmebot to send me messages`.
3. You will receive an **API Key**.
4. Update the `notification-service` configuration in `application.yaml` or set the following environment variables:
   - `WHATSAPP_PHONE`: Your phone number (with international prefix).
   - `WHATSAPP_APIKEY`: The key you received from the bot.

---

## 🚀 Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 21 (for local development/debugging)
- Maven (for building services)
- OpenSSL (for key generation)

### Security Setup (RSA Keys)

The system relies on Asymmetric RSA Keys to sign and validate JWT tokens across microservices. **Before running the project**, you must generate a key pair:

1. Create a `keys` directory in the project root:
   ```bash
   mkdir keys && cd keys
   ```

2. Generate the Private Key (used by the Identity Service):
   ```bash
   openssl genrsa -out private.pem 2048
   ```

3. Generate the Public Key (used by Kong and Resource Servers to validate tokens):
   ```bash
   openssl rsa -in private.pem -pubout -out public.pem
   ```

> ⚠️ **Note:** The `keys/` directory is ignored in `.gitignore`. Never commit these keys to version control.

### Running Locally

1. **Clone the repository**:
   ```bash
   git clone https://github.com/LeoAoun/Crypto-Price-Alert-System-Backend.git
   cd Crypto-Price-Alert-System-Backend
   ```

2. **Start the Infrastructure (DB, Kafka, Gateway)**:
   ```bash
   docker-compose up -d
   ```
   *Wait a few moments for Kafka and PostgreSQL to be fully ready.*

3. **Build & Run the Microservices**:
   You can build all services using Maven:
   ```bash
   mvn clean install
   ```
   To launch the entire environment locally, you can use the provided script:
   ```bash
   ./start_all.bat  # On Windows
   ```