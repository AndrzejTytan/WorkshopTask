package pl.danielw;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private String taskName;
    private LocalDate taskDueDate;
    private boolean taskIsImportant;
    Task(String taskName, LocalDate taskDueDate, boolean taskIsImportant) {
        this.taskName = taskName;
        this.taskDueDate = taskDueDate;
        this.taskIsImportant = taskIsImportant;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public LocalDate getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(LocalDate taskDueDate) {
        this.taskDueDate = taskDueDate;
    }
    public boolean getImportance() {
        return taskIsImportant;
    }

    public void setImportance(boolean important) {
        this.taskIsImportant = important;
    }
}
