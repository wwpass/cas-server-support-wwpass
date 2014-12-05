package com.wwpass.cas.example;

import com.wwpass.connection.WWPassConnection;
import com.wwpass.connection.exceptions.WWPassProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;

@Controller
public class ApplicationController {

    @Autowired
    private JdbcUserServiceDao jdbcUserServiceDao;
    
    @Autowired
    private UserService userService;

    @Autowired
    private WWPassConnection wwpassConnection;
    

    @RequestMapping("/")
    public String index() {
        return "index";
    }
    
    @RequestMapping(value = "/secured", method = RequestMethod.GET)
    public String secured(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetails =
                    (UserDetails) authentication.getPrincipal();
            map.addAttribute("userDetails", userDetails);
        }
        return "secured";
    }

    @PreAuthorize("hasRole('admin')")
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(ModelMap map) {
        UserDetails userDetails =
                (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> securedMessage = userService.getAuthorities(userDetails);
        map.addAttribute("userDetails", userDetails);
        map.addAttribute("userAuthorities", securedMessage);
        return "admin";
    }
    
    @RequestMapping("/register/form")
    public String register(@ModelAttribute RegisterForm form) {
        return "register";
    }
    
    @RequestMapping(value = "/register/new", method = RequestMethod.POST)
    public String create(@Valid RegisterForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "register";
        }
        if (form.getUsername().isEmpty()) {
            result.rejectValue("username", "registerForm.username", "Username is required");
            return "register";
        }
        if (form.getPassword().isEmpty()) {
            result.rejectValue("password", "registerForm.password","Password is required");
            return "register";
        }
        String ticket = form.getTicket();
        String puid = null;
        try {
            ticket = wwpassConnection.putTicket(ticket);
            puid = wwpassConnection.getPUID(ticket);       
        } catch (WWPassProtocolException e) {
            result.rejectValue("ticket", "registerForm.ticket", "Error in WWPass Connection library: " + e.getMessage());
            return "register";
        } catch (IOException e) {
            result.rejectValue("ticket", "registerForm.ticket", "Error during WWPass authentication process: " + e.getMessage());
            return "register"; 
        }
        
        int uid = jdbcUserServiceDao.createUser(form.getUsername(), form.getPassword());
        if (form.isAdminRole()) {
            jdbcUserServiceDao.addRole(uid, new String[]{"user", "admin"});
        } else {
            jdbcUserServiceDao.addRole(uid, new String[]{"user"});
        }
        jdbcUserServiceDao.createWwpass(puid, uid);
        
        return "redirect:/";
    }
}
