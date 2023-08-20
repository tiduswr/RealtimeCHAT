package com.tiduswr.rcimageworker.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@AllArgsConstructor 
@NoArgsConstructor
public class UploadedImage {
    private byte[] imageBytes;
    private String contentType;
    private boolean isEmpty;
}
