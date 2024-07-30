package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.JsonResponse;
import rs.ac.uns.ftn.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.UserTokenState;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private CourierService courierService;

    @Autowired
    private BasicUserService basicUserService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;


    private final Logger logger;

    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }


    @GetMapping("/allAdmin")
    public List<Admin> allAdmin() {
        return this.adminService.getAll();
    }
    @GetMapping("/allUser")
    public List<User> allUser() {
        return this.userService.getAll();
    }
    @GetMapping("/allWorker")
    public List<Worker> allWorker() {
        return this.workerService.getAll();
    }
    @GetMapping("/allCourier")
    public List<Courier> allCourier() {
        return this.courierService.getAll();
    }
    @GetMapping("/allBasicUser")
    public List<BasicUser> allBasicUser() {
        return this.basicUserService.getAll();
    }

    @GetMapping("/profile/{email}")
    public UserDTO profile(@PathVariable String email) {
        logger.info("Find user with email");
        User user = this.userService.findByEmail(email);

        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setShippingAddress(user.getShippingAddress());
        return userDto;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<JsonResponse> registerUser(@RequestBody UserDTO newUser) {

        String validationError = validateUser(newUser);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        if (newUser.getShippingAddress() == null){
            logger.info("Shipping address is required.");
            return ResponseEntity.badRequest().body(new JsonResponse("Shipping address is required."));
        }

        userService.createUser(newUser);

        return ResponseEntity.ok(new JsonResponse("User successfully registered"));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<JsonResponse> registerAdmin(@RequestBody UserDTO newAdmin) {

        String validationError = validateUser(newAdmin);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        adminService.createAdmin(newAdmin);

        return ResponseEntity.ok(new JsonResponse("Admin successfully registered"));
    }

    @PostMapping("/registerWorker")
    public ResponseEntity<JsonResponse> registerWorker(@RequestBody UserDTO newWorker) {

        String validationError = validateUser(newWorker);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        workerService.createWorker(newWorker);

        return ResponseEntity.ok(new JsonResponse("Worker successfully registered"));
    }

    @PostMapping("/registerCourier")
    public ResponseEntity<JsonResponse> registerCourier(@RequestBody UserDTO newCourier) {

        String validationError = validateUser(newCourier);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        courierService.createCourier(newCourier);

        return ResponseEntity.ok(new JsonResponse("Courier successfully registered"));
    }

    public String validateUser(UserDTO newUser) {
        if (newUser.getEmail() == null || !newUser.getEmail().contains("@gmail.com")) {
            return "Validation error. Email format must be @gmail.com.";
        }
        if (newUser.getFirstName() == null || newUser.getFirstName().isEmpty() ||
                newUser.getLastName() == null || newUser.getLastName().isEmpty() ||
                newUser.getEmail() == null || newUser.getEmail().isEmpty() ||
                newUser.getPassword() == null || newUser.getPassword().isEmpty() ||
                newUser.getPhoneNumber() == null || newUser.getPhoneNumber().isEmpty()) {
            return "Validation error. All fields are required.";
        }
        if (newUser.getPassword().length() < 6) {
            return "Validation error. Password must be at least 6 characters long.";
        }
        BasicUser user = basicUserService.findByEmail(newUser.getEmail());
        if (user != null) {
            return "User with this email already exists.";
        }
        return null;
    }


    @PostMapping("/login")
    public ResponseEntity<UserTokenState> login(@RequestBody JwtAuthenticationRequest authenticationRequest) {

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

            // Set the authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Retrieve the user details
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            // Generate JWT token
            String jwt = tokenUtils.generateToken(user);
            int expiresIn = tokenUtils.getExpiredIn();

            logger.info("Token is created");

            // Return token as a response
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<JsonResponse> logout(HttpServletRequest request, HttpServletResponse response) {

        // Clear the authentication and security context
        SecurityContextHolder.clearContext();

        // Clear the authentication cookie
        TokenUtils.clearAuthenticationCookie(request, response);

        // Create response body
        logger.info("Logged out successfully");

        // Return the response
        return ResponseEntity.ok(new JsonResponse("Logged out successfully"));
    }


}

