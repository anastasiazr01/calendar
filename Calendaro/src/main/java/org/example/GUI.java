package org.example;


import gr.hua.dit.oop2.calendar.TimeService;
import gr.hua.dit.oop2.calendar.TimeTeller;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import static org.example.EventOptions.*;

public class GUI {
    //basic init
    public static String filePath;
    private File selectedFile;
    private static JSplitPane splitPane;
    private static final JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
    static JScrollPane scrollPane;
    JPanel buttonPanel;

    //GUI Constructor
    public GUI() {

        //basic frame
        JFrame frame = new JFrame("Calendar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //base split pane
        splitPane = new JSplitPane();

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        //initial right side of splitpane
        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BoxLayout(displayPanel, BoxLayout.Y_AXIS));

        JTextArea textArea = new JTextArea(20, 40);
        scrollPane = new JScrollPane(textArea);

        displayPanel.add(scrollPane);

        //option buttons in left side of splitpane
        JButton newevent = new JButton("Add Event");
        JButton changeevent = new JButton("Edit Event");
        JButton displayButton = new JButton("Display Events");
        JButton clearTextButton = new JButton("Clear Text");
        buttonPanel.add(displayButton);
        buttonPanel.add(newevent);
        buttonPanel.add(changeevent);
        buttonPanel.add(clearTextButton);

        ///left panel of ics files/buttons
        splitPane.setLeftComponent(buttonPanel);
        ///right panel of lists and functionality
        splitPane.setRightComponent(displayPanel);
        splitPane.setDividerLocation(150);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(splitPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setBounds(350, 150, 830, 540);

        //first button for file choice and lists display
        displayButton.addActionListener(e -> {
            //clear panel for duplicates elimination
            clearDisplayPanel();

            //fileshooser dialog
            JDialog fileDialog = new JDialog();
            selectedFile = FileChooser(fileDialog);
            if (selectedFile == null) {
                clearDisplayPanel();
            } else {
                filePath = selectedFile.getAbsolutePath();
                EventOptions.EventsInLists(filePath);
                EventOptions.sortEvents();
                splitPane.setRightComponent(rightSplitPane);

                JTextField selectlist = new JTextField("Please select a list to display:");
                rightSplitPane.setTopComponent(selectlist);

                //new panel for list choice buttons
                JPanel moreLists = new JPanel();
                JButton allevents = new JButton("All Events");
                JButton deadlineP = new JButton("Deadline Passed Events");
                JButton deadlineNP = new JButton("Deadline Not Passed Events");
                JButton endweek = new JButton("End of Week");
                JButton endday = new JButton("End of Day");
                JButton endmonth = new JButton("End of Month");
                JButton pastweek = new JButton("Past Week");
                JButton pastmonth = new JButton("Past Month");
                JButton pastday = new JButton("Past Day");
                moreLists.add(allevents);
                moreLists.add(deadlineP);
                moreLists.add(deadlineNP);
                moreLists.add(endweek);
                moreLists.add(endday);
                moreLists.add(endmonth);
                moreLists.add(pastweek);
                moreLists.add(pastmonth);
                moreLists.add(pastday);

                //second split pane in base split pane
                rightSplitPane.setBottomComponent(moreLists);
                //reminders
                ReminderPopup(frame);

                //button for all events display
                allevents.addActionListener(e1 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.eventsInJLists();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    //label does not work
//                            JLabel label = new JLabel("All Events");
//                            rightSplitPane.add(label);

                    //method for event details display when event doubleclicked
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for deadline passed events display
                deadlineP.addActionListener(e2 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.DeadLineP();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for deadline not passed event display
                deadlineNP.addActionListener(e3 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.DeadLineNP();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for events until the end of the week display
                endweek.addActionListener(e4 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.EndWeek();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for events until the end of the month display
                endmonth.addActionListener(e5 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.EndMonth();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for events until the end of the day display
                endday.addActionListener(e6 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.EndDay();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for events from start of the week until now display
                pastweek.addActionListener(e7 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.PastWeek();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for events from start of the month until now display
                pastmonth.addActionListener(e8 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.PastMonth();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
                //button for events from start of the day until now display
                pastday.addActionListener(e9 -> {
                    clearRightSplit();
                    JList<Event> eventJList = EventOptions.PastDay();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    detailsDisplay(eventJList,frame);
                    rightSplitPane.setTopComponent(newScrollPane);
                });
            }
        });

        //add new event button
        newevent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //file chooser dialog
                JDialog fileDialog = new JDialog();
                selectedFile = FileChooser(fileDialog);
                if (selectedFile == null) {
                    clearDisplayPanel();
                } else {
                    filePath = selectedFile.getAbsolutePath();

                    //user selects to add appointment or task and the equivalent dialog pops up
                    String[] options = {"Appointment", "Task"};
                    int userInputType = JOptionPane.showOptionDialog(frame,
                            "Select the type of event:",
                            "Event Type",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[0]
                    );
                    if (userInputType == 0) {
                        showAddAppointentDialog();
                    } else {
                        showAddTaskDialog();
                    }
                    EventOptions.EventsInLists(filePath);
                    JList<Event> eventJList = EventOptions.eventsInJLists();
                    JScrollPane newScrollPane = new JScrollPane(eventJList);
                    splitPane.setRightComponent(newScrollPane);
                    //method for event details change when event doubleclicked
                    detailsChange(eventJList,frame);
                }
            }

            private void showAddAppointentDialog() {
                //new dialog for add appointment pops up
                JDialog addEventDialog = new JDialog(frame,true);
                addEventDialog.setTitle("Add Appointment");

                //components initialized
                JTextField eventNameField = new JTextField();
                JTextField eventDescriptionField = new JTextField();
                JTextField eventDateField = new JTextField();
                JTextField eventTimeField = new JTextField();
                JTextField eventDurationField = new JTextField();

                //add button listener added below
                JButton addApptBtn = new JButton("Add Appointment");

                //components added to the dialog
                addEventDialog.setLayout(new GridLayout(7, 2));
                addEventDialog.add(new JLabel("Appointment Name:"));
                addEventDialog.add(eventNameField);
                addEventDialog.add(new JLabel("Appointment Description:"));
                addEventDialog.add(eventDescriptionField);
                addEventDialog.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
                addEventDialog.add(eventDateField);
                addEventDialog.add(new JLabel("Appointment Time (HH:mm):"));
                addEventDialog.add(eventTimeField);
                addEventDialog.add(new JLabel("Appointment Duration(in hours):"));
                addEventDialog.add(eventDurationField);
                addEventDialog.add(new JLabel(""));
                addEventDialog.add(addApptBtn);

                //add appointment button listener(save)
                addApptBtn.addActionListener(e -> {
                    try {
                        //user gives input
                        String eventName = eventNameField.getText();
                        String eventDescription = eventDescriptionField.getText();
                        LocalDate eventDate = LocalDate.parse(eventDateField.getText());
                        LocalTime eventTime = LocalTime.parse(eventTimeField.getText());
                        String eventDuration = eventDurationField.getText();

                        //calling AddEvent method from EventOptions with given filepath and null values in task related attributes
                        AddEvent(filePath, eventName, eventDescription, eventDate, eventTime, eventDuration, null, false, "appointment");

                        //closing dialog
                        addEventDialog.dispose();
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(addEventDialog, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                //adding components and functionality to the dialog
                addEventDialog.add(new JLabel("")); // Empty label for spacing
                addEventDialog.add(addApptBtn);
                addEventDialog.pack();
                addEventDialog.setLocationRelativeTo(null);
                addEventDialog.setVisible(true);
            }

            private void showAddTaskDialog() {
                //new dialog for add task pops up
                JDialog addEventDialog = new JDialog(frame,true);
                addEventDialog.setTitle("Add Task");

                //components initialized
                JTextField eventNameField = new JTextField();
                JTextField eventDescriptionField = new JTextField();
                JTextField eventDateField = new JTextField();
                JTextField eventTimeField = new JTextField();
                JTextField eventDeadlineField = new JTextField();

                //add button listener added below
                JButton addTaskBtn = new JButton("Add Task");

                //components added to the dialog
                addEventDialog.setLayout(new GridLayout(7, 2));
                addEventDialog.add(new JLabel("Task Name:"));
                addEventDialog.add(eventNameField);
                addEventDialog.add(new JLabel("Task Description:"));
                addEventDialog.add(eventDescriptionField);
                addEventDialog.add(new JLabel("Task Date (YYYY-MM-DD):"));
                addEventDialog.add(eventDateField);
                addEventDialog.add(new JLabel("Task Time (HH:mm):"));
                addEventDialog.add(eventTimeField);
                addEventDialog.add(new JLabel("Task Deadline (YYYY-MM-DD):")); // Additional label for task
                addEventDialog.add(eventDeadlineField); // Additional field for task
                addEventDialog.add(new JLabel(""));
                addEventDialog.add(addTaskBtn);

                //add task button listener(save)
                addTaskBtn.addActionListener(e -> {
                    try {
                        //user gives input
                        String eventName = eventNameField.getText();
                        String eventDescription = eventDescriptionField.getText();
                        LocalDate eventDate = LocalDate.parse(eventDateField.getText());
                        LocalTime eventTime = LocalTime.parse(eventTimeField.getText());
                        LocalDate eventDeadline = LocalDate.parse(eventDeadlineField.getText()); // Additional field for task


                        //calling AddEvent method from EventOptions with given filepath and null values in appointment related attributes
                        AddEvent(filePath, eventName, eventDescription, eventDate, eventTime, null, eventDeadline, false, "task");

                        //closing dialog
                        addEventDialog.dispose();
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(addEventDialog, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                //adding components and functionality to the dialog
                addEventDialog.add(new JLabel("")); // Empty label for spacing
                addEventDialog.add(addTaskBtn);
                addEventDialog.pack();
                addEventDialog.setLocationRelativeTo(null);
                addEventDialog.setVisible(true);
            }
        });

        //change existing event button
        changeevent.addActionListener(e -> {
            //filechooser dialog
            JDialog fileDialog = new JDialog();
            selectedFile = FileChooser(fileDialog);
            if (selectedFile == null) {
                clearDisplayPanel();
            } else {
                filePath = selectedFile.getAbsolutePath();
                //puts events in lists if not already put and creates new list display pane
                EventOptions.EventsInLists(filePath);
                JList<Event> eventJList = EventOptions.eventsInJLists();
                JScrollPane newScrollPane = new JScrollPane(eventJList);
                splitPane.setRightComponent(newScrollPane);
                //method for event details change when event doubleclicked
                detailsChange(eventJList,frame);
            }
        });

        //Clears the text field (actually deletes the whole field)
        clearTextButton.addActionListener(e -> clearRightSplit());

        //resize and set option icons segment
        //clear text button
        ImageIcon clearIcon = new ImageIcon("clear-icon.png");
        Image image = clearIcon.getImage();
        Image scaledImageClearText = image.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        clearTextButton.setIcon(new ImageIcon(scaledImageClearText));
        clearTextButton.setText(" Clear Text");
        clearTextButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        clearTextButton.setVerticalTextPosition(SwingConstants.CENTER);

        //add event button
        ImageIcon addEventIcon = new ImageIcon("add-event-icon.png");
        Image originalImageAddEvent = addEventIcon.getImage();
        Image scaledImageAddEvent = originalImageAddEvent.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Icon scaledIconAddEvent = new ImageIcon(scaledImageAddEvent);
        newevent.setIcon(scaledIconAddEvent);

        //edit(change) event button
        ImageIcon originalEditEventIcon = new ImageIcon("edit-event-icon.png");
        Image originalEditEventImage = originalEditEventIcon.getImage();
        Image scaledEditEventImage = originalEditEventImage.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        Icon scaledIconEditEvent = new ImageIcon(scaledEditEventImage);
        changeevent.setIcon(scaledIconEditEvent);

        //display calendar button
        ImageIcon displayIcon = new ImageIcon("calendar-import-icon.png");
        Image imageDisplay = displayIcon.getImage();
        Image scaledImageDisplay = imageDisplay.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
        displayButton.setIcon(new ImageIcon(scaledImageDisplay));
        displayButton.setHorizontalTextPosition(SwingConstants.RIGHT);
        displayButton.setVerticalTextPosition(SwingConstants.CENTER);
    }

    //method area
    //filechooser method
    public static File FileChooser(JDialog dialog){
        JFileChooser fileChooser = new JFileChooser();
        //file filter for .ics files selection
        FileNameExtensionFilter filter = new FileNameExtensionFilter("ICS Files", "ics");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(dialog);
        //if option valid return contents of selected file otherwise return null
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return null;
    }

    //clear button removes display panel(right side of base split pane) components
    //and empties lists to avoid duplicates
    private static void clearDisplayPanel(){
        JScrollPane newScrollPane = new JScrollPane();
        splitPane.setRightComponent(newScrollPane);
        EventOptions.clearLists();
    }

    //clear right split called when different lists of same file are being chosen to be displayed
    private static void clearRightSplit(){
        JScrollPane newScrollPane = new JScrollPane();
        rightSplitPane.setTopComponent(newScrollPane);
        rightSplitPane.setResizeWeight(1.0);
        rightSplitPane.setDividerLocation(0.67);
    }
    private static void ReminderPopup(JFrame frame)   {
        //new reminder dialog pops up
        JDialog reminder = new JDialog(frame,"Reminder!");
        JSplitPane remSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        remSplit.setResizeWeight(0.5);
        remSplit.setDividerLocation(0.5);
        //future reminder method called and displays FIRST next event on the bottom of a split pane
        Event nextEvent = EventOptions.FutureReminder();
        JTextField remfield ;
        if (nextEvent instanceof Appointment) {
            Appointment nextAppt = EventOptions.ApptReminder();
            remfield = new JTextField("Do not forget!!\n Next Appointment  ~"
                    + nextAppt.getTitle() + "~\n  for: " + nextAppt.getDate());
        }else {
            Task nextTask = EventOptions.TaskReminder();
            remfield = new JTextField("Do not forget!!\n Next Task  ~"
                    + nextTask.getTitle() + "~\n  due: " + nextTask.getDeadline());
        }
        //on the top of the split pane the current date and time are displayed
        //using the TimeTeller interface
        TimeTeller teller = TimeService.getTeller();
        JTextField timetext = new JTextField("Current time is: " + teller.now());
        teller.addTimeListener(e -> timetext.setText("Current time is: " + teller.now()));

        //adding components and functionality to the reminder dialog
        remSplit.setTopComponent(timetext);
        remSplit.setBottomComponent(remfield);
        reminder.add(remSplit);
        reminder.setBounds(800, 400, 400, 200);
        reminder.setResizable(true);
        reminder.setVisible(true);

    }
    private void detailsDisplay(JList<Event> events, JFrame frame) {
        //details of events method using the Jlist of the events chosen to be displayed as argument
        //jframe is used to be passed as the parent frame in case the program is closed
        events.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //mouse listener for doubleclick on specific event
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                //when event doubleclicked another method for selected event details dialog is called
                if (e.getClickCount() == 2) {
                    Event selectedEvent = events.getSelectedValue();
                    if (selectedEvent != null) {
                        displayEventDetailsPopup(selectedEvent,frame);
                    }
                }
            }
        };
        events.addMouseListener(mouseListener);
    }
    private void displayEventDetailsPopup(Event event, JFrame frame) {
        JDialog EventDetails = new JDialog(frame,"Details");

        JTextArea remfield = new JTextArea();
        remfield.setEditable(false);
        //detects if event is appointment or task so the right method with
        //given event type arguments is called
        if (event instanceof Appointment) {
            remfield.setText(EventOptions.DisplayAppointment((Appointment) event));
        } else {
            remfield.setText(EventOptions.DisplayTask((Task) event));
        }
        //dialog config
        EventDetails.setLayout(new BorderLayout());
        EventDetails.add(remfield, BorderLayout.CENTER);
        EventDetails.setLocationRelativeTo(null);
        EventDetails.setVisible(true);
        EventDetails.setBounds(400, 200, 350, 250);
        EventDetails.setResizable(true);
    }
    private void detailsChange(JList<Event> events, JFrame frame ) {
        //similar to detailsDisplay
        events.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //when event is doubleclicked, it gets checked if appointment or task and
        //change (edit event) method is called for either
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Event selectedEvent = events.getSelectedValue();
                    if (selectedEvent != null) {
                        if (selectedEvent instanceof Appointment) {
                            Appointment selectedAppt = (Appointment) selectedEvent;
                            ChangeAppointment(selectedAppt, frame);
                        }else if (selectedEvent instanceof  Task) {
                            Task selectedTask = (Task) selectedEvent;
                            ChangeTask(selectedTask, frame);
                        }
                    }
                }
            }

        };
        events.addMouseListener(mouseListener);
    }
    private void ChangeAppointment(Appointment appointment, JFrame frame) {
        //change method gets selected appointment and parent frame as arguments
        //creates new dialog
        JDialog addEventDialog = new JDialog(frame);
        addEventDialog.setTitle("change Appointment");
        //dialog components
        JTextField eventNameField = new JTextField();
        JTextField eventDescriptionField = new JTextField();
        JTextField eventDateField = new JTextField();
        JTextField eventTimeField = new JTextField();
        JTextField eventDurationField = new JTextField();

        //important temp string for changed appointment identification on the replacement method
        String previousTitle = appointment.getTitle();

        JButton savechanges = new JButton("Save Changes");
        //new appointment details fields created
        addEventDialog.setLayout(new GridLayout(7, 2));
        addEventDialog.add(new JLabel("Appointment Name:\n" + appointment.getTitle()));
        addEventDialog.add(eventNameField);
        addEventDialog.add(new JLabel("Appointment Description:\n" + appointment.getDescription()));
        addEventDialog.add(eventDescriptionField);
        addEventDialog.add(new JLabel("Appointment Date (YYYY-MM-DD):\n" + appointment.getDate()));
        addEventDialog.add(eventDateField);
        addEventDialog.add(new JLabel("Appointment Time (HH:mm):\n" + appointment.getTime()));
        addEventDialog.add(eventTimeField);
        addEventDialog.add(new JLabel("Appointment Duration:\n" + appointment.getDuration()));
        addEventDialog.add(eventDurationField);
        addEventDialog.add(new JLabel(""));
        addEventDialog.add(savechanges);
        //save changes button and procedure
        savechanges.addActionListener(e -> {

                try {
                //user inputs checked if changed or fields empty
                //if field empty, keep old values else parse textfields to value types
                String eventName = eventNameField.getText().isEmpty() ? appointment.getTitle() : eventNameField.getText();
                String eventDescription = eventDescriptionField.getText().isEmpty() ? appointment.getDescription() : eventDescriptionField.getText();
                String eventDuration = eventDurationField.getText().isEmpty() ? appointment.getDuration() : eventDurationField.getText();
                LocalDate eventDate = eventDateField.getText().isEmpty() ? appointment.getDate() : LocalDate.parse(eventDateField.getText());
                LocalTime eventTime = eventTimeField.getText().isEmpty() ? appointment.getTime() : LocalTime.parse(eventTimeField.getText());

                //set new appointment values
                appointment.setDate(eventDate);
                appointment.setTitle(eventName);
                appointment.setDuration(eventDuration);
                appointment.setTime(eventTime);
                appointment.setDescription(eventDescription);

                //call change appointment method for replacement in .ics file
                EventOptions.ChangeAppointment(filePath, previousTitle , appointment);

                //closing dialog
                addEventDialog.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(addEventDialog, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                    throw new RuntimeException(ex);
            }
        });
        addEventDialog.setBounds(500, 200, 700, 400);
        addEventDialog.setVisible(true);
        addEventDialog.setResizable(true);
    }
    private void ChangeTask(Task task, JFrame frame) {
        //change method gets selected task and parent frame as arguments
        //creates new dialog
        JDialog addEventDialog = new JDialog(frame);
        addEventDialog.setTitle("change Task");
        //dialog components
        JTextField eventNameField = new JTextField();
        JTextField eventDescriptionField = new JTextField();
        JTextField eventDateField = new JTextField();
        JTextField eventTimeField = new JTextField();
        JTextField eventDeadlineField = new JTextField();
        JCheckBox taskFinishedCheckBox = new JCheckBox();

        //important temp string for changed task identification on the replacement method
        String previousTitle = task.getTitle();

        JButton savechanges = new JButton("Save Changes");
        //new task details fields created
        addEventDialog.setLayout(new GridLayout(7, 2));
        addEventDialog.add(new JLabel("Task Name:\n" + task.getTitle()));
        addEventDialog.add(eventNameField);
        addEventDialog.add(new JLabel("Task Description:\n" + task.getDescription()));
        addEventDialog.add(eventDescriptionField);
        addEventDialog.add(new JLabel("Task Date (YYYY-MM-DD):\n" + task.getDate()));
        addEventDialog.add(eventDateField);
        addEventDialog.add(new JLabel("Task Time (HH:mm):\n" + task.getTime()));
        addEventDialog.add(eventTimeField);
        addEventDialog.add(new JLabel("Task Duration:\n" + task.getDeadline()));
        addEventDialog.add(eventDeadlineField);
        addEventDialog.add(new JLabel("Is task finished"));
        taskFinishedCheckBox.setSelected(task.isFinished());
        addEventDialog.add(taskFinishedCheckBox);
        addEventDialog.add(savechanges);

        //save changes button and procedure
        savechanges.addActionListener(e -> {
            try {
                //user inputs checked if changed or fields empty
                //if field empty, keep old values else parse textfields to value types
                String eventName = eventNameField.getText().isEmpty() ? task.getTitle() : eventNameField.getText();
                String eventDescription = eventDescriptionField.getText().isEmpty() ? task.getDescription() : eventDescriptionField.getText();
                LocalDate eventDate = eventDateField.getText().isEmpty() ? task.getDate() : LocalDate.parse(eventDateField.getText());
                LocalTime eventTime = eventTimeField.getText().isEmpty() ? task.getTime() : LocalTime.parse(eventTimeField.getText());
                LocalDate eventDeadline = eventDeadlineField.getText().isEmpty() ? task.getDeadline() : LocalDate.parse(eventDeadlineField.getText());
                boolean isFinished = taskFinishedCheckBox.isSelected();

                //set new task values
                task.setDate(eventDate);
                task.setTitle(eventName);
                task.setFinished(isFinished);
                task.setDeadline(eventDeadline);
                task.setTime(eventTime);
                task.setDescription(eventDescription);

                //call change task method for replacement in .ics file
                EventOptions.ChangeTask(filePath, previousTitle, task);

                // Close the dialog
                addEventDialog.dispose();
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(addEventDialog, "Invalid date or time format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        addEventDialog.setBounds(500, 200, 700, 400);
        addEventDialog.setVisible(true);
        addEventDialog.setResizable(true);
    }
}