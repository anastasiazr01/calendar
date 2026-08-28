package org.example;

import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class EventOptions {

    //lists and models for Jlists initialized
    public static ArrayList<Event> AllEvents = new ArrayList<>();
    public static ArrayList<Appointment> AppointmentEvents = new ArrayList<>();
    public static ArrayList<Task> TaskEvents = new ArrayList<>();
    public static ArrayList<Event> FutureEvents = new ArrayList<>();
    public static ArrayList<Appointment> FutureAppointments = new ArrayList<>();
    public static ArrayList<Task> FutureTasks = new ArrayList<>();
    public static ArrayList<Event> PastEvents = new ArrayList<>();
    public static ArrayList<Event> DeadlineNotPassedEvents = new ArrayList<>();
    public static ArrayList<Event> DeadlinePassedEvents = new ArrayList<>();
    public static ArrayList<Event> EndOfWeekEvents = new ArrayList<>();
    public static ArrayList<Event> EndOfDayEvents = new ArrayList<>();
    public static ArrayList<Event> EndOfMonthEvents = new ArrayList<>();
    public static ArrayList<Event> PastMonthEvents = new ArrayList<>();
    public static ArrayList<Event> PastDayEvents = new ArrayList<>();
    public static ArrayList<Event> PastWeekEvents = new ArrayList<>();

    public static DefaultListModel<Event> model = new DefaultListModel<>();
    public static DefaultListModel<Event> model2 = new DefaultListModel<>();
    public static DefaultListModel<Event> model3 = new DefaultListModel<>();
    public static DefaultListModel<Event> model4 = new DefaultListModel<>();
    public static DefaultListModel<Event> model5 = new DefaultListModel<>();
    public static DefaultListModel<Event> model6 = new DefaultListModel<>();
    public static DefaultListModel<Event> model7 = new DefaultListModel<>();
    public static DefaultListModel<Event> model8 = new DefaultListModel<>();
    public static DefaultListModel<Event> model9 = new DefaultListModel<>();

    //method for reading .ics file and putting events in correct lists
    public static void EventsInLists(String icsFilePath) {
        //clears probable full lists to avoid duplicates
        clearLists();

        //buffered reader goes through file and distinguishes appointments from tasks
        //then calls process methods to (re)create the events as Event objects and put them in lists
        try (BufferedReader reader = new BufferedReader(new FileReader(icsFilePath))) {
            String line;
            boolean inAppt = false;
            boolean inTask = false;
            StringBuilder currentAppt = new StringBuilder();
            StringBuilder currentTask = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("BEGIN:VEVENT")) {
                    inAppt = true;
                    currentAppt = new StringBuilder();
                } else if (line.startsWith("END:VEVENT")) {
                    inAppt = false;
                    processAppt(currentAppt.toString());

                } else if (line.startsWith("BEGIN:VTODO")) {
                    inTask = true;
                    currentTask = new StringBuilder();
                } else if (line.startsWith("END:VTODO")) {
                    inTask = false;
                    processTask(currentTask.toString());
                }

                if (inAppt) {
                    currentAppt.append(line).append("\n");
                } else if (inTask) {
                    currentTask.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void processAppt(String eventString) {
        //Appointment string is broken through
        String[] lines = eventString.split("\n");

        Map<String, String> eventData = new HashMap<>();
        //first part of line shows the key and second shows the value
        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                eventData.put(key, value);
            }
        }

        String summary = eventData.get("SUMMARY");
        String description = eventData.get("DESCRIPTION");
        String dtStart = eventData.get("DTSTART");
        String duration = eventData.get("DURATION");

        //date formatter for .ics files
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");

        LocalDateTime dateTime = LocalDateTime.parse(dtStart, formatter);
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();

        //storing date and time in different variables and then parses them to LocalDateTime
        String dateString = String.format("%04d-%02d-%02d", year, month, day);
        String timeString = String.format("%02d:%02d:%02d", hour, minute, second);

        LocalDate localDate = LocalDate.parse(dateString);
        LocalTime localTime = LocalTime.parse(timeString);

        //appointment created with its variables
        Appointment appointment = new Appointment(localDate, summary, description, localTime, duration);

        //date checks to put appointment in the appropriate list
        //after making sure that the same event is not twice put in total events list
        if (!AllEvents.contains(appointment)){

            AllEvents.add(appointment);
            AppointmentEvents.add(appointment);

            TimeTeller teller = TimeService.getTeller();

            long daysBetween = ChronoUnit.DAYS.between(teller.now().toLocalDate(), appointment.getDate());

            boolean isEventWithinWeek = appointment.getDate().isAfter(LocalDate.now()) && appointment.getDate().isBefore(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));
            boolean isEventWithinMonth = appointment.getDate().isAfter(LocalDate.now()) && appointment.getDate().isBefore(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).plusDays(1));
            boolean isEventWithinDay = appointment.getDate().atTime(appointment.getTime()).isAfter(LocalDateTime.now()) && appointment.getDate().atTime(appointment.getTime()).isBefore(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX));

            boolean isEventWithinPastWeek = appointment.getDate().isBefore(LocalDate.now().plusDays(1)) && appointment.getDate().isAfter(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
            boolean isEventWithinPastMonth = appointment.getDate().isBefore(LocalDate.now().plusDays(1)) && appointment.getDate().isAfter(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
            boolean isEventWithinPastDay = (appointment.getDate().atTime(appointment.getTime()).isBefore(LocalDateTime.now()) || appointment.getDate().atTime(appointment.getTime()).isEqual(LocalDateTime.now()) ) && (appointment.getDate().atTime(appointment.getTime()).isAfter(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN)));

            if (daysBetween >= 0) {
                FutureEvents.add(appointment);
                FutureAppointments.add(appointment);
            }else {
                PastEvents.add(appointment);
            }
            if (isEventWithinWeek) EndOfWeekEvents.add(appointment);
            if(isEventWithinMonth) EndOfMonthEvents.add(appointment);
            if(isEventWithinDay) EndOfDayEvents.add(appointment);
            if (isEventWithinPastWeek) PastWeekEvents.add(appointment);
            if(isEventWithinPastMonth) PastMonthEvents.add(appointment);
            if(isEventWithinPastDay) PastDayEvents.add(appointment);
        }
    }

    public static void processTask(String eventString) {
        //Task string is broken through
        String[] lines = eventString.split("\n");

        Map<String, String> eventData = new HashMap<>();
        //first part of line shows the key and second shows the value
        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();
                eventData.put(key, value);
            }
        }

        String summary = eventData.get("SUMMARY");
        String description = eventData.get("DESCRIPTION");
        String dtStart = eventData.get("DTSTART");
        String due = eventData.get("DUE");

        //status is true when completed and false when needs-action in the .ics
        boolean statusbool = false;
        String status = eventData.get("STATUS");
        if (status != null) {
            if (status.equals("COMPLETED")) {
                statusbool = true;
            } else if (status.equals("NEEDS-ACTION")) {
                statusbool = false;
            }
        }
        //date formatter for .ics files
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");

        LocalDateTime dateTime = LocalDateTime.parse(dtStart, formatter);
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int day = dateTime.getDayOfMonth();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();
        int second = dateTime.getSecond();

        //deadline formatter for .ics files
        DateTimeFormatter dueformatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");
        LocalDateTime dueDate = LocalDateTime.parse(due, dueformatter);
        int dueyear = dueDate.getYear();
        int duemonth = dueDate.getMonthValue();
        int dueday = dueDate.getDayOfMonth();

        //storing date and time in different variables and then parses them to LocalDateTime
        String dateString = String.format("%04d-%02d-%02d", year, month, day);
        String timeString = String.format("%02d:%02d:%02d", hour, minute, second);
        String dueString = String.format("%04d-%02d-%02d", dueyear, duemonth, dueday);

        LocalDate localDate = LocalDate.parse(dateString);
        LocalTime localTime = LocalTime.parse(timeString);
        LocalDate deadline = LocalDate.parse(dueString);

        //task created with its variables
        Task task = new Task(localDate, summary, description, localTime, deadline, statusbool);

        //date checks to put appointment in the appropriate list
        //after making sure that the same event is not twice put in total events list
        if (!AllEvents.contains(task)) {

            AllEvents.add(task);
            TaskEvents.add(task);

            TimeTeller teller = TimeService.getTeller();

            long daysBetween = ChronoUnit.DAYS.between(teller.now().toLocalDate(), task.getDeadline());
            long daysDue = ChronoUnit.DAYS.between(teller.now().toLocalDate(), task.getDeadline());

            boolean isEventWithinWeek = task.getDeadline().isAfter(LocalDate.now()) && task.getDeadline().isBefore(LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)));
            boolean isEventWithinMonth = task.getDeadline().isAfter(LocalDate.now()) && task.getDeadline().isBefore(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).plusDays(1));

            //something we missed is that the task time indicates the time it was assigned and not the time of the deadline
            //but task time here was put in the deadline
            boolean isEventWithinDay = task.getDeadline().atTime(task.getTime()).isAfter(LocalDateTime.now()) && task.getDeadline().atTime(task.getTime()).isBefore(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX));

            boolean isEventWithinPastWeek = task.getDeadline().isBefore(LocalDate.now().plusDays(1)) && task.getDeadline().isAfter(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)));
            boolean isEventWithinPastMonth = task.getDeadline().isBefore(LocalDate.now().plusDays(1)) && task.getDeadline().isAfter(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
            //same problem with task time here
            boolean isEventWithinPastDay = (task.getDeadline().atTime(task.getTime()).isBefore(LocalDateTime.now()) || task.getDeadline().atTime(task.getTime()).isEqual(LocalDateTime.now()) ) && (task.getDeadline().atTime(task.getTime()).isAfter(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MIN)));

            if (daysBetween >= 0) {
                FutureEvents.add(task);
                FutureTasks.add(task);
            } else {
                PastEvents.add(task);
            }
            if (daysDue > 0) {
                DeadlineNotPassedEvents.add(task);
            } else {
                DeadlinePassedEvents.add(task);
            }
            if (isEventWithinWeek) EndOfWeekEvents.add(task);
            if (isEventWithinMonth) EndOfMonthEvents.add(task);
            if (isEventWithinDay) EndOfDayEvents.add(task);
            if (isEventWithinPastWeek) PastWeekEvents.add(task);
            if (isEventWithinPastMonth) PastMonthEvents.add(task);
            if (isEventWithinPastDay) PastDayEvents.add(task);
        }
    }

    //9 methods that sort event lists and put all displayed lists contents
    //in Jlists (with different models) after making sure they are not already in the models
    public static JList<Event> eventsInJLists() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model);
        for (Event event : AllEvents) {
            if (!model.contains(event)){
                model.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> DeadLineP() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model2);
        for (Event event : DeadlinePassedEvents) {
            if (!model2.contains(event)) {
                model2.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> DeadLineNP() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model3);
        for (Event event : DeadlineNotPassedEvents) {
            if (!model3.contains(event)) {
                model3.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> EndWeek() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model4);
        for (Event event : EndOfWeekEvents) {
            if (!model4.contains(event)) {
                model4.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> EndMonth() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model5);
        for (Event event : EndOfMonthEvents) {
            if (!model5.contains(event)) {
                model5.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> EndDay() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model6);
        for (Event event : EndOfDayEvents) {
            if (!model6.contains(event)) {
                model6.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> PastWeek() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model7);
        for (Event event : PastWeekEvents) {
            if (!model7.contains(event)) {
                model7.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> PastMonth() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model8);
        for (Event event : PastMonthEvents) {
            if (!model8.contains(event)) {
                model8.addElement(event);
            }
        }
        return eventJList;
    }
    public static JList<Event> PastDay() {
        sortEvents();
        JList<Event> eventJList = new JList<>(model9);
        for (Event event : PastDayEvents) {
            if (!model9.contains(event)) {
                model9.addElement(event);
            }
        }
        return eventJList;
    }

    //toString method for the details display popup of an appointment
    public static String DisplayAppointment(Appointment appointment) {
        return "Appointment Details\n" +
                "\nTitle: " + appointment.getTitle() +
                "\nDescription: " + appointment.getDescription() +
                "\nDate: " + appointment.getDate() +
                "\nStarting Time: " + appointment.getTime() +
                "\nDuration: " + appointment.getDuration();
    }
    //toString method for the details display popup of a task
    public static String DisplayTask(Task task) {
        String status= task.isFinished() ? "COMPLETED" : "NOT COMPLETED";
        return "Task Details\n" +
                "\nTitle: " + task.getTitle() +
                "\nDescription: " + task.getDescription() +
                "\nDate: " + task.getDate() +
                "\nStarting Time: " + task.getTime() +
                "\nDeadline: " + task.getDeadline() +
                "\nStatus:" + status;
    }

    //reminder methods returning first element of future events
    public static Event FutureReminder() {
        sortEvents();
        return FutureEvents.get(0);
    }
    public static Appointment ApptReminder() {
        sortEvents();
        return FutureAppointments.get(0);
    }
    public static Task TaskReminder() {
        sortEvents();
        return FutureTasks.get(0);
    }

    //add event method taking every variable as arguments and using them accordingly
    public static void AddEvent(String icsFilePath, String eventName, String eventDescription,
                                LocalDate eventDate, LocalTime eventTime, String eventDuration,
                                LocalDate eventDeadline, boolean isFinished, String userInputType) {


        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss'Z'");
        //quick check of file/calendar existance
        boolean isFileEmptyOrNoCalendar = checkIfFileIsEmptyOrNoCalendar(icsFilePath);

        //if the file is empty or the first line is not BEGIN:VCALENDAR, add BEGIN:VCALENDAR at the beginning
        if (isFileEmptyOrNoCalendar) {
            try (PrintStream printStream = new PrintStream(new FileOutputStream(icsFilePath, true))) {
                printStream.println("BEGIN:VCALENDAR");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //user wants to add appointment
        if ("appointment".equalsIgnoreCase(userInputType)) {
            //creates appointment and adds it to lists
            Appointment appointment = new Appointment(eventDate, eventName, eventDescription, eventTime, eventDuration);
            AppointmentEvents.add(appointment);
            AllEvents.add(appointment);

            //writing appointment details on the .ics file
            try (PrintStream printStream = new PrintStream(new FileOutputStream(icsFilePath, true))) {
                //deletes last calendar line and re-writes it at the end of the event
                deleteEndVCalendarLine(icsFilePath);
                printStream.println("BEGIN:VEVENT");
                printStream.println("SUMMARY:" + appointment.getTitle());
                printStream.println("DESCRIPTION:" + appointment.getDescription());
                printStream.println("DTSTART:" + appointment.getDate().format(dateFormatter) + appointment.getTime().format(timeFormatter));
                printStream.println("DURATION:" + appointment.getDuration());
                printStream.println("END:VEVENT");
                printStream.println("END:VCALENDAR");
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + icsFilePath);
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + icsFilePath);
                e.printStackTrace();
            }
            //Sorting events after adding a new one
            sortEvents();
            System.out.println("Appointment added successfully!");

        //user wants to add task
        } else if ("task".equalsIgnoreCase(userInputType)) {
            //creates task and adds it to lists
            Task task = new Task(eventDate, eventName, eventDescription, eventTime, eventDeadline, isFinished);
            TaskEvents.add(task);
            AllEvents.add(task);

            //writing task details on the .ics file
            try (PrintStream printStream = new PrintStream(new FileOutputStream(icsFilePath, true))) {
                //deletes last calendar line and re-writes it at the end of the event
                deleteEndVCalendarLine(icsFilePath);
                printStream.println("BEGIN:VTODO");
                printStream.println("SUMMARY:" + task.getTitle());
                printStream.println("DESCRIPTION:" + task.getDescription());
                printStream.println("DTSTART:" + task.getDate().format(dateFormatter) + task.getTime().format(timeFormatter));
                printStream.println("DUE:" + dateFormatter.format(task.getDeadline()) + "000000Z");
                printStream.println("STATUS:" + (task.isFinished() ? "COMPLETED" : "NEEDS-ACTION"));
                printStream.println("END:VTODO");
                printStream.println("END:VCALENDAR");
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + icsFilePath);
                e.printStackTrace();
            } catch (IOException e) {
                System.err.println("Error writing to the file: " + icsFilePath);
                e.printStackTrace();
            }
            sortEvents();
            System.out.println("Task added successfully!");
        }
    }

    public static void ChangeTask(String filepath, String prevTitle, Task task) throws IOException {
        //temp file
        File tempFile = new File(filepath + ".ics");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss'Z'");

        //reads the content of the file and append to the temporary file
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath));
             PrintWriter tempWriter = new PrintWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("SUMMARY:" + prevTitle)) {
                        //when old summary matches old task title skip the next 5 lines
                        for (int i = 0; i < 4; i++) {
                            reader.readLine();
                        }
                        //adds changed task lines to the temporary file
                        tempWriter.println("SUMMARY:" + task.getTitle());
                        tempWriter.println("DESCRIPTION:" + task.getDescription());
                        tempWriter.println("DTSTART:" + task.getDate().format(dateFormatter) + task.getTime().format(timeFormatter));
                        tempWriter.println("DUE:" + dateFormatter.format(task.getDeadline()) + "000000Z");
                        tempWriter.println("STATUS:" + (task.isFinished() ? "COMPLETED" : "NEEDS-ACTION"));
                    } else {
                        //adds every other line
                        tempWriter.println(line);
                    }
                }
            }
        //replaces the original file with the temporary file
        Files.move(tempFile.toPath(), new File(filepath).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    public static void ChangeAppointment(String filepath, String prevTitle, Appointment appointment) throws IOException {
        //temp file
        File tempFile = new File(filepath + ".ics");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss'Z'");

        //reads the content of the file and append to the temporary file
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath));
             PrintWriter tempWriter = new PrintWriter(new FileWriter(tempFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("SUMMARY:" + prevTitle)) {
                    //when old summary matches old appointment title skip the next 4 lines
                    for (int i = 0; i < 3; i++) {
                        reader.readLine();
                    }
                    //adds changed task lines to the temporary file
                    tempWriter.println("SUMMARY:" + appointment.getTitle());
                    tempWriter.println("DESCRIPTION:" + appointment.getDescription());
                    tempWriter.println("DTSTART:" + appointment.getDate().format(dateFormatter) + appointment.getTime().format(timeFormatter));
                    tempWriter.println("DURATION:" + appointment.getDuration());
                } else {
                    //adds every other line
                    tempWriter.println(line);
                }
            }
        }
        //replaces the original file with the temporary file
        Files.move(tempFile.toPath(), new File(filepath).toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    //It deletes the END:VCALENDAR line off of the file if it exists, and when a new event gets added, it adds the END:VCALENDAR line back,
    //right bellow the latest event addition. If the "END:VCALENDAR" line is not found in the file, it adds it after the latest event.
    public static void deleteEndVCalendarLine(String file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder modifiedContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // Check if the line contains "END:VCALENDAR"
                if (!line.contains("END:VCALENDAR")) {
                    modifiedContent.append(line).append(System.lineSeparator());
                }
            }

            // Writes the modified content back into the file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(modifiedContent.toString());
            }
        }
    }

    //method to check if the file is empty or the first line is not BEGIN:VCALENDAR
    private static boolean checkIfFileIsEmptyOrNoCalendar(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String firstLine = reader.readLine();
            return firstLine == null || !firstLine.trim().equals("BEGIN:VCALENDAR");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //method to sort events
    public static void sortEvents() {
        // Sort AllEvents by date and time
        AllEvents.sort(new EventDateTimeComparator());

        // Sort AppointmentEvents by date and time
        AppointmentEvents.sort(new EventDateTimeComparator());
        // Sort TaskEvents by date and time
        TaskEvents.sort(new EventDateTimeComparator());
        FutureEvents.sort(new EventDateTimeComparator());
        FutureAppointments.sort(new EventDateTimeComparator());
        FutureTasks.sort(new EventDateTimeComparator());
        PastEvents.sort(new EventDateTimeComparator());
        DeadlinePassedEvents.sort(new EventDateTimeComparator());
        DeadlineNotPassedEvents.sort(new EventDateTimeComparator());
        EndOfWeekEvents.sort(new EventDateTimeComparator());
        EndOfMonthEvents.sort(new EventDateTimeComparator());
        EndOfDayEvents.sort(new EventDateTimeComparator());
        PastWeekEvents.sort(new EventDateTimeComparator());
        PastMonthEvents.sort(new EventDateTimeComparator());
        PastDayEvents.sort(new EventDateTimeComparator());
    }
    //method to clear every list and model
    //datetime comparator for event sorting
    static class EventDateTimeComparator implements Comparator<Event> {
        @Override
        public int compare(Event event1, Event event2) {
            LocalDate date1 = event1.getDate();
            LocalTime time1 = event1.getTime();
            String dateTime1 = date1 + " " + time1;

            LocalDate date2 = event2.getDate();
            LocalTime time2 = event2.getTime();
            String dateTime2 = date2 + " " + time2;
            return dateTime1.compareTo(dateTime2);
        }
    }
    public static void clearLists(){
        AllEvents.clear();
        AppointmentEvents.clear();
        TaskEvents.clear();
        FutureEvents.clear();
        FutureAppointments.clear();
        FutureTasks.clear();
        DeadlinePassedEvents.clear();
        DeadlineNotPassedEvents.clear();
        EndOfDayEvents.clear();
        EndOfMonthEvents.clear();
        EndOfWeekEvents.clear();
        PastEvents.clear();
        PastDayEvents.clear();
        PastWeekEvents.clear();
        PastMonthEvents.clear();
        model.removeAllElements();
        model2.removeAllElements();
        model3.removeAllElements();
        model4.removeAllElements();
        model5.removeAllElements();
        model6.removeAllElements();
        model7.removeAllElements();
        model8.removeAllElements();
        model9.removeAllElements();
    }
}