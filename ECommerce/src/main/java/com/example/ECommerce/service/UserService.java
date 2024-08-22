package com.example.ECommerce.service;

import com.example.ECommerce.dto.request.UserRequestDTO;
import com.example.ECommerce.dto.response.UserResponseDTO;
import com.example.ECommerce.exception.BadRequestException;
import com.example.ECommerce.exception.IllegalArgumentException;
import com.example.ECommerce.exception.ResourceNotFoundException;
import com.example.ECommerce.mapper.UserMapper;
import com.example.ECommerce.model.User;
import com.example.ECommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.ECommerce.util.ECommerceUtils.validateId;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public void validateEmailAndPhoneOnUpdate(User user, UserRequestDTO userRequestDTO) {
        logger.info("Validating email and phone for user update: {}", userRequestDTO);
        if ((!Objects.equals(user.getEmail(), userRequestDTO.getEmail())) && userRepository.existsByEmail(userRequestDTO.getEmail())) {
            logger.warn("Email already taken: {}", userRequestDTO.getEmail());
            throw new BadRequestException("Oops.. this email id is already taken by someone!");
        }

        if ((!Objects.equals(user.getPhoneNo(), userRequestDTO.getPhoneNo())) && userRepository.existsByPhoneNo(userRequestDTO.getPhoneNo())) {
            logger.warn("Phone number already taken: {}", userRequestDTO.getPhoneNo());
            throw new BadRequestException("Oops.. this phone number is already taken by someone!");
        }
    }

    public boolean isInvalidEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        boolean invalid = email == null || email.trim().isEmpty() || !email.matches(emailPattern);
        if (invalid) {
            logger.warn("Invalid email: {}", email);
        }
        return invalid;
    }

    public boolean isInvalidPhoneNumber(String phoneNo) {
        String phoneNumberPattern = "^[0-9]{10}$";
        boolean invalid = phoneNo == null || !phoneNo.matches(phoneNumberPattern);
        if (invalid) {
            logger.warn("Invalid phone number: {}", phoneNo);
        }
        return invalid;
    }

    public void isEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            logger.warn("Email already exists: {}", email);
            throw new BadRequestException("Oops.. this email id is already taken by someone!");
        }
    }

    public void isPhoneExists(String phoneNo) {
        if (userRepository.existsByPhoneNo(phoneNo)) {
            logger.warn("Phone number already exists: {}", phoneNo);
            throw new BadRequestException("OOps.. this phone number is already taken by someone!");
        }
    }

    public void validateUser(UserRequestDTO userRequestDTO) {
        logger.info("Validating user: {}", userRequestDTO);
        if (userRequestDTO.getName() == null || userRequestDTO.getName().trim().isEmpty()) {
            logger.error("Validation failed: Name is required.");
            throw new BadRequestException("Name is required.");
        }

        if (isInvalidEmail(userRequestDTO.getEmail())) {
            throw new BadRequestException("Valid email is required.");
        }

        if (isInvalidPhoneNumber(userRequestDTO.getPhoneNo())) {
            throw new BadRequestException("Valid phone number is required.");
        }
    }

    public List<UserResponseDTO> fetchAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::userToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public List<String> fetchAllPhoneNumbers() {
        logger.info("Fetching all phone numbers");
        return userRepository.findAll().stream()
                .map(User::getPhoneNo)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) throws IllegalArgumentException, BadRequestException {
        logger.info("Creating user: {}", userRequestDTO);
        validateUser(userRequestDTO);
        isEmailExists(userRequestDTO.getEmail());
        isPhoneExists(userRequestDTO.getPhoneNo());
        if (userRepository.findByPhoneNo(userRequestDTO.getPhoneNo()).isPresent()) {
            throw new BadRequestException("User with this phone number already exists.");
        }

        User user = userMapper.userRequestDtoToUser(userRequestDTO);
        User savedUser = userRepository.save(user);
        logger.info("User created successfully: {}", savedUser);
        return userMapper.userToUserResponseDTO(savedUser);
    }

    public UserResponseDTO fetchUsersByPhone(String phoneNo) throws BadRequestException, ResourceNotFoundException {
        logger.info("Fetching user by phone number: {}", phoneNo);
        if (isInvalidPhoneNumber(phoneNo)) {
            throw new BadRequestException("Valid phone number is required.");
        }
        User user = userRepository.findByPhoneNo(phoneNo)
                .orElseThrow(() -> {
                    logger.error("User not found for phone number: {}", phoneNo);
                    return new ResourceNotFoundException("User not found for phone: " + phoneNo);
                });

        logger.info("User fetched successfully for phone number: {}", phoneNo);
        return userMapper.userToUserResponseDTO(user);
    }

    public UserResponseDTO fetchUsersById(Integer id) throws BadRequestException, ResourceNotFoundException {
        logger.info("Fetching user by ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", id);
                    return new ResourceNotFoundException("User not found for id: " + id);
                });
        validateId(id, "User");

        logger.info("User fetched successfully for ID: {}", id);
        return userMapper.userToUserResponseDTO(user);
    }

    @Transactional
    public UserResponseDTO updateUser(Integer id, UserRequestDTO userRequestDTO) throws BadRequestException, ResourceNotFoundException {
        logger.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", id);
                    return new ResourceNotFoundException("User not found for id: " + id);
                });
        validateId(id, "User");
        validateUser(userRequestDTO);
        validateEmailAndPhoneOnUpdate(user, userRequestDTO);

        userMapper.updateUserFromUserRequestDto(userRequestDTO, user);
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully: {}", updatedUser);
        return userMapper.userToUserResponseDTO(updatedUser);
    }

    @Transactional
    public ResponseEntity<HttpStatus> deleteUser(Integer id) throws ResourceNotFoundException {
        logger.info("Deleting user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("User not found for ID: {}", id);
                    return new ResourceNotFoundException("User not found for id: " + id);
                });

        userRepository.delete(user);
        logger.info("User deleted successfully for ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
