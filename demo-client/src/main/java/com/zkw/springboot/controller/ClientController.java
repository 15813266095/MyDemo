package com.zkw.springboot.controller;

import com.zkw.springboot.bean.MapInfo;
import com.zkw.springboot.bean.User;
import com.zkw.springboot.protocol.Message;
import com.zkw.springboot.protocol.MessageType;
import com.zkw.springboot.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author zhangkewei
 * @date 2020/12/14 14:44
 * @desc 客户端与网页端连接的controller
 */
@Controller
public class ClientController {

    @Autowired
    private ClientService service;

    /**
     * 访问首页
     * @param model
     * @return
     */
    @RequestMapping(value = {"/","/index"})
    public String hello(Model model){
        model.addAttribute("user",new User());
        service.start();
        return "index";
    }

    /**
     * 接收网页端的注册请求
     * @param user
     * @param model
     * @return
     */
    @PostMapping(value = "/register")
    public String register(User user, Model model){
        Message message = service.register(user);
        if(message.getMessageType()== MessageType.ERROR){
            model.addAttribute("msg",message.getDescription());
            return "index";
        }
        model.addAttribute("user",(User)message.map.get("user"));
        model.addAttribute("msg",message.getDescription());
        model.addAttribute("maps",(Map<Integer, MapInfo>)message.map.get("mapInfoMap"));
        return "homepage";
    }

    /**
     * 接收网页端的登录请求
     * @param action
     * @param user
     * @param model
     * @return
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam(name="action")String action, User user, Model model){
        if(action.equals("register")){
            return "register";
        }
        Message message = service.login(user.getAccount(), user.getPassword());
        if(message.getMessageType()== MessageType.ERROR){
            model.addAttribute("msg",message.getDescription());
            return "index";
        }
        model.addAttribute("user",(User)message.map.get("user"));
        model.addAttribute("msg",message.getDescription());
        model.addAttribute("maps",(Map<Integer, MapInfo>)message.map.get("mapInfoMap"));
        return "homepage";
    }

    /**
     * 接收网页端的用户移动请求
     * @param user
     * @param direction
     * @param model
     * @return
     */
    @PostMapping(value = "/operator",params="move")
    public String move(User user, @RequestParam(name="move") String direction, Model model){
        Message message = service.move(direction,user);
        model.addAttribute("user",(User)message.map.get("user"));
        model.addAttribute("msg",message.getDescription());
        model.addAttribute("maps",(Map<Integer, MapInfo>)message.map.get("mapInfoMap"));
        return "homepage";
    }

    @PostMapping(value = "/operator",params="get")
    public String get(User user, Model model){
        Message message = service.get(user);
        model.addAttribute("user",(User)message.map.get("user"));
        model.addAttribute("msg",message.getDescription());
        model.addAttribute("maps",(Map<Integer, MapInfo>)message.map.get("mapInfoMap"));
        return "homepage";
    }

    /**
     * 接收网页端的切换场景请求
     * @param user
     * @param mapid
     * @param model
     * @return
     */
    @PostMapping(value = "/operator",params="changeScenes")
    public String changeScenes(User user,@RequestParam(name="changeScenes")Integer mapid,Model model){
        Message message = service.changeMap(user,mapid);
        model.addAttribute("user",(User)message.map.get("user"));
        model.addAttribute("msg",message.getDescription());
        model.addAttribute("maps",(Map<Integer, MapInfo>)message.map.get("mapInfoMap"));
        return "homepage";
    }

    /**
     * 接收网页端的断开连接请求
     * @param user
     * @param model
     * @return
     */
    @PostMapping(value = "/operator",params="disconnect")
    public String disconnect(User user,Model model){
        service.disconnect(user);
        return "redirect:/index";
    }
}
