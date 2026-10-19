package com.rahul.CrudSpringBootDemo.service;

import com.rahul.CrudSpringBootDemo.entity.Student;
import com.rahul.CrudSpringBootDemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student studentreq){

        // Buiseness logic
        studentreq.setDeleted(false);
        Student studentresp = studentRepository.save(studentreq);
        return studentresp;

    }

    // Select * from  sudent where id = ?   --> findById

    // select * from  student where id = ? and deleted = false -->findByIdAndDeletedIsFalse

    public Student getStudent(Long id){
       Optional<Student> studentresp =  studentRepository.findByIdAndDeletedIsFalse(id);
       if(studentresp.isPresent()){
           return studentresp.get();
       }
       return null;
    }

    public List<Student> getAllStudent(){
        List<Student> studentlist = studentRepository.findByDeletedIsFalse();
        return studentlist;
    }

    public Student updateStudent( Long id ,
                                  Student student){
        Optional<Student> existingstudent =  studentRepository.findByIdAndDeletedIsFalse(id);
        if(existingstudent.isEmpty()){
            return null;
        }
        Student studentToSave = existingstudent.get();
         studentToSave.setName(student.getName());
         studentToSave.setSubject(student.getSubject());
         studentToSave.setEmail(student.getEmail());
         studentToSave.setRollNo(student.getRollNo());
         studentToSave.setAge(student.getAge());
         studentToSave.setDeleted(false);

         return studentRepository.save(studentToSave);

    }

    public boolean deleteStudent(Long id){
        boolean isPresent = studentRepository.existsById(id);
        if(!isPresent) return false;

        studentRepository.deleteById(id);

        return true;

    }
    public boolean deleteStudentSoftly(Long id){
        // Record get
        Optional<Student> existingStudent = studentRepository.findByIdAndDeletedIsFalse(id);
        if(existingStudent.isEmpty()){
            return false;
        }
        // deleted = 1

        Student studentToSave = existingStudent.get();
        studentToSave.setDeleted(true);
        // save
        studentRepository.save(studentToSave);
        return true;
    }


}
