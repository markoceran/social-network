package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Reaction;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.repository.ReactionRepository;
import rs.ac.uns.ftn.service.ReactionService;

import java.util.List;
import java.util.Optional;

@Service
public class ReactionServiceImpl implements ReactionService {

    @Autowired
    ReactionRepository reactionRepository;

    @Override
    public List<Reaction> getAll() {
        return reactionRepository.findAll();
    }

    @Override
    public Optional<Reaction> getById(Long id) {
        return reactionRepository.findById(id);
    }

    @Override
    public Reaction save(Reaction reaction) {
        try{
            return reactionRepository.save(reaction);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Reaction update(Long id, Reaction reaction) {

        Optional<Reaction> toUpdate = this.getById(id);

        if (toUpdate.isPresent()) {

            toUpdate.get().setType(reaction.getType());
            toUpdate.get().setTimestamp(reaction.getTimestamp());
            reactionRepository.save(toUpdate.get());

            return toUpdate.get();

        } else {
            return null;
        }

    }

    @Override
    public Reaction delete(Long id) {

        Optional<Reaction> reaction = this.getById(id);
        if(reaction.isPresent()){
            reactionRepository.deleteById(id);
            return reaction.get();
        }else {return null;}

    }
}
