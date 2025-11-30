import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

abstract class Person {
    protected String name;
    protected String email;

    public abstract void displayInfo();
}

class Student extends Person implements Serializable {
    private Integer rollNo;
    private String course;
    private Double marks;
    private char grade;

    public Student() {}

    public Student(Integer rollNo, String name, String email, String course, Double marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
        calculateGrade();
    }

    public void inputDetails(Scanner sc) {
        System.out.print("Enter Roll No: ");
        this.rollNo = Integer.valueOf(sc.nextLine());
        System.out.print("Enter Name: ");
        this.name = sc.nextLine();
        System.out.print("Enter Email: ");
        this.email = sc.nextLine();
        System.out.print("Enter Course: ");
        this.course = sc.nextLine();
        System.out.print("Enter Marks: ");
        this.marks = Double.valueOf(sc.nextLine());
        calculateGrade();
    }

    private void calculateGrade() {
        if (marks >= 90) grade = 'A';
        else if (marks >= 75) grade = 'B';
        else if (marks >= 50) grade = 'C';
        else grade = 'D';
    }

    public Integer getRollNo() { return rollNo; }
    public String getName() { return name; }
    public Double getMarks() { return marks; }

    @Override
    public void displayInfo() {
        System.out.println("Student Info:");
        System.out.println("Roll No: " + rollNo);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Course: " + course);
        System.out.println("Marks: " + marks);
    }

    @Override
    public String toString() {
        return rollNo + "," + name + "," + email + "," + course + "," + marks;
    }

    public static Student fromString(String line) {
        String[] parts = line.split(",");
        return new Student(Integer.valueOf(parts[0]), parts[1], parts[2], parts[3], Double.valueOf(parts[4]));
    }
}

interface RecordActions {
    void addStudent(Student s);
    void deleteStudent(String name) throws StudentNotFoundException;
    void updateStudent(String name, Student newData) throws StudentNotFoundException;
    void searchStudent(String name) throws StudentNotFoundException;
    void viewAllStudents();
}

class StudentManager implements RecordActions {
    private List<Student> students;

    public StudentManager(List<Student> students) {
        this.students = students;
    }

    @Override
    public void addStudent(Student s) {
        for (Student st : students) {
            if (st.getRollNo().equals(s.getRollNo())) {
                System.out.println("Duplicate Roll No! Cannot add student.");
                return;
            }
        }
        students.add(s);
    }

    @Override
    public void deleteStudent(String name) throws StudentNotFoundException {
        Iterator<Student> it = students.iterator();
        boolean found = false;
        while (it.hasNext()) {
            Student s = it.next();
            if (s.getName().equalsIgnoreCase(name)) {
                it.remove();
                System.out.println("Student record deleted.");
                found = true;
                break;
            }
        }
        if (!found) throw new StudentNotFoundException("Student not found.");
    }

    @Override
    public void updateStudent(String name, Student newData) throws StudentNotFoundException {
        boolean found = false;
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getName().equalsIgnoreCase(name)) {
                students.set(i, newData);
                System.out.println("Student record updated.");
                found = true;
                break;
            }
        }
        if (!found) throw new StudentNotFoundException("Student not found.");
    }

    @Override
    public void searchStudent(String name) throws StudentNotFoundException {
        boolean found = false;
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                s.displayInfo();
                found = true;
                break;
            }
        }
        if (!found) throw new StudentNotFoundException("Student not found.");
    }

    @Override
    public void viewAllStudents() {
        Iterator<Student> it = students.iterator();
        while (it.hasNext()) {
            it.next().displayInfo();
            System.out.println();
        }
    }

    public void sortByMarks() {
        students.sort(Comparator.comparing(Student::getMarks).reversed());
        System.out.println("Sorted Student List by Marks:");
        viewAllStudents();
    }

    public List<Student> getStudents() { return students; }
}

class Loader implements Runnable {
    public void run() {
        try {
            System.out.print("Loading");
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(500);
            }
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println("Loading interrupted.");
        }
    }
}

class FileUtil {
    public static void writeToFile(String filename, List<Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Student s : students) {
                bw.write(s.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    public static List<Student> readFromFile(String filename) {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                students.add(Student.fromString(line));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Starting with empty list.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return students;
    }
}

class StudentNotFoundException extends Exception {
    public StudentNotFoundException(String msg) { super(msg); }
}

public class StudentRecordSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String filename = "students.txt";

        List<Student> students = FileUtil.readFromFile(filename);
        StudentManager manager = new StudentManager(students);

        Student s1 = new Student(101, "Rahul", "rahul@mail.com", "B.Tech", 85.0);
        Student s2 = new Student(102, "Ankit", "ankit@mail.com", "B.Tech", 90.5);
        Student s3 = new Student(103, "Riya", "riya@mail.com", "M.Tech", 91.0);

        manager.addStudent(s1);
        manager.addStudent(s2);
        manager.addStudent(s3);

        int choice;
        do {
            System.out.println("===== Capstone Student Menu =====");
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Search by Name");
            System.out.println("4. Delete by Name");
            System.out.println("5. Sort by Marks");
            System.out.println("6. Save and Exit");
            System.out.print("Enter choice: ");
            choice = Integer.parseInt(sc.nextLine());

            try {
                switch (choice) {
                    case 1:
                        Student newS = new Student();
                        newS.inputDetails(sc);
                        Thread t = new Thread(new Loader());
                        t.start();
                        t.join();
                        manager.addStudent(newS);
                        break;
                    case 2:
                        manager.viewAllStudents();
                        break;
                    case 3:
                        System.out.print("Enter name to search: ");
                        manager.searchStudent(sc.nextLine());
                        break;
                    case 4:
                        System.out.print("Enter name to delete: ");
                        manager.deleteStudent(sc.nextLine());
                        break;
                    case 5:
                        manager.sortByMarks();
                        break;
                    case 6:
                        Thread t2 = new Thread(new Loader());
                        t2.start();
                        t2.join();
                        FileUtil.writeToFile(filename, manager.getStudents());
                        System.out.println("Saved and exiting.");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (StudentNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        } while (choice != 6);
        sc.close();
    }
}
