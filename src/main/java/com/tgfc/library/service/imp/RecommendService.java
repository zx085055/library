package com.tgfc.library.service.imp;

import com.tgfc.library.entity.EmployeeSafty;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.enums.RecommendEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafty;
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
    IEmployeeRepositorySafty employeeRepository;

    @Autowired
    IBookRepository bookRepository;

    BaseResponse.Builder builder;

    @Override
    @Transactional(readOnly = true)
    public BaseResponse select(String name, Pageable pageable) {
        builder = new BaseResponse.Builder();
        if (name == null) {
            Page<Recommend> recommends = recommendRepository.findAll(pageable);
            builder.content(recommends);
        } else {
            Page<Recommend> recommends = recommendRepository.getRecommendsByNameLike(name, pageable);
            builder.content(recommends);
        }
        builder.message("成功查詢");
        return builder.build();
    }

    @Override
    public BaseResponse insert(Recommend recommend) {
        builder = new BaseResponse.Builder();
        Recommend existRecommend = recommendRepository.findRecommendByIsbn(recommend.getIsbn());
        if (existRecommend != null) {
            builder.status(false).message("已存在此推薦");
        } else if (bookRepository.findByIsbn(recommend.getIsbn()).size() > 0) {
            builder.status(false).message("已存在此本書籍");
        } else {
            String id = ContextUtil.getAccount();
            EmployeeSafty employee = employeeRepository.findById(id).get();
            recommend.setEmployee(employee);
            recommend.setStatus(RecommendEnum.RECOMMEND_ALIVE.getCode());
            recommendRepository.save(recommend);
            builder.message("成功新增一筆");
        }
        return builder.build();
    }

    @Override
    public BaseResponse update(Recommend recommend) {
        builder = new BaseResponse.Builder();
        Boolean exist = recommendRepository.existsById(recommend.getId());
        if (exist) {
            Recommend dateRecommend = recommendRepository.getOne(recommend.getId());
            BeanUtils.copyProperties(recommend, dateRecommend);
            recommendRepository.save(dateRecommend);
            builder.message("成功更新一筆");
        } else {
            builder.status(false).message("無此推薦");
        }

        return builder.build();
    }

    @Override
    public BaseResponse delete(Integer id) {
        builder = new BaseResponse.Builder();
        boolean exist = recommendRepository.existsById(id);
        if (exist) {
            recommendRepository.deleteById(id);
            builder.message("成功刪除一筆");
        } else {
            builder.status(false).message("無此推薦");
        }
        return builder.build();
    }
}
