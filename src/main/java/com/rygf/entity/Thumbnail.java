package com.rygf.entity;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//...
@Embeddable
public class Thumbnail {
    private String uri;
    private boolean embedded;
}
