package com.example.ECommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import com.example.ECommerce.dto.request.UserRequestDTO;
import com.example.ECommerce.dto.response.UserResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
// import com.example.ECommerce.repository.UserRepository;
import com.example.ECommerce.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/ecommerce/users/")
public class UserController {

    @Autowired
    private UserService userService;

    // api to fetch all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> fetchAllUsers() {
        return ResponseEntity.ok(userService.fetchAllUsers());
    }

    // api to fetch by phone number
    @GetMapping("phone/{phoneNo}")
    public ResponseEntity<UserResponseDTO> fetchByPhone(@PathVariable String phoneNo) {
    //   log.info("Fetching all users...");
        return ResponseEntity.ok(userService.fetchUsersByPhone(phoneNo));
    }

    @GetMapping("phones/id/{id}")
    public ResponseEntity<List<String>> fetchIdByPhoneNumber() {
        List<String> phoneNumbers = userService.fetchAllPhoneNumbers();
        return ResponseEntity.ok(phoneNumbers);
    }

    // api to fetch users by id
    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> fetchUsersById(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.fetchUsersById(id));
    }

    // api to fetch existing user by phone number
    @GetMapping("phones")
    public ResponseEntity<List<String>> fetchAllPhoneNumbers() {
        List<String> phoneNumbers = userService.fetchAllPhoneNumbers();
        return ResponseEntity.ok(phoneNumbers);
    }


    // api to create a user object
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) throws BadRequestException {
        UserResponseDTO createdUserDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.ok(createdUserDTO);
    }


    // api to update user data given its id
    @PutMapping("{id}")
    public ResponseEntity<UserResponseDTO> updateUser
    (@PathVariable Integer id, @RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userRequestDTO));
    }

    // delete user REST api
    @DeleteMapping("{id}")
    public ResponseEntity<HttpStatus> deleteUSerById(@PathVariable Integer id) {
        return userService.deleteUser(id);
    }
}
