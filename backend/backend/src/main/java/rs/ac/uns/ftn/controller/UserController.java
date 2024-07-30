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
import rs.ac.uns.ftn.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.UserTokenState;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public User user(@PathVariable String email) {
        logger.info("Find user with email");
        return this.userService.findByEmail(email);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<JsonResponse> registerUser(@RequestBody UserDTO newUser) {

        JsonResponse jsonResponse;

        String validationError = validateUser(newUser);
        if (validationError != null) {
            logger.info(validationError);
            jsonResponse = new JsonResponse(
                    validationError
            );
            return ResponseEntity.badRequest().body(jsonResponse);
        }

        if (newUser.getShippingAddress() == null){
            logger.info("Shipping address is required.");
            jsonResponse = new JsonResponse(
                    "Shipping address is required."
            );
            return ResponseEntity.badRequest().body(jsonResponse);
        }

        userService.createUser(newUser);

        jsonResponse = new JsonResponse(
                "User successfully registered"
        );
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<JsonResponse> registerAdmin(@RequestBody UserDTO newAdmin) {

        JsonResponse jsonResponse;

        String validationError = validateUser(newAdmin);
        if (validationError != null) {
            logger.info(validationError);
            jsonResponse = new JsonResponse(
                    validationError
            );
            return ResponseEntity.badRequest().body(jsonResponse);
        }

        adminService.createAdmin(newAdmin);

        jsonResponse = new JsonResponse(
                "Admin successfully registered"
        );
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/registerWorker")
    public ResponseEntity<JsonResponse> registerWorker(@RequestBody @Valid UserDTO newWorker) {

        JsonResponse jsonResponse;

        String validationError = validateUser(newWorker);
        if (validationError != null) {
            logger.info(validationError);
            jsonResponse = new JsonResponse(
                    validationError
            );
            return ResponseEntity.badRequest().body(jsonResponse);
        }

        workerService.createWorker(newWorker);

        jsonResponse = new JsonResponse(
                "Worker successfully registered"
        );
        return ResponseEntity.ok(jsonResponse);
    }

    @PostMapping("/registerCourier")
    public ResponseEntity<JsonResponse> registerCourier(@RequestBody @Valid UserDTO newCourier) {

        JsonResponse jsonResponse;

        String validationError = validateUser(newCourier);
        if (validationError != null) {
            logger.info(validationError);
            jsonResponse = new JsonResponse(
                    validationError
            );
            return ResponseEntity.badRequest().body(jsonResponse);
        }

        courierService.createCourier(newCourier);

        jsonResponse = new JsonResponse(
                "Courier successfully registered"
        );
        return ResponseEntity.ok(jsonResponse);
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
        JsonResponse jsonResponse = new JsonResponse("Logged out successfully");
        logger.info("Logged out successfully");

        // Return the response
        return ResponseEntity.ok(jsonResponse);
    }


}

