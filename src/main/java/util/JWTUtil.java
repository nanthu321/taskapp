package util;

import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;


public class JWTUtil {

    private static final String SECRET = "mysecretkeymysecretkeymysecretkey";
    private static final long EXPIRY_MS = 1000L * 60 * 60 * 24; 

    public String generateToken(String subject) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + EXPIRY_MS);
		Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(now)
				.setExpiration(expiry)
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims verifyToken(String token) {
	    try {
	        return Jwts.parserBuilder()
	                .setSigningKey(SECRET.getBytes(StandardCharsets.UTF_8))
	                .build()
	                .parseClaimsJws(token)
	                .getBody();   

	    } catch (JwtException | IllegalArgumentException e) {
	        return null;
	    }
	}
  
	 public static int extractUserId(String token) {

	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        String userIdStr = claims.getSubject(); 
	        return Integer.parseInt(userIdStr);
	    }
}