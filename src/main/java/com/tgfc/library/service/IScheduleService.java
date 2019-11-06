package com.tgfc.library.service;

import com.tgfc.library.request.SchedulePageRequest;
import com.tgfc.library.response.BaseResponse;

import java.text.ParseException;

public interface IScheduleService {


    BaseResponse create(SchedulePageRequest model);

    BaseResponse delete(int id);

    BaseResponse edit(SchedulePageRequest model);

    BaseResponse list(SchedulePageRequest model) throws ParseException;

    BaseResponse changeStatus(int id);

    BaseResponse deleteAllJobs();

    BaseResponse pauseAll();

    BaseResponse resumeAll();

}
