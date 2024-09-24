import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.stream.Collectors;

public class controllerMoethods {

    TaskList taskList = new TaskList();

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

}
