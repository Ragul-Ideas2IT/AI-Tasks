# Connecting Hands - API Specification

## Base URL
```
https://api.connectinghands.org/v1
```

## Authentication
All authenticated endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <jwt_token>
```

## Common Response Formats

### Success Response
```json
{
    "status": "success",
    "data": { ... },
    "message": "Operation completed successfully"
}
```

### Error Response
```json
{
    "status": "error",
    "error": {
        "code": "ERROR_CODE",
        "message": "Human readable error message",
        "details": { ... }
    }
}
```

## API Endpoints

### Authentication Module

#### Register User
- **Path**: `POST /auth/register`
- **Description**: Register a new user
- **Authentication**: None
- **Request Body**:
```json
{
    "email": "string",
    "password": "string",
    "firstName": "string",
    "lastName": "string",
    "phoneNumber": "string",
    "role": "DONOR" | "ORPHANAGE_ADMIN"
}
```
- **Success Response**: `201 Created`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "email": "string",
        "firstName": "string",
        "lastName": "string",
        "role": "string",
        "token": "string"
    }
}
```

#### Login
- **Path**: `POST /auth/login`
- **Description**: Authenticate user and get JWT token
- **Authentication**: None
- **Request Body**:
```json
{
    "email": "string",
    "password": "string"
}
```
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "token": "string",
        "user": {
            "id": "long",
            "email": "string",
            "firstName": "string",
            "lastName": "string",
            "role": "string"
        }
    }
}
```

### Orphanages Module

#### Create Orphanage
- **Path**: `POST /orphanages`
- **Description**: Register a new orphanage
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Request Body**:
```json
{
    "name": "string",
    "registrationNumber": "string",
    "address": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "postalCode": "string",
    "phoneNumber": "string",
    "email": "string",
    "website": "string",
    "description": "string"
}
```
- **Success Response**: `201 Created`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "name": "string",
        "registrationNumber": "string",
        "verificationStatus": "PENDING",
        "createdAt": "timestamp"
    }
}
```

#### Get Orphanage
- **Path**: `GET /orphanages/{id}`
- **Description**: Get orphanage details
- **Authentication**: JWT Required
- **Path Parameters**:
  - `id`: long (Orphanage ID)
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "name": "string",
        "registrationNumber": "string",
        "address": "string",
        "city": "string",
        "state": "string",
        "country": "string",
        "postalCode": "string",
        "phoneNumber": "string",
        "email": "string",
        "website": "string",
        "description": "string",
        "verificationStatus": "string",
        "createdAt": "timestamp",
        "updatedAt": "timestamp"
    }
}
```

#### List Orphanages
- **Path**: `GET /orphanages`
- **Description**: List all orphanages with filtering and pagination
- **Authentication**: JWT Required
- **Query Parameters**:
  - `page`: integer (default: 0)
  - `size`: integer (default: 20)
  - `city`: string (optional)
  - `state`: string (optional)
  - `country`: string (optional)
  - `verificationStatus`: string (optional)
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "content": [
            {
                "id": "long",
                "name": "string",
                "city": "string",
                "state": "string",
                "country": "string",
                "verificationStatus": "string"
            }
        ],
        "page": "integer",
        "size": "integer",
        "totalElements": "long",
        "totalPages": "integer"
    }
}
```

### Resources Module

#### Create Resource
- **Path**: `POST /orphanages/{orphanageId}/resources`
- **Description**: Add a new resource to an orphanage
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Path Parameters**:
  - `orphanageId`: long
- **Request Body**:
```json
{
    "name": "string",
    "category": "string",
    "description": "string",
    "quantity": "integer",
    "unit": "string",
    "minimumQuantity": "integer"
}
```
- **Success Response**: `201 Created`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "name": "string",
        "category": "string",
        "quantity": "integer",
        "unit": "string",
        "createdAt": "timestamp"
    }
}
```

#### Update Resource
- **Path**: `PUT /orphanages/{orphanageId}/resources/{resourceId}`
- **Description**: Update resource details
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Path Parameters**:
  - `orphanageId`: long
  - `resourceId`: long
- **Request Body**:
```json
{
    "quantity": "integer",
    "minimumQuantity": "integer",
    "description": "string"
}
```
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "name": "string",
        "category": "string",
        "quantity": "integer",
        "unit": "string",
        "updatedAt": "timestamp"
    }
}
```

### Donations Module

