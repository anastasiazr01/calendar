package gr.hua.dit.oop2;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.DtEnd;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;

import static gr.hua.dit.oop2.EventOptions.AddEvent;

//import Calendar
//icsFilePath metavliti? thn ekana import sthn EventOptions(??????)

public class CalendarApp {
    public static String icsFilePath;

    /*public static void main(String[] args) {
        System.out.println("unmmmmm!");
        int argc = args.length;
        //save valid argument number
        int filenum = EventOptions.checkArgumentNumber(argc);
        //if 1 argument filenum=0 , 2 arguments filenum=1 and this is where the .ics is
        String type = EventOptions.assignFirstArgumentType(args[filenum]);
        System.out.println(type);
        //save and print out .ics file
        String fileName = args[filenum];
        EventOptions.ReadFile(fileName);

        if (args.length != 2 || !"day".equals(args[0])) {
            return;
        }
        String filename = args[1];
        //EventOptions.displayEvents(filename);

    }
     */

    public static void main(String[] args) {
        if (args.length > 0) {
            String icsFilePath = args[0];
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Welcome to your Calendar!");
                System.out.println("Calendar Options Menu:");
                System.out.println("Please choose an option of the above:");
                System.out.println("1. Add Event");
                System.out.println("2. Change Event");
                System.out.println("3. Change Task Status");
                System.out.println("4. Display Events (Chronological Order)");
                System.out.println("5. Exit");

                int choice = scanner.nextInt();

                switch (choice){
                    case 1:
                        EventOptions.AddEvent(icsFilePath);
                        break;
                    case 2:
                        System.out.println("Enter the title of the event you want to change:");
                        scanner.nextLine();
                        String eventTitle = scanner.nextLine();
                        EventOptions.ChangeEvent(eventTitle, icsFilePath);
                        break;
                    case 3:
                        System.out.println("Enter the title of the task you want to change status:");
                        scanner.nextLine();
                        String taskTitle = scanner.nextLine();
                        EventOptions.changeTaskStatus(taskTitle, icsFilePath);
                        break;
                    case 4:
                        EventOptions.printEventLists();
                        break;
                    case 5:
                        System.out.println("Exiting the program. Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please choose a valid option.");
                }

            }

        } else {
            System.out.println("Please provide the path to the ICS file as a command-line argument.");
        }
    }

}
