-- 카테고리 데이터 추가
INSERT INTO categories (name) VALUES ('JAVA');
INSERT INTO categories (name) VALUES ('Javascript');
INSERT INTO categories (name) VALUES ('Database');

-- 게시판 데이터 추가
INSERT INTO boards (title, content, password, view_count, created_at, updated_at, created_by,
                    category_id)
VALUES ('제목1', '내용1', '1234', 0, NOW(), NULL, '작성자1', '1');

INSERT INTO boards (title, content, password, view_count, created_at, updated_at, created_by,
                    category_id)
VALUES ('제목2', '내용2', '1234', 0, NOW(), NULL, '작성자2', '2');

INSERT INTO boards (title, content, password, view_count, created_at, updated_at, created_by,
                    category_id)
VALUES ('제목3', '내용3', '1234', 0, NOW(), NULL, '작성자3', '3');

-- 게시판 이미지 데이터 추가
INSERT INTO board_images (stored_name, stored_path, extension, board_id)
VALUES ('jnj', '/Users/jijunhyuk/Desktop/etc/', '.webp', 1);

INSERT INTO board_images (stored_name, stored_path, extension, board_id)
VALUES ('jnj2', '/Users/jijunhyuk/Desktop/etc/', '.webp', 1);


INSERT INTO board_images (stored_name, stored_path, extension, board_id)
VALUES ('jnj', '/Users/jijunhyuk/Desktop/etc/', '.webp', 2);

INSERT INTO board_images (stored_name, stored_path, extension, board_id)
VALUES ('jnj', '/Users/jijunhyuk/Desktop/etc/', '.webp', 3);

-- 첨부파일 데이터 추가
INSERT INTO attachments (logical_name, stored_name, stored_path, extension, size,
                         board_id)
VALUES ('jnj', '저장된 이름1', '저장된 경로1', '.webp', 100, 1);

INSERT INTO attachments (logical_name, stored_name, stored_path, extension, size,
                         board_id)
VALUES ('jnj', '저장된 이름2', '저장된 경로2', '.webp', 100, 2);

INSERT INTO attachments (logical_name, stored_name, stored_path, extension, size,
                         board_id)
VALUES ('jnj', '저장된 이름3', '저장된 경로3', '.webp', 100, 3);

-- 댓글 데이터 추가
INSERT INTO comments (content, password, created_at, created_by, board_id)
VALUES ('댓글 내용1', '1234', NOW(), '댓글 작성자1', 1);

INSERT INTO comments (content, password, created_at, created_by, board_id)
VALUES ('댓글 내용2', '1234', NOW(), '댓글 작성자2', 2);

INSERT INTO comments (content, password, created_at, created_by, board_id)
VALUES ('댓글 내용3', '1234', NOW(), '댓글 작성자3', 3);