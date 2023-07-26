package rs.ac.uns.ftn.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.model.Comment;
import rs.ac.uns.ftn.model.Image;
import rs.ac.uns.ftn.repository.CommentRepository;
import rs.ac.uns.ftn.repository.ImageRepository;
import rs.ac.uns.ftn.service.ImageService;

import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Override
    public List<Image> getAll() {
        return imageRepository.findAll();
    }

    @Override
    public Optional<Image> getById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image save(Image image) {
        try{
            return imageRepository.save(image);
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    @Override
    public Image delete(Long id) {

        Optional<Image> image = this.getById(id);
        if(image.isPresent()){
            imageRepository.deleteById(id);
            return image.get();
        }else {return null;}

    }
}
