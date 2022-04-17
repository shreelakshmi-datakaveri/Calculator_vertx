package com.shree.calculator.vertx_starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
  public static final String ADD_ADDRESS = "com.calculator.add";
  public static final String SUBTRACT_ADDRESS = "com.calculator.subtract";
  public static final String MULTIPLY_ADDRESS = "com.calculator.multiply";
  public static final String DIVIDE_ADDRESS = "com.calculator.divide";
  public final Router router = Router.router(vertx);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.exceptionHandler(error->
    {
      LOG.error("Unhandled : ", error);
    });
    vertx.deployVerticle(new MainVerticle(), asynchronousResult -> {
      if(asynchronousResult.failed())
      {
        LOG.error("Failed to deploy : {} ", asynchronousResult.cause());
        return;
      }
      LOG.info("Deployed {} ", MainVerticle.class);
    });
    vertx.deployVerticle(new OutputVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {


//    router.route("/add").handler(BodyHandler.create());
//    GET Endpoint is recieved by the router
    router.get("/add/:num1/:num2").handler(this :: add);
    router.get("/sub/:num1/:num2").handler(this :: sub);
    router.get("/mul/:num1/:num2").handler(this :: mul);
    router.get("/div/:num1/:num2").handler(this :: div);

//    use the Router and attach it to the HTTP Server
    vertx.createHttpServer().requestHandler(router)
      .exceptionHandler(error -> {
        LOG.error("HTTP Server Error : ", error);
      })
      .listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }



  private void div(RoutingContext routingContext) {
//    call the event bus from here
//      final JsonObject response_json = new JsonObject();
//      response_json.put("num1", routingContext.request().getParam("num1"));
//      response_json.put("num2", routingContext.request().getParam("num2"));
//      routingContext.response().end("Dividing...");
//      routingContext.response_json().end(response_json.toBuffer());
    String num1 = routingContext.pathParam("num1");
    String num2 = routingContext.pathParam("num2");
    String message = num1 + " " + num2;
    vertx.eventBus().request(DIVIDE_ADDRESS, message, reply -> {
      routingContext.request().response().end((String) reply.result().body());
    });

  }

  private void mul(RoutingContext routingContext) {
//    routingContext.response().end("Multiplying...");
    String num1 = routingContext.pathParam("num1");
    String num2 = routingContext.pathParam("num2");
    String message = num1 + " " + num2;
    vertx.eventBus().request(MULTIPLY_ADDRESS, message, reply -> {
      routingContext.request().response().end((String) reply.result().body());
    });
  }

  private void sub(RoutingContext routingContext) {
//    routingContext.response().end("Subtracting...");
    String num1 = routingContext.pathParam("num1");
    String num2 = routingContext.pathParam("num2");
    String message = num1 + " " + num2;
    vertx.eventBus().request(SUBTRACT_ADDRESS, message, reply -> {
      routingContext.request().response().end((String) reply.result().body());
    });

  }

  private void add(RoutingContext routingContext) {
//    routingContext.response().end("Adding...");
//    vertx.eventBus().request(ADD_ADDRESS,routingContext);
//    de facto

//    var   jsonRoutingContextMessage = routingContext.toString();
//    vertx.eventBus().request(ADD_ADDRESS,jsonRoutingContextMessage, reply -> {
//      routingContext.request().response().end((String)reply.result().body());
//    });
//  fSystem.out.println(json_routingContext.toString()); // --> GIVES NULL
//    var message = routingContext.getBodyAsString(); -> returns NULL
//  var json = routingContext.getBodyAsJson();
//    var json = new JsonObject();
//    json.put("val0", num1);
//    json.put("val1", num2);
//    System.out.println("JSON Body as JSON {}" + (json));
    String num1 = routingContext.pathParam("num1");
    String num2 = routingContext.pathParam("num2");

    String message = num1 + " " + num2;
    vertx.eventBus().request(ADD_ADDRESS,message, reply -> {
      routingContext.request().response().end((String)reply.result().body());
    });
//    vertx.eventBus().request(ADD_ADDRESS,json, reply -> {
//      routingContext.request().response().end((String)reply.result().body());
//    });
  }
}
