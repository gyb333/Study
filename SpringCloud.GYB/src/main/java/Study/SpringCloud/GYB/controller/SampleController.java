package Study.SpringCloud.GYB.controller;

import Study.SpringCloud.GYB.domain.User;
import Study.SpringCloud.GYB.rabbitmq.MQSender;
import Study.SpringCloud.GYB.redis.RedisClusterService;
import Study.SpringCloud.GYB.redis.RedisService;
import Study.SpringCloud.GYB.redis.UserKey;
import Study.SpringCloud.GYB.result.CodeMsg;
import Study.SpringCloud.GYB.result.Result;
import Study.SpringCloud.GYB.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/demo")
public class SampleController {

	@Autowired
    UserService userService;
	
	@Autowired
    RedisClusterService   redisClusterService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

	@Autowired
    MQSender sender;
	
//	@RequestMapping("/mq/header")
//    @ResponseBody
//    public Result<String> header() {
//		sender.sendHeader("hello,imooc");
//        return Result.success("Hello，world");
//    }
//	
//	@RequestMapping("/mq/fanout")
//    @ResponseBody
//    public Result<String> fanout() {
//		sender.sendFanout("hello,imooc");
//        return Result.success("Hello，world");
//    }
//	
//	@RequestMapping("/mq/topic")
//    @ResponseBody
//    public Result<String> topic() {
//		sender.sendTopic("hello,imooc");
//        return Result.success("Hello，world");
//    }
//	
//	@RequestMapping("/mq")
//    @ResponseBody
//    public Result<String> mq() {
//		sender.send("hello,imooc");
//        return Result.success("Hello，world");
//    }
	
    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> home() {
        return Result.success("Hello，world");
    }
    
    @RequestMapping("/error")
    @ResponseBody
    public Result<String> error() {
        return Result.error(CodeMsg.SESSION_ERROR);
    }
    
    @RequestMapping("/hello/themaleaf")
    public String themaleaf(Model model) {
        model.addAttribute("name", "Joshua");
        return "hello";
    }
    
    @RequestMapping("/db/get")
    @ResponseBody
    public Result<User> dbGet() {
    	User user = userService.getById(1);
        return Result.success(user);
    }
    
    
    @RequestMapping("/db/tx")
    @ResponseBody
    public Result<Boolean> dbTx() {
    	userService.tx();
        return Result.success(true);
    }
    
    @RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() {
    	User  user  = redisClusterService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }
    
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() {
    	User user  = new User();
    	user.setId(1);
    	user.setName("1111");
        redisClusterService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }


    @RequestMapping(value="/test", produces="text/html")
    @ResponseBody
    public  String Test(HttpServletRequest request, HttpServletResponse response, Model model){
        model.addAttribute("userName", "GYB");
        WebContext ctx = new WebContext(request,response,
                request.getServletContext(),request.getLocale(), model.asMap());
        //手动渲染
        String html = thymeleafViewResolver.getTemplateEngine().process("hello", ctx);
        System.out.println(html);
        return html;
    }
    
}
