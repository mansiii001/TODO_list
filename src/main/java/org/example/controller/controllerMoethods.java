package org.example.controller;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.example.application.TaskListOperations;

public class controllerMoethods {

    TaskListOperations taskList = new TaskListOperations();

    public void unCompletedTaskPage(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        response.putHeader("content-type", "text/html");

        routingContext.put("tasks", this.taskList.unCheckedTasks());

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

        routingContext.put("tasks", this.taskList.checkedTasks());

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

    public void openCreateTaskModal(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();

        engine.render(routingContext.data(), "components/modalCreateTask", res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void addNewTask(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();

        String taskName = request.getParam("new_task");

        try {
            this.taskList.addTask(taskName);
        } catch (Exception e) {
            response.setStatusCode(400).end(e.getMessage());
        }

        response.putHeader("HX-Refresh", Boolean.TRUE.toString());
        response.end();
    }

    public void deleteTask(RoutingContext routingContext) {
        System.out.println("deleting task");

        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();
        int taskID = Integer.parseInt(request.getParam("taskID"));

        this.taskList.deleteTask(taskID);

        response.putHeader("HX-Refresh", Boolean.TRUE.toString());
        response.end();
    }

    public void markTaskCompleted(RoutingContext routingContext) {
        System.out.println("mark task completed");
        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();
        int taskId = Integer.parseInt(request.getParam("taskId"));

        this.taskList.toggleCompleteCheckbox(taskId);

        response.putHeader("HX-Refresh", Boolean.TRUE.toString());
        response.end();
    }

    public void editTask(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        int taskId = Integer.parseInt(request.getParam("task_id"));
        String taskName = request.getParam("new_task");

        try{
            this.taskList.editTask(taskId, taskName);
        } catch (Exception e) {
            response.setStatusCode(400).end(e.getMessage());
        }

        response.putHeader("HX-Refresh", Boolean.TRUE.toString());
        response.end();
    }

    public void openEditTaskModal(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();

        int taskID = Integer.parseInt(request.getParam("taskID"));

        routingContext.put("task", this.taskList.getTaskToBeEdit(taskID));

        engine.render(routingContext.data(), "components/EditTaskForm", res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
