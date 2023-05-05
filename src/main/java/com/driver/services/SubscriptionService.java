package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.ELITE)){
            subscription.setTotalAmountPaid(1000);
            subscription.setNoOfScreensSubscribed(350);
        }
        else if(subscriptionEntryDto.getSubscriptionType().equals(SubscriptionType.PRO)){
            subscription.setTotalAmountPaid(800);
            subscription.setNoOfScreensSubscribed(250);
        }
        else{
            subscription.setTotalAmountPaid(500);
            subscription.setNoOfScreensSubscribed(200);
        }
        subscription.setStartSubscriptionDate(new Date());
        subscription.setUser(user);
        user.setSubscription(subscription);
        userRepository.save(user);
        return subscription.getTotalAmountPaid();
    }
    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user=userRepository.findById(userId).get();
        Subscription subscription=user.getSubscription();
        SubscriptionType subscriptionType=user.getSubscription().getSubscriptionType();
        Integer difference=0;
        if(subscriptionType.equals(SubscriptionType.ELITE))
            throw new Exception("Already the best Subscription");
        else if(subscriptionType.equals(SubscriptionType.PRO)){
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(1000);
            subscription.setNoOfScreensSubscribed(350);
            difference=200;
        }
        else if(subscriptionType.equals(SubscriptionType.BASIC)){
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(800);
            subscription.setNoOfScreensSubscribed(250);
            difference=300;
        }
        subscriptionRepository.save(subscription);
        user.setSubscription(subscription);

        return difference;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
       List<Subscription> subscriptions=subscriptionRepository.findAll();
       int totalRevenue=0;
       for(Subscription subscription:subscriptions){
           totalRevenue+=subscription.getTotalAmountPaid();
       }
        return totalRevenue;
    }

}
