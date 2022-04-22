package com.socialnetwork.common.utils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.socialnetwork.common.entities.user.UserInfo;
import com.socialnetwork.common.exceptions.SocialException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenProvider {
	
	// JWT Secret
    private static final String JWT_SECRET = "111111111111111111";
    // Hạn sử dụng JWT (format: ngày*giờ*phút*giây)
    private static final long JWT_EXPIRATION = 1*1*60*60L; 
	
    /**
     * Tạo jwt dựa theo thông tin user
     * @param username
     * @param refreshTokenId tác dụng tăng tính an toàn
     * @return JWT
     */
    public static String generateJwt(String username, long refreshToken){
        String jwt = Jwts.builder().setSubject(username).setId(String.valueOf(refreshToken))
                .setIssuedAt(new Date()).setExpiration(generateExpirationDate()).signWith(SignatureAlgorithm.HS512,JWT_SECRET)
                .compact();
        return jwt;
    }
    
    /**
     * Tạo token random (UUID)
     * @return String
     */
    public static String generateToken() {
    	return UUID.randomUUID().toString();
    }
    
    /**
     * Lấy jwt từ request
     * @param request
     * @return jwt
     */
    public static String getJwtFromRequest(HttpServletRequest request){
        String bearToken = request.getHeader("Authorization");
        if(StringUtil.isNull(bearToken) && !StringUtils.hasText(bearToken) && !bearToken.startsWith("Bearer ")){
            throw new SocialException("W_00002");
        }
        return bearToken.substring(7);
    }
   
    /**
     * Lấy username từ jwt
     * @param jwt
     * @return username
     */
    public static String getUserUsernameFromRequest(HttpServletRequest request){
    	String jwt = getJwtFromRequest(request);
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }
    
    public static long getRefreshTokenIdFromRequest(HttpServletRequest request) {
    	String jwt = getJwtFromRequest(request);
    	Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
    	return Long.parseLong(claims.getId());
    }
    
    /**
     * Kiểm tra jwt có đúng hay không
     * @param jwt
     * @return
     */
    public static boolean validateJwt(String jwt){
        try{
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
    
    /**
     * Khởi tạo ngày sử dụng jwt
     * @return date
     */
    private static Date generateExpirationDate() {
        return Date.from(Instant.now().plusSeconds(JWT_EXPIRATION));
    }
    
    /**
     * Kiểm tra hạn sử dụng jwt
     * @param jwt
     * @return <code>true</code> còn hạn, <code>false</code> hết hạn
     */
    public static boolean isJwtExpired(String jwt){
        Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
        return claims.getExpiration().after(new Date()) ? true : false;
    }
    
    /**
     * Lấy hạn sử dụng của jwt
     * @param jwt
     * @return date
     */
    public static Date getExpiryDate(String jwt) {
    	Claims claims = Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(jwt).getBody();
    	return claims.getExpiration();
    }
}
