import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class Person {
    protected String name;
    protected String phoneNumber;

    public Person(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Phone: " + phoneNumber;
    }
}

class Contact extends Person {
    public Contact(String name, String phoneNumber) {
        super(name, phoneNumber);
    }
}

class Student extends Person {
    private int rollNumber;
    private int marksSubject1;
    private int marksSubject2;
    private int marksSubject3;

    public Student(String name, String phoneNumber, int rollNumber, int marksSubject1, int marksSubject2, int marksSubject3) {
        super(name, phoneNumber);
        this.rollNumber = rollNumber;
        this.marksSubject1 = marksSubject1;
        this.marksSubject2 = marksSubject2;
        this.marksSubject3 = marksSubject3;
    }

    public int getRollNumber() {
        return rollNumber;
    }

    public int calculateTotalMarks() {
        return marksSubject1 + marksSubject2 + marksSubject3;
    }

    public double calculateAverageMarks() {
        return calculateTotalMarks() / 3.0;
    }

    public String getGrade() {
        double averageMarks = calculateAverageMarks();
        if (averageMarks >= 90) {
            return "A+";
        } else if (averageMarks >= 80) {
            return "A";
        } else if (averageMarks >= 70) {
            return "B";
        } else if (averageMarks >= 60) {
            return "C";
        } else if (averageMarks >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", Roll Number: " + rollNumber + ", Total Marks: " + calculateTotalMarks() + ", Grade: " + getGrade();
    }
}

class RecordList {
    private ArrayList<Person> records;

    public RecordList() {
        records = new ArrayList<>();
    }

    public void addRecord(Person record) {
        records.add(record);
    }

    public ArrayList<Person> getRecords() {
        return records;
    }
}

public class StudentResultApp extends JFrame {

    private JTextField nameField, phoneField, rollNumberField, subject1Field, subject2Field, subject3Field;
    private JTextArea recordListArea;
    private JButton addContactButton, addStudentButton, saveButton, loadButton;

    private RecordList recordList;

    private static final String FILE_PATH = "C:/Users/HP/Downloads/FileHandling.txt";

    public StudentResultApp() {
        setTitle("Student Management App");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        recordList = new RecordList();

     
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        rollNumberField = new JTextField(10);
        subject1Field = new JTextField(5);
        subject2Field = new JTextField(5);
        subject3Field = new JTextField(5);
        recordListArea = new JTextArea(20, 40);
        addContactButton = new JButton("Add Contact");
        addStudentButton = new JButton("Add Student");
        saveButton = new JButton("Save Records");
        loadButton = new JButton("Load Records");

        addContactButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();

                Contact contact = new Contact(name, phone);
                recordList.addRecord(contact);
                updateRecordListArea();
            }
        });

        addStudentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                int rollNumber = Integer.parseInt(rollNumberField.getText());
                int subject1, subject2, subject3;
                
                try {
                    subject1 = Integer.parseInt(subject1Field.getText());
                    subject2 = Integer.parseInt(subject2Field.getText());
                    subject3 = Integer.parseInt(subject3Field.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(StudentResultApp.this, "Please enter valid numeric values for subjects.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Exit the method
                }

                Student student = new Student(name, phone, rollNumber, subject1, subject2, subject3);
                recordList.addRecord(student);
                updateRecordListArea();
            }
        });


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveRecordsToFile();
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadRecordsFromFile();
                updateRecordListArea();
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollNumberField);
        inputPanel.add(new JLabel("Subject 1:"));
        inputPanel.add(subject1Field);
        inputPanel.add(new JLabel("Subject 2:"));
        inputPanel.add(subject2Field);
        inputPanel.add(new JLabel("Subject 3:"));
        inputPanel.add(subject3Field);
        inputPanel.add(addContactButton);
        inputPanel.add(addStudentButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        
        JScrollPane scrollPane = new JScrollPane(recordListArea);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(inputPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateRecordListArea() {
        recordListArea.setText("");
        for (Person record : recordList.getRecords()) {
            recordListArea.append(record.toString() + "\n");
        }
    }

    private void saveRecordsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Person record : recordList.getRecords()) {
                if (record instanceof Contact) {
                    writer.println("Contact," + record.getName() + "," + record.getPhoneNumber());
                } else if (record instanceof Student) {
                    Student student = (Student) record;
                    writer.println("Student," + student.getName() + "," + student.getPhoneNumber() + "," +

                    		
                    		student.getRollNumber() + "," + student.calculateTotalMarks());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadRecordsFromFile() {
        recordList.getRecords().clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String type = parts[0];
                    String name = parts[1];
                    String phone = parts[2];
                    if (type.equals("Contact")) {
                        Contact contact = new Contact(name, phone);
                        recordList.addRecord(contact);
                    } else if (type.equals("Student") && parts.length == 5) {
                        int rollNumber = Integer.parseInt(parts[3]);
                        int totalMarks = Integer.parseInt(parts[4]);
                        Student student = new Student(name, phone, rollNumber, 0, 0, 0); 
                        student.calculateAverageMarks(); 
                        recordList.addRecord(student);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace(); 
                }

                StudentResultApp app = new StudentResultApp();
                app.setVisible(true);
            }
        });
    }
}
