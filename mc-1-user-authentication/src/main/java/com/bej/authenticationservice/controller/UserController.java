package com.bej.authenticationservice.controller;


import com.bej.authenticationservice.domain.User;
import com.bej.authenticationservice.exception.UserAlreadyExistException;
import com.bej.authenticationservice.service.SecurityTokenGenerator;
import com.bej.authenticationservice.service.UserService;
import com.bej.authenticationservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
public class UserController {
    private ResponseEntity responseEntity;
    private UserService userService;
    private SecurityTokenGenerator securityTokenGenerator;

  /*
   * Autowiring should be implemented for the UserService and SecurityTokenGenerator. (Use Constructor-based
   * autowiring) Please note that we should not create an object using the new
   * keyword
   */
    public UserController(UserService userService, SecurityTokenGenerator securityTokenGenerator){
        this.userService = userService;
        this.securityTokenGenerator = securityTokenGenerator;
    }


  // first step - register the User

  /*
   * Define a handler method which will save a specific User by reading the
   *  object from request body and save the User details in the
   * database. This handler method should return any one of the status messages
   * basis on different situations:
   * 1. 201(CREATED) - If the user saved successfully.
   * 2. 500(INTERNAL_SERVER_ERROR) - If the User already exists
   *
   * This handler method should map to the URL "/register" using HTTP POST method
   */
  @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user){
        try{
            User savedUser = userService.saveUser(user);
            return new ResponseEntity<>("User with Id"+savedUser.getUserId()+"created successfully",HttpStatus.CREATED);
        } catch (UserAlreadyExistException e){
            return new ResponseEntity<>("User already exists",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


  // second step - login the customer


  /* Define a handler method which will authenticate a User by reading the User
   * object from request body containing the username and password. The username and password should be validated
   * before proceeding ahead with JWT token generation. The User credentials will be validated against the database entries.
   * The error should be return if validation is not successful. If credentials are validated successfully, then JWT
   * token will be generated. The token should be returned back to the caller along with the API response.
   * This handler method should return any one of the status messages basis on different
   * situations:
   * 1. 200(OK) - If login is successful
   * 2. 401(UNAUTHORIZED) - If login is not successful
   *
   * This handler method should map to the URL "/login" using HTTP POST method
   */
  @PostMapping("/login")
  public ResponseEntity loginCustomer(@RequestBody User user) throws UserNotFoundException {
      Map<String, String> map = null;
      try {
          User userObj = userService.findByUsernameAndPassword(user.getUsername(), user.getPassword());
          if (userObj.getUsername().equals(user.getUsername())) {
              map = securityTokenGenerator.generateToken(user);
          }
          responseEntity = new ResponseEntity(map, HttpStatus.OK);
      } catch (Exception e) {
          responseEntity = new ResponseEntity("Try after sometime!!!", HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return responseEntity;
  }



//     Define a handler method that will get all  User from the database the
//     handler mapping url will be "/api/v1/userservice/users"
//    Status will be
//    1. 200(OK) - If all Users are successfully retrieved
//    2. 500(INTERNAL_SERVER_ERROR) - If any other error occurs
@GetMapping("/user/fetchdata")
    public ResponseEntity<List<User>> getAllUsers(){
      try{
          List<User> users = userService.getAllUsers();
          return new ResponseEntity<>(users,HttpStatus.OK);
      } catch (Exception e){
          return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }


  //    Define a handler method that will delete a  User
  //    the handler mapping url will be "/api/v1/userservice/{userId}"
//    Status will be
//    1. 200(OK) - If delete is successful
//    2. 500(INTERNAL_SERVER_ERROR) - If any other error occurs
  @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable int userId){
      try{
          boolean deleted = userService.deleteUser(userId);
          if(deleted){
              return new ResponseEntity<>("User with Id" + userId + "deleted successfully",HttpStatus.OK);
          }else{
              return new ResponseEntity<>("Failed to delete customer", HttpStatus.OK);
          }
      }catch (UserNotFoundException e){
          return new ResponseEntity<>("Customer with ID " + userId + " not found", HttpStatus.OK);
      }
    }
}
