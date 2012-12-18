package com.cs.uaas.base;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.cs.uaas.model.User;

public class CommonValidatorTest extends BaseSpringTestCase {
	@Resource
	CommonValidator commonValidator;

	@Test
	public void testValidate() {
		User target = new User();
		target.getContactInfo().setEmail("asdf");
		BindException errors = new BindException(target, "userModel");
		commonValidator.validate(target, errors);

		for (FieldError error : errors.getFieldErrors()) {
			System.out.println("invalid value for: '" + error.getField() + "': " + error.getDefaultMessage());
		}

		Assert.assertEquals(6, errors.getFieldErrors().size());

		target.setUserName("asdf");
		target.getContactInfo().setEmail("asdf@qwe.com");
		target.getContactInfo().setMobile("13612345678");
		target.setRealName("asdf");
		target.setPassword("123456");
		target.setConfirmPassword("123456");
		errors = new BindException(target, "userModel");
		commonValidator.validate(target, errors);
		Assert.assertEquals(0, errors.getFieldErrors().size());
	}
}