package com.rahul.CrudSpringBootDemo.service;

import com.rahul.CrudSpringBootDemo.dto.CreateStudentRequestDTO;
import com.rahul.CrudSpringBootDemo.dto.CreateStudentResponseDTO;
import com.rahul.CrudSpringBootDemo.dto.UpdateStudentRequestDTO;
import com.rahul.CrudSpringBootDemo.dto.UpdateStudentResponseDTO;
import com.rahul.CrudSpringBootDemo.entity.Student;
import com.rahul.CrudSpringBootDemo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    public CreateStudentResponseDTO createStudent(CreateStudentRequestDTO createStudentRequestDTO){
        Student student = mapToEntity(createStudentRequestDTO);

        student.setCretedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());
        Student studentresp = studentRepository.save(student);
        return mapToDto(studentresp);


    }

    // Select * from  sudent where id = ?   --> findById

    // select * from  student where id = ? and deleted = false -->findByIdAndDeletedIsFalse

    public CreateStudentResponseDTO getStudent(Long id){
       Optional<Student> studentresp =  studentRepository.findByIdAndDeletedIsFalse(id);
       if(studentresp.isPresent()){
           return mapToDto(studentresp.get());
       }
       return null;
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

        Optional<Student> existingstudent =  studentRepository.findByIdAndDeletedIsFalse(id);

        if(existingstudent.isEmpty()){
            return null;
        }

        Student studentToSave = existingstudent.get();

         studentToSave.setName(studentReq.getName());
         studentToSave.setSubject(studentReq.getSubject());

         studentToSave.setRollNo(studentReq.getRollNo());
         studentToSave.setAge(studentReq.getAge());
         studentToSave.setDeleted(false);
         studentToSave.setUpdatedAt(LocalDateTime.now());
         Student savedStudent =  studentRepository.save(studentToSave);

         return mapToUpdateDto(savedStudent);

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

    private Student mapToEntity(CreateStudentRequestDTO createStudentRequestDTO){
        Student student = new Student();
        student.setName(createStudentRequestDTO.getName());
        student.setAge(createStudentRequestDTO.getAge());
        student.setEmail(createStudentRequestDTO.getEmail());
        student.setRollNo(createStudentRequestDTO.getRollNo());
        student.setSubject(createStudentRequestDTO.getSubject());
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


}
