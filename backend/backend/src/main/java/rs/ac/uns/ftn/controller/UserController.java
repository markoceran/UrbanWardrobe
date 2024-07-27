package rs.ac.uns.ftn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.model.*;
import rs.ac.uns.ftn.model.dto.JwtAuthenticationRequest;
import rs.ac.uns.ftn.model.dto.UserDTO;
import rs.ac.uns.ftn.model.dto.UserTokenState;
import rs.ac.uns.ftn.security.TokenUtils;
import rs.ac.uns.ftn.service.AdminService;
import rs.ac.uns.ftn.service.CourierService;
import rs.ac.uns.ftn.service.UserService;
import rs.ac.uns.ftn.service.WorkerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @GetMapping("/profile/{email}")
    public User user(@PathVariable String email) {
        logger.info("Find user with email");
        return this.userService.findByEmail(email);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO newUser) {

        User createdUser = userService.createUser(newUser);

        if (createdUser == null) {

            logger.info("User is null");
            ErrorResponse errorResponse = new ErrorResponse(
                    "User with this email already exists",
                    HttpStatus.BAD_REQUEST.value(),
                    System.currentTimeMillis()
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);

        }

        newUser.setPassword("");

        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

//    @PostMapping("/login")
//    public ResponseEntity<UserTokenState> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) {
//
//        boolean isValid = true;
//
//        List<Banned> banneds = bannedService.getAll();
//
//        if(!banneds.isEmpty()){
//            for (Banned b : banneds) {
//                if (b.getUser() != null && b.getUser().getUsername().equals(authenticationRequest.getUsername()) && b.getBy2() != null) {
//                    isValid = false;
//                    logger.info("User is bannned");
//                }
//            }
//        }
//
//
//        if (isValid) {
//            // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
//            // AuthenticationException
//            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
//
//            // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
//            // kontekst
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            // Kreiraj token za tog korisnika
//            UserDetails user = (UserDetails) authentication.getPrincipal();
//            String jwt = tokenUtils.generateToken(user);
//            int expiresIn = tokenUtils.getExpiredIn();
//
//            User loggedUser = userService.findByUsername(authenticationRequest.getUsername());
//            loggedUser.setLastLogin(LocalDateTime.now());
//
//            userService.update(loggedUser.getId(), loggedUser);
//
//            logger.info("Token is created");
//
//            // Vrati token kao odgovor na uspesnu autentifikaciju
//            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn));
//        }
//
//        logger.info("User is unauthorized");
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request, HttpServletResponse response) {
//
//        SecurityContextHolder.getContext().setAuthentication(null);
//
//        TokenUtils.clearAuthenticationCookie(request, response);
//
//        Map<String, String> responseBody = new HashMap<>();
//        responseBody.put("message", "Logged out successfully");
//
//        logger.info("Logged out successfully");
//
//        return ResponseEntity.ok(responseBody);
//    }

//    @PutMapping("/editProfile")
//    public ResponseEntity<User> updateUserData(@RequestHeader("Authorization") String token, @RequestBody User user) throws Exception {
//
//        String tokenValue = token.replace("Bearer ", "");
//
//        String username = tokenUtils.getUsernameFromToken(tokenValue);
//        User loggedUser = userService.findByUsername(username);
//
//        if (loggedUser == null) {
//            logger.info("Logged user is not present");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        List<User> allUser = userService.getAll();
//        for (User u : allUser) {
//            if (!loggedUser.getUsername().equals(user.getUsername()) && u.getUsername().equals(user.getUsername())) {
//                logger.info("FORBIDDEN");
//                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//            }
//        }
//
//        user.setPassword(loggedUser.getPassword());
//
//
//        User updated = userService.update(loggedUser.getId(), user);
//        if (updated != null) {
//            logger.info("Success");
//            return ResponseEntity.ok(updated);
//        } else {
//            logger.info("Not found");
//            return ResponseEntity.notFound().build();
//        }
//    }
//
//    @PutMapping("/editPassword")
//    public ResponseEntity<User> editPassword(@RequestHeader("Authorization") String token, @RequestParam String oldPassword, @RequestParam String newPassword) throws Exception {
//
//        User updated = null;
//
//        String tokenValue = token.replace("Bearer ", "");
//
//        String username = tokenUtils.getUsernameFromToken(tokenValue);
//        User loggedUser = userService.findByUsername(username);
//
//        if (loggedUser == null) {
//            logger.info("User is not present");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//
//        if (passwordEncoder.matches(oldPassword, loggedUser.getPassword())) {
//
//            loggedUser.setPassword(passwordEncoder.encode(newPassword));
//            updated = userService.update(loggedUser.getId(), loggedUser);
//
//        }
//
//        if (updated != null) {
//            logger.info("Success");
//            return ResponseEntity.ok(updated);
//        } else {
//            logger.info("Bad request");
//            return ResponseEntity.badRequest().build();
//        }
//    }


}

