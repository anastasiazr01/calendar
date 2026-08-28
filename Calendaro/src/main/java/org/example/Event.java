package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private LocalDate date;
    private LocalTime time;
    private String title;
    private String description;

    //constructor WITH time
    public Event(LocalDate date, String title, String description, LocalTime time) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.time=time;
    }

    //constructor WITHOUT time (for appointment)
    public Event(LocalDate date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }


    // Getter for time
    public LocalTime getTime() {
        return time;
    }

    // Setter for time
    public void setTime(LocalTime time) {
        this.time = time;
    }

    // Getter for date
    public LocalDate getDate() {
        return date;
    }

    // Setter for date
    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Getter for title
    public String getTitle() {
        return title;
    }

    // Setter for title
    public void setTitle(String title) {
        this.title = title;
    }

    // Getter for description
    public String getDescription() {
        return description;
    }

    // Setter for description
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "title=" + title +
                ", ~Date=" + date ;
//                + '\'' +
//                ", time='" + time + '\'' +
//                ", description='" + description + '\'' ;
    }
}
