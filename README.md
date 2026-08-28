# Calendar

A Java-based desktop calendar application for managing events and tasks through a graphical user interface.

The application allows users to import `.ics` calendar files, view and organize events, create and edit appointments and tasks, and save changes back to the calendar file.

## Features

- 📅 Display calendar events in a chronological list
- ➕ Add new appointments and tasks
- ✏️ Edit existing events and tasks
- ✅ Mark tasks as completed
- 🔔 Notifications for upcoming appointments and task deadlines
- 🔎 Filter events based on different time periods
- 📂 Import `.ics` calendar files through a file chooser
- 💾 Save changes directly to `.ics` files
- 🖥️ Graphical User Interface (GUI)
- ⏱️ Automatic sorting of events by date and time

## Project Structure

The application is organized around the following main classes:

- **Event** – Base class for calendar events, containing common properties such as date, time, title and description.
- **Appointment** – Represents appointments and extends `Event` with a duration.
- **Task** – Represents tasks and extends `Event` with a deadline and completion status.
- **EventOptions** – Implements the main application logic for adding, editing, displaying, sorting and managing events.
- **GUI** – Provides the graphical user interface and handles user interaction.
- **App** – Main entry point of the application.

## Technologies

- Java
- Maven
- Java Swing
- iCalendar (`.ics`) file format

## Requirements

- Java Development Kit (JDK)
- Maven

## Running the Application

Clone the repository and navigate to the project directory:

```bash
cd Calendaro
```

Build the project with Maven:

```bash
mvn clean install
```

Then run the application using:

```bash
java -jar Calendaro.jar
```

Alternatively, the application can be launched directly from IntelliJ IDEA using the `App` main class.

## Test Calendar Files

The project includes example `.ics` files that can be used to test the application:

- `GoodTest.ics`
- `testFinal.ics`

These files follow the format expected by the application.

> **Note:** `greece.ics` contains additional iCalendar fields that are not currently supported by the application's parser and therefore may not work correctly.

## Known Limitations

- In some cases, the application may not terminate correctly when the window is closed and may require termination through the console.
- After editing an event, the displayed list is not automatically re-sorted if the edited date or time changes. Reopening the calendar through the display function refreshes and re-sorts the list.
- Task time handling is based on the assumption that the specified time represents the task deadline, while the current `.ics` implementation associates the time differently.
- The application supports a specific subset of the iCalendar format rather than all possible `.ics` fields.

## Background

This project was originally developed in 2023 as part of a university programming assignment. It was later uploaded to GitHub as part of my project portfolio.
