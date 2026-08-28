package gr.hua.dit.oop2;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.validate.ValidationException;
import org.slf4j.Logger;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

//import gr.hua.dit.oop2.calendar.TimeService;
//import gr.hua.dit.oop2.calendar.TimeTeller;



import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.Content;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VToDo;
import java.io.FileOutputStream;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import static gr.hua.dit.oop2.CalendarApp.icsFilePath;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Comparator;
import java.util.Collections;



public class EventOptions {

    public static ArrayList<Event> AllEvents = new ArrayList<>();

    public static ArrayList<Appointment> AppointmentEvents = new ArrayList<>();

    public static ArrayList<Task> TaskEvents = new ArrayList<>();
    public static ArrayList<Event> FutureEvents = new ArrayList<>();
    public static ArrayList<Event> PastEvents = new ArrayList<>();
    public static ArrayList<Event> DeadlineNotPassedEvents = new ArrayList<>();
    public static ArrayList<Event> DeadlinePassedEvents = new ArrayList<>();

    Logger logger;

    //Adding an event
    public static Event AddEvent(String icsFilePath){
        System.out.println("What type of event would you like to add? Enter 'Appointment' or 'Task'.");
        Scanner scanner = new Scanner(System.in);
        String userInputType = scanner.nextLine();

        Event event = null; //Declare a variable to hold the created event

        //Checking if user wants to add an Appointment or Task event
        if ("appointment".equalsIgnoreCase(userInputType)){
            System.out.println("Enter Appointment's name.");
            String eventName = scanner.nextLine();

            System.out.println("Enter Appointment's description.");
            String eventDescription = scanner.nextLine();

            System.out.println("Enter Appointment's date (format: DD-MM-YYYY).");
            String eventDate = scanner.nextLine();

            System.out.println("Enter Appointment's time (format: HH:mm).");
            String eventTime = scanner.nextLine();

            System.out.println("Enter Appointment's duration.");
            String eventDuration = scanner.nextLine();

            Appointment appointment = new Appointment(eventDate, eventName, eventDescription, eventTime, eventDuration);
            event = appointment; // Assign the created appointment to the event variable
            AppointmentEvents.add(appointment);
            AllEvents.add(appointment);

            // Add code to write the appointment details to the ICS file
            try (PrintStream printStream = new PrintStream(new FileOutputStream(icsFilePath, true))) {
                printStream.println("BEGIN:VEVENT");
                printStream.println("SUMMARY:" + appointment.getTitle());
                printStream.println("DESCRIPTION:" + appointment.getDescription());
                printStream.println("DTSTART:" + appointment.getDate() + "T" + appointment.getStartingTime());
                printStream.println("DURATION:" + appointment.getDuration());
                printStream.println("END:VEVENT");

            } catch (Exception e) {
                e.printStackTrace();
            }
            // Sorting events after adding a new one
            sortEvents();
            System.out.println("Appointment added successfully!");
            return appointment;

        }else if ("task".equalsIgnoreCase(userInputType)){
            System.out.println("Enter Task's name.");
            String eventName = scanner.nextLine();

            System.out.println("Enter Task's description.");
            String eventDescription = scanner.nextLine();

            System.out.println("Enter Task's date (format: DD-MM-YYYY).");
            String eventDate = scanner.nextLine();

            System.out.println("Enter Task's time (format: HH:mm).");
            String eventTime = scanner.nextLine();

            System.out.println("Enter Task's deadline (format: HH:mm).");
            String eventDeadline = scanner.nextLine();

            //Setting Task as "not finished" by default
            Boolean isFinished = false;
            Boolean eventIsFinished = isFinished;

            Task task = new Task(eventDate, eventName, eventDescription, eventTime, eventDeadline, eventIsFinished);
            event = task; // Assign the created task to the event variable
            TaskEvents.add(task);
            AllEvents.add(task);

            // Add code to write the task details to the ICS file
            try (PrintStream printStream = new PrintStream(new FileOutputStream(icsFilePath, true))) {
                printStream.println("BEGIN:VTODO");
                printStream.println("SUMMARY:" + task.getTitle());
                printStream.println("DESCRIPTION:" + task.getDescription());
                printStream.println("DTSTART:" + task.getDate() + "T" + task.getTime());
                printStream.println("DUE:" + task.getDeadline());
                printStream.println("STATUS:" + (task.isFinished() ? "COMPLETED" : "NEEDS-ACTION"));
                printStream.println("END:VTODO");
            } catch (Exception e) {
                e.printStackTrace();
            }
            sortEvents();
            System.out.println("Task added successfully!");
            return task;

        }else{
            System.out.println("Invalid event type. Please enter 'appointment' or 'task'.");
            return null;
        }
    }