#### Create Donation
- **Path**: `POST /donations`
- **Description**: Create a new donation
- **Authentication**: JWT Required
- **Request Body**:
```json
{
    "recipientOrphanageId": "long",
    "amount": "decimal",
    "currency": "string",
    "donationType": "MONETARY" | "RESOURCE" | "SERVICE",
    "description": "string",
    "resources": [
        {
            "resourceId": "long",
            "quantity": "integer"
        }
    ]
}
```
- **Success Response**: `201 Created`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "amount": "decimal",
        "currency": "string",
        "donationType": "string",
        "status": "PENDING",
        "createdAt": "timestamp"
    }
}
```

#### List Donations
- **Path**: `GET /donations`
- **Description**: List donations with filtering
- **Authentication**: JWT Required
- **Query Parameters**:
  - `page`: integer (default: 0)
  - `size`: integer (default: 20)
  - `donationType`: string (optional)
  - `status`: string (optional)
  - `startDate`: date (optional)
  - `endDate`: date (optional)
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "content": [
            {
                "id": "long",
                "amount": "decimal",
                "currency": "string",
                "donationType": "string",
                "status": "string",
                "createdAt": "timestamp"
            }
        ],
        "page": "integer",
        "size": "integer",
        "totalElements": "long",
        "totalPages": "integer"
    }
}
```

### Resource Requests Module

#### Create Resource Request
- **Path**: `POST /resource-requests`
- **Description**: Create a new resource request
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Request Body**:
```json
{
    "resourceId": "long",
    "quantity": "integer",
    "priority": "LOW" | "NORMAL" | "HIGH" | "URGENT",
    "description": "string"
}
```
- **Success Response**: `201 Created`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "resourceId": "long",
        "quantity": "integer",
        "priority": "string",
        "status": "PENDING",
        "createdAt": "timestamp"
    }
}
```

#### Update Request Status
- **Path**: `PATCH /resource-requests/{requestId}/status`
- **Description**: Update resource request status
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Path Parameters**:
  - `requestId`: long
- **Request Body**:
```json
{
    "status": "APPROVED" | "FULFILLED" | "REJECTED"
}
```
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "status": "string",
        "updatedAt": "timestamp"
    }
}
```

### Messages Module

#### Send Message
- **Path**: `POST /messages`
- **Description**: Send a message to another orphanage
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Request Body**:
```json
{
    "recipientOrphanageId": "long",
    "subject": "string",
    "content": "string"
}
```
- **Success Response**: `201 Created`
```json
{
    "status": "success",
    "data": {
        "id": "long",
        "subject": "string",
        "createdAt": "timestamp"
    }
}
```

#### List Messages
- **Path**: `GET /messages`
- **Description**: List messages with filtering
- **Authentication**: JWT Required (ORPHANAGE_ADMIN)
- **Query Parameters**:
  - `page`: integer (default: 0)
  - `size`: integer (default: 20)
  - `isRead`: boolean (optional)
  - `startDate`: date (optional)
  - `endDate`: date (optional)
- **Success Response**: `200 OK`
```json
{
    "status": "success",
    "data": {
        "content": [
            {
                "id": "long",
                "subject": "string",
                "isRead": "boolean",
                "createdAt": "timestamp"
            }
        ],
        "page": "integer",
        "size": "integer",
        "totalElements": "long",
        "totalPages": "integer"
    }
}
```

## Common Error Responses

### 400 Bad Request
```json
{
    "status": "error",
    "error": {
        "code": "INVALID_REQUEST",
        "message": "Invalid request parameters",
        "details": {
            "field": "Error description"
        }
    }
}
```

### 401 Unauthorized
```json
{
    "status": "error",
    "error": {
        "code": "UNAUTHORIZED",
        "message": "Authentication required"
    }
}
```

### 403 Forbidden
```json
{
    "status": "error",
    "error": {
        "code": "FORBIDDEN",
        "message": "Insufficient permissions"
    }
}
```

### 404 Not Found
```json
{
    "status": "error",
    "error": {
        "code": "NOT_FOUND",
        "message": "Resource not found"
    }
}
```

### 500 Internal Server Error
```json
{
    "status": "error",
    "error": {
        "code": "INTERNAL_ERROR",
        "message": "An internal error occurred"
    }
}
```

## Security Notes

1. **Rate Limiting**:
   - Authentication endpoints: 5 requests per minute
   - Other endpoints: 60 requests per minute per IP

2. **Input Validation**:
   - All input must be validated for type, length, and format
   - SQL injection prevention
   - XSS prevention
   - CSRF protection

3. **Data Protection**:
   - Sensitive data encryption
   - Secure password storage
   - JWT token expiration (1 hour)
   - Refresh token mechanism

4. **Audit Logging**:
   - All API calls are logged
   - Sensitive operations are tracked
   - Failed authentication attempts are monitored

5. **CORS Policy**:
   - Strict origin validation
   - Limited HTTP methods
   - Secure cookie handling 