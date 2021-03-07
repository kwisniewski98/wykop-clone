package forum.com.Vykop.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class JsonConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init(){
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.addMixIn(Object.class, IgnoreHibernatePropertiesInJackson.class);
    }
}
