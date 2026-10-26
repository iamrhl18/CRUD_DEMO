package com.rahul.CrudSpringBootDemo.repository;

import com.rahul.CrudSpringBootDemo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository             // JpaRepo want two argument 1. which class it will handle and second is type of primary key
public interface StudentRepository  extends JpaRepository<Student , Long> {
        Optional<Student> findByIdAndDeletedIsFalse(Long id);
        List<Student> findByDeletedIsFalse();
        Boolean existsByEmail(String studentemail);

}




/*
         JPA jakarta persistence api : which have the method through which we
         can directly perform the operations to the databases without writing the sql queries

           1. create() --> save() : insert into
           2. Read All--> findAll() : select * from table
           3. Read --> find() : select * from student where id is 1
           4. delete --> deleteById()
           5. update --> save()
                     --existById() : return that id is present or not
        * */