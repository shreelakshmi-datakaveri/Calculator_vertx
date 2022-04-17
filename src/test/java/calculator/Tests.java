package calculator;

import com.shree.calculator.vertx_starter.MainVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
public abstract class Tests implements Handler<RoutingContext> {
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @org.junit.jupiter.api.Test
  void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
    testContext.completeNow();
  }

  @Test
  public void canAdd(Vertx vertx, VertxTestContext vertxTestContext) throws Throwable
  {

    var client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
    client.get("/add/:2/:5")
//      .send() returns us a Future containing HTTP response object
//      .onComplete() handler gives us an asychronous result as return parameter
//      in context.succeeding handler we will verify our expectations
      .send()
      .onComplete(vertxTestContext.succeeding(
        response -> {
          var message = "";
//          var message = RoutingContext.request().response().toString();
          LOG.info("Response : {}", message);
          assertEquals("", message);
          assertEquals(200, response.statusCode());
        }
      ));

  }
}
