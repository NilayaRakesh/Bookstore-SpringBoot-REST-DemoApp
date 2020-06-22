package com.nr.bookstore.model.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MediaCoverage {
    private Integer userId;
    private Integer id;
    private String title;
    private String body;
}
