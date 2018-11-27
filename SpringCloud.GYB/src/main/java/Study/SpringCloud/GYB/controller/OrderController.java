package Study.SpringCloud.GYB.controller;

import Study.SpringCloud.GYB.domain.MiaoshaUser;
import Study.SpringCloud.GYB.domain.OrderInfo;
import Study.SpringCloud.GYB.redis.RedisService;
import Study.SpringCloud.GYB.result.CodeMsg;
import Study.SpringCloud.GYB.result.Result;
import Study.SpringCloud.GYB.service.GoodsService;
import Study.SpringCloud.GYB.service.MiaoshaUserService;
import Study.SpringCloud.GYB.service.OrderService;
import Study.SpringCloud.GYB.vo.GoodsVo;
import Study.SpringCloud.GYB.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	GoodsService goodsService;
	
    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, MiaoshaUser user,
									  @RequestParam("orderId") long orderId) {
    	if(user == null) {
    		return Result.error(CodeMsg.SESSION_ERROR);
    	}
    	OrderInfo order = orderService.getOrderById(orderId);
    	if(order == null) {
    		return Result.error(CodeMsg.ORDER_NOT_EXIST);
    	}
    	long goodsId = order.getGoodsId();
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	OrderDetailVo vo = new OrderDetailVo();
    	vo.setOrder(order);
    	vo.setGoods(goods);
    	return Result.success(vo);
    }
    
}
