# Jackpot Service Backend

This Spring Boot application manages jackpot contributions and rewards based on bets.

## Overview

The application provides:
1.  An API endpoint to publish bets to a Kafka topic.
2.  A Kafka consumer to listen for bets and process jackpot contributions.
3.  Logic for different jackpot contribution types (fixed percentage, variable percentage).
4.  An API endpoint to evaluate if a bet wins a jackpot reward.
5.  Logic for different jackpot reward types (fixed chance, variable chance).
6.  Persistence using an in-memory H2 database.

## Requirements

* Git.
* Docker and docker compose.

## Setup & Running

1.  **Clone the repository:**
    ```bash
    git clone <repo-url> && cd jackpot-service
    ```

2.  **Build the project:**
    ```bash
    docker run --rm -v "$(pwd):/app" -w /app maven:3.9.6-eclipse-temurin-17 mvn clean package
    ```

3.  **Run the application:**
    ```bash
    docker compose up
    ```

4.  **Access H2 Console (for in-memory database inspection):**
    * Open your browser and go to: `http://localhost:8080/h2-console`
    * JDBC URL: `jdbc:h2:mem:jackpotdb`
    * User Name: `dev`
    * Password: `secret`
    * Click "Connect". You can then inspect the tables (`JACKPOTS`, `JACKPOT_CONTRIBUTIONS`, `JACKPOT_REWARDS`).

## API Endpoints

The service runs on port `8080` by default.

### 1. Publish a Bet

* **URL:** `/api/v1/jackpots/bets`
* **Method:** `POST`
* **Request Body (JSON):**
    ```json
    {
        "betId": "string (e.g., B123)",
        "userId": "string (e.g., U456)",
        "jackpotId": "string (e.g., JP001)",
        "betAmount": number (e.g., 10.50)
    }
    ```
* **Success Response (202 Accepted):**
    ```json
    {
        "timestamp":1748507200327,
        "status":202,
        "message":"Bet received and queued for processing."
    }
    ```
* **Validation Error Response (400 Bad Request)**
    ```json
    {
        "message":"Validation failed",
        "errors":{
            "betAmount":"Bet amount cannot be null",
            "jackpotId":"Jackpot ID cannot be blank",
            "betId":"Bet ID cannot be blank",
            "userId":"User ID cannot be blank"
        },
        "timestamp":1748514357994,
        "status":400
    }
    ```
* **Action:** Publishes the bet to the `jackpot-bets` Kafka topic. The Kafka consumer will then process it for contribution.

### 2. Evaluate Bet for Jackpot Reward

* **URL:** `/api/v1/jackpots/bets/evaluate-reward`
* **Method:** `POST`
* **Request Body (JSON):**
    ```json
    {
        "betId": "string (e.g., B123, must be a bet that has contributed)",
        "userId": "string (e.g., U456)",
        "jackpotId": "string (e.g., JP001)"
    }
    ```
* **Success Response (200 OK) - Win:**
    ```json
    {
        "betId": "B123",
        "userId": "U456",
        "jackpotId": "JP001",
        "jackpotRewardAmount": 1050.75,
        "createdAt": "2023-10-27T10:30:00.123456",
        "message": "Congratulations! You won the jackpot."
    }
    ```
* **Success Response (200 OK) - No Win:**
    ```json
    {
        "betId": "B124",
        "userId": "U457",
        "jackpotId": "JP001",
        "jackpotRewardAmount": null,
        "createdAt": null,
        "message": "Sorry, this bet did not win a jackpot reward."
    }
    ```
* **Validation Error Response (400 Bad Request)**
    ```json
    {
        "message":"Validation failed",
        "errors":{
            "jackpotId":"Jackpot ID cannot be blank",
            "betId":"Bet ID cannot be blank",
            "userId":"User ID cannot be blank"
        },
        "timestamp":1748514357994,
        "status":400
    }
    ```
* **Action:** Evaluates if the specified bet wins the jackpot. If it wins, a reward record is created, and the jackpot pool is reset.

## Jackpot Configuration

Jackpots are pre-configured in `src/main/resources/data.sql` or can be managed directly in the `JACKPOTS` table via the H2 console for testing.

Each jackpot has:
* `jackpot_id`: Unique identifier.
* `initial_pool_amount`: The amount the pool resets to after a win.
* `current_pool_amount`: The current value of the jackpot.
* `contribution_type`: Name of the contribution strategy bean (e.g., `FIXED_PERCENTAGE_CONTRIBUTION`, `VARIABLE_PERCENTAGE_CONTRIBUTION`). This should match the Enum constant name if refactored.
* `contribution_value`: Parameter for the contribution strategy (e.g., percentage for fixed, initial percentage for variable).
* `reward_type`: Name of the reward strategy bean (e.g., `FIXED_CHANCE_REWARD`, `VARIABLE_CHANCE_REWARD`). This should match the Enum constant name if refactored.
* `reward_value`: Parameter for the reward strategy (e.g., chance for fixed, initial chance for variable).
* `reward_pool_limit`: For variable chance rewards, the pool amount at which the win chance becomes 100%.

## Design Choices & Future Considerations

* **Strategy Pattern:** Used for flexible contribution and reward calculations. New strategies can be added by implementing `ContributionStrategy` or `RewardStrategy` and annotating them as `@Component` with a unique name.
* **Kafka for Decoupling:** Bets are published to Kafka, decoupling the initial bet reception from the processing logic.
* **In-Memory H2 Database:** Used for simplicity as per requirements. For production, a persistent database (e.g., PostgreSQL, MySQL) would be used.
* **Error Handling:** Basic error logging is in place. Production systems would require more robust error handling, dead-letter queues for Kafka, and potentially retry mechanisms.
* **Variable Strategy Simplification:** The variable contribution/reward strategies are simplified. Real-world implementations might involve more complex calculations based on historical data or more granular pool thresholds.
* **Security:** No security is implemented in this example. Production APIs should be secured (e.g., using Spring Security with OAuth2/JWT).
* **Scalability:** For high-volume betting, consider optimizing database queries, Kafka consumer group scaling, and potentially CQRS patterns.
* **Enum for Types:** Refactoring string-based types (like `contributionType` and `rewardType`) to Java Enums is recommended for type safety and maintainability. The service layer would then use `enum.name()` to get the string representation for strategy resolution.
* **Constants for Magic Numbers:** Replacing hardcoded numerical values in strategies with named constants improves readability and maintainability.
