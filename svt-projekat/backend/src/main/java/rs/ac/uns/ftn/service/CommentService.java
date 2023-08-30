package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Groupp;
import rs.ac.uns.ftn.model.Post;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> getAll();
    Optional<Comment> getById(Long id);
    Comment save(Comment comment);

    Comment update(Long id, Comment comment);

    Comment delete(Long id);

    List<Comment> orderAscLikes(List<Comment> comments);

    List<Comment> orderAscDislikes(List<Comment> comments);

    List<Comment> orderAscHearts(List<Comment> comments);

    List<Comment> orderDescLikes(List<Comment> comments);

    List<Comment> orderDescDislikes(List<Comment> comments);

    List<Comment> orderDescHearts(List<Comment> comments);

    List<Comment> orderAscDate(List<Comment> comments);

    List<Comment> orderDescDate(List<Comment> comments);
}
