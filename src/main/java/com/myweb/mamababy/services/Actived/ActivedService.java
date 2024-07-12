package com.myweb.mamababy.services.Actived;

import com.myweb.mamababy.dtos.ActivedDTO;
import com.myweb.mamababy.exceptions.DataNotFoundException;
import com.myweb.mamababy.models.*;
import com.myweb.mamababy.repositories.ActivedRepository;
import com.myweb.mamababy.repositories.UserRepository;
import com.myweb.mamababy.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivedService implements IActivedService{

    private final ActivedRepository activedRepository;
    private final UserRepository userRepository;
    private final VoucherRepository voucherRepository;

    @Override
    public Actived createActived(ActivedDTO activedDTO) throws DataNotFoundException {

        User existingUser = userRepository
                .findById(activedDTO.getUserID())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find user with id: "+activedDTO.getUserID()));

        Voucher existingVoucher = voucherRepository
                .findById(activedDTO.getVoucherId())
                .orElseThrow(() ->
                        new DataNotFoundException("Cannot find voucher with id: "+activedDTO.getVoucherId()));

        Actived newActived = Actived
                .builder()
                .voucherId(existingVoucher.getId())
                .userId(existingUser.getId())
                .build();

        return activedRepository.save(newActived);
    }

    @Override
    public List<Actived> getActivedByUserId(int id) {
        return activedRepository.findByUserId(id);
    }

    @Override
    public List<Actived> getAllActived() {
        return activedRepository.findAll();
    }

    @Override
    public Actived deleteActived(int id) {
        return null;
    }
}
