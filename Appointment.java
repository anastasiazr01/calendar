package gr.hua.dit.oop2;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Summary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.DtStart;

public class Appointment extends Event {

    private String startingTime;
    private String duration;

    //constructor WITHOUT time giati ETSI //VASIKA efoson exei startingTime de xreiazetai time
    public Appointment(String date, String title, String description, String startingTime, String duration ) {
        super(date, title, description);
        this.startingTime = startingTime;
        this.duration = duration;
    }

    // Getter for startingTime
    public String getStartingTime() {
        return startingTime;
    }

    // Setter for startingTime
    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
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
        return "Appointment{" +
                super.toString() +
                "startingTime='" + startingTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }



}
