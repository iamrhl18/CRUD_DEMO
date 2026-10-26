package com.rahul.CrudSpringBootDemo.service;

import com.rahul.CrudSpringBootDemo.exception.ResourceNotFoundException;
import com.rahul.CrudSpringBootDemo.dto.CreateStudentRequestDTO;
import com.rahul.CrudSpringBootDemo.dto.CreateStudentResponseDTO;
import com.rahul.CrudSpringBootDemo.dto.UpdateStudentRequestDTO;
import com.rahul.CrudSpringBootDemo.dto.UpdateStudentResponseDTO;
import com.rahul.CrudSpringBootDemo.entity.Student;
import com.rahul.CrudSpringBootDemo.exception.DuplicateResourceException;
import com.rahul.CrudSpringBootDemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public CreateStudentResponseDTO createStudent(CreateStudentRequestDTO createStudentRequestDTO){
        Student student = mapToEntity(createStudentRequestDTO);
        if(emailExist(student)){
            throw new DuplicateResourceException("Student with email" + student.getEmail()  + " is Already  exist ");
        }
        Student studentresp = studentRepository.save(student);
        return mapToDto(studentresp);


    }

    // Select * from  sudent where id = ?   --> findById

    // select * from  student where id = ? and deleted = false -->findByIdAndDeletedIsFalse

    public CreateStudentResponseDTO getStudent(Long id){
       Student  studentresp =  studentRepository
               .findByIdAndDeletedIsFalse(id)
               .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));

       return mapToDto(studentresp);
    }

    public List<CreateStudentResponseDTO> getAllStudent(){
        List<Student> studentlist = studentRepository.findByDeletedIsFalse();
        List<CreateStudentResponseDTO> result = new ArrayList<>();
        for(Student st : studentlist){
            result.add(mapToDto(st));
        }
        return result;
    }

    public UpdateStudentResponseDTO updateStudent(Long id ,
                                                  UpdateStudentRequestDTO studentReq){

        Student existingstudent =  studentRepository.
                findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " is not found"));

        existingstudent.setName(studentReq.getName());
        existingstudent.setSubject(studentReq.getSubject());
        existingstudent.setRollNo(studentReq.getRollNo());
        existingstudent.setAge(studentReq.getAge());
        existingstudent.setDeleted(false);
        existingstudent.setUpdatedAt(LocalDateTime.now());
         Student savedStudent =  studentRepository.save(existingstudent);
         return mapToUpdateDto(savedStudent);

    }

    public void deleteStudent(Long id){
         Student studentToBeDeleted = studentRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student with id " + id + " not exist"));

        studentRepository.delete(studentToBeDeleted);


    }
    public void deleteStudentSoftly(Long id){
        // Record get
        Student studentToBeDeleted = studentRepository
                .findByIdAndDeletedIsFalse(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student with id " + id + " not exist"));

        studentToBeDeleted.setDeleted(true);
        // save
        studentRepository.save(studentToBeDeleted);
    }

    private Student mapToEntity(CreateStudentRequestDTO createStudentRequestDTO){
        Student student = new Student();
        student.setName(createStudentRequestDTO.getName());
        student.setAge(createStudentRequestDTO.getAge());
        student.setEmail(createStudentRequestDTO.getEmail());
        student.setRollNo(createStudentRequestDTO.getRollNo());
        student.setSubject(createStudentRequestDTO.getSubject());
        student.setCretedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        student.setDeleted(false);
        return student;
    }

    private CreateStudentResponseDTO mapToDto(Student student){
        CreateStudentResponseDTO studentRespDto =  new CreateStudentResponseDTO();
        studentRespDto.setId(student.getId());
        studentRespDto.setName(student.getName());
        studentRespDto.setAge(student.getAge());
        studentRespDto.setEmail(student.getEmail());
        studentRespDto.setRollNo(student.getRollNo());
        studentRespDto.setSubject(student.getSubject());
        studentRespDto.setMessage("student saved sucessfully");
        studentRespDto.setCreatedAt(student.getCretedAt());
        studentRespDto.setUpdatedAt(student.getUpdatedAt());
        return studentRespDto;
    }

    private UpdateStudentResponseDTO mapToUpdateDto(Student studentreq){
        UpdateStudentResponseDTO studentRespDto =  new UpdateStudentResponseDTO();
        studentRespDto.setId(studentreq.getId());
        studentRespDto.setName(studentreq.getName());
        studentRespDto.setAge(studentreq.getAge());
        studentRespDto.setEmail(studentreq.getEmail());
        studentRespDto.setRollNo(studentreq.getRollNo());
        studentRespDto.setSubject(studentreq.getSubject());
        studentRespDto.setMessage("student Updated sucessfully");
        studentRespDto.setUpdatedAt(studentreq.getUpdatedAt());
        return studentRespDto;
    }

    private boolean emailExist(Student student){
        return studentRepository.existsByEmail(student.getEmail());
    }


}
