package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.service.CommentService;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> getById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        try{
            return commentRepository.save(comment);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Comment update(Long id, Comment comment) {

        Optional<Comment> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setText(comment.getText());
            toUpdate.get().setTimestamp(comment.getTimestamp());

            commentRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public Comment delete(Long id) {

        Optional<Comment> comment = this.getById(id);
        if(comment.isPresent()){
            comment.get().setIsDeleted(true);
            commentRepository.save(comment.get());
            return comment.get();
        }else {return null;}

    }
}
