package com.cs401.storagevault.dbservices;

import com.cs401.storagevault.model.repository.*;
import com.cs401.storagevault.model.tables.Brand;
import com.cs401.storagevault.model.tables.DeviceRegistration;
import com.cs401.storagevault.model.tables.SpaceRequest;
import com.cs401.storagevault.model.tables.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
public class DBService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ModelRepository modelRepository;
    @Autowired
    private PricingRepository pricingRepository;
    @Autowired
    private DeviceRegistrationRepo deviceRegistrationRepo;
    @Autowired
    private SpaceRequestRepository spaceRequestRepository;



    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<String> getBrands() {
        return brandRepository.findAllBrands();
    }

    public Map<String, Set<String>> getModels(String brand) {
        Map<String, Set<String>> models = new HashMap<>();

       Set<String> modelSet = new HashSet<>();
       modelSet.add("Nothing Selected");
       modelSet.addAll(modelRepository.findByBrandName(brand));
       models.put(brand, modelSet);
        return models;
    }

    public int getPricingModel(char customerType, String subscriptionType) {

        if(subscriptionType.equals("Y"))
            return pricingRepository.yearlySubscriptionPrice(customerType);
        else
            return pricingRepository.findByCustomerType(customerType);
    }

    public void saveDeviceRegistrationDetails(DeviceRegistration deviceRegistration) {
        deviceRegistrationRepo.save(deviceRegistration);
    }

    public List<DeviceRegistration> getUserLentDeviceDetails(String email) {
        return deviceRegistrationRepo.findByUserEmail(email);
    }

    public void saveConsumerSpaceRequestDetails(SpaceRequest spaceRequest) {
        spaceRequestRepository.save(spaceRequest);
    }

    public String getConsumersPriceDetails(String email) {
        return spaceRequestRepository.getPrice(email);
    }
}
