package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository

        User user1=userRepository.save(user);
        return user1.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository

        User user=userRepository.findById(userId).get();
        List<WebSeries> webSeriesList=webSeriesRepository.findAll();
        int availableCount=0;
        for(WebSeries webSeries:webSeriesList){
            if(user.getAge()>=webSeries.getAgeLimit()){
                if(webSeries.getSubscriptionType().equals(SubscriptionType.ELITE) &&
                        user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE))
                    availableCount+=1;
                else if(webSeries.getSubscriptionType().equals(SubscriptionType.PRO) &&
                        (user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE) || user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)))
                    availableCount+=1;
                else if(webSeries.getSubscriptionType().equals(SubscriptionType.BASIC))
                    availableCount+=1;
            }
        }
        return availableCount;
    }


}
