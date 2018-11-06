package Study.SpringBoot.Bicycle.mapper;


import Study.SpringBoot.Bicycle.pojo.Bike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BikeMapper {

    public void save(Bike bike);
}
