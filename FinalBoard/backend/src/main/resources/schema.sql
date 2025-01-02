CREATE TABLE users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    name       VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE announcement_categories
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE announcement_posts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    view_count  INT DEFAULT 0,
    is_pinned   BOOLEAN,
    created_by  VARCHAR(255) NOT NULL,
    created_at  DATETIME,
    category_id BIGINT,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES announcement_categories (id)
);

CREATE TABLE free_categories
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);


CREATE TABLE free_posts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    view_count  INT DEFAULT 0,
    created_by  VARCHAR(255) NOT NULL,
    created_at  DATETIME,
    category_id BIGINT,
    user_id     BIGINT,

    FOREIGN KEY (category_id) REFERENCES free_categories (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE free_attachments
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    logical_name VARCHAR(255) NOT NULL,
    stored_name  VARCHAR(255) NOT NULL,
    stored_path  VARCHAR(255) NOT NULL,
    extension    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    post_id      BIGINT,
    FOREIGN KEY (post_id) REFERENCES free_posts (id)
);

CREATE TABLE free_comments
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    content    TEXT         NOT NULL,
    created_at DATETIME,
    created_by VARCHAR(255) NOT NULL,
    post_id    BIGINT,

    FOREIGN KEY (post_id) REFERENCES free_posts (id)
);

CREATE TABLE gallery_categories
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE gallery_posts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    view_count  INT DEFAULT 0,
    created_by  VARCHAR(255) NOT NULL,
    created_at  DATETIME,
    category_id BIGINT,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES gallery_categories (id)
);

CREATE TABLE gallery_images
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    logical_name VARCHAR(255) NOT NULL,
    stored_name  VARCHAR(255) NOT NULL,
    stored_path  VARCHAR(255) NOT NULL,
    extension    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    post_id      BIGINT,
    FOREIGN KEY (post_id) REFERENCES gallery_posts (id)
);

CREATE TABLE question_categories
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE question_posts
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    view_count  INT DEFAULT 0,
    is_secret   TINYINT,
    created_by  VARCHAR(255) NOT NULL,
    created_at  DATETIME,
    category_id BIGINT,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (category_id) REFERENCES question_categories (id)
);

CREATE TABLE question_answers
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    post_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (post_id) REFERENCES question_posts (id)
)