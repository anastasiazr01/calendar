package gr.hua.dit.oop2;

import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Summary;

public class Task extends Event{

    private String deadline;
    private boolean isFinished;

    //constructor WITH time  // de ftiaxnoume WITHOUT time giati ETSI
    public Task(String date, String title, String description, String time, String deadline, boolean isFinished ) {
        super(date, title, description, time);
        this.deadline = deadline;
        this.isFinished = isFinished;
    }


    //Getter for deadline
    public String getDeadline() {
        return deadline;
    }

    //Setter for deadline
    public void setDeadline(String deadline) {
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
        return "Task{" +
                super.toString() +
                "deadline='" + deadline + '\'' +
                ", isFinished=" + isFinished +
                '}';
    }


}
