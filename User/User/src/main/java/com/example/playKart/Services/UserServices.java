package com.example.playKart.Services;

import com.example.playKart.DTO.*;
import com.example.playKart.Exception.AddressNotFoundException;
import com.example.playKart.Exception.UnauthorizedAccess;
import com.example.playKart.Exception.UserAlreadyExists;
import com.example.playKart.Exception.UserNotFoundException;
import com.example.playKart.JwtGenerator.JwtGeneratorInterface;
import com.example.playKart.Entity.Address;
import com.example.playKart.Entity.User;
import com.example.playKart.Repository.AddressRepository;
import com.example.playKart.Repository.UserRepository;
import com.example.playKart.Utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServices {

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

    private static final Logger logger = Logger.getLogger(UserServices.class.getName());

    public ResponseEntity<?> registerUser(RegisterDTO userRegisterDTO) {
        try {
            logger.info("Registering user with email: " + userRegisterDTO.getEmail());
            User newUser = new User();
            newUser.setName(userRegisterDTO.getName());
            newUser.setEmail(userRegisterDTO.getEmail());
            if (userRepo.findByEmail(userRegisterDTO.getEmail()) != null) {
                logger.warning("Email already exists: " + userRegisterDTO.getEmail());
                throw new UserAlreadyExists("Email already exists, please enter a new email");
            }

            String encryptedPassword = passwordServices.encryptPassword(userRegisterDTO.getPassword());
            newUser.setPassword(encryptedPassword);
            newUser.setPhoneNumber(userRegisterDTO.getPhoneNumber());
            newUser.setAdmin(false);

            userRepo.save(newUser);
            ResponseUserDTO responseUserDTO = new ResponseUserDTO(newUser.getName(), newUser.getEmail(), newUser.getPhoneNumber(), newUser.isAdmin());
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
                    .map(user -> new ResponseUserDTO(

                            user.getName(),
                            user.getEmail(),
                            user.getPhoneNumber(),
                            user.isAdmin()
                    ))
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
            ResponseUserDTO responseUserDTO = new ResponseUserDTO(responseUser.getName(), responseUser.getEmail(), responseUser.getPhoneNumber(),responseUser.isAdmin());
            logger.info("Fetched user details successfully for user ID: " + userId);
            return responseUserDTO;
        } catch (Exception e) {
            logger.severe("Error fetching user details: " + e.getMessage());
            throw e;
        }
    }

    public Address addAddress(AddressDTO addressdto, String token) {
        try {
            logger.info("Adding address for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

            Address address = new Address();
            address.setCity(addressdto.getCity());
            address.setStreet(addressdto.getStreet());
            address.setState(addressdto.getState());
            address.setZipCode(addressdto.getZipCode());
            address.setUser(user);
            addressRepository.save(address);
            logger.info("Address added successfully for user ID: " + userId);
            return address;
        } catch (Exception e) {
            logger.severe("Error adding address: " + e.getMessage());
            throw e;
        }
    }

    public List<Address> getUserAddresses(String token) {
        try {
            logger.info("Fetching addresses for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            List<Address> userAddresses = user.getAddresses();


            return userAddresses;
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
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
          //  System.out.println(updateUserDTO.getName());
            if (updateUserDTO.getName()!= null && !updateUserDTO.getName().isEmpty()) user.setName(updateUserDTO.getName());
            if (updateUserDTO.getNewPassword()!= null && !updateUserDTO.getNewPassword().isEmpty()) {
                if (!passwordServices.matches(updateUserDTO.getOldPassword(), user.getPassword())) {
                    logger.warning("Invalid password for email: " + user.getEmail());
                   throw new RuntimeException("Not valid Password");
                }
                String encryptedPassword = passwordServices.encryptPassword(updateUserDTO.getNewPassword());
                user.setPassword(encryptedPassword);
            }
            if (updateUserDTO.getName()!= null && !updateUserDTO.getPhoneNumber().isEmpty()) user.setPhoneNumber(updateUserDTO.getPhoneNumber());
            userRepo.save(user);
            ResponseUserDTO responseUserDTO = new ResponseUserDTO( user.getName(), user.getEmail(), user.getPhoneNumber(), user.isAdmin());
            logger.info("User profile updated successfully for user ID: " + userId);
            return responseUserDTO;
        } catch (Exception e) {
            logger.severe("Error updating user profile: " + e.getMessage());
            throw e;
        }
    }

    public void deleteAddress(Integer id, String token) {
        try {
            logger.info("Deleting address with ID: " + id + " for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

            Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException("No Such address present"));
            if (!user.hasAddress(address)) {
                logger.warning("Unauthorized access attempt to delete address with ID: " + id);
                throw new UnauthorizedAccess("Don't have permission to execute this operation");
            }

            addressRepository.deleteById(id);
            logger.info("Address deleted successfully with ID: " + id);
        } catch (Exception e) {
            logger.severe("Error deleting address: " + e.getMessage());
            throw e;
        }
    }

    public Address getAddressById(Integer id, String token) {
        try {
            logger.info("Fetching address with ID: " + id + " for user with token: " + token);
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            Address userAddress = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException("No such address present"));
            if (userAddress.getUser().getUserId() != userId) {
                logger.warning("Unauthorized access attempt to fetch address with ID: " + id);
                throw new UnauthorizedAccess("Don't have access ");
            }

           // AddressDTO address = new AddressDTO(userAddress.getStreet(), userAddress.getCity(), userAddress.getState(), userAddress.getZipCode());

            return userAddress;
        } catch (Exception e) {
            logger.severe("Error fetching address: " + e.getMessage());
            throw e;
        }
    }

    public ResponseUserDTO getUserDetailsById(int userId) {
        User user = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("No Such User Found"));
        ResponseUserDTO userDetails = new ResponseUserDTO(user.getName(), user.getEmail(), user.getPhoneNumber(), user.isAdmin());
        return userDetails;
    }

    public ResponseUserDTO makeUserAdmin(AdminRequestDTO adminRequestDTO, String token) {
        if(!jwtTokenUtil.isAdmin(token)){
            throw new UnauthorizedAccess("This is a admin functionality");
        }
        User user = userRepo.findByEmail(adminRequestDTO.getUserEmail());
        user.setAdmin(true);
        userRepo.save(user);

        ResponseUserDTO responseUserDTO = new ResponseUserDTO(user.getName(), user.getEmail(), user.getPhoneNumber(), user.isAdmin());
        return responseUserDTO;
    }
}