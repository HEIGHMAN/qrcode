package user.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;
import user.provider.mapper.LogMapper;
import user.provider.mapper.UserMapper;
import yx.qrcode.pojo.User;
import yx.qrcode.pojo.UserBusinessLog;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Lazy
    private UserMapper userMapper;
    @Autowired
    @Lazy
    private LogMapper logMapper;

    //用户登录
    @PostMapping("/login")
    public User login(@RequestParam("username")String username,@RequestParam("password")String password){
        System.out.println("provide login");
        User tmp = userMapper.userLogin(username);
        if(tmp != null && tmp.getPassword().equals(password)){
            userMapper.addLoginLog(tmp.getId(),System.currentTimeMillis());
            return tmp;
        }
        return null;
    }
    //用户退出更新日志
    @PutMapping("/exit")
    public void addExitLog(@RequestParam("userId")int userId){
        int id=userMapper.getLoginLogId(userId);
        userMapper.addExitLog(id,System.currentTimeMillis());
    }
//    //用户添加
//    @PostMapping("/register")
//    public boolean add(@RequestBody User user){
//        return userMapper.add(user);
//    }

    //更新密码
    @PutMapping("/rePwd")
    public boolean updatePwd(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("newPassword") String newPassword){
        User user = userMapper.userLogin(username);
        if(password.equals(user.getPassword())){
            logMapper.addLog(user.getId(),"更改密码成功",System.currentTimeMillis());
            userMapper.update(user.getId(),newPassword);
            return true;
        }else {
            logMapper.addLog(user.getId(),"更改密码失败",System.currentTimeMillis());
        }
        return false;
    }

    //获取用户登录记录
    @GetMapping("/userLog")
    public List<User> getLoginLog(@RequestParam("userId") int userId,@RequestParam("start") int start,@RequestParam("size") int size){
        System.out.println("userController");
        logMapper.addLog(userId,"管理员获取用户登录记录: start="+start+",size="+size,System.currentTimeMillis());
        return userMapper.getLoginLogs(start,size);
    }

    @GetMapping("/uL")
    public List<User> userAllLoginLogs(@RequestParam("userId") int userId){
        System.out.println("userController");
        logMapper.addLog(userId,"管理员获取用户登录记录",System.currentTimeMillis());
        return userMapper.userAllLoginLogs();
    }
    //用户登录条数
    @GetMapping("/uLC")
    public int getUserLogCount(){
        return userMapper.getUserLogCount();
    }

    //获取用户在登录后的操作日志
    @GetMapping("/bLogs")
    public List<UserBusinessLog> getBusinessLogs(@RequestParam("userId")int userId,@RequestParam("login") long login,@RequestParam("exit") long exit){
//        if(exit < login)
        System.out.println("login: "+login+"  exit: "+exit);
        return logMapper.getLogs(userId,login,exit);
    }

//    public List<UserBusinessLog> getBusinessLogs(@RequestParam("userId")int userId,@RequestParam("login") long login,@RequestParam("exit") long exit,
//                                                 @RequestParam("start") int start,@RequestParam("size") int size){
//        return logMapper.getLogs(userId,login,exit,start,size);
//    }

    //通过用户名查找日志
    @GetMapping("/nL")
    public List<User> getLoginLogByUsername(@RequestParam("username")String username,@RequestParam("start")int start,@RequestParam("size")int size){
        username="%"+username+"%";
        return userMapper.getLoginLogByUsername(username,start,size);
    }
    @GetMapping("/nLC")
    public int getUserLogCountByusername(@RequestParam("username")String username){
        username="%"+username+"%";
        return userMapper.getUserLogCountByusername(username);
    }
}