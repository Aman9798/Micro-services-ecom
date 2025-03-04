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

    public ResponseUserDTO registerUser(RegisterDTO userRegisterDTO) {
        try {
            User newUser = new User();
            newUser.setName(userRegisterDTO.getName());
            newUser.setEmail(userRegisterDTO.getEmail());
            if (userRepo.findByEmail(userRegisterDTO.getEmail()) != null) {
                throw new UserAlreadyExists("Email already exists, please enter a new email");
            }

            String encryptedPassword = passwordServices.encryptPassword(userRegisterDTO.getPassword());
            newUser.setPassword(encryptedPassword);
            newUser.setPhoneNumber(userRegisterDTO.getPhoneNumber());
            newUser.setAdmin(false);

            userRepo.save(newUser);
            ResponseUserDTO responseUserDTO = new ResponseUserDTO(newUser.getUserId(), newUser.getName(), newUser.getEmail(), newUser.getPhoneNumber());
            return responseUserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error registering user", e);
        }
    }

    public List<ResponseUserDTO> getAllUsers(String token) {
        try {
            if (!jwtTokenUtil.isAdmin(token)) {
                throw new UnauthorizedAccess("This is an admin functionality");
            }
            List<User> users = userRepo.findAll();
            List<ResponseUserDTO> responseUserDTOs = users.stream()
                    .map(user -> new ResponseUserDTO(
                            user.getUserId(),
                            user.getName(),
                            user.getEmail(),
                            user.getPhoneNumber()
                    ))
                    .collect(Collectors.toList());
            return responseUserDTOs;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all users", e);
        }
    }

    public ResponseEntity<?> loginUser(LoginDTO userLoginDto) {
        try {
            String userInputEmail = userLoginDto.getEmail();
            String userInputPassword = userLoginDto.getPassword();

            User user = userRepo.findByEmail(userInputEmail);
            if (user == null) {
                return new ResponseEntity<>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
            }

            if (!passwordServices.matches(userInputPassword, user.getPassword())) {
                return new ResponseEntity<>("Invalid Email or Password", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(jwtGenerator.generateToken(user), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException("Error logging in user", e);
        }
    }

    public ResponseUserDTO getUserDetails(Integer userId) {
        try {
            Optional<User> user = userRepo.findById(userId);
            if (!user.isPresent()) {
                throw new UserNotFoundException("User not found");
            }
            User responseUser = user.get();
            ResponseUserDTO responseUserDTO = new ResponseUserDTO(responseUser.getUserId(), responseUser.getName(), responseUser.getEmail(), responseUser.getPhoneNumber());
            return responseUserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user details", e);
        }
    }

    public AddressDTO addAddress(AddressDTO addressdto, String token) {
        try {
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
            return addressdto;
        } catch (Exception e) {
            throw new RuntimeException("Error adding address", e);
        }
    }

    public List<AddressDTO> getUserAddresses(String token) {
        try {
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
            List<Address> userAddresses = user.getAddresses();

            List<AddressDTO> userAddressDTOs = userAddresses.stream()
                    .map(address -> new AddressDTO(
                            address.getStreet(),
                            address.getCity(),
                            address.getState(),
                            address.getZipCode()
                    )).collect(Collectors.toList());
            return userAddressDTOs;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user addresses", e);
        }
    }

    public ResponseUserDTO updateUserProfile(String token, UpdateUserDTO updateUserDTO) {
        try {
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

            if (!updateUserDTO.getName().isEmpty()) user.setName(updateUserDTO.getName());
            if (!updateUserDTO.getPassword().isEmpty()) {
                String encryptedPassword = passwordServices.encryptPassword(updateUserDTO.getPassword());
                user.setPassword(encryptedPassword);
            }
            if (!updateUserDTO.getPhoneNumber().isEmpty()) user.setPhoneNumber(updateUserDTO.getPhoneNumber());
            userRepo.save(user);
            ResponseUserDTO responseUserDTO = new ResponseUserDTO(user.getUserId(), user.getName(), user.getEmail(), user.getPhoneNumber());
            return responseUserDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error updating user profile", e);
        }
    }

    public void deleteAddress(Integer id, String token) {
        try {
            String userID = jwtTokenUtil.getUserId(token);
            Integer userId = Integer.parseInt(userID);
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

            Address address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException("No Such address present"));
            if (!user.hasAddress(address)) {
                throw new UnauthorizedAccess("Don't have permission to execute this operation");
            }

            addressRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting address", e);
        }
    }

    public AddressDTO getAddressById(Integer id, String token) {
        String userID = jwtTokenUtil.getUserId(token);
        Integer userId = Integer.parseInt(userID);
        Address userAddress = addressRepository.findById(id).orElseThrow(()-> new AddressNotFoundException("No such address present"));
        if(userAddress.getUser().getUserId() != userId){
            throw new UnauthorizedAccess("Don't have access ");
        }

        AddressDTO address = new AddressDTO(userAddress.getStreet(),userAddress.getCity(),userAddress.getState(),userAddress.getZipCode());

        return address;
    }
}