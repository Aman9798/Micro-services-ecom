package com.example.user.Service;

import com.example.user.DTO.*;
import com.example.user.Exception.AddressNotFoundException;
import com.example.user.Exception.UnauthorizedAccess;
import com.example.user.Exception.UserAlreadyExists;
import com.example.user.Exception.UserNotFoundException;
import com.example.user.JwtGenerator.JwtGeneratorInterface;
import com.example.user.Entity.Address;
import com.example.user.Entity.User;
import com.example.user.Mapper.AddressMapper;
import com.example.user.Mapper.UserMapper;
import com.example.user.Repository.AddressRepository;
import com.example.user.Repository.UserRepository;
import com.example.user.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    PasswordService passwordServices;

    @Autowired
    JwtGeneratorInterface jwtGenerator;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    public ResponseEntity<?> registerUser(RegisterDTO userRegisterDTO) {
        try {
            logger.info("Registering user with email: " + userRegisterDTO.getEmail());
            if (userRepo.findByEmail(userRegisterDTO.getEmail()) != null) {
                logger.warning("Email already exists: " + userRegisterDTO.getEmail());
                throw new UserAlreadyExists("Email already exists, please enter a new email");
            }

            User newUser = User.builder()
                    .name(userRegisterDTO.getName())
                    .email(userRegisterDTO.getEmail())
                    .phoneNumber(userRegisterDTO.getPhoneNumber())
                    .isAdmin(false)
                    .build();

            String encryptedPassword = passwordServices.encryptPassword(userRegisterDTO.getPassword());
            newUser.setPassword(encryptedPassword);

            userRepo.save(newUser);
            ResponseUserDTO responseUserDTO = UserMapper.convertToResponseUserDTO(newUser);
            logger.info("User registered successfully with ID: " + newUser.getUserId());
            return new ResponseEntity<>(jwtGenerator.generateToken(newUser), HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error registering user: " + e.getMessage());
            throw e;
        }
    }

    public List<ResponseUserDTO> getAllUsers(String token) {
        try {
            logger.info("Fetching all users");
            if (!jwtTokenUtil.isAdmin(token)) {
                logger.warning("Unauthorized access attempt");
                throw new UnauthorizedAccess("This is an admin functionality");
            }
            List<User> users = userRepo.findAll();
            List<ResponseUserDTO> responseUserDTOs = users.stream()
                    .map(UserMapper::convertToResponseUserDTO)
                    .collect(Collectors.toList());
            logger.info("Fetched all users successfully");
            return responseUserDTOs;
        } catch (Exception e) {
            logger.severe("Error fetching all users: " + e.getMessage());
            throw e;
        }
    }

    public ResponseEntity<?> loginUser(LoginDTO userLoginDto) {
        try {
            logger.info("Logging in user with email: " + userLoginDto.getEmail());
            String userInputEmail = userLoginDto.getEmail();
            String userInputPassword = userLoginDto.getPassword();

            User user = userRepo.findByEmail(userInputEmail);
            if (user == null) {
                logger.warning("Invalid email: " + userInputEmail);
                return new ResponseEntity<>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
            }

            if (!passwordServices.matches(userInputPassword, user.getPassword())) {
                logger.warning("Invalid password for email: " + userInputEmail);
                return new ResponseEntity<>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
            }

            logger.info("User logged in successfully with email: " + userInputEmail);
            return new ResponseEntity<>(jwtGenerator.generateToken(user), HttpStatus.OK);
        } catch (Exception e) {
            logger.severe("Error logging in user: " + e.getMessage());
            throw e;
        }
    }

    public ResponseUserDTO getUserDetails(String token) {
        try {
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            logger.info("Fetching details for user ID: " + userId);
            Optional<User> user = userRepo.findById(userId);
            if (!user.isPresent()) {
                logger.warning("User not found with ID: " + userId);
                throw new UserNotFoundException("User not found");
            }
            User responseUser = user.get();
            ResponseUserDTO responseUserDTO = UserMapper.convertToResponseUserDTO(responseUser);
            logger.info("Fetched user details successfully for user ID: " + userId);
            return responseUserDTO;
        } catch (Exception e) {
            logger.severe("Error fetching user details: " + e.getMessage());
            throw e;
        }
    }

    public AddressResponseDTO addAddress(AddressRequestDTO addressRequest, String token) {
        try {
            logger.info("Adding address for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

            Address address = AddressMapper.convertToAddress(addressRequest);
            address.setUser(user);

            addressRepository.save(address);
            logger.info("Address added successfully for user ID: " + userId);

            return AddressMapper.convertToAddressResponseDTO(address);
        } catch (Exception e) {
            logger.severe("Error adding address: " + e.getMessage());
            throw e;
        }
    }

    public List<AddressResponseDTO> getUserAddresses(String token) {
        try {
            logger.info("Fetching addresses for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            List<Address> userAddresses = user.getAddresses();

            return userAddresses.stream()
                    .map(AddressMapper::convertToAddressResponseDTO)
                    .toList();
        } catch (Exception e) {
            logger.severe("Error fetching addresses: " + e.getMessage());
            throw e;
        }
    }

    public ResponseUserDTO updateUserProfile(String token, UpdateUserDTO updateUserDTO) {
        try {
            logger.info("Updating user profile for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);

            User user = userRepo.findById(userId)
                    .orElseThrow(
                            () -> new UserNotFoundException("User not found with id: " + userId)
                    );


            if (updateUserDTO.getName()!= null && !updateUserDTO.getName().isEmpty()) {
                user.setName(updateUserDTO.getName());
            }

            if (updateUserDTO.getNewPassword()!= null && !updateUserDTO.getNewPassword().isEmpty()) {
                if (!passwordServices.matches(updateUserDTO.getOldPassword(), user.getPassword())) {
                    logger.warning("Invalid password for email: " + user.getEmail());
                   throw new RuntimeException("Not valid Password");
                }
                String encryptedPassword = passwordServices.encryptPassword(updateUserDTO.getNewPassword());
                user.setPassword(encryptedPassword);
            }

            if (updateUserDTO.getName()!= null && !updateUserDTO.getPhoneNumber().isEmpty()){
                user.setPhoneNumber(updateUserDTO.getPhoneNumber());
            }

            userRepo.save(user);
            logger.info("User profile updated successfully for user ID: " + userId);

            return UserMapper.convertToResponseUserDTO(user);
        } catch (Exception e) {
            logger.severe("Error updating user profile: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAddress(Integer addressId, String token) {
        try {
            logger.info("Deleting address with ID: " + addressId + " for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

            Address address = addressRepository.findById(addressId)
                    .orElseThrow(
                            () -> new AddressNotFoundException("No Such address present")
                    );

            if (!user.hasAddress(address)) {
                logger.warning("Unauthorized access attempt to delete address with ID: " + addressId);
                throw new UnauthorizedAccess("Don't have permission to execute this operation");
            }

            addressRepository.deleteById(addressId);
            logger.info("Address deleted successfully with ID: " + addressId);
        } catch (Exception e) {
            logger.severe("Error deleting address: " + e.getMessage());
            throw e;
        }
    }

    public AddressResponseDTO getAddressById(Integer addressId, String token) {
        try {
            logger.info("Fetching address with ID: " + addressId + " for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);

            Address userAddress = addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("No such address present"));

            if (userAddress.getUser().getUserId() != userId) {
                logger.warning("Unauthorized access attempt to fetch address with ID: " + addressId);
                throw new UnauthorizedAccess("Don't have access ");
            }

            return AddressMapper.convertToAddressResponseDTO(userAddress);
        } catch (Exception e) {
            logger.severe("Error fetching address: " + e.getMessage());
            throw e;
        }
    }

    public ResponseUserDTO getUserDetailsById(int userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(
                        ()-> new UserNotFoundException("No Such User Found")
                );
        return UserMapper.convertToResponseUserDTO(user);
    }

    public ResponseUserDTO makeUserAdmin(AdminRequestDTO adminRequest, String token) {
        if(!jwtTokenUtil.isAdmin(token)){
            throw new UnauthorizedAccess("This is a admin functionality");
        }
        User user = userRepo.findByEmail(adminRequest.getUserEmail());
        user.setAdmin(true);
        userRepo.save(user);

        return UserMapper.convertToResponseUserDTO(user);
    }
}