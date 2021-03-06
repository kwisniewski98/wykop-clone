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

import javax.persistence.EntityExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public String  subscribe(String username, int subVykopId) throws NotFoundException {
        User user = userRepository.findByUsername(username);
        Optional<SubVykop> subVykop = sub_vykopRepository.findById(subVykopId);
        if (subVykop.isEmpty()) {
            throw new NotFoundException("subVykop not found");
        }
        SubVykop s = subVykop.get();
        if(s.getSubcribedList().contains(user)) {
            user.getSubVykopList().remove(s);
            s.getSubcribedList().remove(user);
            userRepository.save(user);
            sub_vykopRepository.save(s);
            return "not subscribed";
        }
        user.getSubVykopList().add(s);
        s.getSubcribedList().add(user);
        userRepository.save(user);
        sub_vykopRepository.save(s);
        return "subscribed";
    }
    public List<SubVykop> subVykopsMatching(String match) {
        if (match.equals("")) return new ArrayList<>();
        return sub_vykopRepository.findAll().stream().filter(
                x -> x.getName().toLowerCase().contains(match.toLowerCase())).collect(Collectors.toList());
    }
    public SubVykop createSubVykop(MultipartFile banner, MultipartFile avatar, String name, String description, String username) {
        User user = userRepository.findByUsername(username);
        if (sub_vykopRepository.findByName(name) != null){
            throw new EntityExistsException();
        }
        storageService.store(banner);
        storageService.store(avatar);
        SubVykop subVykop = new SubVykop();
        subVykop.setBanner("http://localhost:8080/files/" + banner.getOriginalFilename());
        subVykop.setAvatar("http://localhost:8080/files/" + avatar.getOriginalFilename());
        subVykop.setDescription(description);
        subVykop.setName(name);
        subVykop = sub_vykopRepository.saveAndFlush(subVykop);
        //subVykop.getAdmins().add(user);
        //subVykop = sub_vykopRepository.saveAndFlush(subVykop);
        return subVykop;
    }
}
