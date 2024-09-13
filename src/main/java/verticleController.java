import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

import java.util.Map;

public class verticleController extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.get("/").handler(this::getHomePage);

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router).listen(8080, "localhost", res->{
            if (res.succeeded()) {
                System.out.println("HTTP server started on port 8080");
            } else {
                System.out.println("HTTP server failed to start");
            }
        });
    }

    public void getHomePage(RoutingContext routingContext) {
        final ThymeleafTemplateEngine engine = ThymeleafTemplateEngine.create(vertx);

        Task doingHomeWorkTask = new Task("doing HomeWork");

        TaskList taskList = new TaskList();
        taskList.addTask(doingHomeWorkTask);

        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/html");

        JsonObject jsonObject = new JsonObject().put("tasks", taskList.getAllTasks());

        engine.render(jsonObject, "src/main/resources/HomePage.html").onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                System.out.println("failed");
                res.cause().printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new verticleController());

    }
    
}