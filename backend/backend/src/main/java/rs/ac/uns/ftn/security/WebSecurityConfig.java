package rs.ac.uns.ftn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.ac.uns.ftn.service.implementation.UserDetailsServiceImpl;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 1. koji servis da koristi da izvuce podatke o korisniku koji zeli da se autentifikuje
        // prilikom autentifikacije, AuthenticationManager ce sam pozivati loadUserByUsername() metodu ovog servisa
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        // 2. kroz koji enkoder da provuce lozinku koju je dobio od klijenta u zahtevu
        // da bi adekvatan hash koji dobije kao rezultat hash algoritma uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain lozinka)
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Handler za vracanje 401 kada klijent sa neodogovarajucim korisnickim imenom i lozinkom pokusa da pristupi resursu
    /*@Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;*/


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // svim korisnicima dopusti da pristupe sledecim putanjama:
        // komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST aplikacija
        // ovo znaci da server ne pamti nikakvo stanje, tokeni se ne cuvaju na serveru
        // ovo nije slucaj kao sa sesijama koje se cuvaju na serverskoj strani - STATEFUL aplikacija
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
        //http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
        http.authorizeRequests()

                .antMatchers("/user/wishlist/{email}").hasAuthority("USER")
                .antMatchers("/user/basket/{email}").hasAuthority("USER")
                .antMatchers("/user/profile/{email}").hasAuthority("USER")
                .antMatchers("/user/registerAdmin").hasAuthority("ADMIN")
                .antMatchers("/user/registerWorker").hasAuthority("ADMIN")
                .antMatchers("/user/registerCourier").hasAuthority("ADMIN")
                .antMatchers("/user/logout").permitAll()
                .antMatchers("/user/updateShippingAddress").hasAuthority("USER")
                .antMatchers("/user/updateUser").hasAuthority("USER")

                .antMatchers("/product/create").hasAuthority("ADMIN")
                .antMatchers("/product/{code}").hasAnyAuthority("USER", "ADMIN")
                .antMatchers("/product/home").hasAuthority("USER")
                .antMatchers("/product/byCategory").hasAuthority("USER")
                .antMatchers("/product/refillQuantity/{productId}").hasAuthority("ADMIN")
                .antMatchers("/product/allProduct").hasAuthority("ADMIN")
                .antMatchers("/product/allProductsByCategory").hasAuthority("ADMIN")
                .antMatchers("/product/searchByCode").hasAuthority("USER")
                .antMatchers("/product/searchAllByCode").hasAuthority("ADMIN")

                .antMatchers("/basket/**").hasAuthority("USER")
                .antMatchers("/wishlist/**").hasAuthority("USER")

                .antMatchers("/order/create").hasAuthority("USER")
                .antMatchers("/order/{code}").hasAnyAuthority("USER", "COURIER", "WORKER")
                .antMatchers("/order/cancelOrder/{orderId}").hasAuthority("USER")
                .antMatchers("/order/sentOrder/{orderId}").hasAuthority("WORKER")
                .antMatchers("/order/deliverOrder/{orderId}").hasAuthority("COURIER")
                .antMatchers("/order/sentOrders").hasAnyAuthority("WORKER", "COURIER")
                .antMatchers("/order/pendingOrders").hasAuthority("WORKER")
                .antMatchers("/order/deliveredOrders").hasAuthority("COURIER")
                .antMatchers("/order/searchPendingOrdersByCode").hasAuthority("WORKER")
                .antMatchers("/order/searchSentOrdersByCode").hasAnyAuthority("WORKER", "COURIER")

                .antMatchers("/image/upload/{productCode}").hasAuthority("ADMIN")
                .antMatchers("/image/download/{productCode}/{fileName}").permitAll()
                .antMatchers("/image/list/{productCode}").permitAll()


                //.antMatchers(HttpMethod.GET, "/api/clubs/{id}/**").access("@webSecurity.checkClubId(authentication,request,#id)")
                // ukoliko ne zelimo da koristimo @PreAuthorize anotacije nad metodama kontrolera, moze se iskoristiti hasRole() metoda da se ogranici
                // koji tip korisnika moze da pristupi odgovarajucoj ruti. Npr. ukoliko zelimo da definisemo da ruti 'admin' moze da pristupi
                // samo korisnik koji ima rolu 'ADMIN', navodimo na sledeci nacin:
                //.antMatchers("/users/me").hasRole("USER")// ili .antMatchers("/admin").hasAuthority("ROLE_ADMIN")

                // za svaki drugi zahtev korisnik mora biti autentifikovan
                .anyRequest().authenticated().and()

                // za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
                .cors().and()

                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);


        // zbog jednostavnosti primera ne koristimo Anti-CSRF token (https://cheatsheetseries.owasp.org/cheatsheets/Cross-Site_Request_Forgery_Prevention_Cheat_Sheet.html)
        http.csrf().disable();


        // ulancavanje autentifikacije
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    // metoda u kojoj se definisu putanje za igorisanje autentifikacije
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Autentifikacija ce biti ignorisana ispod navedenih putanja (kako bismo ubrzali pristup resursima)
        // Zahtevi koji se mecuju za web.ignoring().antMatchers() nemaju pristup SecurityContext-u
        // Dozvoljena POST metoda na ruti /users/login, za svaki drugi tip HTTP metode greska je 401 Unauthorized
        return (web) -> web.ignoring().antMatchers(HttpMethod.POST, "/user/login")
                .antMatchers(HttpMethod.POST, "/user/registerUser")

                // Ovim smo dozvolili pristup statickim resursima aplikacije
                .antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html", "favicon.ico",
                        "/**/*.html", "/**/*.css", "/**/*.js");

    }

}
