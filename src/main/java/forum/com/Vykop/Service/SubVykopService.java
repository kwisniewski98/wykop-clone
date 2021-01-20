package forum.com.Vykop.Service;

import forum.com.Vykop.Models.SubVykop;
import forum.com.Vykop.Models.User;
import forum.com.Vykop.Repositories.Sub_vykopRepository;
import forum.com.Vykop.Repositories.UserRepository;
import forum.com.Vykop.Storage.StorageService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Optional;

@Component
public class SubVykopService {
    @Qualifier("sub_vykopRepository")
    @Autowired
    private Sub_vykopRepository sub_vykopRepository;

    @Autowired
    private StorageService storageService;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;


    public String uploadBanner(MultipartFile file, int subVykopId){
        String fileURL = "http://localhost:8080/files/" + file.getOriginalFilename();
        Optional<SubVykop> subVykop = sub_vykopRepository.findById(subVykopId);
        if (subVykop.isEmpty()) return "subVykop not found";
        storageService.store(file);
        SubVykop s = subVykop.get();
        s.setBanner(fileURL);
        sub_vykopRepository.save(s);
        return fileURL;
    }
    public boolean isSubscribed(String username, int subVykopId) throws NotFoundException {
        User user = userRepository.findByUsername(username);
        Optional<SubVykop> subVykop = sub_vykopRepository.findById(subVykopId);
        if (subVykop.isEmpty()) {
            throw new NotFoundException("subVykop not found");
        }
        return user.getSubVykopList().contains(subVykop.get());
    }
    public void subscribe(String username, int subVykopId) throws NotFoundException {
        User user = userRepository.findByUsername(username);
        Optional<SubVykop> subVykop = sub_vykopRepository.findById(subVykopId);
        if (subVykop.isEmpty()) {
            throw new NotFoundException("subVykop not found");
        }
        SubVykop s = subVykop.get();
        user.getSubVykopList().add(s);
        s.getSubcribedList().add(user);
        userRepository.save(user);
        sub_vykopRepository.save(s);
    }
}
