package com.online.school.school.service.jpaDaoService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import com.online.school.school.databasefiles.Student;
import com.online.school.school.databasefiles.Subject;
import com.online.school.school.databasefiles.jpaRepositories.StudentRepo;
import com.online.school.school.databasefiles.jpaRepositories.SubjectRepo;
import com.online.school.school.exceptions.StudentNotFoundException;
import com.online.school.school.exceptions.SubjectNotFoundException;

@Service
public class SubjectJpaDaoService {

    @Autowired
    private StudentRepo studentRepoService;

    @Autowired
    private SubjectRepo subjectRepoService;

    public List<Subject> assigneSubjectsToStudent(int stId, List<Subject> subjects) {
        List<Subject> savedSubjects = subjects.stream().map(subjectRepoService::save).collect(Collectors.toList());

        Student student = studentRepoService.findById(stId)
                .orElseThrow(() -> new StudentNotFoundException("Student with Id: " + stId + " Not Found"));

        student.setSubjects(savedSubjects);

        studentRepoService.save(student);

        return savedSubjects;
    }

    public List<Subject> listAssignedSubjectsToStudent(int stID) {
        Optional<Student> studentOptional = studentRepoService.findById(stID);
        if (!studentOptional.isPresent()) {
            throw new StudentNotFoundException("Student with Id: " + stID + " is not Present");
        }
        return studentOptional.get().getSubjects();

    }

    public void deleteSubjectOfStudentBySubId(int stId, int subId) {
        Student student = studentRepoService.findById(stId)
                .orElseThrow(() -> new StudentNotFoundException("Student with Id: " + stId + " Not Found"));

        List<Subject> subjects = student.getSubjects();

        Optional<Subject> subjectOptional = subjects.stream().filter(subject -> subject.getSubjectId() == subId)
                .findFirst();

        if (subjectOptional.isPresent()) {
            subjects.remove(subjectOptional.get());

            student.setSubjects(subjects);

            studentRepoService.save(student);
        } else {
            throw new SubjectNotFoundException("Subject with Id: " + subId + " Not Found!");
        }
    }

}