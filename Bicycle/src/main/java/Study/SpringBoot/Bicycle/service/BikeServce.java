package Study.SpringBoot.Bicycle.service;


import Study.SpringBoot.Bicycle.pojo.Bike;

import java.util.List;

public interface BikeServce {

    public void save(Bike bike);

    public void save(String bike);

    public List<Bike> findAll();

}
