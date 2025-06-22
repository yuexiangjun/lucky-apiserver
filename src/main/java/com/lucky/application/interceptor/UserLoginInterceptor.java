package com.lucky.application.interceptor;


import com.lucky.domain.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.servlet.HandlerInterceptor;


import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class UserLoginInterceptor implements HandlerInterceptor {


	/***
	 * 在请求处理之前进行调用(Controller方法调用之前)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		try {

			this.checkToken(request.getHeader(SecurityConstants.AUTHORIZATION_HEADER));


		} catch (BusinessException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(e.getMessage());
			return false;
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write("拦截器发生错误了");
			return false;
		}

		return true;

	}

	private void checkToken(String token) {
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