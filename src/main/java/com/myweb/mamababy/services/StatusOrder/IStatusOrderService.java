package com.myweb.mamababy.services.StatusOrder;

import com.myweb.mamababy.dtos.StatusOrderDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.StatusOrder;

import java.util.List;

public interface IStatusOrderService {

    StatusOrder createStatusOrder(StatusOrderDTO statusOrderDTO) throws Exception;

    StatusOrder updateStatusOrder(int id, StatusOrderDTO statusOrderDTO)throws DataNotFoundException;

    List<StatusOrder> getAllStatusOrder() throws Exception;
}
