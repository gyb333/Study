package Study.SpringBoot.Bicycle.service;

import Study.SpringBoot.Bicycle.mapper.BikeMapper;
import Study.SpringBoot.Bicycle.pojo.Bike;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BikeServiceImpl implements BikeServce {

    @Autowired
    private BikeMapper bikeMapper;

    @Override
    public void save(Bike bike) {
        bikeMapper.save(bike);
        System.out.println("save");
        //int i = 100 / 0;
//        bikeMapper.save(bike);
    }
}
