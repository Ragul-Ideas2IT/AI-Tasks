ALTER TABLE todos ADD COLUMN user_id BIGINT;

ALTER TABLE todos ADD CONSTRAINT fk_todos_users FOREIGN KEY (user_id) REFERENCES users(id); 