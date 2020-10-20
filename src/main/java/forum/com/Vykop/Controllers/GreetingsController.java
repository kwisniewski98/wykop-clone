package forum.com.Vykop.Controllers;



import java.util.concurrent.atomic.AtomicLong;

import forum.com.Vykop.Models.Greetings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class GreetingsController {

    private static final String template = "Hello, siusiak %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greetings greeting(@RequestParam(value = "name", defaultValue = "halo penis") String name) {
        return new Greetings(counter.incrementAndGet(), String.format(template, name));
    }
}