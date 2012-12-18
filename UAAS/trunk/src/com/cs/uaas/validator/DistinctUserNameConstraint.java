package com.cs.uaas.validator;

import javax.annotation.Resource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.cs.uaas.model.User;
import com.cs.uaas.service.UserService;

@Component
public class DistinctUserNameConstraint implements ConstraintValidator<DistinctUserName, User> {

	@Resource
	UserService userService;

	@Override
	public void initialize(DistinctUserName constraintAnnotation) {
	}

	@Override
	public boolean isValid(User value, ConstraintValidatorContext context) {
		if (value == null) {
			return false;
		}
		return userService.validateUserNameExisting(value.getUserName(), value.getId());
	}

}