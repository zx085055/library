package com.tgfc.library.service.imp;

import com.tgfc.library.entity.EmployeeSafety;
import com.tgfc.library.entity.Recommend;
import com.tgfc.library.enums.RecommendEnum;
import com.tgfc.library.repository.IBookRepository;
import com.tgfc.library.repository.IEmployeeRepositorySafety;
import com.tgfc.library.repository.IRecommendRepository;
import com.tgfc.library.response.BaseResponse;
import com.tgfc.library.service.IRecommendService;
import com.tgfc.library.util.ContextUtil;
import com.tgfc.library.util.MessageUtil;
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
    IEmployeeRepositorySafety employeeRepository;

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
        builder.message(MessageUtil.getMessage("recommend.searchSuccess")).status(true);
        return builder.build();
    }

    @Override
    public BaseResponse insert(Recommend recommend) {
        builder = new BaseResponse.Builder();
        Recommend existRecommend = recommendRepository.findRecommendByIsbn(recommend.getIsbn());
        if (existRecommend != null) {
            builder.status(false).message(MessageUtil.getMessage("recommend.recommendExisted"));
        } else if (bookRepository.findByIsbn(recommend.getIsbn()).size() > 0) {
            builder.status(false).message(MessageUtil.getMessage("recommend.bookExisted"));
        } else {
            String id = ContextUtil.getAccount();
            EmployeeSafety employee = employeeRepository.findById(id).get();
            recommend.setEmployee(employee);
            recommend.setStatus(RecommendEnum.RECOMMEND_ALIVE.getCode());
            recommendRepository.save(recommend);
            builder.message(MessageUtil.getMessage("recommend.insertSuccess")).status(true);
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
            builder.message(MessageUtil.getMessage("recommend.updateSuccess")).status(true);
        } else {
            builder.status(false).message(MessageUtil.getMessage("recommend.findNoData"));
        }

        return builder.build();
    }

    @Override
    public BaseResponse delete(Integer id) {
        builder = new BaseResponse.Builder();
        boolean exist = recommendRepository.existsById(id);
        if (exist) {
            recommendRepository.deleteById(id);
            builder.message(MessageUtil.getMessage("recommend.deleteSuccess")).status(true);
        } else {
            builder.status(false).message(MessageUtil.getMessage("recommend.findNoData"));
        }
        return builder.build();
    }
}
