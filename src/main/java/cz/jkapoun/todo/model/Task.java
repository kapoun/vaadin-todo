package cz.jkapoun.todo.model;

import cz.jkapoun.todo.services.TaskService;

/**
 * POJO representation of the created tasks.
 * Task objects are created and managed by the TaskService object. There is no
 * need to create them manually (using constructor).
 * 
 * @author Jiří Kapoun <jiri.kapoun@profinit.eu>
 * @see    TaskService
 */
public class Task {

  protected int    id;
  protected String text;

  public Task(int id, String text) {
    this.id   = id;
    this.text = text;
  }

  public int getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

}
