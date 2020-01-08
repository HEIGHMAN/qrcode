package user.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import yx.qrcode.pojo.User;
import yx.qrcode.pojo.UserBusinessLog;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserConsumerController {
    private static final String adress="http://localhost:17811/";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    public User login(@RequestParam("username")String username,@RequestParam("password")String password, HttpServletRequest request, HttpServletResponse response) throws Exception{
        HttpSession session = request.getSession();
        String url=adress+"user/login?username="+username+"&password="+password;
        System.out.println(url);
        User t = restTemplate.postForEntity(url, null, User.class).getBody();
        if(null != t){
            session.setAttribute("user",t);
            t.setPassword(null);
//            Cookie cookie = new Cookie("username",user.getUsername());
        }
        System.out.println(t);
        return t;
    }
    //退出日志更新
    @PutMapping("/exit")
    public void addExitLog(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        session.removeAttribute("user");
        String url = adress+"user/exit?userId="+user.getId();
        restTemplate.put(url,null);
    }
    /**
     * 或取用户数据
     * @return
     */
    @GetMapping("/info/{name}")
    public User getInfo(@PathVariable("name") String name, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute(name);
        user.setPassword(null);
        return user;
    }

    //更新密码
    @PutMapping("/rePwd")
    public boolean updatePwd(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("newPassword") String newPassword, HttpServletRequest request){
        String url = adress+"user/rePwd?username="+username+"&password="+password+"&newPassword="+newPassword;
        restTemplate.put(url,null);
//        request.getSession().setAttribute(user.getUsername(), user);
        return true;
    }

    /**
     * 管理员接口
     * 获取用户登录记录
     * @param userId    管理员id
     * @param start
     * @param size
     * @return
     */
    @GetMapping("/userLog")
    public List<User> userLoginLog(@RequestParam("userId") int userId,@RequestParam("start") int start,@RequestParam("size") int size){
        String url = adress+"user/userLog?userId="+userId+"&start="+start+"&size="+size;
        return restTemplate.getForObject(url, List.class);
    }
    @GetMapping("/uL")
    public List<User> userAllLoginLogs(@RequestParam("userId") int userId){
        String url = adress+"user/uL?userId="+userId;
        return restTemplate.getForObject(url, List.class);
    }
    @GetMapping("/uLC")
    public int getUserLogCount(){
        String url = adress+"user/uLC";
        return restTemplate.getForObject(url,Integer.class);
    }
    //获取用户的操作日志
    @GetMapping("/bLogs")
    public List<UserBusinessLog> getBusinessLogs(@RequestParam("userId")int userId, @RequestParam("login") long login, @RequestParam("exit") long exit){
        String url= adress+"user/bLogs?userId="+userId+"&login="+login+"&exit="+exit;
        return restTemplate.getForObject(url,List.class);
    }
//    public List<UserBusinessLog> getBusinessLogs(@RequestParam("userId")int userId, @RequestParam("login") long login, @RequestParam("exit") long exit,
//                                                 @RequestParam("start") int start, @RequestParam("size") int size){
//        String url= adress+"api/user/bLogs?userId="+userId+"&login="+login+"&exit="+exit+"&start="+start+"&size="+size;
//        return restTemplate.getForObject(url,List.class);
//    }

    //通过用户名来查询
    @GetMapping("/nL")
    public List<User> getLoginLogByUsername(@RequestParam("username")String username,@RequestParam("start")int start,@RequestParam("size")int size){
        String url=adress+"user/nL?username="+username+"&start="+start+"&size="+size;
        return restTemplate.getForObject(url,List.class);
    }
    @GetMapping("/nLC")
    public int getUserLogCountByusername(@RequestParam("username")String username){
        String url=adress+"user/nLC?username="+username;
        return restTemplate.getForObject(url,Integer.class);
    }

    //Redis Session测试
    @GetMapping("/getUser")
    public User getUser(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        return user;
//        String username = (String)session.getAttribute("username");
//        if(StringUtils.isEmpty(username))
//            session.setAttribute("username","TestSessionRedis"+System.currentTimeMillis());
//        return username;
    }
}
