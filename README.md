# lab_java_assignment_5
Student Record Management System
Pointwise Theory for Submission

Objective:

Implement a Student Record Management System with persistent storage, file handling, exception handling, multithreading, and OOP concepts.

Core Features:

Add, update, delete, search, view student records.

Persistent storage using BufferedReader and BufferedWriter.

Multithreading to simulate loading operations.

Sorting students by marks using Comparator.

Custom Exception (StudentNotFoundException) for invalid operations.

Class Design:

Person (abstract): Contains common fields name and email.

Student (extends Person): Contains rollNo, course, marks, grade, and methods inputDetails(), displayDetails(), calculateGrade().

RecordActions (interface): Declares methods for CRUD operations.

StudentManager (implements RecordActions): Handles student records using ArrayList<Student>.

Loader (implements Runnable): Simulates loading via multithreading.

FileUtil: Handles reading and writing students to/from a file.

Java Concepts Applied:

OOP: Inheritance, abstraction, interfaces, polymorphism.

Exception Handling: try-catch-finally and custom exceptions.

Collections API: ArrayList for student records management.

File I/O: BufferedReader, BufferedWriter for persistence.

Multithreading: Using Thread to simulate delays and improve responsiveness.

Sorting & Iterator: Sort by marks and display students using iterator.

Advantages:

Modular and reusable code.

Persistent storage ensures data is saved across sessions.

Safe execution with proper input validation and exception handling.

User-friendly, menu-driven interface with responsive loading simulation.
