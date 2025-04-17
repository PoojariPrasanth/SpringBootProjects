package com.example.crud.service;


import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.model.User;
import com.example.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> create(User user) {
        User user1;
        try{
            user1=userRepository.save(user);
        }
        catch (Exception e){
            return ResponseEntity.ok("some error occured");
        }

        if(user1!=null){
            return ResponseEntity.ok("registered successfully");
        }
        return ResponseEntity.ok("some error occured");

    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred: " + e.getMessage());
    }

    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    public ResponseEntity<?> findById(int id) {
        Optional<User> user=userRepository.findById(id);
System.out.println("user "+user);

              //  ->new ResourceNotFoundException("not found"+id));

        if(!user.isEmpty()){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.status(404).body("not found "+id);
    }

    public ResponseEntity<?> delete(int id) {
        List<User> users=userRepository.findAll();
        int before=users.size();
        userRepository.deleteById(id);
        users=userRepository.findAll();
        int after=users.size();
        if(before!=after){
            return ResponseEntity.ok("deleted successfully");
        }
        return ResponseEntity.ok("Not available");
    }

    public ResponseEntity<?> updateById(int id,User user) {
        Optional<User> user1=userRepository.findById(id);
        if(!user1.isEmpty()){
            User user2=userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("not found"+id));
            if(user.getId()!=0){
                user2.setId(user.getId());
            }
            if(user.getUserName()!=null){
                user2.setUserName(user.getUserName());
            }
            if(user.getEmail()!=null){
                user2.setEmail(user.getEmail());
            }
            if(user.getPassword()!=null){
                user2.setPassword(user.getPassword());
            }
            if(user.getMobileNumber()!=null){
                user2.setMobileNumber(user.getMobileNumber());
            }
            try{
                user2=userRepository.save(user2);
            }
            catch(Exception e) {
                return ResponseEntity.status(409).body("email already exists");
            }
                return ResponseEntity.ok("update Successful");
        }
        return ResponseEntity.status(404).body("not found");
    }
}
