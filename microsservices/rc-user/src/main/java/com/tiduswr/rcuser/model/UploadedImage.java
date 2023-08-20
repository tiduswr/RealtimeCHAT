package com.tiduswr.rcuser.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor 
@NoArgsConstructor
public class UploadedImage {
    private byte[] imageBytes;
    private String contentType;
    private boolean isEmpty;
}
