import model.entity.Club;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> getAll();
    Optional<Comment> getById(Long id);
    Comment save(Comment comment);
}
