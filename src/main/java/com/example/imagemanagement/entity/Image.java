package com.example.imagemanagement.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

@Data
@Builder
public class Image {

    @Id
    private int id;

    private String filename;

    @Column("mime_type")
    private String mimeType;

    private byte[] data;
}
