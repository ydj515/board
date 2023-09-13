CREATE TABLE member (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birth VARCHAR(8),
    role VARCHAR(10),
    sns_id VARCHAR(255),
    sns_type VARCHAR(10),
    status VARCHAR(20),
    created_at DATETIME,
    modified_at DATETIME
);