    //Calls specific method whether user wants to change Appointment's or Task's details
    public static void ChangeEvent(String eventTitle, String icsFilePath) {
        // Find the index of the event in the lists
        int appointmentIndex = findEventIndexByTitle(AppointmentEvents, eventTitle);
        int taskIndex = findEventIndexByTitle(TaskEvents, eventTitle);

        if (appointmentIndex != -1) {
            // Modify details of the Appointment event
            ChangeAppointment(AppointmentEvents.get(appointmentIndex),icsFilePath);
        } else if (taskIndex != -1) {
            // Modify details of the Task event
            ChangeTask(TaskEvents.get(taskIndex), icsFilePath);
        } else {
            System.out.println("Event not found.");
        }
    }

    //Changing an Appointment's details
    private static void ChangeAppointment(Appointment appointment, String icsFilePath) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("Which field would you like to update?");
            System.out.println("Enter: 1 (name), 2 (description), 3 (date), 4 (time), 5 (duration)");
            option = scanner.nextInt();

            if (option == 1) {
                System.out.println("Enter Appointment's new name.");
                String newName = scanner.nextLine();
                appointment.setTitle(newName);
            } else if (option == 2) {
                System.out.println("Enter Appointment's new description.");
                String newDescription = scanner.nextLine();
                appointment.setDescription(newDescription);
            } else if (option == 3) {
                System.out.println("Enter Appointment's new date (format: DD-MM-YYYY).");
                String newDate = scanner.nextLine();
                appointment.setDate(newDate);
            } else if (option == 4) {
                System.out.println("Enter Appointment's new time (format: HH:mm)");
                String newTime = scanner.nextLine();
                appointment.setTime(newTime);
            } else if (option == 5) {
                /* TODO prepei to Duration na einai int kai oxi string?!?!?!?! */
                System.out.println("Enter Appointment's new duration (in minutes).");
                String newDuration = scanner.nextLine();
                appointment.setDuration(newDuration);
            } else {
                System.out.println("Wrong input. Input must be between 1 and 5.");
                System.exit(1);
            }
        }while (option>=1 && option<=5);
        sortEvents();
        System.out.println("Appointment details changed successfully!");
    }

    //Changing a Task's details
    private static void ChangeTask(Task task, String icsFilePath) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("Which field would you like to update?");
            System.out.println("Enter: 1 (name), 2 (description), 3 (date), 4 (time), 5 (deadline)");
            option = scanner.nextInt();

            if (option==1) {
                System.out.println("Enter Task's new name.");
                String newName = scanner.nextLine();
                task.setTitle(newName);
            } else if (option==2) {
                System.out.println("Enter Task's new description.");
                String newDescription = scanner.nextLine();
                task.setDescription(newDescription);
            } else if (option==3) {
                System.out.println("Enter Task's new date (format: DD-MM-YYYY).");
                String newDate = scanner.nextLine();
                task.setDate(newDate);
            } else if (option==4) {
                System.out.println("Enter Task's new time (format: HH:mm)");
                String newTime = scanner.nextLine();
                task.setTime(newTime);
            } else if (option==5) {
                System.out.println("Enter Task's new deadline (format: DD-MM-YYYY).");
                String newDeadline = scanner.nextLine();
                task.setDeadline(newDeadline);
            }else {
                System.out.println("Wrong input. Input must be between 1 and 5.");
                System.exit(1);
            }
        }while(option>=1 && option<=5);
        sortEvents();
        System.out.println("Task details changed successfully!");
    }

    //Changing a Task's status
    public static void changeTaskStatus(String taskTitle, String icsFilePath) {
        int taskIndex = findEventIndexByTitle(TaskEvents, taskTitle);

        if (taskIndex != -1) { //task was found
            Task task = TaskEvents.get(taskIndex);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter Task's new status ('true' for finished, 'false' for unfinished).");
            boolean newStatus = scanner.nextBoolean();
            task.setFinished(newStatus);

            // Update the status in the ICS file
            writeTaskStatusToFile(task, icsFilePath);

            System.out.println("Task's status changed successfully!");
        } else {
            System.out.println("Task was not found.");
        }
    }


    private static void writeTaskStatusToFile(Task task, String icsFilePath) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(icsFilePath, true))) {
            // Find the existing event in the ICS file and update its status
            int taskIndexInFile = findEventIndexByTitleInFile(icsFilePath, task.getTitle());

            if (taskIndexInFile != -1) {
                // Rewind the file pointer to the beginning of the line
                RandomAccessFile raf = new RandomAccessFile(icsFilePath, "rw");
                raf.seek(taskIndexInFile);

                // Update the status in the ICS file
                raf.writeBytes("STATUS:" + (task.isFinished() ? "COMPLETED" : "NEEDS-ACTION"));

                // Close the RandomAccessFile
                raf.close();
            }
        } catch (Exception e) {
            System.out.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int findEventIndexByTitleInFile(String icsFilePath, String eventTitle) {
        try (BufferedReader reader = new BufferedReader(new FileReader(icsFilePath))) {
            String line;
            int lineIndex = 0;

            while ((line = reader.readLine()) != null) {
                if (line.contains("SUMMARY:" + eventTitle)) {
                    return lineIndex;
                }

                lineIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1; // Event was not found in the file
    }

    //Find event's index by title
    private static int findEventIndexByTitle(List<? extends Event> events, String eventTitle) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getTitle().equals(eventTitle)) {
                return i; // Event was found
            }
        }
        return -1; // Event was not found
    }

    public static int checkArgumentNumber(int arguments){
        int num=0;

        if(arguments<1) {
            System.out.println("not enough arguments");//logger.fatal("Please give 1 or 2 arguments.");
//            logger.info("Exiting..");
            System.exit(1);
        } else if(arguments>2) {
            System.out.println("more than enough arguments");//logger.fatal("Please give 1 or 2 arguments.");
            System.exit(1);
        } else if(arguments==1){
            System.out.println("that's 1 argument");
            num=0;
        }else if(arguments==2){
            System.out.println("that's 2 arguments");
            num=1;
        }
        return num;
    }

    public static String assignFirstArgumentType(String argument) {
        //case-insensitive check for argument ending with .txt
        if(argument.toLowerCase().endsWith(".txt") ){
            ////logger.debug("First Argument ends with '.txt'.");
            return "txt";
            //case-insensitive check for argument ending with .ics
        }else if(argument.toLowerCase().endsWith(".ics") ) {
            ////logger.debug("First Argument ends with '.ics'.");
            return "ics";
        }else { //incorrect input
            return "error";
        }
    }
    public static void ReadFile( String fileName){

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //de tha xreiastei
    //Displaying lists for Appointments, Tasks, and all the events combined.
    public static void printEventLists() {
        System.out.println("Appointment Events:");
        for (Appointment appointment : AppointmentEvents) {
            System.out.println(appointment);
        }

        System.out.println("\nTask Events:");
        for (Task task : TaskEvents) {
            System.out.println(task);
        }

        System.out.println("\nAll Events:");
        for (Event event : AllEvents) {
            System.out.println(event);
        }
    }
  /*  private static void writeICalComponentToFile(CalendarComponent component, String filePath) {
        Calendar calendar = new Calendar();
        calendar.getComponents().add(component);

        try (FileOutputStream fout = new FileOutputStream(filePath)) {
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.output(calendar, fout);
            System.out.println("Event written to " + filePath);
        } catch (IOException | ValidationException e) {
            e.printStackTrace();
        }
    }
*/

    public static void writeEventToFile(String filePath, String eventContent) {
        try (FileWriter writer = new FileWriter(filePath)) {
            String icsContent = "BEGIN:VCALENDAR\nVERSION:2.0\n" + eventContent + "\nEND:VCALENDAR";
            writer.write(icsContent);
            System.out.println("Event written to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Additional method to sort events
    private static void sortEvents() {
        // Sort AllEvents by date and time
        Collections.sort(AllEvents, new EventDateTimeComparator());

        // Sort AppointmentEvents by date and time
        Collections.sort(AppointmentEvents, new EventDateTimeComparator());

        // Sort TaskEvents by date and time
        Collections.sort(TaskEvents, new EventDateTimeComparator());
    }

    // Custom comparator for sorting events based on date and time
    private static class EventDateTimeComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {
            // Implement logic to compare events based on date and time
            // You may need to parse the date and time and compare them accordingly
            // For simplicity, let's assume events have a getDate() method returning a String

            String date1 = event1.getDate();
            String time1 = event1.getTime();
            String dateTime1 = date1 + " " + time1;

            String date2 = event2.getDate();
            String time2 = event2.getTime();
            String dateTime2 = date2 + " " + time2;

            // Assuming date and time are in the format "DD-MM-YYYY HH:mm"
            return dateTime1.compareTo(dateTime2);
        }
    }

}
