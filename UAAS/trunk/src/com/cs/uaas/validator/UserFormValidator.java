package com.cs.uaas.validator;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.bind.Property;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.validator.AbstractValidator;

import com.cs.uaas.base.LoggerUtil;

public class UserFormValidator extends AbstractValidator {
	Logger logger = LoggerUtil.getLogger();

	@Override
	public void validate(ValidationContext ctx) {
		// all the bean properties
		Map<String, Property> beanProps = ctx.getProperties(ctx.getProperty().getBase());

		// first let's check the passwords match
		validatePasswords(ctx, (String) beanProps.get("password").getValue(),
				(String) ctx.getValidatorArg("retypedPassword"));
		Object birthday = beanProps.get("birthday").getValue();
		logger.debug("birthday:" + birthday);
		validateBirthday(ctx, (Date) birthday);
		Object weight = beanProps.get("weight").getValue();
		logger.debug("weight:" + weight);
		validateWeight(ctx, (Double) weight);
		validateEmail(ctx, (String) beanProps.get("email").getValue());
	}

	private void validatePasswords(ValidationContext ctx, String password, String retype) {
		if (password == null || retype == null || (!password.equals(retype))) {
			this.addInvalidMessage(ctx, "password", "Your passwords do not match!");
		}
	}

	private void validateWeight(ValidationContext ctx, Double weight) {
		if (weight != null && weight <= 0) {
			this.addInvalidMessage(ctx, "weight", "Your weight should be > 0!");
		}
	}

	private void validateBirthday(ValidationContext ctx, Date birthday) {
		if (birthday == null) {
			this.addInvalidMessage(ctx, "birthday", "Your birthday should not null!");
		}
	}

	private void validateEmail(ValidationContext ctx, String email) {
		if (email == null || !email.matches(".+@.+\\.[a-z]+")) {
			this.addInvalidMessage(ctx, "email", "Please enter a valid email!");
		}
	}
}
