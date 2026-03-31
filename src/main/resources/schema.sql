-- HealthNet Module 3: Disease Case Reporting & Tracking System
-- MySQL DDL Script

-- Create the database (run once manually or via CI)
CREATE DATABASE IF NOT EXISTS healthnet CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE healthnet;

-- ============================================================
-- Users Table
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    username   VARCHAR(100) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(150),
    role       ENUM('DOCTOR','ADMIN','CITIZEN') NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uq_username (username),
    UNIQUE KEY uq_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Disease Cases Table
-- ============================================================
CREATE TABLE IF NOT EXISTS disease_cases (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    citizen_id     BIGINT       NOT NULL,
    doctor_id      BIGINT       NOT NULL,
    disease_type   VARCHAR(255) NOT NULL,
    diagnosis_date DATE         NOT NULL,
    status         ENUM('OPEN','UNDER_TREATMENT','RECOVERED','DECEASED','CLOSED') NOT NULL DEFAULT 'OPEN',
    initial_notes  TEXT,
    created_at     DATETIME(6)  NOT NULL,
    updated_at     DATETIME(6),
    PRIMARY KEY (id),
    INDEX idx_citizen_id (citizen_id),
    INDEX idx_doctor_id  (doctor_id),
    INDEX idx_status     (status),
    CONSTRAINT fk_case_doctor   FOREIGN KEY (doctor_id)  REFERENCES users(id) ON DELETE RESTRICT,
    CONSTRAINT fk_case_citizen  FOREIGN KEY (citizen_id) REFERENCES users(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Case Updates Table
-- ============================================================
CREATE TABLE IF NOT EXISTS case_updates (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    case_id     BIGINT      NOT NULL,
    status      VARCHAR(50) NOT NULL,
    notes       TEXT,
    update_date DATETIME(6) NOT NULL,
    created_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_case_id (case_id),
    CONSTRAINT fk_update_case FOREIGN KEY (case_id) REFERENCES disease_cases(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- Sample Seed Data (for development / testing)
-- Passwords are BCrypt hashes of 'Password123!'
-- ============================================================
INSERT IGNORE INTO users (username, password, email, role) VALUES
    ('admin1',   '$2a$10$7EqJtq98hPqEX7fNZaFWoO1mVO9SWQb8yxjCDoNEH.Y/mwRV6Prjq', 'admin@healthnet.gov',    'ADMIN'),
    ('doctor1',  '$2a$10$7EqJtq98hPqEX7fNZaFWoO1mVO9SWQb8yxjCDoNEH.Y/mwRV6Prjq', 'doctor1@healthnet.gov',  'DOCTOR'),
    ('doctor2',  '$2a$10$7EqJtq98hPqEX7fNZaFWoO1mVO9SWQb8yxjCDoNEH.Y/mwRV6Prjq', 'doctor2@healthnet.gov',  'DOCTOR'),
    ('citizen1', '$2a$10$7EqJtq98hPqEX7fNZaFWoO1mVO9SWQb8yxjCDoNEH.Y/mwRV6Prjq', 'citizen1@example.com',   'CITIZEN'),
    ('citizen2', '$2a$10$7EqJtq98hPqEX7fNZaFWoO1mVO9SWQb8yxjCDoNEH.Y/mwRV6Prjq', 'citizen2@example.com',   'CITIZEN');
