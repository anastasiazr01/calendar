package gr.hua.dit.oop2;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;

import java.io.*;

import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


public class ICalReader {
/*
    public static Calendar readCalendar(InputStream stream) throws IOException, ParserException {
        CalendarBuilder builder = new CalendarBuilder();
        return builder.build(stream);
    }

    private static void loadEventsFromICalFile(String filePath) {
        try (InputStream stream = new FileInputStream(filePath)) {
            Calendar calendar = readCalendar(stream);

            for (Object component : calendar.getComponents(Component.VEVENT)) {
                VEvent vEvent = (VEvent) component;

                String summary = vEvent.getSummary().getValue();
                String description = vEvent.getDescription().getValue();
                LocalDateTime startDateTime = LocalDateTime.ofInstant(vEvent.getStartDate().getDate().toInstant(), ZoneOffset.UTC);

                Event event = new Event(startDateTime, summary, description);
                EventOptions.AllEvents.add(event);

                if (event instanceof Appointment) {
                    EventOptions.AppointmentEvents.add((Appointment) event);
                } else if (event instanceof Task) {
                    EventOptions.TaskEvents.add((Task) event);
                }
            }
        } catch (IOException | ParserException e) {
            e.printStackTrace();
        }
    }

    private static void displayFutureEvents() {
        TimeTeller teller = TimeService.getTeller();
        LocalDateTime currentTime = teller.now();

        List<Event> futureEvents = new ArrayList<>();
        for (Event event : AllEvents) {
            if (event.getDate().isAfter(currentTime)) {
                futureEvents.add(event);
            }
        }

        private static boolean isAppointment (VEvent vEvent){
            return vEvent.getProperty(Property.CATEGORIES) != null;
        }


    }

 */
}


