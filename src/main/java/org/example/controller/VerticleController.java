package org.example.controller;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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

        controllerMoethods controllerMoethods = new controllerMoethods();

        router.get("/").handler(routingContext -> controllerMoethods.unCompletedTaskPage(routingContext, engine));
        router.get("/completed_tasks").handler(routingContext -> controllerMoethods.getAllCompletedTasks(routingContext, engine));
        router.get("/uncompleted_tasks").handler(routingContext -> controllerMoethods.unCompletedTaskPage(routingContext, engine));

        router.get("/save_task").handler(routingContext -> controllerMoethods.saveTask(routingContext));
        router.get("/delete").handler(routingContext -> controllerMoethods.deleteTask(routingContext));
        router.get("/openCreatEditModal").handler(routingContext -> controllerMoethods.openCreateEditTaskModal(routingContext, engine));
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
}