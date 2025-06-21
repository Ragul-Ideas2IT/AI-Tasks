CREATE TABLE resource_requests (
    id BIGSERIAL PRIMARY KEY,
    orphanage_id BIGINT NOT NULL REFERENCES orphanages(id),
    resource_id BIGINT NOT NULL REFERENCES resources(id),
    quantity INTEGER NOT NULL,
    priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_resource_requests_orphanage_id ON resource_requests(orphanage_id);
CREATE INDEX idx_resource_requests_resource_id ON resource_requests(resource_id);
CREATE INDEX idx_resource_requests_status ON resource_requests(status);
CREATE INDEX idx_resource_requests_priority ON resource_requests(priority); 