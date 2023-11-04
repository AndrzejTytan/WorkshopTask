package pl.danielw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TaskManager {
    public static Scanner userInput = new Scanner(System.in);
    public static File tasksFile = new File("src/main/resources/tasks.csv");

    public static void main(String[] args) {
        // @todo kolorki w konsoli
        // @todo do drukowania tasków - print z filtrami zamiast na poszczególne pola osobno
        ArrayList<Task> tasks = loadTasksFromCSV(tasksFile);
        String userInput = null;
        boolean continueProgram = true;

        while (continueProgram) {
            System.out.print(ConsoleColors.RESET);
            printOptions();
            userInput = getUserInput();
            switch (userInput) {
                case "add":
                    addTask(tasks);
                    break;
                case "remove":
                    removeTask(tasks);
                    break;
                case "lst":
                    printAllTasks(tasks);
                    break;
                case "lstimp":
                    printImportantTasks(tasks);
                    break;
                case "exit":
                    continueProgram = false;
                    break;
                default:
                    System.out.println();
            }
        }
        exitAndSave(tasks);
    }

    public static ArrayList<Task> loadTasksFromCSV(File file) {
        try {
            Scanner fileScanner = new Scanner(tasksFile);
            ArrayList<Task> tasks = new ArrayList<>();
            while (fileScanner.hasNext()) {
                String[] line = fileScanner.nextLine().split(",");
                String taskName = line[0].trim();
                LocalDate taskDueDate = LocalDate.parse(line[1].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                boolean taskIsImportant = Boolean.parseBoolean(line[2].trim());
                tasks.add(new Task(taskName, taskDueDate, taskIsImportant));
            }
            return tasks;
        } catch (FileNotFoundException e) {
            System.out.println("File " + file.getName() + " not found");
        }
        return null;
    }

    public static void printAllTasks(ArrayList<Task> tasks) {
        int taskCounter = 1;
        System.out.println("List of all tasks:");
        System.out.print(ConsoleColors.RED);
        for (Task t : tasks) {
            System.out.println("Task " + taskCounter + ": " + t.getTaskName());
            System.out.println("Do by: " + t.getTaskDueDate());
            System.out.println("Important?: " + t.getImportance());
            System.out.println();
            taskCounter++;
        }
        System.out.print(ConsoleColors.RESET);
    }

    public static void printImportantTasks(ArrayList<Task> tasks) {
        int taskCounter = 1;
        System.out.println("List of important tasks:");
        System.out.print(ConsoleColors.RED);
        for (Task t : tasks) {
            if (t.getImportance()) {
                System.out.println("Task " + taskCounter + ": " + t.getTaskName());
                System.out.println("Do by: " + t.getTaskDueDate());
                System.out.println("Important?: " + t.getImportance());
                System.out.println();
                taskCounter++;
            }
        }
        System.out.print(ConsoleColors.RESET);
    }

    public static void exitAndSave(ArrayList<Task> tasks) {
        System.out.println("Saving and exiting program");
        StringBuilder lineBuilder = new StringBuilder();
        try {
            // clear the file before writing
            Files.writeString(tasksFile.toPath(), "");
            for (Task task : tasks) {
                lineBuilder.append(task.getTaskName()).append(",");
                lineBuilder.append(task.getTaskDueDate()).append(",");
                lineBuilder.append(task.getImportance()).append("\n");
                Files.writeString(tasksFile.toPath(), lineBuilder, StandardOpenOption.APPEND);
                lineBuilder.setLength(0);
            }
        } catch (IOException e) {
            System.out.println("Error writing to or saving file!");
        }
    }

    public static void addTask(ArrayList<Task> tasks) {
        try {
            System.out.println("Adding task");
            System.out.print(ConsoleColors.GREEN_BOLD);
            System.out.print("Task name: ");
            String taskName = getUserInput();
            System.out.print("Task due date (YYYY-MM-DD): ");
            LocalDate taskDueDate = LocalDate.parse(getUserInput(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println("Important? (Y/N): ");
            System.out.print(ConsoleColors.RESET);
            String userInputTaskImportanceToParse = getUserInput();
            boolean taskImportance = false;
            switch (userInputTaskImportanceToParse.toUpperCase()) {
                case "Y":
                    taskImportance = true;
                    break;
                case "N":
                    taskImportance = false;
                    break;
                default:
                    throw new InvalidUserInputException();
            }
            tasks.add(new Task(taskName, taskDueDate, taskImportance));
        } catch (InputMismatchException e1) {
            System.out.println("Invalid input for task importance");
        } catch (DateTimeParseException e2) {
            System.out.println("Invalid date or date format");
        }
    }

    public static void removeTask(ArrayList<Task> tasks) {
        System.out.println("Removing task");
        System.out.print(ConsoleColors.GREEN_BOLD);
        System.out.print("Enter task number to remove: ");
        System.out.print(ConsoleColors.RESET);
        int removeTaskNo = Integer.parseInt(getUserInput());
        if (removeTaskNo <= 0 || removeTaskNo > tasks.size()) {
            System.out.println("Invalid task number.");
        } else {
            System.out.println("Removing task: " + tasks.get(removeTaskNo - 1).getTaskName());
            tasks.remove(removeTaskNo - 1);
        }
    }

    public static void printOptions() {
        System.out.print(ConsoleColors.GREEN_BOLD);
        System.out.println("Select Option:");
        System.out.print(ConsoleColors.RESET);
        System.out.println("add");
        System.out.println("remove");
        System.out.println("lst");
        System.out.println("lstimp");
        System.out.println("exit");
        System.out.println();
    }

    public static String getUserInput() {
        return userInput.nextLine();
    }
}