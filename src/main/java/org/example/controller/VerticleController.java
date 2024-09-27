package org.example.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.example.CustomThymeleafTemplateEngineImpl;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


public class VerticleController extends AbstractVerticle {

    private ThymeleafTemplateEngine engine;

    public VerticleController() {
        this.engine = new CustomThymeleafTemplateEngineImpl(vertx);
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

        TaskController taskController = new TaskController();

        router.get("/").handler(routingContext -> taskController.unCompletedTaskPage(routingContext, engine));
        router.get("/completed-tasks").handler(routingContext -> taskController.getAllCompletedTasks(routingContext, engine));
        router.get("/uncompleted-tasks").handler(routingContext -> taskController.unCompletedTaskPage(routingContext, engine));

        router.get("/add-task").handler(routingContext -> taskController.addNewTask(routingContext, engine));
        router.get("/edit-task").handler(routingContext -> taskController.editTask(routingContext));
        router.delete("/delete").handler(routingContext -> taskController.deleteTask(routingContext));
        router.get("/openCreateTaskModal").handler(routingContext -> taskController.openCreateTaskModal(routingContext, engine));
        router.get("/openEditTaskModal").handler(routingContext -> taskController.openEditTaskModal(routingContext, engine));
        router.get("/markCompleted").handler(routingContext -> taskController.markTaskCompleted(routingContext));

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router).listen(8080, "localhost", res->{
            if (res.succeeded()) {
                System.out.println("HTTP server started on port 8080");
            } else {
                System.out.println("HTTP server failed to start");
            }
        });
    }

}