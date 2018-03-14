package cz.jkapoun.todo.views;

import com.vaadin.spring.annotation.UIScope;
import cz.jkapoun.todo.model.Task;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.ComponentRenderer;
import cz.jkapoun.todo.presenters.TodoPresenter;
import java.util.Collection;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * View class of the main application UI.
 * Manages the Vaadin components shown to the user. Also provides the way for
 * the presenter to subscribe to UI events (button clicks in this case).
 * 
 * @author Jiří Kapoun <jiri.kapoun@profinit.eu>
 * @see    TodoPresenter
 */
@Component
@UIScope
public class TodoView extends CustomComponent {

  protected VerticalLayout   layout;
  protected TextField        newTaskField;
  protected Button           addTaskButton;
  protected Grid<Task>       taskGrid;

  protected Consumer<String> addTaskHandler;
  protected Consumer<Task>   deleteTaskHandler;

  @Autowired
  public TodoView() {
    layout        = new VerticalLayout();
    newTaskField  = new TextField();
    addTaskButton = new Button();
    taskGrid      = new Grid<>();

    initializeLayout();
  }

  /**
   * Sets what tasks will be shown in the task list.
   */
  public void setTasks(Collection<Task> tasks) {
    taskGrid.setItems(tasks);
  }

  /**
   * Enables the presenter to get notified on "Add task" button click.
   */
  public void setAddTaskHandler(Consumer<String> handler) {
    addTaskHandler = handler;
  }

  /**
   * Enables the presenter to get notified on "Delete task" button click.
   */
  public void setDeleteTaskHandler(Consumer<Task> handler) {
    deleteTaskHandler = handler;
  }

  /**
   * Should be called by presenter after handling the "Add task" event.
   */
  public void afterTaskAdded() {
    taskGrid.getDataProvider().refreshAll();
    newTaskField.clear();
    Notification.show("Task added", Notification.Type.TRAY_NOTIFICATION);
  }

  /**
   * Should be called by presenter after handling the "Delete task" event.
   */
  public void afterTaskDeleted() {
    taskGrid.getDataProvider().refreshAll();
    Notification.show("Task deleted", Notification.Type.TRAY_NOTIFICATION);
  }

  protected void initializeLayout() {
    newTaskField.setPlaceholder("Enter new task here");
    
    addTaskButton.setCaption("Add");
    addTaskButton.addClickListener(event -> onAddTask());

    taskGrid.setCaption("Tasks");
    taskGrid.addColumn(Task::getId)
            .setCaption("ID");
    taskGrid.addColumn(Task::getText)
            .setCaption("Text");
    taskGrid.addColumn(task -> new Button("×", event -> onDeleteTask(task)), new ComponentRenderer())
            .setCaption("Delete");

    layout.addComponent(newTaskField);
    layout.addComponent(addTaskButton);
    layout.addComponent(taskGrid);
    setCompositionRoot(layout);
  }

  protected void onAddTask() {
    if (addTaskHandler != null)
      addTaskHandler.accept(newTaskField.getValue());
  }

  protected void onDeleteTask(Task task) {
    if (deleteTaskHandler != null)
      deleteTaskHandler.accept(task);
  }

}
