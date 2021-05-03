package com.cs401.storagevault.controller;

import com.cs401.storagevault.model.tables.*;
import com.cs401.storagevault.services.DBService;
import com.cs401.storagevault.model.FileStorage;
import com.cs401.storagevault.services.FileStorageService;
import com.cs401.storagevault.services.ProductStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UIController {

    @Autowired
    private DBService dbService;
    @Autowired
    private ProductStorageService productStorageService;

    private final FileStorage fileStorage;
    @Autowired
    public UIController(FileStorageService fileStorageService) {
        this.fileStorage = fileStorageService;
    }

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
        SpaceRequest spaceRequest = dbService.getConsumerSpaceRequests(getCookieValue(request, "email"));
        List<String> spaceRequestListString = new ArrayList<>();
        String resultString = "";
        String percentage = "";
        String percentageValue = "";

        if (spaceRequest != null) {
            resultString = "Allocated storage space:   " + spaceRequest.getCapacity() + "GB" + "\n" +
                    "usage cost:                " + spaceRequest.getPrice() + "\n" +
                    "Used Storage Space:        " + getFileSize(spaceRequest.getUsedSpace(), false, spaceRequest.getCapacity()) + "\n";
            spaceRequestListString.add(resultString);
            percentage = getFileSize(spaceRequest.getUsedSpace(), true, spaceRequest.getCapacity());
            percentageValue = getFileSize(spaceRequest.getUsedSpace(), false, spaceRequest.getCapacity());
        }

        model.addAttribute("spaceRequests",  spaceRequestListString);
        model.addAttribute("percentage", percentage);
        model.addAttribute("percentageValue", percentageValue );
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
        amount = dbService.getTotalPrice(getCookieValue(request, "email")).getPrice() +"$";
        model.addAttribute("amount", amount);
        return "consumerPayment";
    }

    @GetMapping(value = "/cardPayment")
    public  String cardPayment(HttpServletRequest request, Model model) {

        String amount = dbService.getTotalPrice(getCookieValue(request, "email")).getPrice() +"$";
        model.addAttribute("amount", amount);
        return "cardPayment";
    }

    @RequestMapping(value = "/models")
    @ResponseBody
    public Set<String> getModels(@RequestParam String brand) {
        Map<String, Set<String>> models = dbService.getModels(brand);
        return models.get(brand);
    }

    @RequestMapping(value = "/lender/pricing")
    @ResponseBody
    public double getPricingModel(@RequestParam char customerType) {
        return dbService.getPricingModel(customerType, "");
    }

    @RequestMapping(value = "/consumer/pricing")
    @ResponseBody
    public double getPricingModel(@RequestParam char customerType, @RequestParam String subscription) {

        return dbService.getPricingModel(customerType, subscription);
    }

    @PostMapping(value = "/deviceRegistration/save")
    public String saveDeviceRegistrationDetails(DeviceRegistration deviceRegistration, Model model) {

        dbService.saveDeviceRegistrationDetails(deviceRegistration);
        return "redirect:/lenderDashboard";
    }

    @PostMapping(value = "/consumerPayment/save")
    public String saveConsumerPayDetails() {
        return "redirect:/storageRetrieval";
    }

    @PostMapping(value = "/cardPayment/save")
    public String saveCardPayDetails() {
        return "redirect:/products";
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

        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals(cookieName)).collect(Collectors.toList()).get(0).getValue();
    }

    @GetMapping("/storageRetrieval")
    public String listUploadedFiles(HttpServletRequest request, Model model) throws IOException {

        model.addAttribute("userName", getCookieValue(request, "userName"));
        model.addAttribute("files", fileStorage.loadAllFiles().map(
                path -> MvcUriComponentsBuilder.fromMethodName(UIController.class,
                        "getFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));
        return "storageRetrieval";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws Exception {

        Resource file = fileStorage.loadAsResourceFile(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/storageRetrieval/save")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes, HttpServletRequest request) throws IOException {

        dbService.updateUsageSpace(getCookieValue(request, "email"), file.getSize());
        fileStorage.storeFile(file);
        return "redirect:/storageRetrieval";
    }

    @GetMapping("/sell")
    public String marketPlaceSellPage(HttpServletRequest request, Model model) {

        System.out.println("user name:"+getCookieValue(request, "userName"));
        model.addAttribute("userName", getCookieValue(request, "userName"));
        return "sell";
    }

    @PostMapping("/product/save")
    public String createProduct(@RequestParam("name") String name,
                                @RequestParam("description") String description,
                                @RequestParam("seller") String seller,
                                @RequestParam("price") double price,
                                @RequestParam("image") MultipartFile file,
                                Model model, HttpServletRequest request) throws IOException {
        System.out.println("inside");
        productStorageService.initService();
        productStorageService.storeFile(file);

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setSeller(seller);
        newProduct.setPrice(price);
        newProduct.setImage(file.getBytes());
        dbService.saveProduct(newProduct);
        System.out.println("successfully saved product");
        return "redirect:/sell";
    }

    @GetMapping("/product/display/{id}")
    @ResponseBody
    void showProductImage(@PathVariable("id") int id, HttpServletResponse response, Optional<Product> product)
            throws IOException {
        product = dbService.getProductById(id);
        response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
        response.getOutputStream().write(product.get().getImage());
        response.getOutputStream().close();
    }

    @GetMapping("/products")
    public String show(Model model, HttpServletRequest request) {

        List<Product> products = dbService.getAllProducts();
        model.addAttribute("userName", getCookieValue(request, "userName"));
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpServletRequest request) {

        model.addAttribute("userName", getCookieValue(request, "userName"));
        return "cart";
    }

    @RequestMapping("/selected/itemPrice")
    @ResponseBody
    public void selectedItemPrice(@RequestParam double price, HttpServletRequest request) {

        String email = getCookieValue(request, "email");
        System.out.println("payload::email::"+price+"::"+email);
        dbService.saveTotalPriceToCart(new Cart(email, price));
    }

    private String getFileSize(long size, boolean inGBFormat, int capacity) {

        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        float kb = 1024.0f;
        float mb = kb * kb;
        float gb = mb * kb;

        if(inGBFormat) {

            String value = (size / mb)/((capacity * 1024)) * 100 +"%";

            return value;
        } else {
            if (size < mb)
                return decimalFormat.format(size / kb) + " Kb";
            else if (size < gb)
                return decimalFormat.format(size / mb) + " Mb";
            else return "";
        }
    }
}
