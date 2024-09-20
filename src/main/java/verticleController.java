import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

public class verticleController extends AbstractVerticle {
    TaskList taskList = new TaskList();

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route("/static/*").handler(StaticHandler.create());

        router.get("/").handler(this::getHomePage);
        router.get("/create_edit_task").handler(this::createEditTask);
        router.get("/delete").handler(this::deleteTask);
        router.get("/openCreatEditModal").handler(this::openCreateEditTaskModal);
        router.get("/markCompleted").handler(this::markTaskCompleted);

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router).listen(8080, "localhost", res->{
            if (res.succeeded()) {
                System.out.println("HTTP server started on port 8080");
            } else {
                System.out.println("HTTP server failed to start");
            }
        });
    }

    private void markTaskCompleted(RoutingContext routingContext) {
        System.out.println("mark task completed");
        HttpServerRequest request = routingContext.request();
        int taskId = Integer.parseInt(request.getParam("taskId"));
        Boolean isCompleted = this.taskList.getAllTasks().get(taskId).isCompleted;

        System.out.println("-before-----is completed : "+isCompleted);

        this.taskList.toggleCompleteCheckbox(taskId, isCompleted);

        System.out.println("----after------- is completed : "+this.taskList.getAllTasks().get(taskId).isCompleted);

        HttpServerResponse response = routingContext.response();
        response.putHeader("HX-Redirect","/");
        response.end();
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

    public void openCreateEditTaskModal(RoutingContext routingContext) {
        final ThymeleafTemplateEngine engine = ThymeleafTemplateEngine.create(vertx);

        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();

        String taskID = request.getParam("taskID");

        if(taskID != null) {
            try {
                MySQLConnect mySQLConnect = new MySQLConnect();
                String task = mySQLConnect.findTask(Integer.parseInt(taskID));
                routingContext.put("task", task);
                routingContext.put("taskID", taskID);
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        engine.render(routingContext.data(), "src/main/resources/modalCreateTask.html").onComplete(res -> {
            if (res.succeeded()) {
                response.putHeader("content-type", "text/html");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void createEditTask(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String taskId = request.getParam("task_id");
        String taskName = request.getParam("new_task");

        if (taskId.isEmpty()) {
            this.taskList.addTask(taskName);
        } else {
            this.taskList.editTask(Integer.parseInt(taskId), taskName);
        }

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