package com.example.demoForAmigoscode.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Email taken");
        }

        studentRepository.save(student);
    }

    @Transactional
    public void updateStudent(Long studentId, String newName, String newEmail) {
        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new IllegalStateException("No student with id " + studentId + " was found"));

        if (newName != null && !Objects.equals(newName, student.getName())) {
            student.setName(newName);
        }
        if (newEmail != null && !Objects.equals(newEmail, student.getEmail())) {
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(newEmail);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("Email " + newEmail + " was taken");
            }

            student.setEmail(newEmail);
        }
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);

        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exist");
        }

        studentRepository.deleteById(studentId);
    }
}
