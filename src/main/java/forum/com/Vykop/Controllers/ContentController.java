package forum.com.Vykop.Controllers;

import forum.com.Vykop.Models.Content;
import forum.com.Vykop.Repositories.ContentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class ContentController {

    private final ContentRepository repository;

    ContentController(@Qualifier("contentRepository")ContentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/contents")
    List<Content> all() {
        return repository.findAll();
    }

    @PostMapping("/contents")
    Content newContent(@RequestBody Content newContent) {
        return repository.save(newContent);
    }

    @PutMapping("/contents/{id}")
    Content replaceContent(@RequestBody Content newContent, @PathVariable int id) {
        return repository.findById(id)
                .map(Content -> {
                    Content.setImage(newContent.getImage());
                    Content.setText(newContent.getText());
                    Content.setVideo(newContent.getVideo());
                    return repository.save(Content);
                })
                .orElseGet(() -> {
                    newContent.setId(id);
                    return repository.save(newContent);
                });
    }

    @DeleteMapping("/contents/{id}")
    void deleteContent(@PathVariable int id) {
        repository.deleteById(id);
    }
}
