package br.uece.sisdoc.configuration;

import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenAuthenticationService {

	// EXPIRATION_TIME = 10 dias
	static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 10;
	static final String SECRET = "MySecret";
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";
	
	
    public void addAuthentication(HttpServletResponse response, String username) {

        String JWT = Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SECRET)
            .compact();

        response.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);

    }
    
    
    public static Authentication getAuthentication(HttpServletRequest request) {
    	
        String token = request.getHeader(HEADER_STRING);
        
        if (token != null) {
            // parse the token.
            String username = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody()
                .getSubject();

            if (username != null) // we managed to retrieve a user
            {
                return new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
            }

        }

        return null;

    }
	
}
