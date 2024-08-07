package com.myweb.mamababy.controllers;

import com.myweb.mamababy.dtos.ActivedDTO;
import com.myweb.mamababy.models.Actived;
import com.myweb.mamababy.responses.ResponseObject;
import com.myweb.mamababy.services.Actived.IActivedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/active")
@RequiredArgsConstructor
public class ActivedController {

    private final IActivedService activedService;

    @PostMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> createActive(
            @Valid @RequestBody ActivedDTO activedDTO,
            BindingResult result) {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Actived actived  = activedService.createActived(activedDTO);
            return ResponseEntity.ok(actived);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getAllActived() {
        List<Actived> listActived = activedService.getAllActived();
        return ResponseEntity.ok(ResponseObject
                .builder()
                .message("Get list actived successfully !!!")
                .status(HttpStatus.OK)
                .data(listActived)
                .build());
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<?> getActivedByUserId(
            @PathVariable("id") int userId
    ){
        try{
            List<Actived> listActived = activedService.getActivedByUserId(userId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .message("Get detail product successfully")
                    .status(HttpStatus.OK)
                    .data(listActived)
                    .build());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
