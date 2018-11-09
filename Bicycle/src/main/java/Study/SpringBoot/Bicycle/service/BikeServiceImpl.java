package Study.SpringBoot.Bicycle.service;

import Study.SpringBoot.Bicycle.mapper.BikeMapper;
import Study.SpringBoot.Bicycle.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class BikeServiceImpl implements BikeServce {

    @Autowired
    private BikeMapper bikeMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void save(Bike bike) {
        bikeMapper.save(bike);
        System.out.println("save");

    }

    @Override
    public void save(String bike) {
        mongoTemplate.save(bike, "bikes");
    }

    @Override
    public List<Bike> findAll() {
        return mongoTemplate.findAll(Bike.class, "bikes");

    }
}
