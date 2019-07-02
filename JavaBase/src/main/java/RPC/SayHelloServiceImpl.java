package RPC;

public class SayHelloServiceImpl implements SayHelloService {


 
	public String sayHello(String msg) {
		// TODO Auto-generated method stub
		  if("hello".equals(msg)){
	            return "hello client";
	        }else{
	            return "bye bye";
	        }

	}

}
