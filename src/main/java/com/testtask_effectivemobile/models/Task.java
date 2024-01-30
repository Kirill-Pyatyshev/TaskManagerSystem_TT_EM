package com.testtask_effectivemobile.models;

import com.fasterxml.jackson.annotation.*;
import com.testtask_effectivemobile.models.enums.TaskPriority;
import com.testtask_effectivemobile.models.enums.TaskStatus;
import com.testtask_effectivemobile.utils.Views;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @JsonView(Views.Private.class)
    private Long id;
    @Column(name = "header")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private String header;
    @Column(name = "description")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private String description;
    @Column(name = "status")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private TaskStatus status;
    @Column(name = "priority")
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private TaskPriority priority;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private User author;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn
    @JsonView({Views.Public.class, Views.PublicLow.class})
    private User executor;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "task")
    @JsonView(Views.Public.class)
    private List<Comment> comments = new ArrayList<>();

    public void addComments(Comment comment) {
        this.comments.add(comment);
    }
}
