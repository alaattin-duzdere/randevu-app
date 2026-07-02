# Verification Module

## Overview

This module provides a robust and extensible framework for handling time-sensitive user verification processes. It is designed to manage the lifecycle of verification tokens for various purposes, such as user registration, password resets, and email/phone confirmation. The system supports multiple verification methods and includes built-in security features like spam protection and attempt tracking.

## Features

- **Strategy-Based Verification:** Supports different verification methods (`CODE` and `LINK`) through a strategy pattern, making it easy to add new methods in the future.
- **Purpose-Driven Logic:** Each verification process is tied to a specific `VerificationPurpose` (e.g., `REGISTRATION`, `PASSWORD_RESET`), allowing for distinct validation rules and workflows.
- **Pre-Verification Validation Chain:** Utilizes a chain of filters to validate incoming verification requests before processing them. This includes checks for user status, allowed purposes, and spam prevention.
- **Spam & Abuse Protection:** Integrates with Redis to rate-limit verification requests from a single user for a specific purpose, preventing resource abuse.
- **Event-Driven Architecture:** Publishes a `VerificationCompletedEvent` upon successful verification, allowing other parts of the application to react to the event in a decoupled manner.
- **Automated Token Cleanup:** A scheduled service (`TokenCleanupService`) runs daily to purge expired and irrelevant verification tokens from the database, maintaining data hygiene.
- **Database Persistence:** All verification tokens are stored in a dedicated `verification_tokens` table with optimized indexes for efficient querying.

## Architecture

The verification process follows a well-defined flow, orchestrated by the `VerificationService`.

1.  **Initiation:** A client sends a request to the `VerificationController` to start a verification process.
2.  **Validation:** The request is first processed by the `VerificationFilterChainManager`, which executes a series of `IVerificationFilter` implementations (`UserStatusFilter`, `PurposeInitiationFilter`, `SpamProtectionFilter`) to ensure the request is valid and not abusive.
3.  **Strategy Selection:** The `VerificationService` selects the appropriate `IVerificationStrategy` (`CodeVerificationStrategy` or `LinkVerificationStrategy`) based on the requested `VerificationType`.
4.  **Token Generation:** The selected strategy generates a secret (a numeric code or a unique token). The `VerificationService` encodes this secret and persists it as a `VerificationEntity` in the database via the `VerificationTokenRepository`. Any previous pending tokens for the same user, type, and purpose are cleared.
5.  **Notification:** The `VerificationService` uses the `INotificationService` to send the raw secret (code or link) to the user through the specified channel (Email or SMS).
6.  **Confirmation:** The user provides the secret back to the system via the `VerificationController` (by submitting a form with the code or clicking the link).
7.  **Verification:** The `VerificationService` retrieves the token, validates its expiration and attempt count, and uses the corresponding strategy to match the user's input against the stored secret.
8.  **Completion:** Upon successful validation, the token is marked as confirmed, and a `VerificationCompletedEvent` is published for other modules to consume.

## Core Components

-   **`VerificationController`**: Exposes REST endpoints for initiating and confirming verifications.
-   **`VerificationService`**: The central orchestrator that manages the entire verification lifecycle.
-   **`IVerificationStrategy`**: An interface defining the contract for different verification methods.
    -   `CodeVerificationStrategy`: Generates a numeric code and prepares notifications for SMS/email.
    -   `LinkVerificationStrategy`: Generates a unique token and constructs a verification URL.
-   **`VerificationFilterChainManager`**: Manages a chain of validation filters that run before a verification process is initiated.
    -   `UserStatusFilter`: Checks if the user's current status is compatible with the verification purpose.
    -   `PurposeInitiationFilter`: Restricts which verification purposes can be manually initiated by a user.
    -   `SpamProtectionFilter`: Provides rate-limiting to prevent abuse.
-   **`VerificationEntity`**: The JPA entity representing a verification token in the database. It stores the user ID, purpose, type, hashed secret, expiration date, and attempt count.
-   **`VerificationTokenRepository`**: The Spring Data JPA repository for database operations on `VerificationEntity`.
-   **`TokenCleanupService`**: A scheduled component that periodically removes expired tokens from the database.

## API Workflow

### 1. Initiate Verification

To start a verification process, send a `POST` request to `/api/v1/verification/initiate`.

**Endpoint:** `POST /api/v1/verification/initiate`

**Body (`VerificationRequest`):**

```json
{
  "userId": 123,
  "type": "CODE",
  "channel": "EMAIL",
  "purpose": "REGISTRATION",
  "referenceId": "optional-reference-string"
}
```

-   `type`: `CODE` or `LINK`.
-   `channel`: `EMAIL` or `SMS`.
-   `purpose`: The reason for the verification (e.g., `REGISTRATION`, `PASSWORD_RESET`, `EMAIL_VERIFICATION`).
-   `referenceId`: An optional identifier to associate with the verification. If not provided, it defaults to the `userId`.

### 2. Confirm Verification

#### A. Using a Code

To confirm a verification using a code received by the user, send a `POST` request to `/api/v1/verification/confirm-code`.

**Endpoint:** `POST /api/v1/verification/confirm-code`

**Body (`CodeConfirmRequest`):**

```json
{
  "userId": 123,
  "code": "123456",
  "purpose": "REGISTRATION"
}
```

On success, this returns an HTTP 200 with the message "Kod doğrulandı!".

#### B. Using a Link

The user clicks a link sent to their email. This triggers a `GET` request to the confirmation endpoint. The link is constructed by the `LinkVerificationStrategy`.

**Endpoint:** `GET /api/v1/verification/confirm-link`

**Query Parameters:**

-   `token`: The unique verification token from the link.
-   `userId`: The ID of the user being verified.
-   `purpose`: The purpose of the verification.

**Example URL:**
`https://your-app-domain.com/api/v1/verification/confirm-link?token=...&userId=123&purpose=REGISTRATION`

On success, this returns an HTTP 200 with the message "Hesap doğrulandı!".

## Configuration

The module's behavior can be customized via application properties, managed through the `VerificationProperties` class. Key properties include:

-   `verification.token-validity-minutes`: The duration (in minutes) for which a token is valid.
-   `verification.max-attempts`: The number of failed confirmation attempts allowed before a token is invalidated.
-   `verification.code-length`: The number of digits for a `CODE` verification.
-   `verification.spam.max-requests-per-hour`: The maximum number of verification requests a user can make within the block duration.
-   `verification.spam.block-duration-minutes`: The duration for which the rate-limiting window is active.
-   `app.base-url`: The base URL of the application, used to construct confirmation links.