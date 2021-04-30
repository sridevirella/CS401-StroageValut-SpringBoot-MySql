package com.cs401.storagevault.controller;

import com.cs401.storagevault.dbservices.DBService;
import com.cs401.storagevault.model.tables.DeviceRegistration;
import com.cs401.storagevault.model.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UIController {

    @Autowired
    private DBService dbService;

    @GetMapping(value = "/")
    public String home() {
        return "home";
    }

    @GetMapping(value = "/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping(value = "/users/save")
    public String saveUser(User user) {
        System.out.println(user.toString());
        dbService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @PostMapping(value = "/users/login")
    public String userAuthentication(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password, RedirectAttributes redirectAttrs, HttpServletResponse response ) {

        System.out.println("email:"+email);
        System.out.println("password:"+password);

        User _user = dbService.findByEmail(email);
        redirectAttrs.addAttribute("userName", _user.getName());
        redirectAttrs.addFlashAttribute("message", "successfully logged in");
        System.out.println("redirectAttrs?"+redirectAttrs);

        ResponseCookie userNameCookie = ResponseCookie.from("userName", _user.getName())
                                                 .httpOnly(true)
                                                 .sameSite("None")
                                                 .secure(true)
                                                 .path("/")
                                                 .build();
        ResponseCookie emailCookie = ResponseCookie.from("email", _user.getEmail())
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", userNameCookie.toString());
        response.addHeader("Set-Cookie", emailCookie.toString());


       if(_user.getEmail().equals(email) && _user.getPassword().equals(password) && _user.getUserType() == 'L')
           return "redirect:/lenderDashboard";
       else if(_user.getEmail().equals(email) && _user.getPassword().equals(password) && _user.getUserType() == 'C')
           return "redirect:/consumerDashboard";
       else
           return "redirect:/login";

    }

    @GetMapping(value = "lenderMainPage")
    public String lenderMainPage(@RequestParam(value = "userName") String userName, Model model) {
        model.addAttribute("userName", userName);
        return "lenderMainPage";
    }

    @GetMapping(value = "/lenderDashboard")
    public String lenderDashBoard(HttpServletRequest request, Model model) {

        List<Cookie> userNameCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("userName")).collect(Collectors.toList());
        List<Cookie> emailCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("email")).collect(Collectors.toList());
        model.addAttribute("userName", userNameCookie.get(0).getValue());
        List<DeviceRegistration> lentDevicesList = dbService.getUserLentDeviceDetails(emailCookie.get(0).getValue());
        List<String> lentDeviceListString = new ArrayList<>();

        lentDevicesList.forEach( device -> {
            String resultString = "Device Brand:           " + device.getBrand() + "\n" +
                                  "Device Model:           " + device.getBrandModel() + "\n" +
                                  "Lent Storage Capacity:  " + device.getCapacity() + " GB" +  "\n" +
                                  "Lent Duration:          " + device.getDuration() + " Hours" + "\n" +
                                  "Earnings:               " + device.getPrice() + " $" + "\n";
            lentDeviceListString.add(resultString);
        });
        model.addAttribute("registrations",  lentDeviceListString);
        model.addAttribute("newLineChar", '\n');
        return "lenderDashboard";
    }

    @GetMapping(value = "/deviceLending")
    public String deviceLending(HttpServletRequest request, Model model) {

        List<Cookie> userNameCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("userName")).collect(Collectors.toList());
        List<Cookie> emailCookie = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("email")).collect(Collectors.toList());

        System.out.println("email:"+ emailCookie.get(0).getValue());
        model.addAttribute("userName", userNameCookie.get(0).getValue());
        model.addAttribute("email", emailCookie.get(0).getValue());
        model.addAttribute("brands", dbService.getBrands());
        return "deviceLending";
    }

    @RequestMapping(value = "/models")
    @ResponseBody
    public Set<String> getRegions(@RequestParam String brand) {
        Map<String, Set<String>> models = dbService.getModels(brand);
        return models.get(brand);
    }

    @RequestMapping(value = "/lender/pricing")
    @ResponseBody
    public int getPricingModel(@RequestParam char customerType) {
        int pricingValue = dbService.getPricingModel(customerType);
        return pricingValue;
    }

    @PostMapping(value = "/deviceRegistration/save")
    public String saveDeviceRegistrationDetails(DeviceRegistration deviceRegistration, Model model) {

        System.out.println("payload:"+deviceRegistration.toString());
        dbService.saveDeviceRegistrationDetails(deviceRegistration);
        return "redirect:/lenderDashboard";
    }


    @GetMapping(value = "/marketPlace")
    public String getMarketPlace() {
        return "marketPlace";
    }

}
