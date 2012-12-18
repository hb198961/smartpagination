package com.cs.uaas.validator;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;
import org.zkoss.spring.SpringUtil;

import com.cs.uaas.model.UserContactInfo;
import com.cs.uaas.service.UserService;

@Component
public class UserEmailValidator implements ConstraintValidator<DistinctEmail, UserContactInfo> {

	@Resource
	UserService userService;

	@Override
	public void initialize(DistinctEmail constraintAnnotation) {
		userService = (UserService) SpringUtil.getBean("userService");
	}

	@Override
	public boolean isValid(UserContactInfo value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		return userService.validateEmailExisting(value.getEmail(), value.getUserModel().getId());
	}

}