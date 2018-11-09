package Study.SpringBoot.Bicycle.contorller;




import Study.SpringBoot.Bicycle.pojo.Bike;
import Study.SpringBoot.Bicycle.service.BikeServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class BikeController {

    @GetMapping("/getbike")
    @ResponseBody
    public String getById(HttpServletRequest  request) {
        String data=request.getParameter("qrCode");
        System.out.println(data);
        return "get success";
    }

    @PostMapping("/bike")
    @ResponseBody  //响应Ajax请求，会将响应的对象转成json
    public String getBydata(@RequestBody String data) {
        //(@RequestBody请求时结束json类型的数据
        System.out.println(data);
        return "post success";
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


}
