package com.testtask_effectivemobile.repositories;

import com.testtask_effectivemobile.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
