package task;

import exception.DukeException;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Task {
  protected String description;
  protected boolean isDone;
  protected String type;
  private static String[] taskTypes = {"todo", "event", "deadline"};

  public Task(String type, String description, String isDone) {
    this.type = type;
    this.description = description;
    this.isDone = isDone.equals("Y");
  }

  public Task(String type, String description) {
    this.type = type;
    this.description = description;
    this.isDone = false;
  }

  private static String getType(String words) throws DukeException {
    String acceptedTypes = String.format("(%s)", String.join("|", Task.taskTypes));
    if (Pattern.matches(String.format("^%s\\s+.*|.*\\s+%s$|.*\\s+%s\\s+.*", acceptedTypes,
        acceptedTypes, acceptedTypes), words)) {
      if (Pattern.matches(String.format("^%s\\s+.*", acceptedTypes), words)) {
        return words.split(" ")[0];
      } else {
        throw new DukeException("Please start with event type");
      }
    }
    throw new DukeException("No accepted types present");
  }

  public static Task newTask(String description) throws DukeException {
    String type = Task.getType(description);

    String typeLess = description.substring(type.length()).trim();

    switch (type) {
      case "todo":
        return new Todo(typeLess);
      case "event":
        return new Event(typeLess);
      case "deadline":
        return new Deadline(typeLess);
      default:
        throw new DukeException("Task not recognized");
    }
  }

  public static Task newTaskFromMemory(String entry) throws DukeException {
    String[] splitEntry = entry.split("\\|");
    String type = splitEntry[0];
    switch (type) {
      case "[T]":
        return new Todo(splitEntry[2], splitEntry[1]);
      case "[E]":
        return new Event(splitEntry[2], splitEntry[1], splitEntry[3]);
      case "[D]":
        return new Deadline(splitEntry[2], splitEntry[1], splitEntry[3]);
      default:
        throw new DukeException("Task not recognized");
    }
  }


  public static String getTime(String description, String regex) throws DukeException {
    Matcher matcher = Pattern.compile(regex).matcher(description);
    int index = matcher.find() ? matcher.start() : -1;
    if (index == -1) {
      throw new DukeException(String.format("Please provide %s for this event type", regex));
    }
    String time = description.substring(index + regex.length()).trim();
    if (time.length() == 0)
      throw new DukeException("Please provide a time");
    return time;
  }

  public static String getContent(String description) throws DukeException {
    Matcher matcher = Pattern.compile("(/by|/at)").matcher(description);
    int index = matcher.find() ? matcher.start() : -1;
    if (index == -1 && description.trim().length() > 0) {
      return description.trim();
    }
    String content = description.substring(0, index).trim();
    if (content.length() > 0)
      return content;
    throw new DukeException("Content cannot be empty!");
  }

  public String getStatusIcon() {
    return (this.isDone ? "Y" : "N"); // Couldn't view the ticks and crosses on my terminal
  }

  public void setDone() {
    this.isDone = true;
  }

  @Override
  public String toString() {
    return String.format("%s[%s] %s", this.type, this.getStatusIcon(), description);
  }

  public String toStorable() {
    return String.format("%s|%s|%s", this.type, this.isDone ? "Y" : "N", this.description);
  }
}
