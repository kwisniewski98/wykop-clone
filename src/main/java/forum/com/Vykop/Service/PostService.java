package forum.com.Vykop.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import forum.com.Vykop.Models.*;
import forum.com.Vykop.Repositories.*;
import forum.com.Vykop.Storage.StorageService;
import javassist.NotFoundException;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.sql.Date;
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

    @Autowired
    private StorageService storageService;

    @Qualifier("sub_vykopRepository")
    @Autowired
    private Sub_vykopRepository sub_vykopRepository;

    @Autowired
    private CommentService commentService;



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
    public List<Post> getFeedPosts(Principal principal, int page) throws NotFoundException {
        User user = userRepository.findByUsername(principal.getName());
        List<Post> posts = new ArrayList<>();
        for (SubVykop subVykop : user.getSubVykopList()) {
            posts.addAll(subVykop.getPosts());
        }
        posts.sort(Comparator.comparing(Post::getCreationDate).thenComparing(Post::getId));
        int size = 3;
        int offset = page * size;
        // Potrzebujemy innych postow niz z zasubskrybowanych
        if (offset + size > posts.size() ) {

            List<Post> allPosts = postRepository.findAll(Sort.by("creationDate", "id"));
            allPosts.removeAll(posts);
            if (offset > posts.size()) {
                offset -= posts.size();
                if (allPosts.size() < offset + size) {
                    size = allPosts.size() - offset;
                }
                if (size < 0) throw new NotFoundException(" ");
                try {
                    return addUpvoteToPosts(allPosts.subList(offset, offset + size), principal.getName());
                } catch (IndexOutOfBoundsException e) {
                    return addUpvoteToPosts(allPosts.subList(offset, allPosts.size()), principal.getName());

                }
            }
            int firstSize = posts.size() - offset;
            List<Post> result;
            try {
                result = posts.subList(offset, offset + firstSize);
            } catch (IndexOutOfBoundsException e){
                result = posts.subList(offset, posts.size());
            }
            try {
                result.addAll(allPosts.subList(0, size -  firstSize));
            } catch (IndexOutOfBoundsException e){
                result.addAll(allPosts.subList(0, allPosts.size()));
            }
            return addUpvoteToPosts(result, principal.getName());
        }
        try {
            return addUpvoteToPosts(posts.subList(offset, offset + size), principal.getName());
        }
        catch (IndexOutOfBoundsException e) {
            return addUpvoteToPosts(posts.subList(offset, posts.size() ), principal.getName());
        }
    }
//        List<Integer> subscribedSubVykops = user.getSubVykopList().stream()
//                .map(x -> x.getId()).collect(Collectors.toList());
//
//        final String CHECK_ACCOUNT = "select Post from Post post inner join sub_vykop sv on sv.id = post.sub_vykopid" +
//                " where sv.id in (:ids)";
//        MapSqlParameterSource sqlParam = new MapSqlParameterSource();
//        sqlParam.addValue("ids", subscribedSubVykops);
//        return pgsqlTemplateNamed.query(CHECK_ACCOUNT, sqlParam, new BeanPropertyRowMapper<>(Post.class));

    public String upvote(int postId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()){
            return "post not found";
        }
        Post p = post.get();
        if (user.getUpvotedPosts().contains(p)) {
            p.setVotes(p.getVotes()-1);
            user.getUpvotedPosts().remove(p);
            p.getUpvotedUsers().remove(user);
            userRepository.save(user);
            postRepository.save(p);
            return "downvote";
        }
        p.setVotes(p.getVotes()+1);
        user.getUpvotedPosts().add(p);
        p.getUpvotedUsers().add(user);
        userRepository.save(user);
        postRepository.save(p);
        return "upvote";
    }
    public List<String> postsMatching(String match, String subVykop) {
        if (match.equals("")) {
            return new ArrayList<>();
        }
        return postRepository.findAll().stream().filter(x -> x.getSubVykop().getName().equals(subVykop))
                .map(Post::getTitle).filter(
                x -> x.contains(match)).collect(Collectors.toList());
    }
    public Post createPost(MultipartFile file, String title, String text, String username, String subvykopName) {
        User user = userRepository.findByUsername(username);
        SubVykop subVykop = sub_vykopRepository.findByName(subvykopName);
        storageService.store(file);
        file.getOriginalFilename();
        Content content = new Content();
        content.setText(text);
        if (file.getContentType().startsWith("image")){
            content.setImage("http://localhost:8080/files/" + file.getOriginalFilename());
        }
        if (file.getContentType().startsWith("video")){
            content.setVideo("http://localhost:8080/files/" + file.getOriginalFilename());
        }
        content = contentRepository.saveAndFlush(content);
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setCreationDate(new Date(System.currentTimeMillis()));
        post.setSubVykop(subVykop);
        post.setVotes(0);
        post.setAuthor(user);
        post = postRepository.saveAndFlush(post);
        content.setPost(post);
        contentRepository.save(content);
        return post;
    }
    public Post postWithCommentUpvoted(Post post, String username){
        User user = userRepository.findByUsername(username);
        Set<Comment> comments = new TreeSet<>();
        for (Comment comment: post.getComments()) {
            comment.setUpvoted(comment.getUpvotedUsers().contains(user));
            comments.add(comment);
        }
        post.setComments(comments);
        return post;
    }
    public List<Post> addUpvoteToPosts(Collection<Post> posts, String username){
        User user = userRepository.findByUsername(username);
        List<Post> resultPosts = new ArrayList<>();
        for(Post post : posts){
            post.setUpvoted(post.getUpvotedUsers().contains(user));
            resultPosts.add(post);
        }
        return resultPosts;
    }
    public List<Post> getPostsBySubvykop(String subVykop, int page, String username) throws NotFoundException {
        List<Post> posts = postRepository.findBySubVykop_Name(subVykop);
        if (posts.size() == 0) throw new NotFoundException("subVykop not found");
        int size = 20;
        int offset = page * size;
        if (posts.size() < offset) throw new NotFoundException("subVykop not found");
        if ( size + posts.size() > offset ) size = posts.size() - offset;
        return addUpvoteToPosts(posts.subList(offset, offset + size), username);

        //return commentService.createComment(text, postId, principal.getName()
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