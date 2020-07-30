package com.tgfc.library.controller;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.EmployeeSafety;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafety;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    IEmployeeRepository employeeRepository;

    @Autowired
    IEmployeeRepositorySafety employeeRepositorySafety;

    @PostMapping("/register")
    public BaseResponse register(@RequestBody Employee employee) {
        BaseResponse.Builder builder = new BaseResponse.Builder();
        String account = employee.getId();
        String password = employee.getPassword();
        if (!employeeRepository.existsById(account)) {
            Employee loginUser = new Employee();
            loginUser.setId(account);
            loginUser.setPassword(ContextUtil.encoder.encode(password));
            loginUser.setName(employee.getName());
            loginUser.setEmail(employee.getEmail());
            loginUser.setDepartment("會員");
            employeeRepository.save(loginUser);

            EmployeeSafety safetyUser = new EmployeeSafety();
            safetyUser.setId(account);
            safetyUser.setName(employee.getName());
            safetyUser.setEmail(employee.getEmail());
            safetyUser.setDepartment("會員");
            return builder.content(safetyUser).status(true).message(MessageUtil.getMessage("register.success")).build();
        }
        return builder.status(false).message(MessageUtil.getMessage("register.error")).build();
    }

    @PutMapping("/editUserInfo")
    public BaseResponse editUserInfo(@RequestBody EmployeeSafety employee) {
        BaseResponse.Builder builder = new BaseResponse.Builder();
        String id = ContextUtil.getAccount();
        Employee existEmployee = employeeRepository.findById(id).get();
        String account = existEmployee.getId();

        EmployeeSafety safetyUser = new EmployeeSafety();
        safetyUser.setId(account);
        safetyUser.setName(employee.getName());
        safetyUser.setEmail(employee.getEmail());
        safetyUser.setDepartment(existEmployee.getDepartment());
        employeeRepositorySafety.save(safetyUser);
        return builder.status(true).message(MessageUtil.getMessage("editUserInfo.success")).build();
    }

}
