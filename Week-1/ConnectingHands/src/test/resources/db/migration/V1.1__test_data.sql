-- Test user with password 'password123'
INSERT INTO users (email, password, first_name, last_name, role, email_verified)
VALUES (
    'test@example.com',
    '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG',
    'Test',
    'User',
    'ROLE_USER',
    true
); 