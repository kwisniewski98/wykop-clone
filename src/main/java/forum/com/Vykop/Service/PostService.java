package forum.com.Vykop.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import forum.com.Vykop.Models.*;
import forum.com.Vykop.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.*;


@Component
public class PostService {

    @Autowired
    @Qualifier("pgsqlJdbcTemplate")
    private JdbcTemplate pgsqlTemplate;

    @Autowired
    @Qualifier("pgsqlJdbcTemplateNamed")
    private NamedParameterJdbcTemplate pgsqlTemplateNamed;

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




    @PostConstruct
    public void init(){
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public List<Post> getPostbyUser(User user) {
        final String CHECK_POST = "SELECT * FROM post WHERE author = :user_id";
        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
        sqlParam.addValue("author", user.getId());
        return pgsqlTemplateNamed.query(CHECK_POST, sqlParam, new BeanPropertyRowMapper<>(Post.class));
    }
    public Set<Post> getFeedPosts(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Set<Post> posts = new HashSet<>();
        for (SubVykop subVykop : user.getSubVykopList()) {
            posts.addAll(subVykop.getPosts());

        }
        return posts;
//        List<Integer> subscribedSubVykops = user.getSubVykopList().stream()
//                .map(x -> x.getId()).collect(Collectors.toList());
//
//        final String CHECK_ACCOUNT = "select Post from Post post inner join sub_vykop sv on sv.id = post.sub_vykopid" +
//                " where sv.id in (:ids)";
//        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        sqlParam.addValue("ids", subscribedSubVykops);
//        return pgsqlTemplateNamed.query(CHECK_ACCOUNT, sqlParam, new BeanPropertyRowMapper<>(Post.class));
    }
    public String upvote(int postId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()){
            return "post not found";
        }
        Post p = post.get();
        if (user.getUpvotedPosts().contains(p)) {
            return "user already upvoted this post";
        }
        p.setVotes(p.getVotes()+1);
        user.getUpvotedPosts().add(p);
        p.getUpvotedUsers().add(user);
        userRepository.save(user);
        postRepository.save(p);
        return "ok";
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