package com.edu.cjp.service;

import com.edu.cjp.beans.AutoWired;
import com.edu.cjp.beans.Bean;
import com.edu.cjp.controllers.SalaryController;

/**
 * Created by 澎仔 on 2019/6/28.
 */
@Bean
public class SalaryService {
    public Integer calSalary(int experience){
        return experience * 5000;
    }

}
