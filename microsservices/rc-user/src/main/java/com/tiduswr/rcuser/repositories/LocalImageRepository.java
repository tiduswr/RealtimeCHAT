package com.tiduswr.rcuser.repositories;

import org.springframework.stereotype.Repository;

import java.awt.image.BufferedImage;
import java.io.IOException;

@Repository
public interface LocalImageRepository {

    void store(String filename, BufferedImage file, String format) throws IOException;

    byte[] retrieveImage(String filename, String format) throws IOException;
}
