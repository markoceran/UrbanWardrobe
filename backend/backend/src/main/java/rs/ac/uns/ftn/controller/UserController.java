package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.*;
import rs.ac.uns.ftn.helper.TokenUtils;
import rs.ac.uns.ftn.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    AuthenticationManager authenticationManager;

    @Autowired
    TokenUtils tokenUtils;


    private final Logger logger;


    public UserController() {
        this.logger = Logger.getLogger(String.valueOf(UserController.class));
    }


    @GetMapping("/wishlist/{email}")
    public Wishlist wishlist(@PathVariable String email) {
        logger.info("Find user with email");
        Wishlist wishlistDTO = this.userService.getUserWishlist(email);
        return wishlistDTO;
    }

    @GetMapping("/basket/{email}")
    public Basket basket(@PathVariable String email) {
        logger.info("Find user with email");
        Basket basketDTO = this.userService.getUserBasket(email);
        return basketDTO;
    }

    @GetMapping("/profile/{email}")
    public UserDTO profile(@PathVariable String email) {
        logger.info("Find user with email");
        UserDTO userDTO = this.userService.getUserProfile(email);
        return userDTO;
    }

    @PostMapping("/registerUser")
    public ResponseEntity<JsonResponse> registerUser(@RequestBody UserDTO newUser) {

        String validationErrorUser = validateUser(newUser);
        if (validationErrorUser != null) {
            logger.info(validationErrorUser);
            return ResponseEntity.badRequest().body(new JsonResponse(validationErrorUser));
        }

        String validationErrorShippingAddress = validateShippingAddress(newUser.getShippingAddress());
        if (validationErrorShippingAddress != null) {
            logger.info(validationErrorShippingAddress);
            return ResponseEntity.badRequest().body(new JsonResponse(validationErrorShippingAddress));
        }

        User user = userService.createUser(newUser);
        if(user == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("User with this email already exists."));
        }

        return ResponseEntity.ok(new JsonResponse("User successfully registered"));
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<JsonResponse> registerAdmin(@RequestBody UserDTO newAdmin) {

        String validationError = validateUser(newAdmin);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        Admin admin = adminService.createAdmin(newAdmin);
        if(admin == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("User with this email already exists."));
        }

        return ResponseEntity.ok(new JsonResponse("Admin successfully registered"));
    }

    @PostMapping("/registerWorker")
    public ResponseEntity<JsonResponse> registerWorker(@RequestBody UserDTO newWorker) {

        String validationError = validateUser(newWorker);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        Worker worker = workerService.createWorker(newWorker);
        if(worker == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("User with this email already exists."));
        }

        return ResponseEntity.ok(new JsonResponse("Worker successfully registered"));
    }

    @PostMapping("/registerCourier")
    public ResponseEntity<JsonResponse> registerCourier(@RequestBody UserDTO newCourier) {

        String validationError = validateUser(newCourier);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        Courier courier = courierService.createCourier(newCourier);
        if(courier == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("User with this email already exists."));
        }

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
        return null;
    }

    public String validateUserInfo(UserDTO newUser) {
        if (newUser.getFirstName() == null || newUser.getFirstName().isEmpty() ||
                newUser.getLastName() == null || newUser.getLastName().isEmpty() ||
                newUser.getPassword() == null || newUser.getPassword().isEmpty() ||
                newUser.getPhoneNumber() == null || newUser.getPhoneNumber().isEmpty()) {
            return "Validation error. All fields are required.";
        }
        if (newUser.getPassword().length() < 6) {
            return "Validation error. Password must be at least 6 characters long.";
        }
        return null;
    }

    public String validateShippingAddress(ShippingAddress shippingAddress) {
        if (shippingAddress == null) {
            return "Validation error. Shipping address is required.";
        }
        if (shippingAddress.getCountry() == null || shippingAddress.getCountry().isEmpty() ||
                shippingAddress.getCity() == null || shippingAddress.getCity().isEmpty() ||
                shippingAddress.getStreet() == null || shippingAddress.getStreet().isEmpty() ||
                shippingAddress.getPostalCode() == null || shippingAddress.getPostalCode().isEmpty()) {
            return "Validation error. All fields are required.";
        }
        if (shippingAddress.getNumber() <= 0) {
            return "Validation error. Address number must be greater than zero.";
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

        try{
            // Clear the authentication and security context
            SecurityContextHolder.clearContext();

            // Clear the authentication cookie
            TokenUtils.clearAuthenticationCookie(request, response);

            // Return the response
            return ResponseEntity.ok(new JsonResponse("Logged out successfully"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new JsonResponse("Logout failed"));
        }
    }

    @PutMapping("/updateShippingAddress")
    public ResponseEntity<JsonResponse> updateShippingAddress(@RequestBody ShippingAddress newShippingAddress, HttpServletRequest request) {

        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        String validationErrorShippingAddress = validateShippingAddress(newShippingAddress);
        if (validationErrorShippingAddress != null) {
            logger.info(validationErrorShippingAddress);
            return ResponseEntity.badRequest().body(new JsonResponse(validationErrorShippingAddress));
        }

        ShippingAddress shippingAddress = userService.updateShippingAddress(newShippingAddress, loggedUserEmail);

        if (shippingAddress != null) {
            return ResponseEntity.ok(new JsonResponse("Shipping address updated successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("Failed to update shipping address."));
        }
    }

    @PutMapping("/updateUser")
    public ResponseEntity<JsonResponse> updateUser(@RequestBody UserDTO userDTO, HttpServletRequest request) {

        String token = tokenUtils.extractTokenFromRequest(request);
        String loggedUserEmail = tokenUtils.getEmailFromToken(token);

        String validationError = validateUserInfo(userDTO);
        if (validationError != null) {
            logger.info(validationError);
            return ResponseEntity.badRequest().body(new JsonResponse(validationError));
        }

        User user = userService.updateUser(userDTO, loggedUserEmail);

        if (user != null) {
            return ResponseEntity.ok(new JsonResponse("User updated successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponse("Failed to update user."));
        }
    }


}

