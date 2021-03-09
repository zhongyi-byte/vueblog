package com.zhongyi.controller;




import com.zhongyi.common.lang.Result;
import com.zhongyi.entity.User;
import com.zhongyi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhongyi
 * @since 2021-02-27
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/{id}")
    public Object test(@PathVariable("id") long id){
        return userService.getById(id);
    } 
    @PostMapping("/save")
    public Result save(@Validated@RequestBody User user){
        return Result.succ(user);
    }
}
