package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Post;
import rs.ac.uns.ftn.model.Reaction;
import rs.ac.uns.ftn.model.ReactionType;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.service.CommentService;
import rs.ac.uns.ftn.service.ReactionService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ReactionService reactionService;


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
            toUpdate.get().setIsDeleted(comment.getIsDeleted());
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

    @Override
    public List<Comment> orderAscLikes(List<Comment> comments) {

        List<Reaction> reactions = reactionService.getAll();

        for(Comment c:comments){
            for(Reaction r:reactions){
                if(r.getComment() != null && r.getComment().getId().equals(c.getId()) && r.getType().equals(ReactionType.LIKE)){
                    c.setLikes(c.getLikes() + 1);
                }
            }
        }

        Collections.sort(comments, Comparator.comparing(Comment::getLikes));
        return comments;
    }

    @Override
    public List<Comment> orderAscDislikes(List<Comment> comments) {

        List<Reaction> reactions = reactionService.getAll();

        for(Comment c:comments){
            for(Reaction r:reactions){
                if(r.getComment() != null && r.getComment().getId().equals(c.getId()) && r.getType().equals(ReactionType.DISLIKE)){
                    c.setDislikes(c.getDislikes() + 1);
                }
            }
        }

        Collections.sort(comments, Comparator.comparing(Comment::getDislikes));
        return comments;
    }

    @Override
    public List<Comment> orderAscHearts(List<Comment> comments) {

        List<Reaction> reactions = reactionService.getAll();

        for(Comment c:comments){
            for(Reaction r:reactions){
                if(r.getComment() != null && r.getComment().getId().equals(c.getId()) && r.getType().equals(ReactionType.HEART)){
                    c.setHearts(c.getHearts() + 1);
                }
            }
        }

        Collections.sort(comments, Comparator.comparing(Comment::getHearts));
        return comments;
    }

    @Override
    public List<Comment> orderDescLikes(List<Comment> comments) {

        List<Reaction> reactions = reactionService.getAll();

        for(Comment c:comments){
            for(Reaction r:reactions){
                if(r.getComment() != null && r.getComment().getId().equals(c.getId()) && r.getType().equals(ReactionType.LIKE)){
                    c.setLikes(c.getLikes() + 1);
                }
            }
        }

        Collections.sort(comments, Comparator.comparing(Comment::getLikes).reversed());
        return comments;
    }

    @Override
    public List<Comment> orderDescDislikes(List<Comment> comments) {

        List<Reaction> reactions = reactionService.getAll();

        for(Comment c:comments){
            for(Reaction r:reactions){
                if(r.getComment() != null && r.getComment().getId().equals(c.getId()) && r.getType().equals(ReactionType.DISLIKE)){
                    c.setDislikes(c.getDislikes() + 1);
                }
            }
        }

        Collections.sort(comments, Comparator.comparing(Comment::getDislikes).reversed());
        return comments;
    }

    @Override
    public List<Comment> orderDescHearts(List<Comment> comments) {

        List<Reaction> reactions = reactionService.getAll();

        for(Comment c:comments){
            for(Reaction r:reactions){
                if(r.getComment() != null && r.getComment().getId().equals(c.getId()) && r.getType().equals(ReactionType.HEART)){
                    c.setHearts(c.getHearts() + 1);
                }
            }
        }

        Collections.sort(comments, Comparator.comparing(Comment::getHearts).reversed());
        return comments;
    }

    @Override
    public List<Comment> orderAscDate(List<Comment> comments) {
        Collections.sort(comments, Comparator.comparing(Comment::getTimestamp));
        return comments;
    }

    @Override
    public List<Comment> orderDescDate(List<Comment> comments) {
        Collections.sort(comments, Comparator.comparing(Comment::getTimestamp).reversed());
        return comments;
    }
}
