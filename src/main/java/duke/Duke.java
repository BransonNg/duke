package duke;

import task.Task;
import java.util.Scanner;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Duke {
    private Scanner sc;
    private ArrayList<Task> tasks;
    public static final String separator =
            "____________________________________________________________";

    private final int indent1 = 4;
    private final int indent2 = 5;
    private final int indent3 = 7;

    public static void out(String in, int indent) {
        System.out.println(" ".repeat(indent) + in);
    }

    public static void main(String[] args) {
        String logo = " ____        _        \n" + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n" + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo);
        System.out.println("How may duke serve the master today?\n");
        Duke bot = new Duke();
        bot.start();
    }

    public Duke() {
        this.sc = new Scanner(System.in);
        this.tasks = new ArrayList<>();
    }

    public void start() {
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            System.out.println(input);
            Duke.out(Duke.separator, indent1);
            dispatch(input);
            Duke.out(Duke.separator, indent1);
            System.out.println();
            if (input.equals("bye")) {
                this.sc.close();
                break;
            }
        }
    }

    private void dispatch(String input) {
        switch (input) {
            case "list":
                int tasksLength = this.tasks.size();
                Duke.out("Here are the tasks in your list:", indent2);
                for (int i = 0; i < tasksLength; i++) {
                    Duke.out(String.format("%d.%s", i + 1, this.tasks.get(i)), indent2);
                }
                return;
            case "bye":
                Duke.out("Bye. Hope to see you again soon!", indent2);
                return;
            default:
                if (Pattern.matches("^done\\s+\\d$", input)) {
                    int doneInd = Integer.parseInt((input.split(" "))[1]) - 1;
                    if (doneInd >= this.tasks.size()) {
                        Duke.out(String.format(
                                "Please choose an index that is lesser or equal to the length of the list: %d",
                                this.tasks.size()), indent2);
                        return;
                    }
                    Task currTask = this.tasks.get(doneInd);
                    currTask.setDone();
                    Duke.out("Nice! I've marked this task as done:", indent2);
                    Duke.out(currTask.toString(), indent3);
                    return;
                } else if (Task.isTask(input)) {
                    try {
                        Task newTask = Task.newTask(input);
                        this.tasks.add(newTask);
                        Duke.out("Got it. I've added this task: ", indent2);
                        Duke.out(newTask.toString(), indent3);
                        Duke.out(String.format("Now you have %d %s in the list.", this.tasks.size(),
                                this.tasks.size() > 1 ? "tasks" : "task"), indent2);
                        return;
                    } catch (IllegalArgumentException e) {
                        Duke.out("Please provide a valid input!", indent2);
                        return;
                    }
                } else {
                    Duke.out("Please provide a valid input!", indent2);
                }
        }
    }
}
