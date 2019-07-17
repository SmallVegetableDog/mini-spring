package com.edu.cjp.controllers;

import com.edu.cjp.beans.AutoWired;
import com.edu.cjp.service.SalaryService;
import com.edu.cjp.web.mvc.Controller;
import com.edu.cjp.web.mvc.RequestMapping;
import com.edu.cjp.web.mvc.RequestParam;

/**
 * Created by 澎仔 on 2019/6/28.
 */
@Controller
public class SalaryController {
    @AutoWired
    private  SalaryService salaryService ;
    @RequestMapping("/get_salary.json")
    public Integer getSalary(@RequestParam("name") String name, @RequestParam("experience") String experience){
        return salaryService.calSalary(Integer.parseInt(experience));
    }
}
