package com.tiduswr.rcemailworker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiduswr.rcemailworker.model.Email;

public interface EmailRepository extends JpaRepository<Email, Long>{}
