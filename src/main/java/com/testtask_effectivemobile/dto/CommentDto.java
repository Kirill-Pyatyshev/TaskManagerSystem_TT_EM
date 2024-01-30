package com.testtask_effectivemobile.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.testtask_effectivemobile.utils.Views;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto{
    @JsonView(Views.PublicLow.class)
    private String content;
    @JsonView(Views.PublicLow.class)
    private String author;
}
