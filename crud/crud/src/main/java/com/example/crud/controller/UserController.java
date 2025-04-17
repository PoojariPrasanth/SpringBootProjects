package com.example.crud.controller;


import com.example.crud.model.User;
import com.example.crud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("create")
    public ResponseEntity<?> create(@RequestBody User user){
        return userService.create(user);

    }

    @GetMapping("findAll")
    public ResponseEntity<?> findAll(){
        return userService.findAll();
    }

    @GetMapping("findById/{id}")
    public ResponseEntity<?> findById(@PathVariable int id){
        return userService.findById(id);

    }

    @DeleteMapping("deleteById/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id){
        return userService.delete(id);
    }

    @PutMapping("updateById/{id}")
    public ResponseEntity<?> updateById(@PathVariable int id,@RequestBody User user){
        return userService.updateById(id,user);
    }

}
