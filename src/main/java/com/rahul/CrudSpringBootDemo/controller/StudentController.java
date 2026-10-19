package com.rahul.CrudSpringBootDemo.controller;

import com.rahul.CrudSpringBootDemo.entity.Student;
import com.rahul.CrudSpringBootDemo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {


    private StudentService studentservice;

    @Autowired
    public StudentController(StudentService studentservice){
        this.studentservice =studentservice;
    }

    //create student -- > POST (/app/students)

    @PostMapping("/create")
    public ResponseEntity<Student> createStudent(@RequestBody  Student student){

        Student createdStudent = studentservice.createStudent(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdStudent);

    }
    // read one  student --> GET --> /api/students/create/{id}

    @GetMapping("/get")
    public ResponseEntity<Student> getStudent(@RequestParam Long id){
        Student Studentresp = studentservice.getStudent(id);
        if(Studentresp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Studentresp);
    }



    // Read all student --> GET --> /api/students

    @GetMapping("/getAll")
    public ResponseEntity<List<Student>> getAllStudent(){
        List<Student> studentList = studentservice.getAllStudent();
        if(studentList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentList);

    }

    //update student -->  PUT --> /api/students/{id}

    @PutMapping("/update")
    public ResponseEntity<Student> updateStudent(@RequestParam Long id
    , @RequestBody Student studentReq){
        Student Studentresp = studentservice.updateStudent(id,studentReq);

        if(Studentresp == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(Studentresp);
    }

    // delete student --> DELETE --> /api/students/{id}

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteStudent(@RequestParam Long id){
        Boolean isDeleted = studentservice.deleteStudent(id);
        if(isDeleted ==false){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Record Deleted");
    }

    @PatchMapping("/delete-soft")
    public ResponseEntity<String> deleteStudentSoftly(@RequestParam Long id){
        Boolean isDeleted = studentservice.deleteStudentSoftly(id);

        if(!isDeleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Record Deleted sucessfully");
    }
}
