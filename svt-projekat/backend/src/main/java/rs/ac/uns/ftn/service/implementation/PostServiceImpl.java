package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.repository.PostRepository;
import rs.ac.uns.ftn.service.PostService;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    PostRepository postRepository;

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> getById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post save(Post post) {
        try{
            return postRepository.save(post);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Post update(Long id, Post post) {

        Optional<Post> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setContent(post.getContent());
            toUpdate.get().setCreationDate(post.getCreationDate());
            postRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public Post delete(Long id) {

        Optional<Post> post = this.getById(id);
        if(post.isPresent()){
            postRepository.deleteById(id);
            return post.get();
        }else {return null;}

    }
}
