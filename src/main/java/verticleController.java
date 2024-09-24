import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


public class verticleController extends AbstractVerticle {

    private ThymeleafTemplateEngine engine;
    TaskList taskList = new TaskList();

    public verticleController() {
        this.engine = ThymeleafTemplateEngine.create(vertx);
        configureThymeleafEngine(engine);
    }

    private void configureThymeleafEngine(ThymeleafTemplateEngine engine) {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        engine.getThymeleafTemplateEngine().setTemplateResolver(templateResolver);
    }

    @Override
    public void start() throws Exception {
        Router router = Router.router(vertx);

        router.route("/static/*").handler(StaticHandler.create());

        controllerMoethods controllerMoethods = new controllerMoethods();

        router.get("/").handler(routingContext -> getHomePage(routingContext, engine));
        router.get("/create_edit_task").handler(routingContext -> controllerMoethods.createEditTask(routingContext));
        router.get("/delete").handler(routingContext -> controllerMoethods.deleteTask(routingContext));
        router.get("/openCreatEditModal").handler(routingContext -> openCreateEditTaskModal(routingContext, engine));
        router.get("/markCompleted").handler(routingContext -> controllerMoethods.markTaskCompleted(routingContext));

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router).listen(8080, "localhost", res->{
            if (res.succeeded()) {
                System.out.println("HTTP server started on port 8080");
            } else {
                System.out.println("HTTP server failed to start");
            }
        });
    }

    static void redirectHomePage(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("HX-Redirect","/");
        response.end();
    }

    public void getHomePage(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "text/html");

        routingContext.put("uncheckedTasks", this.taskList.allUnCkecked());
        routingContext.put("checkedTasks", this.taskList.allCkecked());

        engine.render(routingContext.data(), "HomePage").onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                System.out.println("failed");
                res.cause().printStackTrace();
            }
        });
    }

    public void openCreateEditTaskModal(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();

        String taskID = request.getParam("taskID");

        if (taskID != null) {
            String taskToBeEdit = this.taskList.getTaskToBeEdit(Integer.parseInt(taskID));
            routingContext.put("task", taskToBeEdit);
            routingContext.put("taskID", taskID);
        }

        engine.render(routingContext.data(), "modalCreateTask").onComplete(res -> {
            if (res.succeeded()) {
                response.putHeader("content-type", "text/html");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}