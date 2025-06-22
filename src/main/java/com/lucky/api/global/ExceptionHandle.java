package  com.lucky.api.global;

import com.lucky.api.utils.R;
import com.lucky.domain.exception.BusinessException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class ExceptionHandle {


	@ExceptionHandler(BusinessException.class)
	public R handleBusinessException(HttpServletResponse response, BusinessException e) {
		this.log.error(e.getMessage(), e);
		if (Objects.nonNull(e.getCode()))
			return R.fail(e.getCode(), e.getMessage());
		return R.fail(e.getMessage());
	}


	@ExceptionHandler(Exception.class)
	public R handleException(HttpServletResponse response, Exception e) {
		e.printStackTrace();
		this.log.error(e.getMessage(), e);
		return R.fail(e.getMessage());
	}

	@ExceptionHandler(Throwable.class)
	public R handleException(HttpServletResponse response, Throwable throwable) {
		this.log.error(throwable.getMessage(), throwable);
		return R.fail(throwable.getMessage());
	}

	@ExceptionHandler(ClassCastException.class)
	public R handleException(HttpServletResponse response, ClassCastException throwable) {
		this.log.error(throwable.getMessage(), throwable);
		return R.fail(throwable.getMessage());
	}

}
