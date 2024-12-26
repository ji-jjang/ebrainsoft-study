package com.juny.finalboard.domain.post.free;

import com.juny.finalboard.domain.post.free.common.dto.ReqUpdateFreePost;
import com.juny.finalboard.domain.post.free.common.entity.FreePost;
import com.juny.finalboard.domain.post.free.common.repository.FreePostRepository;
import com.juny.finalboard.domain.post.free.common.service.FreePostService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FreePostServiceTest {

  @Autowired private FreePostService freePostService;

  @Autowired private FreePostRepository freePostRepository;

  @Test
  void test() {

    ReqUpdateFreePost req =
        ReqUpdateFreePost.builder()
            .title("변경 제목")
            .content("변경 내용")
            .categoryId("2")
            .deleteAttachmentIds(List.of(1L))
            .build();

    FreePost freePost =
        freePostRepository.findFreePostDetailById(1L).orElseThrow(RuntimeException::new);

    freePostService.updatePost(req, freePost, 3L);
  }
}
