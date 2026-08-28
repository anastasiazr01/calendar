package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task extends Event{

    private LocalDate deadline;
    private boolean isFinished;

    public Task(LocalDate date, String title, String description, LocalTime time, LocalDate deadline, boolean isFinished ) {
        super(date, title, description, time);
        this.deadline = deadline;
        this.isFinished = isFinished;
    }


    //Getter for deadline
    public LocalDate getDeadline() {
        return deadline;
    }

    //Setter for deadline
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }


    //Getter for isFinished (status)
    public boolean isFinished() {
        return isFinished;
    }

    //Setter for isFinished (status)
    public void setFinished(boolean finished) {
        isFinished = finished;
    }


    @Override
    public String toString() {
        return "~Task " +
                "title=" + getTitle() +
                ", ~Deadline=" + getDeadline() ;
//                super.toString();
//                +
//                ", deadline='" + deadline + '\'' +
//                ", isFinished=" + isFinished +
//                '}';
    }
}
