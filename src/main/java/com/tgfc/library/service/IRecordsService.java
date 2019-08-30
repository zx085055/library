package com.tgfc.library.service;

import com.tgfc.library.request.SendMailRequest;

public interface IRecordsService {
    Boolean returnNotify(SendMailRequest model);
}
