package com.rygf.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@NoArgsConstructor
@AllArgsConstructor
@Data
//...
@Embeddable
public class Image {
    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String url;
}
