# 이전 게시판과 달라진 점 
- [이전 게시판 링크(Spring & JSP)](./https://github.com/ji-jjang/ebrainsoft/tree/main/JspBoardWithMybatis)

## 1. SPA 구조 (Spring & React)
- JSP -> React 도입

## 2. docer-compose 환경 구성(spring, mysql, nginx)
- nginx가 API는 백엔드 서버(/api prefix), 페이지 요청은 빌드된 react 파일에서 처리
```yaml
networks:
  juny:
    driver: bridge

services:
  mysql:
    image: mysql:9.1
    container_name: MyMysql
    ports:
      - 3306:3306
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      TZ: Asia/Seoul
    command:
      - --character-set-server=UTF8MB4
      - --collation-server=UTF8MB4_UNICODE_CI
    networks:
      - juny

  nginx:
    build:
      context: Dockerfiles/nginx
      dockerfile: Dockerfile
    container_name: MyNginx
    ports:
      - 80:80
    volumes:
      - ../frontend/dist:/usr/share/nginx/html
    restart: unless-stopped
    networks:
      - juny

  app:
    build:
      context: ./
      dockerfile: Dockerfiles/app/Dockerfile
    container_name: MyApp
    ports:
      - 8080:8080
    networks:
      - juny
```

## 3. 계층별 역할 수정
- 컨트롤러 -> DTO를 {엔티티, VO}로 변환, {엔티티, VO}를 DTO로 변환 
- 서비스 -> {엔티티, VO}받아 {엔티티, VO} 반환
- 데이터베이스 -> {엔티티, VO}받아 {엔티티, VO 반환 }
```java
@Mapper
public interface BoardMapper {

  void increaseViewCount(Long boardId);

  Map<String, Object> findBoardDetailById(Long id);

  long getTotalBoardCount(Map<String, Object> searchConditions);

  List<Map<String, Object>> getBoardList(Map<String, Object> searchConditions);

  void saveBoard(Board board);

  void updateBoard(Board board);

  void deleteBoardById(Long id);

  String getBoardPassword(Long id);
}

@Mapper
public interface BoardRepository {

  void increaseViewCount(Long id);

  Board findBoardDetailById(Long id);

  long getTotalBoardCount(SearchConditionVO searchConditionVO);

  List<Board> getBoardList(SearchConditionVO searchConditionVO);

  void saveBoard(Board board);

  void updateBoard(Board board);

  void deleteBoardById(Long id);

  String getBoardPassword(Long id);
}
```

> 
- 컨트롤러에서 DTO <-> 엔티티, 서비스에서 DTO <-> 엔티티
- 전자는 이번에 사용한 방식, 후자에 비해 컨트롤러가 더 많은 책임을 가짐. 서비스 메서드는 엔티티만 받으므로 일관성 유지에 좋음. 

## 4. Spring Bean Validator 도입
- DTO 검증을 빈 밸리데이션을 통해 간단한 유효성 검증
- 서비스 Validator에서는 비즈니스 로직 유효성 검증

## 5. Mapstruct 도입 및 리팩토링
- Mapstruct로 간단한 변환은 자동 변환

### 1) FileUtils 파일 저장 정적 클래스를 FileService로 인터페이스로 빈 등록
  - LocalFileService는 로컬 파일 시스템에 저장하는 로직
  - S3FileService는 Aws S3에 저장할 수 있도록

### 2) 검색 조건 URL 파라미터로 유지
- 단순 페이지 이동 뿐만 아니라, 새로운 페이지로 이동시에도 검색 조건을 물고 있도록 수정
- 기존 세션에 저장한 방식과 달리 세션 정보가 섞일 위험 없고, 세션 로직 단순해짐
