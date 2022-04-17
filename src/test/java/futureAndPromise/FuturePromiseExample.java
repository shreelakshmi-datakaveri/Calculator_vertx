package futureAndPromise;


import com.shree.calculator.vertx_starter.MainVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ExtendWith annotation form JUnit, we can enhance the test with Vertx
 * functionality
 * When we do this we will have access to the Vertx instance when the Test is
 * executed and this helps us a lot when executing asynchronous code
 */
@ExtendWith(VertxExtension.class)
public class FuturePromiseExample {
  private static final Logger LOG = LoggerFactory.getLogger(FuturePromiseExample.class);


  /**
   * with annotations
   * @param vertx
   * @param vertxTestContext
   * we have access to Vertx eventloops and vertx eventbus
   */

  @Test
  void promise_success(Vertx vertx, VertxTestContext vertxTestContext)
  {
    final Promise<String> promise = Promise.promise();
    LOG.info("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.info("Success");
//      complete the vertx test context when we are in the unit test
      vertxTestContext.completeNow();
    });
    LOG.info("End");

  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext vertxTestContext)
  {
    final Promise<String> promise = Promise.promise();
    LOG.info("Start");
    vertx.setTimer(500, id-> {
//      instead of promise.complete() use promise.fail
      promise.fail(new RuntimeException("Failed Message"));
      LOG.info("Failed");
//    Using  vertxTestContext.completeNow(); makes our test to succeed but
//     our promise will fail
      vertxTestContext.completeNow();
    });
    LOG.info("End");
  }

  @Test
  void future_success(Vertx vertx, VertxTestContext vertxTestContext)
  {
    final Promise<String> promise = Promise.promise();
    final Future<String> future = promise.future();
    LOG.info("Start");
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOG.info("Success");
//      complete the vertx test context when we are in the unit test

    });
    future
      .onSuccess(result -> {
      LOG.info(" Result :  {} ", result);
      vertxTestContext.completeNow();
    })
      .onFailure(vertxTestContext :: failNow)
    ;

  }
  @Test
  void future_failure(Vertx vertx, VertxTestContext vertxTestContext)
  {
    final Promise<String> promise = Promise.promise();
    final Future<String> future = promise.future();
    LOG.info("Start");
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOG.info("Success");
//      complete the vertx test context when we are in the unit test

    });
    future
      .onSuccess(result -> {
        LOG.info(" Result :  {} ", result);
        vertxTestContext.completeNow();
      })
//      Fails the Junit Test
//      .onFailure(vertxTestContext :: failNow)
//    LOG the error instead of failing the Junit Test
      .onFailure(error -> {
        LOG.error("Result : ", error);
        vertxTestContext.completeNow();
      })
    ;

  }
}
