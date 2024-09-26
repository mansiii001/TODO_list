package org.example.controller;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.example.application.Task;
import org.example.application.TaskList;

import java.util.List;
import java.util.stream.Collectors;

public class controllerMoethods {

    TaskList taskList = new TaskList();

    public void unCompletedTaskPage(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        response.putHeader("content-type", "text/html");

        routingContext.put("tasks", this.taskList.allUnCkecked());

        String isHTMXCall = request.getHeader("HX-Request");

        String templateFileName = "unCompletedTasks";

        if(isHTMXCall != null) {
            templateFileName = "unCompletedTasks::unCompletedTask";
        }

        engine.render(routingContext.data(), templateFileName, res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                System.out.println("failed");
                res.cause().printStackTrace();
            }
        });
    }

    public void getAllCompletedTasks(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        response.putHeader("content-type", "text/html");

        routingContext.put("tasks", this.taskList.allCkecked());

        String isHTMXCall = request.getHeader("HX-Request");

        String templateFileName = "completedTasks";

        if(isHTMXCall != null) {
            templateFileName = "completedTasks::completedTask";
        }

        engine.render(routingContext.data(), templateFileName, res -> {
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

        engine.render(routingContext.data(), "components/modalCreateTask", res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void saveTask(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String taskId = request.getParam("task_id");
        String taskName = request.getParam("new_task");

        if(taskName.isEmpty()){
            routingContext.response().setStatusCode(400).end("empty task name");
            return;
        }

        if (taskId.isEmpty()) {
            this.taskList.addTask(taskName);
        } else {
            this.taskList.editTask(Integer.parseInt(taskId), taskName);
        }

        VerticleController.redirectHomePage(routingContext);
    }

    public void deleteTask(RoutingContext routingContext) {
        System.out.println("deleting task");

        HttpServerRequest request = routingContext.request();
        int taskID = Integer.parseInt(request.getParam("taskID"));

        this.taskList.deleteTask(taskID);

        VerticleController.redirectHomePage(routingContext);
    }

    public void markTaskCompleted(RoutingContext routingContext) {
        System.out.println("mark task completed");
        HttpServerRequest request = routingContext.request();
        int taskId = Integer.parseInt(request.getParam("taskId"));

        List<Task> allTasks = this.taskList.getAllTasks();
        Task filteredTask = allTasks.stream().filter(task -> task.id == taskId).collect(Collectors.toList()).get(0);
        boolean isCompleted = filteredTask.isDone;

        this.taskList.toggleCompleteCheckbox(taskId, isCompleted);

        VerticleController.redirectHomePage(routingContext);
    }

}
