package com.snailfast.coding.application.control;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import com.alibaba.fastjson.JSONObject;
import com.snailfast.coding.application.entity.Greeting;
import com.snailfast.coding.application.entity.HelloMessage;

@Controller
public class GreetingController {
	
	@Autowired
	SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/hello")	//模拟接收客户端发送的发消息，请求你路径是/app/hello
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        System.out.println(message.getName());
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
    
    @RequestMapping("/client")
    public String getClient(Model m){
        m.addAttribute("now",DateFormat.getDateTimeInstance().format(new Date()));
        return "client";  //视图重定向client.jsp
    }
    
    @RequestMapping("send")	//模拟服务器主动给客户端发送消息
    public String serverSendMsg(){
    		JSONObject data = new JSONObject();
    		data.put("content", "server data");
    		simpMessagingTemplate.convertAndSend("/topic/greetings", data);	 //客户端必须已经订阅了/topic/greetings才能接受到消息
    		return "send";	//视图重定向send.jsp
    }

}
