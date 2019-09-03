package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.repository.IEmployeeRepository;
import com.tgfc.library.repository.IRecommendRepository;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecommendService;
import com.tgfc.library.util.ContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class RecommendService implements IRecommendService {

    @Autowired
    IRecommendRepository recommendRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String name,Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        if (name==null){
            Page<Recommend> recommends = recommendRepository.findAll(pageable);
            baseResponse.setData(recommends.getContent());
        }else {
            Page<Recommend> recommends = recommendRepository.getRecommendsByNameLike(name, pageable);
            baseResponse.setData(recommends.getContent());
        }
        baseResponse.setMessage("成功查詢");
        baseResponse.setStatus(true);
        return baseResponse;
    }

    @Override
    public BaseResponse insert(Recommend recommend) {
        BaseResponse baseResponse = new BaseResponse();
        Recommend ExistRecommend  = recommendRepository.getRecommendByName(recommend.getName());
        if (ExistRecommend!=null){
            baseResponse.setStatus(false);
            baseResponse.setMessage("已存在此紀錄");
        }else{
            //String id = ContextUtil.getPrincipal().toString();
            Employee employee = employeeRepository.getOne("TGFC062");
            recommend.setEmployee(employee);
            recommendRepository.save(recommend);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功新增一筆");
        }

        return baseResponse;
    }

    @Override
    public BaseResponse update(Recommend recommend) {
        BaseResponse baseResponse = new BaseResponse();
        Boolean exist = recommendRepository.existsById(recommend.getId());
        if (exist){
            Recommend dateRecommend = recommendRepository.getOne(recommend.getId());
            BeanUtils.copyProperties(recommend,dateRecommend);
            recommendRepository.save(dateRecommend);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功更新一筆");
        }else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此紀錄");
        }

        return baseResponse;
    }

    @Override
    public BaseResponse delete(Integer id) {
        BaseResponse baseResponse = new BaseResponse();
        boolean exist = recommendRepository.existsById(id);
        if (exist){
            recommendRepository.deleteById(id);
            baseResponse.setStatus(true);
            baseResponse.setMessage("成功刪除一筆");
        }else {
            baseResponse.setStatus(false);
            baseResponse.setMessage("無此紀錄");
        }
        return baseResponse;
    }

    @Override
    public BaseResponse findAll(Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        Page<Recommend> recommends = recommendRepository.findAll(pageable);
        baseResponse.setData(recommends.getContent());
        baseResponse.setStatus(true);
        baseResponse.setMessage("成功查詢");
        return baseResponse;
    }
}
