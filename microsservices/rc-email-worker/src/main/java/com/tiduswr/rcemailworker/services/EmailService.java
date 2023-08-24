package com.tiduswr.rcemailworker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tiduswr.rcemailworker.model.Email;
import com.tiduswr.rcemailworker.repositories.EmailRepository;

@Service
public class EmailService {
    
    @Autowired
    private EmailRepository emailRepository;

    public void sendEmail(Email email){
        
    }

}
