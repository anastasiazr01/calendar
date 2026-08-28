package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment extends Event {

    private String duration;

    public Appointment(LocalDate date, String title, String description, LocalTime time, String duration ) {
        super(date,title,description,time);
        this.duration = duration;
    }


    // Getter for duration
    public String getDuration() {
        return duration;
    }

    // Setter for duration
    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "~Appointment " +
                "title=" + getTitle() +
                ", ~Date=" + getDate() ;
//                + '\'' +
//                ", startingTime='" + getTime() + '\'' +
//                ", description='" + getDescription() + '\'' +
//                ", duration='" + duration + '\'' +
//                '}';
    }
}
