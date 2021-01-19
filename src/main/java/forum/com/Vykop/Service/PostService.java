package forum.com.Vykop.Service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import forum.com.Vykop.Models.*;
import forum.com.Vykop.Repositories.*;
import org.hibernate.boot.model.source.spi.Sortable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Component
public class PostService {

    @Autowired
    @Qualifier("pgsqlJdbcTemplate")
    private JdbcTemplate pgsqlTemplate;

    @Autowired
    @Qualifier("pgsqlJdbcTemplateNamed")
    private NamedParameterJdbcTemplate pgsqlTemplateNamed;

    @Qualifier("sub_vykop_listRepository")
    @Autowired
    private Sub_vykop_listRepository sub_vykop_listRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("commentRepository")
    @Autowired
    private CommentRepository commentRepository;

    @Qualifier("contentRepository")
    @Autowired
    private ContentRepository contentRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;
    @Qualifier("postRepository")
    @Autowired
    private PostRepository postRepository;

    public List<Post> getPostbyUser(User user) {
        final String CHECK_POST = "SELECT * FROM post WHERE author = :user_id";
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("author", user.getId());
        return pgsqlTemplateNamed.query(CHECK_POST, sqlParam, new BeanPropertyRowMapper<>(Post.class));
    }
    public List<HashMap<String ,Object>> getFeedPosts(Principal principal) {
        int userId = userRepository.findByUsername(principal.getName()).getId();
        List<Post> posts =  postRepository.findByUser(userId);
        posts = posts.stream().sorted(Comparator.comparing(Post::getCreation_date).reversed())
                .collect(Collectors.toList());
        List<HashMap<String, Object>> result = new ArrayList<>();
        for(Post post : posts){
            HashMap<String , Object> map = new HashMap<>();
            Map<String, Object> m = objectMapper.convertValue(post, Map.class);
            for (Map.Entry<String , Object> entry: m.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
            List<Comment> comments = commentRepository.findByPost(post.getId());
            Content content = contentRepository.findByPost(post.getId()).orElse( new Content());
            map.put("comments", comments);
            map.put("content", content);
            map.put("author", userRepository.findById(post.getAuthor()));
            map.remove("sub_vykopid");
            result.add(map);
        }
        return result;

    }
    /*

    public Account getAccount(Long user_id){
        final String CHECK_ACCOUNT = "SELECT * FROM account WHERE user_id= :user_id";
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("user_id", user_id);
        return pgsqlTemplateNamed.queryForObject(CHECK_ACCOUNT, sqlParam, new BeanPropertyRowMapper<>(Account.class));
    }

    public List<Post> getHistory(Integer id){
        final String CHECK_HISTORY = "SELECT * FROM history WHERE account_id = :id";
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("id", id);
        return pgsqlTemplateNamed.query(CHECK_HISTORY, sqlParam, new BeanPropertyRowMapper<>(Post.class));
    }

    public Piwo getPivo(Long pivo_id){
        final String CHECK_PIWO = "SELECT * FROM piwo WHERE id = :id";
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("id", pivo_id);
        return pgsqlTemplateNamed.queryForObject(CHECK_PIWO, sqlParam, new BeanPropertyRowMapper<>(Piwo.class));
    }

    public void addHistory(Account account, Piwo piwo){
        final String INSERT_QUERY = "insert into history (piwo_id, history_date, account_id) values (:piwo_id, :history_date, :account_id)";
        Date date = Date.valueOf(LocalDate.now());
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("piwo_id", piwo.getId());
        sqlParam.addValue("history_date", date);
        sqlParam.addValue("account_id", account.getId());
        pgsqlTemplateNamed.update(INSERT_QUERY,sqlParam);
    }

    public void addPoints(Long account_id, Long pivo_id){
        final String UPDATE_QUERY = "update account set points = :points where id = :id";
        final String CHECK_ACCOUNT = "SELECT * FROM account WHERE id = :id";
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("id", account_id);
        Account account = pgsqlTemplateNamed.queryForObject(CHECK_ACCOUNT, sqlParam, new BeanPropertyRowMapper<>(Account.class));
        Piwo piwo = getPivo(pivo_id);
        MapSqlParameterSource sqlParam_update = new MapSqlParameterSource();
        sqlParam_update.addValue("points", account.getPoints()+piwo.getValue());
        sqlParam_update.addValue("id", account_id);
        pgsqlTemplateNamed.update(UPDATE_QUERY, sqlParam_update);
        addHistory(account,piwo);
    }
*/
}