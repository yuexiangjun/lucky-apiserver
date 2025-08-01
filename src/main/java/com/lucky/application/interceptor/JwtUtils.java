package com.lucky.application.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.lucky.domain.exception.BusinessException;
import io.jsonwebtoken.*;
import org.apache.logging.log4j.util.Strings;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Jwt工具类
 *
 * @author happy
 */
public class JwtUtils {
	public static String secret = "fafasdgdhrthfdgnfgfh";


	/**
	 * 创建令牌
	 */
	public static String createToken(TokenEntity entity) {

		if (Objects.isNull(entity))
			return null;

		var jsonObject = JSONObject.parseObject(JSONObject.toJSONString(entity));

		return Jwts.builder()
				.setClaims(jsonObject)
				.setExpiration(new Date(System.currentTimeMillis() + 604800000))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();

	}


	/**
	 * 从令牌中获取数据声明
	 *
	 * @param token 令牌
	 * @return 数据声明
	 */
	public static Claims parseToken(String token) {
		checkToken(token);

		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * 获取客户端
	 */
	public static Integer getClient(String token) {
		Claims claims = parseToken(token);
		String value = getValue(claims, SecurityConstants.CLIENT);
		if (Strings.isNotBlank(value))
			return Integer.parseInt(value);
		return null;
	}

	/**
	 * 获取客户端
	 */
	public static Integer getClient(Claims claims) {

		var value = getValue(claims, SecurityConstants.CLIENT);
		if (Strings.isNotBlank(value))
			return Integer.parseInt(value);
		return null;
	}

	/**
	 * 根据令牌获取用户ID
	 *
	 * @param token 令牌
	 * @return 用户ID
	 */
	public static String getUserId(String token) {

		Claims claims = parseToken(token);
		return getValue(claims, SecurityConstants.DETAILS_USER_ID);
	}

	/**
	 * 根据身份信息获取用户ID
	 *
	 * @param claims 身份信息
	 * @return 用户ID
	 */
	public static String getUserId(Claims claims) {
		return getValue(claims, SecurityConstants.DETAILS_USER_ID);
	}

	/**
	 * 根据令牌获取用户名
	 *
	 * @param token 令牌
	 * @return 用户名
	 */
	public static String getUserName(String token) {
		Claims claims = parseToken(token);
		return getValue(claims, SecurityConstants.DETAILS_USERNAME);
	}

	/**
	 * 根据身份信息获取用户名
	 *
	 * @param claims 身份信息
	 * @return 用户名
	 */
	public static String getUserName(Claims claims) {
		return getValue(claims, SecurityConstants.DETAILS_USERNAME);
	}

	/**
	 * 根据身份信息获取键值
	 *
	 * @param claims 身份信息
	 * @param key    键
	 * @return 值
	 */
	public static String getValue(Claims claims, String key) {
		return toStr(claims.get(key), "");

	}

	public static String toStr(Object value, String defaultValue) {
		if (null == value) {
			return defaultValue;
		}
		if (value instanceof String) {
			return (String) value;
		}
		return value.toString();
	}


	private static void checkToken(String token) {
		if (Objects.isNull(token)) {
			throw BusinessException.newInstance("令牌不能为空");

		}
		Claims claims = null;
		try {
			claims = JwtUtils.parseToken(token);
		} catch (ExpiredJwtException e) {

			throw BusinessException.newInstance("token过期");

		} catch (MalformedJwtException e) {

			throw BusinessException.newInstance("json web token格式错误");

		} catch (Exception e) {

			throw BusinessException.newInstance("token解析异常");
		}
		if (claims == null) {
			throw BusinessException.newInstance("令牌已过期或验证不正确");
		}
		String userId = JwtUtils.getUserId(claims);
		String username = JwtUtils.getUserName(claims);
		Integer client = JwtUtils.getClient(claims);

		if (Strings.isBlank(userId) ||
				Strings.isBlank(username) ||
				Objects.isNull(client) || !List.of(1, 2).contains(client)
		) {
			throw BusinessException.newInstance("令牌验证失败");
		}
	}
}
