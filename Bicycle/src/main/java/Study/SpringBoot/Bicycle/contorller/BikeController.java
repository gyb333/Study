package Study.SpringBoot.Bicycle.contorller;




import Study.SpringBoot.Bicycle.pojo.Bike;
import Study.SpringBoot.Bicycle.service.BikeServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class BikeController {

    @GetMapping("/getbike")
    @ResponseBody
    public String getById(HttpServletRequest  request) {
        String data=request.getParameter("qrCode");
        System.out.println(data);
        return "get success";
    }



    @Autowired
    private BikeServce bikeServce;


    @GetMapping("/bike")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public String getById(Bike bike) {
        System.out.println(bike);
        //调用Service保存map
        bikeServce.save(bike);
        return "success";
    }



    @PostMapping("/bike")
    @ResponseBody
    public  String save(@RequestBody String data){
        System.out.println(data);
        bikeServce.save(data);
        return  "success";
    }

    @GetMapping("/bikes")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public List<Bike> findAll() {
        List<Bike> bikes = bikeServce.findAll();
        return bikes;
    }

}
