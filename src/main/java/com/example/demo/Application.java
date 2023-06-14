package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
                                        StudentIdCardRepository studentIdCardRepository){
        return args -> {

            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Integer age = faker.number().numberBetween(17,55);
            Student student = new Student(firstName,
                    lastName,
                    email,
                    age);

            student.addBook(new Book("Spring boot", LocalDateTime.now()));
            student.addBook(new Book("Spring DATA JPA", LocalDateTime.now().minusDays(30)));

            Course  course1 = new Course("Physics", "SMP");
            Course course2 = new Course("Chemistry ", "SMC");

            student.addEnrollment(new Enrollment(new EnrollmentId(1L,1L),student,course1,LocalDateTime.now()));
            student.addEnrollment(new Enrollment(new EnrollmentId(1L,2L),student,course2, LocalDateTime.now().minusMonths(6)));


            StudentIdCard studentIdCard = new StudentIdCard("123456", student);

            student.setStudentIdCard(studentIdCard);
            studentRepository.save(student);

            studentRepository.findById(1L).ifPresent(s -> {
                System.out.println("fetch is lazy");
                List<Book> books = student.getBooks();
                books.forEach(book -> {
                    System.out.println(s.getFirstName() + " borrowed " + book.getBookName() );
                });
            });

        };
    }

    private static void generateRandoStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Integer age = faker.number().numberBetween(17,55);
            Student student = new Student(firstName,
                    lastName,
                    email,
                    age);
            studentRepository.save(student);
        }
    }

}
