package rs.ac.uns.ftn.service;

import rs.ac.uns.ftn.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    List<Post> getAll();
    Optional<Post> getById(Long id);
    Post save(Post post);

    Post update(Long id, Post post);

    Post delete(Long id);

    List<Post> orderAsc(List<Post> posts);

    List<Post> orderDesc(List<Post> posts);
}
