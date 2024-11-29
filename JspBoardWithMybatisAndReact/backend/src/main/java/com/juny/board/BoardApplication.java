package com.juny.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO 1.서비스는 DTO가 아니라 엔티티, VO만 다루게 해라 - O
// TODO 2.엔티티는 생각보다 커질 수 있다. DB 필드가 아닌 컬럼이 추가되었다면 주석으로 꼭 남겨라 - O
// TODO 3.Controller에서 엔티티로 변환해라 (mapstruct) - O
// TODO 4.검색 조건은 URL 파라미터로 넘겨라. 어떤 페이지건 쿼리 스트링을 물고 있게 해라.
// TODO 5.스프링을 사용한다면 구현체를 갈아끼울 수 있게 인터페이스 의존 관계를 주입받아라. (확장될 가능성 있다면 정적 메서드보단 빈으로) - O
// TODO 6.Builder로 유연하게 해라 - O
// TODO 7.DAO 엔티티, VO 받고 엔티티 또는 VO 반환 - O
// TODO 8.Spring Bean Validation을 사용해서 컨트롤러에서 간단한 예외는 처리해라
// TODO 9.Mapstruct를 쓴다면 mybatis mapper는 repository 이름으로 해라 - O
// TODO 10.Mapper.xml 줄바꿈 눈에 보기 좋게 해라
// TODO 11.Include WHERE 절 공유해서 count 쿼리와 목록 쿼리 재사용할 수 있게 해라 - O
@SpringBootApplication
public class BoardApplication {

  public static void main(String[] args) {
    SpringApplication.run(BoardApplication.class, args);
  }
}
