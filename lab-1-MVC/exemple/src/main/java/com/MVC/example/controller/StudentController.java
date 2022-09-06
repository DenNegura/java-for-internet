package com.MVC.example.controller;

import com.MVC.example.model.Student;
import com.MVC.example.model.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StudentController {

    @Autowired
    private StudentRepo studentRepo;

    @GetMapping("/")
    public String addStudents(Model model) {
        Iterable<Student> students = studentRepo.findAll();

        model.addAttribute("students", students);
        model.addAttribute("student", new Student());
        return "view";
    }
    @PostMapping("/")
    public String setStudent(@ModelAttribute("student") Student student, Model model) {
        studentRepo.save(student);
        Iterable<Student> students = studentRepo.findAll();
        model.addAttribute("students", students);
        return "view";
    }

}
