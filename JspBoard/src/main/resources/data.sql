INSERT INTO categories (name) VALUES ('JAVA');
INSERT INTO categories (name) VALUES ('Javascript');
INSERT INTO categories (name) VALUES ('Database');

INSERT INTO boards (title, content, password, viewCount, created_at, updated_at, created_by, category_id) VALUES  ('자바 어렵더라', '내용 1', '1234', 0, NOW(), NOW(), 'juny', '1');

INSERT INTO board_images (stored_name, stored_path, board_id) VALUES ('jnj.webp', '/Users/jijunhyuk/Desktop/etc/', 1);

INSERT INTO attachments (logical_name, logical_path, stored_name, stored_path, extension, size, board_id)
VALUES ('슾.png', '/Users/jijunhyuk/Desktop/etc/', 'spring.png', '/Users/jijunhyuk/Desktop/etc/', '.png', 12345, 1);

INSERT INTO comments (content, createdAt, createdBy, board_id) VALUES ('댓글 내용1', NOW(), 'jiny', 1);
