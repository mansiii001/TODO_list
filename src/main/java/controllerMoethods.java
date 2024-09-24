import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.thymeleaf.ThymeleafTemplateEngine;

import java.util.List;
import java.util.stream.Collectors;

public class controllerMoethods {

    TaskList taskList = new TaskList();

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

    public void createEditTask(RoutingContext routingContext) {
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

        verticleController.redirectHomePage(routingContext);
    }

    public void deleteTask(RoutingContext routingContext) {
        System.out.println("deleting task");

        HttpServerRequest request = routingContext.request();
        int taskID = Integer.parseInt(request.getParam("taskID"));

        this.taskList.deleteTask(taskID);

        verticleController.redirectHomePage(routingContext);
    }

    public void markTaskCompleted(RoutingContext routingContext) {
        System.out.println("mark task completed");
        HttpServerRequest request = routingContext.request();
        int taskId = Integer.parseInt(request.getParam("taskId"));

        List<Task> allTasks = this.taskList.getAllTasks();
        Task filteredTask = allTasks.stream().filter(task -> task.id == taskId).collect(Collectors.toList()).get(0);
        boolean isCompleted = filteredTask.isDone;

        this.taskList.toggleCompleteCheckbox(taskId, isCompleted);

        verticleController.redirectHomePage(routingContext);
    }

    public void getAllCompletedTasks(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        routingContext.put("tasks", this.taskList.allCkecked());
        routingContext.put("canAddNew", false);
        renderTaskTable(routingContext, engine);
    }

    public void getAllUnCompletedTasks(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        routingContext.put("tasks", this.taskList.allUnCkecked());
        routingContext.put("canAddNew", true);
        renderTaskTable(routingContext, engine);
    }

    private static void renderTaskTable(RoutingContext routingContext, ThymeleafTemplateEngine engine) {
        engine.render(routingContext.data(), "TaskTable").onComplete(res -> {
            if (res.succeeded()) {
                System.out.println("success");
                routingContext.response().putHeader("content-type", "text/html").end(res.result());
            } else {
                res.cause().printStackTrace();
            }
        });
    }
}
