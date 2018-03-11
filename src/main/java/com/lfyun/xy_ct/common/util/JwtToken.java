package com.lfyun.xy_ct.common.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtToken {  
	
	private static final String SECRET = "297c615a5d024991a7769b496aa31a4b";
	
    public static String createToken(Long userId) {  
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();  
            map.put("alg", "HS256");  
            map.put("typ", "JWT");  
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 3);
            Date expiresAt = calendar.getTime();
            String token = JWT.create()  
                    .withHeader(map)//header  
                    .withExpiresAt(expiresAt)
                    .withClaim("userId", String.valueOf(userId))//payload  
                    .sign(Algorithm.HMAC256(SECRET));//加密  
            return token;
    	} catch (Throwable e) {
			System.err.println(e.getMessage());
		}
        return "";  
    }  
    
    public static String createUserQCodeData(Long userId) {  
    	try {
    		Map<String, Object> map = new HashMap<String, Object>();  
            map.put("alg", "HS256");  
            map.put("typ", "JWT");  
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, 5);
            Date expiresAt = calendar.getTime();
            String token = JWT.create()  
                    .withHeader(map)//header  
                    .withExpiresAt(expiresAt)
                    .withClaim("userId", String.valueOf(userId))//payload  
                    .sign(Algorithm.HMAC256(SECRET));//加密  
            return token;
    	} catch (Throwable e) {
			System.err.println(e.getMessage());
		}
        return "";  
    }  
    
    public static Long verify(String token) {  
    	DecodedJWT decodedJWT = verifyToken(token);
    	if(decodedJWT != null) {
    		String userId = decodedJWT.getClaim("userId").asString();
    		if(userId != null && !"".equals(userId)) {
    			return Long.parseLong(userId);
    		}
    	}
    	return 0L;
    }
    public static DecodedJWT verifyToken(String token) {  
    	try {
    		JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET))  
                    .build();   
                DecodedJWT jwt = verifier.verify(token);  
                return jwt;
    	} catch (Throwable e) {
			System.err.println(e.getMessage());
		}
    	return null;
    }  
    
    public static void main(String[] args) {
		String token = JwtToken.createToken(1L);
		DecodedJWT verifyToken = verifyToken(token);
		System.out.println(verifyToken.getClaim("userId").asString());
	}
}  
