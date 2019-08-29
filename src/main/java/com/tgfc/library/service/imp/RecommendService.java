package com.tgfc.library.service.imp;

import com.tgfc.library.com.tgfc.library.util.ContextUtil;
import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IRecommendRepository;
import com.tgfc.library.service.IRecommendService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendService implements IRecommendService {

    @Autowired
    IRecommendRepository recommendRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Recommend> select(String name,Pageable pageable) {
        if (name==null){
            return recommendRepository.findAll(pageable);
        }else return recommendRepository.getRecommendsByNameLike(name,pageable);
    }

    @Override
    @Transactional
    public Boolean insert(Recommend recommend) {

        Recommend ExistRecommend  = recommendRepository.getRecommendByName(recommend.getName());
        if (ExistRecommend!=null){
            return false;
        }
        String id = ContextUtil.getPrincipal().toString();
        Employee employee = employeeRepository.getOne(id);
        recommend.setEmployee(employee);
        recommendRepository.save(recommend);
        return true;
    }

    @Override
    @Transactional
    public Boolean update(Recommend recommend) {
        Recommend oldRecommend = recommendRepository.getOne(recommend.getId());
        BeanUtils.copyProperties(recommend,oldRecommend);
        recommendRepository.save(recommend);
        return true;
    }

    @Override
    @Transactional
    public Boolean delete(Integer id) {
        boolean exist = recommendRepository.existsById(id);
        if (exist){
            recommendRepository.deleteById(id);
            return true;
        }else {
            return false;
        }

    }
}
