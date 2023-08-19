package com.tiduswr.rcauth.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tiduswr.rcauth.models.dto.UserPasswordRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/internal/password")
public class InternalPasswordService {
    
    @PostMapping("/check")
    public ResponseEntity<Void> checkPassword(@Valid @RequestBody UserPasswordRequestDTO passwordRequestDTO){
        return ResponseEntity.ok().build();
    }

}
