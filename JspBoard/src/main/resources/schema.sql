use ebrainsoft_study;

CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE boards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    password VARCHAR(255) NOT NULL,
    viewCount INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME,
    created_by VARCHAR(255) NOT NULL,
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE board_images (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    stored_name VARCHAR(255) NOT NULL,
    stored_path VARCHAR(255) NOT NULL,
    board_id BIGINT,
    FOREIGN KEY (board_id) REFERENCES boards(id)
);

CREATE TABLE attachments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    logical_name VARCHAR(255) NOT NULL,
    logical_path VARCHAR(255) NOT NULL,
    stored_name VARCHAR(255) NOT NULL,
    stored_path VARCHAR(255) NOT NULL,
    extension VARCHAR(50),
    size VARCHAR(50),
    board_id BIGINT,
    FOREIGN KEY (board_id) REFERENCES boards(id)
);

CREATE TABLE comments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    content TEXT NOT NULL,
    createdAt DATETIME,
    createdBy VARCHAR(255) NOT NULL,
    board_id BIGINT,
    FOREIGN KEY (board_id) REFERENCES boards(id)
);