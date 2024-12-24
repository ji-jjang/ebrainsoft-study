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
    password    VARCHAR(255) NOT NULL,
    view_count  INT DEFAULT 0,
    is_pinned   BOOLEAN,
    created_by  VARCHAR(255) NOT NULL,
    created_at  DATETIME,
    category_id BIGINT,
    user_id     BIGINT,
    FOREIGN KEY (user_id) REFERENCES  users (id),
    FOREIGN KEY (category_id) REFERENCES announcement_categories (id)
);


/**
ALTER TABLE announcement_posts
ADD COLUMN user_id BIGINT,
ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id);
 */