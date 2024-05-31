package com.myweb.mamababy.services.Actived;

import com.myweb.mamababy.dtos.ActivedDTO;
import com.myweb.mamababy.dtos.AgeDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.Actived;
import com.myweb.mamababy.models.Age;

import java.util.List;

public interface IActivedService {
    Actived createActived(ActivedDTO activedDTO) throws DataNotFoundException;
    List<Actived> getActivedByUserId(int id);
    List<Actived> getAllActived();
    Actived deleteActived(int id);
}
