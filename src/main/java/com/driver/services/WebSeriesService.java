package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        int productionHouseId=webSeriesEntryDto.getProductionHouseId();
        ProductionHouse productionHouse=productionHouseRepository.findById(productionHouseId).get();
        String seriesName=webSeriesEntryDto.getSeriesName();
        if(webSeriesRepository.findBySeriesName(seriesName)!=null)
             throw new Exception("Series is already present");
        WebSeries webSeries=new WebSeries();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        webSeries.setProductionHouse(productionHouse);
        webSeriesRepository.save(webSeries);
        //finding new average of production house
        double oldRating=productionHouse.getRatings();
        double newRating=webSeriesEntryDto.getRating();
        double changeInAverage=(newRating-oldRating)/productionHouse.getWebSeriesList().size()+1;
        double newAverage=oldRating+changeInAverage;
        productionHouse.setRatings(newAverage);
        productionHouse.getWebSeriesList().add(webSeries);
        productionHouseRepository.save(productionHouse);

        return  null;
    }

}
