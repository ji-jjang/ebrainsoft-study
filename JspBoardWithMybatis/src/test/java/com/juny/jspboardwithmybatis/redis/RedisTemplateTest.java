package com.juny.jspboardwithmybatis.redis;

import static org.assertj.core.api.Assertions.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class RedisTemplateTest {

  @Autowired
  RedisTemplate redisTemplate;


  @Test
  void setString() {
    redisTemplate.delete("juny");
    redisTemplate.delete("지니");

    redisTemplate.opsForValue().set("juny", "쭈니야");
    redisTemplate.opsForValue().set("지니", "쭈니");

    Product product1 = new Product("Monitor", 120, 15);
    redisTemplate.opsForValue().set("product:1", product1);

    String value1 = (String) redisTemplate.opsForValue().get("juny");
    String value2 = (String) redisTemplate.opsForValue().get("지니");
    Product value3 = (Product) redisTemplate.opsForValue().get("product:1");


    assertThat(value1).isEqualTo("쭈니야");
    assertThat(value2).isEqualTo("쭈니");
    assertThat(value3).isEqualTo(product1);
  }
  /**
   * 127.0.0.1:6379> get juny
   * "쭈니야"
   * 127.0.0.1:6379> get 지니
   * "쭈니"
   */

  @Test
  void setList() {

    redisTemplate.delete("user");

    redisTemplate.opsForList().leftPush("user", "1");
    redisTemplate.opsForList().leftPush("user", "2");
    redisTemplate.opsForList().leftPush("user", "3");

    List<String> list = redisTemplate.opsForList().range("user", 0, -1);
    assertThat(list).containsExactly("3", "2", "1");
  }
  /**
   * 127.0.0.1:6379> LRANGE user 0 -1
   * "3"
   * "2"
   * "1"
   */

  @Test
  void setSet() {
    redisTemplate.delete("numbers");

    redisTemplate.opsForSet().add("numbers", "1", "1", "2", "2", "3", "4", "5", "5");

    Set<Object> numbers = redisTemplate.opsForSet().members("numbers");

    assertThat(numbers).contains("1", "2", "3", "4", "5");
    assertThat(numbers).hasSize(5);
  }
  /**
   * 127.0.0.1:6379> SMEMBERS numbers
   * "1"
   * "2"
   * "3"
   * "4"
   * "5"
   */

  @Test
  void setSortedSet() {
    redisTemplate.delete("scores");

    redisTemplate.opsForZSet().add("scores", "korean", 100);
    redisTemplate.opsForZSet().add("scores", "science", 80);
    redisTemplate.opsForZSet().add("scores", "math", 90);

    Set scores = redisTemplate.opsForZSet().rangeWithScores("scores", 0, -1);

    scores.forEach(System.out::println);

    assertThat(scores).hasSize(3);

    assertThat(scores)
      .extracting("value")
      .containsExactly("science", "math", "korean");

    assertThat(scores)
      .extracting("score")
      .containsExactly(80.0, 90.0, 100.0);
  }
  /**
   * 127.0.0.1:6379> ZRANGE scores 0 -1 WITHSCORES
   * "science"
   * 80
   * "math"
   * 90
   * "korean"
   * 100
   */

  @Test
  void setHash() {

    redisTemplate.opsForHash().put("product", "name", "상품 1");
    redisTemplate.opsForHash().put("product", "price", "50000");
    redisTemplate.opsForHash().put("product", "quantity", "30");

    Map<String, String> map = redisTemplate.opsForHash().entries("product");
    assertThat(map).containsEntry("name", "상품 1");
    assertThat(map).containsEntry("price", "50000");
    assertThat(map).containsEntry("quantity", "30");
  }

  @Test
  @DisplayName("객체를 String에 저장")
  void setStringObject() {

    Product product1 = new Product("Monitor", 120, 15);
    redisTemplate.opsForValue().set("product:1", product1);

    Product product2 = new Product("Laptop", 1500, 10);
    redisTemplate.opsForValue().set("product:2", product2);

    Product ret1 = (Product) redisTemplate.opsForValue().get("product:1");
    Product ret2 = (Product) redisTemplate.opsForValue().get("product:2");

    assertThat(ret1).isEqualTo(product1);
    assertThat(ret2).isEqualTo(product2);
  }
  /**
   * 127.0.0.1:6379> get product:1
   * {"@class":"com.juny.jspboardwithmybatis.redis.RedisTemplateTest$Product","name":"Monitor","price":120,"quantity":15}
   *
   * 127.0.0.1:6379> get product:2
   * {"@class":"com.juny.jspboardwithmybatis.redis.RedisTemplateTest$Product","name":"Laptop","price":1500,"quantity":10}
   */

  public static class Product {
    private String name;
    private int price;
    private int quantity;

    public Product(
        @JsonProperty("name") String name,
        @JsonProperty("price") int price,
        @JsonProperty("quantity") int quantity) {
      this.name = name;
      this.price = price;
      this.quantity = quantity;
    }

    public Product() {}

    public String getName() {
      return name;
    }

    public int getPrice() {
      return price;
    }

    public int getQuantity() {
      return quantity;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Product product = (Product) o;
      return price == product.price &&
        quantity == product.quantity &&
        Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, price, quantity);
    }
  }
}
