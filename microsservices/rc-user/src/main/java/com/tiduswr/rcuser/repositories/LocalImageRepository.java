package com.tiduswr.rcuser.repositories;

import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public interface LocalImageRepository {
    byte[] retrieveImage(String filename, String format) throws IOException;
}
