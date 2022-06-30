package services;

import java.security.Key;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

public class JWTParser {
	public static String parseUsername(List<String> authHeaders) throws JwtException {
		String token = authHeaders.get(0).substring("Bearer".length()).trim();

		Jws<Claims> jws;
		String username = "";

		SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
		Key key = keyGenerator.generateKey();

		jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
		username = jws.getBody().getSubject();
		return username;
	}

	public static String parseRole(List<String> authHeaders) throws JwtException {
		String token = authHeaders.get(0).substring("Bearer".length()).trim();

		Jws<Claims> jws;
		String role = "";

		SimpleKeyGenerator keyGenerator = new SimpleKeyGenerator();
		Key key = keyGenerator.generateKey();

		jws = Jwts.parser().setSigningKey(key).parseClaimsJws(token);
		role = (String) jws.getBody().get("role");
		return role;
	}

	/*public static User parseUser(List<String> authHeaders) throws JwtException {

	}*/
}
