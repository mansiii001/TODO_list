import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

public class verticleController extends AbstractVerticle {
    TaskList taskList = new TaskList();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route("/static/*").handler(StaticHandler.create());

        router.get("/").handler(this::getHomePage);
        router.post("/create_new_task").handler(BodyHandler.create()).handler(this::createNewTask);
        router.post("/delete").handler(BodyHandler.create()).handler(this::deleteTask);

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

        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/html");

        routingContext.put("tasks", this.taskList.getAllTasks());

        engine.render(routingContext.data(), "src/main/resources/HomePage.html").onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                System.out.println("failed");
                res.cause().printStackTrace();
            }
        });
    }

    public void createNewTask(RoutingContext routingContext) {
        String string = routingContext.getBody().toString();
        String[] requestParams = string.split("=");
        String taskName = requestParams[1];
        this.taskList.addTask(taskName);

        HttpServerResponse response = routingContext.response();
        response.putHeader("HX-Redirect","/");
        response.end();
    }

    private void deleteTask(RoutingContext routingContext) {
        System.out.println("deleting task");

        HttpServerRequest request = routingContext.request();
        int taskID = Integer.parseInt(request.getParam("taskID"));

        this.taskList.deleteTask(taskID);

        HttpServerResponse response = routingContext.response();
        response.putHeader("HX-Redirect","/");
        response.end();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new verticleController());

    }
    
}