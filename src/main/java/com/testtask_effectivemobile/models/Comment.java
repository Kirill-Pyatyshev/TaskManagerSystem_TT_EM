package com.testtask_effectivemobile.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.testtask_effectivemobile.utils.Views;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "content")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private String content;
    @Column(name = "author")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private String author;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonView({Views.Public.class, Views.PublicLow.class})
    @JoinColumn
    private Task task;

}
