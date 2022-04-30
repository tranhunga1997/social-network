package com.socialnetwork.common.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.socialnetwork.common.utils.MessageUtils;

import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
	
	/**
	 * Lỗi không xác định
	 * @param e
	 * @return http 400
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String exceptionHandle(Exception e) {
		log.error(e.getMessage());
		e.getStackTrace();
		return "Lỗi không xác định";
	}
	
	
//	@ExceptionHandler(AbstractSocialException.class)
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	public String abstractSocialExceptionHandle(AbstractSocialException e) {
//		log.debug(e.getMessage());
//		return e.getMessage();
//	}
	
	@ExceptionHandler(InputException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public String inputExceptionHandle(InputException e) {
		String msg = MessageUtils.getMessage(e.getMessageCode(), e.getArgs());
		log.debug(msg);
		return msg;
	}
	
	/**
	 * Lỗi liên quan đến nhập form
	 * @param e
	 * @return http 400
	 */
	@ExceptionHandler(BindException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public List<String> bindExceptionHandle(BindException e) {
		// lấy danh sách thông báo lỗi 
		List<String> listErr = e.getBindingResult()
				.getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
		
		return listErr;
	}
	
	/**
	 * Lỗi liên quan đến logic và filter
	 * @param e
	 * @return http status 400, 401
	 */
	@ExceptionHandler(SocialException.class)
	public ResponseEntity<String> socialException(SocialException e) {
		// đoạn if trả về lỗi unauthentication viết sau
		
		//
		String msg = MessageUtils.getMessage(e.getMessageCode(), e.getArgs());
		log.debug(msg);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
	}
	
}
