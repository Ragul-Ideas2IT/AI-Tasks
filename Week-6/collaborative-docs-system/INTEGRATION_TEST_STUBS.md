# End-to-End Integration Test Stubs

These are high-level test scenarios to validate the integration of all core services in the collaborative document system.

---

## 1. User Registration and Login
- Register a new user via `user-service` (`/api/auth/register`).
- Log in and obtain a JWT token (`/api/auth/login`).

## 2. Document Creation and Sharing
- Create a new document via `document-service` (`/api/documents`).
- Share the document with another user (`/api/documents/{id}/share`).

## 3. Real-Time Collaboration
- Connect two users to the same document via `collaboration-service` WebSocket (`/ws/collaborate/{documentId}`).
- Simulate editing and verify both users receive updates in real time.

## 4. Notification Delivery
- Connect a user to `notification-service` WebSocket (`/ws/notify`).
- Trigger a notification (e.g., document shared, new comment) and verify delivery to all connected clients.

## 5. Analytics Dashboard
- Query `dashboard-service` for analytics (`/api/analytics/stats`).
- Validate returned stats reflect user and document activity.

## 6. Gateway, Auth, and Rate Limiting
- Access all endpoints via the `gateway` with a valid JWT.
- Attempt access with an invalid/expired JWT and expect 401.
- Exceed rate limits and expect 429 response.

---

*These stubs can be implemented using integration test frameworks (Spring Boot Test, Testcontainers, Selenium, etc.) for full automation.* 