package com.cs401.storagevault.controller;

import com.cs401.storagevault.dbservices.DBService;
import com.cs401.storagevault.model.tables.DeviceRegistration;
import com.cs401.storagevault.model.tables.SpaceRequest;
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

        User _user = dbService.findByEmail(email);
        redirectAttrs.addAttribute("userName", _user.getName());
        createCookie(response, "userName", _user.getName());
        createCookie(response, "email", _user.getEmail());

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

        model.addAttribute("userName", getCookieValue(request, "userName"));
        List<DeviceRegistration> lentDevicesList = dbService.getUserLentDeviceDetails(getCookieValue(request, "email"));
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
        return "lenderDashboard";
    }

    @GetMapping(value = "/consumerDashboard")
    public String consumerDashBoard(HttpServletRequest request, Model model) {

        model.addAttribute("userName", getCookieValue(request, "userName"));
        List<DeviceRegistration> lentDevicesList = dbService.getUserLentDeviceDetails(getCookieValue(request, "email"));
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
        return "consumerDashboard";
    }

    @GetMapping(value = "/deviceLending")
    public String deviceLending(HttpServletRequest request, Model model) {

        model.addAttribute("userName", getCookieValue(request, "userName"));
        model.addAttribute("email", getCookieValue(request, "email"));
        model.addAttribute("brands", dbService.getBrands());
        return "deviceLending";
    }

    @GetMapping(value = "/spaceRequest")
    public String consumerSpaceRequest(HttpServletRequest request, Model model) {

        model.addAttribute("userName", getCookieValue(request, "userName"));
        model.addAttribute("email", getCookieValue(request, "email"));
        return "spaceRequest";
    }

    @PostMapping(value = "/spaceRequest/save")
    public String saveConsumerSpaceRequestDetails(SpaceRequest spaceRequest) {

        dbService.saveConsumerSpaceRequestDetails(spaceRequest);
        return "redirect:/consumerPayment";
    }

    @GetMapping(value = "/consumerPayment")
    public  String consumerPayment(HttpServletRequest request, Model model) {

        String amount = dbService.getConsumersPriceDetails(getCookieValue(request, "email"));
        model.addAttribute("amount", amount);
        return "consumerPayment";
    }

    @GetMapping(value = "/storageRetrieval")
    public String consumerStorageAndRetrieval() {
        return "storageRetrieval";
    }

    @RequestMapping(value = "/models")
    @ResponseBody
    public Set<String> getModels(@RequestParam String brand) {
        Map<String, Set<String>> models = dbService.getModels(brand);
        return models.get(brand);
    }

    @RequestMapping(value = "/lender/pricing")
    @ResponseBody
    public int getPricingModel(@RequestParam char customerType) {
        return dbService.getPricingModel(customerType, "");
    }

    @RequestMapping(value = "/consumer/pricing")
    @ResponseBody
    public int getPricingModel(@RequestParam char customerType, @RequestParam String subscription) {

        System.out.println("inside:"+ subscription);
        return dbService.getPricingModel(customerType, subscription);
    }

    @PostMapping(value = "/deviceRegistration/save")
    public String saveDeviceRegistrationDetails(DeviceRegistration deviceRegistration, Model model) {

        System.out.println("payload:"+deviceRegistration.toString());
        dbService.saveDeviceRegistrationDetails(deviceRegistration);
        return "redirect:/lenderDashboard";
    }

    @PostMapping(value = "/consumerPayment/save")
    public String saveConsumerPayDetails() {
        return "redirect:/storageRetrieval";
    }

    @GetMapping(value = "/marketPlace")
    public String getMarketPlace() {
        return "marketPlace";
    }

    private void createCookie(HttpServletResponse response, String cookieName, String cookieValue) {

        ResponseCookie newCookie = ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();

        response.addHeader("Set-Cookie", newCookie.toString());
    }

    private String getCookieValue(HttpServletRequest request, String cookieName) {

        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("email")).collect(Collectors.toList()).get(0).getValue();
    }
}
