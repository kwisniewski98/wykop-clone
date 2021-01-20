package forum.com.Vykop.Controllers;

import forum.com.Vykop.Storage.StorageException;
import forum.com.Vykop.Storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;

@Controller
public class UploadController {
    @Autowired
    private StorageService storageService;

    @PostConstruct
    public void init() {
        storageService.init();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        HttpHeaders headers = new HttpHeaders();
        Resource resource;
        try {
            resource = storageService.loadAsResource(filename);
        } catch (StorageException e) {
            return new ResponseEntity(null, headers, HttpStatus.NOT_FOUND);
        }
        headers.setContentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM));
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @PostMapping("/file")
    public ResponseEntity handleFileUpload(@RequestParam("file") MultipartFile file) {
        storageService.store(file);
        return ResponseEntity.ok().body("http://localhost:8080/files/" + file.getOriginalFilename());
    }
}
