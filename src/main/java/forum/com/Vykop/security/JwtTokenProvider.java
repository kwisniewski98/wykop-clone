package forum.com.Vykop.security;
import forum.com.Vykop.Models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Key;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;



import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {
    @Value("${security.jwt.token.secret-key:secret-key")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private int validityInMilliseconds = 3600000; // 1h

    @Autowired
    private myUserDetails myUserDetails;


    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(User user, List<User> users) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        claims.put("auth", users.stream().map(s -> new SimpleGrantedAuthority(user.getRole())));

        Date today = new Date();
        return Jwts.builder().setIssuer("Vykop").setSubject(user.getUsername())
                .claim("role", user.getRole()).setIssuedAt(today)
                // add one hour to actual date
                .setExpiration(new Date(today.getTime() + (1000 * 60 * 60))).
                        signWith(SignatureAlgorithm.HS256,
                                getSigningKey())
                .compact();

    }

    public boolean validateToken(String jwt)throws Exception {
        try {
            Jwts.parser().setSigningKey(secretKey).parse(jwt);
        } catch (JwtException | IllegalArgumentException e) {
            throw new Exception("Expired or invalid JWT token");
        }
        return true;
    }
    public String resolveToken(HttpServletRequest req){
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    public String getUser(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJwt(token).getBody().getIssuer();
    }
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetails.loadUserByUsername(getUser(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
