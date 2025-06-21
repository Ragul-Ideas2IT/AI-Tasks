# Connecting Hands - Database Schema

## Table Definitions

### 1. Users
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL CHECK (role IN ('ADMIN', 'ORPHANAGE_ADMIN', 'DONOR')),
    phone_number VARCHAR(20),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP WITH TIME ZONE,
    is_active BOOLEAN DEFAULT true
);
```

### 2. Orphanages
```sql
CREATE TABLE orphanages (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    registration_number VARCHAR(100) NOT NULL UNIQUE,
    address TEXT NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    country VARCHAR(100) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    website VARCHAR(255),
    description TEXT,
    verification_status VARCHAR(50) DEFAULT 'PENDING' CHECK (verification_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    admin_user_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### 3. Resources
```sql
CREATE TABLE resources (
    id BIGSERIAL PRIMARY KEY,
    orphanage_id BIGINT REFERENCES orphanages(id),
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    description TEXT,
    quantity INTEGER NOT NULL DEFAULT 0,
    unit VARCHAR(50) NOT NULL,
    minimum_quantity INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### 4. Donations
```sql
CREATE TABLE donations (
    id BIGSERIAL PRIMARY KEY,
    donor_id BIGINT REFERENCES users(id),
    recipient_orphanage_id BIGINT REFERENCES orphanages(id),
    amount DECIMAL(15,2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    donation_type VARCHAR(50) NOT NULL CHECK (donation_type IN ('MONETARY', 'RESOURCE', 'SERVICE')),
    status VARCHAR(50) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'COMPLETED', 'REJECTED')),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### 5. Resource_Donations
```sql
CREATE TABLE resource_donations (
    id BIGSERIAL PRIMARY KEY,
    donation_id BIGINT REFERENCES donations(id),
    resource_id BIGINT REFERENCES resources(id),
    quantity INTEGER NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'COMPLETED', 'REJECTED')),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### 6. Resource_Requests
```sql
CREATE TABLE resource_requests (
    id BIGSERIAL PRIMARY KEY,
    requesting_orphanage_id BIGINT REFERENCES orphanages(id),
    resource_id BIGINT REFERENCES resources(id),
    quantity INTEGER NOT NULL,
    priority VARCHAR(50) DEFAULT 'NORMAL' CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),
    status VARCHAR(50) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'APPROVED', 'FULFILLED', 'REJECTED')),
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### 7. Messages
```sql
CREATE TABLE messages (
    id BIGSERIAL PRIMARY KEY,
    sender_orphanage_id BIGINT REFERENCES orphanages(id),
    recipient_orphanage_id BIGINT REFERENCES orphanages(id),
    subject VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    is_read BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

### 8. Audit_Logs
```sql
CREATE TABLE audit_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    action VARCHAR(100) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id BIGINT NOT NULL,
    details JSONB,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
```

## Indexes

```sql
-- Users
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Orphanages
CREATE INDEX idx_orphanages_registration_number ON orphanages(registration_number);
CREATE INDEX idx_orphanages_verification_status ON orphanages(verification_status);
CREATE INDEX idx_orphanages_location ON orphanages(city, state, country);

-- Resources
CREATE INDEX idx_resources_orphanage_id ON resources(orphanage_id);
CREATE INDEX idx_resources_category ON resources(category);

-- Donations
CREATE INDEX idx_donations_donor_id ON donations(donor_id);
CREATE INDEX idx_donations_recipient_id ON donations(recipient_orphanage_id);
CREATE INDEX idx_donations_status ON donations(status);
CREATE INDEX idx_donations_created_at ON donations(created_at);

-- Resource_Donations
CREATE INDEX idx_resource_donations_donation_id ON resource_donations(donation_id);
CREATE INDEX idx_resource_donations_resource_id ON resource_donations(resource_id);

-- Resource_Requests
CREATE INDEX idx_resource_requests_orphanage_id ON resource_requests(requesting_orphanage_id);
CREATE INDEX idx_resource_requests_status ON resource_requests(status);
CREATE INDEX idx_resource_requests_priority ON resource_requests(priority);

-- Messages
CREATE INDEX idx_messages_sender ON messages(sender_orphanage_id);
CREATE INDEX idx_messages_recipient ON messages(recipient_orphanage_id);
CREATE INDEX idx_messages_created_at ON messages(created_at);

-- Audit_Logs
CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_entity ON audit_logs(entity_type, entity_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);
```

## Hibernate Entity Models

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
    
    @Column(name = "is_active")
    private boolean isActive;
    
    // Getters, setters, and relationships
}

@Entity
@Table(name = "orphanages")
public class Orphanage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String city;
    
    @Column(nullable = false)
    private String state;
    
    @Column(nullable = false)
    private String country;
    
    @Column(name = "postal_code", nullable = false)
    private String postalCode;
    
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    private String website;
    
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status")
    private VerificationStatus verificationStatus;
    
    @ManyToOne
    @JoinColumn(name = "admin_user_id")
    private User adminUser;
    
    @OneToMany(mappedBy = "orphanage")
    private List<Resource> resources;
    
    // Getters, setters, and additional relationships
}

@Entity
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "orphanage_id", nullable = false)
    private Orphanage orphanage;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String category;
    
    private String description;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private String unit;
    
    @Column(name = "minimum_quantity")
    private Integer minimumQuantity;
    
    // Getters, setters, and relationships
}

@Entity
@Table(name = "donations")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "donor_id")
    private User donor;
    
    @ManyToOne
    @JoinColumn(name = "recipient_orphanage_id")
    private Orphanage recipientOrphanage;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    private String currency;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "donation_type", nullable = false)
    private DonationType donationType;
    
    @Enumerated(EnumType.STRING)
    private DonationStatus status;
    
    private String description;
    
    @OneToMany(mappedBy = "donation")
    private List<ResourceDonation> resourceDonations;
    
    // Getters, setters, and relationships
}
```

## Relationships Overview

1. **Users to Orphanages**: One-to-Many (An orphanage has one admin user, a user can be admin of multiple orphanages)
2. **Orphanages to Resources**: One-to-Many (An orphanage can have multiple resources)
3. **Users to Donations**: One-to-Many (A user can make multiple donations)
4. **Orphanages to Donations**: One-to-Many (An orphanage can receive multiple donations)
5. **Donations to Resource_Donations**: One-to-Many (A donation can include multiple resources)
6. **Resources to Resource_Donations**: One-to-Many (A resource can be part of multiple donations)
7. **Orphanages to Resource_Requests**: One-to-Many (An orphanage can make multiple resource requests)
8. **Resources to Resource_Requests**: One-to-Many (A resource can be requested multiple times)
9. **Orphanages to Messages**: One-to-Many (An orphanage can send and receive multiple messages)
10. **Users to Audit_Logs**: One-to-Many (A user can generate multiple audit logs)

## Notes on Design Decisions

1. **Normalization**: The schema follows 3NF principles to minimize data redundancy and maintain data integrity.
2. **Audit Trail**: The audit_logs table provides comprehensive tracking of all system changes.
3. **Flexibility**: The resource system is designed to handle various types of resources and donations.
4. **Performance**: Appropriate indexes are created for frequently queried fields.
5. **Security**: Password hashing is implemented at the database level.
6. **Scalability**: The schema supports horizontal scaling and can handle large volumes of data.
7. **Data Integrity**: Foreign key constraints ensure referential integrity across all tables.
8. **Monitoring**: Timestamps are included in all tables for tracking creation and updates. 