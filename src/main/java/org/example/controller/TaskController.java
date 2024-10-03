package org.example.controller;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;
import org.example.application.Task;
import org.example.application.TaskListOperations;

import java.util.Date;
import java.util.List;

public class TaskController {

    TaskListOperations taskList = new TaskListOperations();

    public void unCompletedTaskPage(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        response.putHeader("content-type", "text/html");

        routingContext.put("tasks", this.taskList.unCheckedTasks());

        String templateFileName =(request.getHeader("HX-Request") != null) ? "unCompletedTasks::unCompletedTask" : "unCompletedTasks";

        engine.render(routingContext.data(), templateFileName, res -> {
            if (res.succeeded()) {
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void getAllCompletedTasks(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();

        response.putHeader("content-type", "text/html");

        routingContext.put("tasks", this.taskList.checkedTasks());
        String templateFileName = (request.getHeader("HX-Request") != null) ? "completedTasks::completedTask" : "completedTasks";

        engine.render(routingContext.data(), templateFileName, res -> {
            if (res.succeeded()) {
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void openCreateTaskModal(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();

        routingContext.put("dueDate", taskList.convertDateToString(new Date()));

        engine.render(routingContext.data(), "components/modalCreateTask", res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void addNewTask(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();

        String taskName = request.getParam("new_task");
        String taskDescription = request.getParam("taskDescription");
        String stringDate = request.getParam("dueDate");

        Integer statusCode = this.taskList.addTask(taskName, taskDescription, stringDate);
        response.setStatusCode(statusCode);

        String currentURL = request.getHeader("HX-Current-Url");

        if (currentURL.contains("/completed-tasks")) {
            engine.render(routingContext.data(), "fragments/SuccessAlertFragment::successAlert", res -> {
                if (res.succeeded()) {
                    System.out.println("success----response : "+res.result());
                    response.end(res.result());
                } else {
                    System.out.println("failed");
                }
            });
        } else {
            response.putHeader("HX-Refresh", Boolean.TRUE.toString()).end();
        }
    }

    public void deleteTask(RoutingContext routingContext) {
        System.out.println("deleting task");

        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();
        int taskID = Integer.parseInt(request.getParam("taskID"));

        this.taskList.deleteTask(taskID);

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
        String taskDescription = request.getParam("taskDescription");
        String stringDate = request.getParam("dueDate");

        Integer statusCode = this.taskList.editTask(taskId, taskName, taskDescription, stringDate);
        response.setStatusCode(statusCode);

        response.putHeader("HX-Refresh", Boolean.TRUE.toString());
        response.end();
    }

    public void openEditTaskModal(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerRequest request = routingContext.request();
        HttpServerResponse response = routingContext.response();

        int taskID = Integer.parseInt(request.getParam("taskID"));

        Task taskToBeEdit = this.taskList.getTaskToBeEdit(taskID);
        routingContext.put("task", taskToBeEdit);
        routingContext.put("dueDate", taskList.convertDateToString(taskToBeEdit.taskDueDate));

        engine.render(routingContext.data(), "components/EditTaskForm", res -> {
            if (res.succeeded()) {
                System.out.println("success");
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }

    public void sortedTasks(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        HttpServerResponse response = routingContext.response();
        HttpServerRequest request = routingContext.request();
        response.putHeader("content-type", "text/html");

        String clickCounts = request.getParam("count");
        String currentURL = request.getHeader("HX-Current-Url");

        String templateFileName = "unCompletedTasks::unCompletedTask";
        List<Task> allTasks = this.taskList.unCheckedTasks();

        if (currentURL.contains("/completed-tasks")) {
            allTasks = this.taskList.checkedTasks();
            templateFileName = "completedTasks::completedTask";
        }

        routingContext.put("tasks", this.taskList.getSortedTasks(allTasks, Integer.parseInt(clickCounts)));

        engine.render(routingContext.data(), templateFileName, res -> {
            if (res.succeeded()) {
                response.end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
