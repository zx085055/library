package com.tgfc.library.service.imp;

import com.tgfc.library.entity.Employee;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.enums.RecommendEnum;
import com.tgfc.library.repository.IBookRepository;
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

import java.util.HashMap;
import java.util.Map;

@Transactional
@Service
public class RecommendService implements IRecommendService {

    @Autowired
    IRecommendRepository recommendRepository;

    @Autowired
    IEmployeeRepository employeeRepository;

    @Autowired
    IBookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String name,Pageable pageable) {
        BaseResponse baseResponse = new BaseResponse();
        if (name==null){
            Page<Recommend> recommends = recommendRepository.findAll(pageable);
            Map<String,Object> data = new HashMap<>();
            data.put("totalCount",recommends.getTotalElements());
            data.put("results",recommends.getContent());
            baseResponse.setData(data);
        }else {
            Page<Recommend> recommends = recommendRepository.getRecommendsByNameLike(name, pageable);
            Map<String,Object> data = new HashMap<>();
            data.put("totalCount",recommends.getTotalElements());
            data.put("results",recommends.getContent());
            baseResponse.setData(data);
        }
        baseResponse.setMessage("成功查詢");
        baseResponse.setStatus(true);
        return baseResponse;
    }

    @Override
    public BaseResponse insert(Recommend recommend) {
        BaseResponse baseResponse = new BaseResponse();
        Recommend existRecommend  = recommendRepository.findRecommendByIsbn(recommend.getIsbn());
        if (existRecommend!=null){
            baseResponse.setStatus(false);
            baseResponse.setMessage("已存在此推薦");
        }else if (bookRepository.findByIsbn(recommend.getIsbn())!=null){
            baseResponse.setStatus(false);
            baseResponse.setMessage("已存在此本書籍");
        }else {
            String id = ContextUtil.getAccount();
            Employee employee = employeeRepository.findById(id).get();
            recommend.setEmployee(employee);
            recommend.setStatus(RecommendEnum.RECOMMEND_ALIVE.getCode());
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
            baseResponse.setMessage("無此推薦");
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
            baseResponse.setMessage("無此推薦");
        }
        return baseResponse;
    }
}
