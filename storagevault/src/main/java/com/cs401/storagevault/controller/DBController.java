package com.cs401.storagevault.controller;

import com.cs401.storagevault.dbservices.UserService;
import com.cs401.storagevault.model.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class DBController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(path="/save/user", headers = {
            "content-type=application/json" }, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    public void addUser(@RequestBody User user) {
        System.out.println(user.toString());
        userService.saveUser(user);
    }

    @RequestMapping(path="/get/user", method = RequestMethod.GET)
    public void getUserByEmail(@RequestParam String email) {
        try {
            System.out.println("inside:"+email);
            List<User> _user = userService.getUserByEmail(email);
            System.out.println("emails::"+_user);
           // return new ResponseEntity<>(_user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAll/users")
    public List<User> getUsersList() {
        return userService.listAllUsers();
    }
}
