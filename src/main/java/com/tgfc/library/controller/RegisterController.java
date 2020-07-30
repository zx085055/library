package com.tgfc.library.controller;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.EmployeeSafty;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.request.RegisterRequest;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {
    @Autowired
    IEmployeeRepository employeeRepository;

    @PostMapping("/register")
    public BaseResponse select(@RequestBody RegisterRequest register) {
        BaseResponse.Builder builder = new BaseResponse.Builder();
        String account = register.getAccount();
        String password = register.getPassword();
        if (!employeeRepository.existsById(account)) {
            Employee loginUser = new Employee();
            loginUser.setId(account);
            loginUser.setPassword(ContextUtil.encoder.encode(password));
            loginUser.setName(register.getName());
            loginUser.setEmail(register.getEmail());
            loginUser.setDepartment("會員");
            employeeRepository.save(loginUser);

            EmployeeSafty saftyUser = new EmployeeSafty();
            saftyUser.setId(account);
            saftyUser.setName(register.getName());
            saftyUser.setEmail(register.getEmail());
            saftyUser.setDepartment("會員");
            return builder.content(saftyUser).status(true).message(MessageUtil.getMessage("register.success")).build();
        }
        return builder.status(false).message(MessageUtil.getMessage("register.error")).build();
    }

}
