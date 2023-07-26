package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Groupp;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> getAll();
    Optional<Comment> getById(Long id);
    Comment save(Comment comment);

    Comment update(Long id, Comment comment);

    Comment delete(Long id);
}
