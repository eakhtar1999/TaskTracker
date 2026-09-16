INSERT INTO tasks (title, description, status, created_at, updated_at) VALUES
('Set up CI pipeline', 'GitHub Actions workflow to build and test on every push', 'COMPLETED', NOW(), NOW()),
('Design task schema', 'tasks table with status, timestamps', 'COMPLETED', NOW(), NOW()),
('Wire up REST endpoints', 'CRUD endpoints for tasks resource', 'IN_PROGRESS', NOW(), NOW()),
('Write integration tests', 'MockMvc tests covering the full stack', 'PENDING', NOW(), NOW());