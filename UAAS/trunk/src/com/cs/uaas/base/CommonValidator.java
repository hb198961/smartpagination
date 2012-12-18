package com.cs.uaas.base;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zkplus.databind.Binding;
import org.zkoss.zkplus.databind.BindingValidateEvent;

@SuppressWarnings("rawtypes")
public class CommonValidator implements org.springframework.validation.Validator {
	Logger logger = LoggerUtil.getLogger();

	private Validator validator;

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public WrongValuesException validate(BindingValidateEvent event) {
		Iterator<Component> refs = event.getReferences().iterator();
		Iterator<Binding> bindings = event.getBindings().iterator();
		Iterator<?> values = event.getValues().iterator();

		List<WrongValueException> wvelist = new ArrayList<WrongValueException>();
		while (refs.hasNext() && bindings.hasNext() && values.hasNext()) {
			Component ref = refs.next();
			Binding binding = bindings.next();
			Object value = values.next();
			if (value != null) {
				logger.debug(value.getClass());
			}
			Object bean = binding.getBean(ref);
			String expr = binding.getExpression();
			String propName = expr.substring(expr.lastIndexOf('.') + 1);

			@SuppressWarnings("unchecked")
			Set<ConstraintViolation<?>> vs = validate(bean.getClass(), propName, value);

			// collect WrongValueException
			for (ConstraintViolation<?> v : vs) {
				wvelist.add(new WrongValueException(ref, v.getMessage()));
			}
		}

		if (!wvelist.isEmpty()) {
			return new WrongValuesException(wvelist.toArray(new WrongValueException[0]));
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Object populateFormModel(BindingValidateEvent event, Class formClass) {
		Iterator<Component> refs = event.getReferences().iterator();
		Iterator<Binding> bindings = event.getBindings().iterator();
		Iterator<?> values = event.getValues().iterator();
		Object model;
		try {
			model = formClass.newInstance();
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			return null;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
		while (refs.hasNext() && bindings.hasNext() && values.hasNext()) {
			Binding binding = bindings.next();
			Object value = values.next();
			String expr = binding.getExpression();
			expr = expr.substring(expr.indexOf(".") + 1);
			expr = expr.substring(expr.indexOf(".") + 1);
			Map bindPropertyPair = new HashMap();
			bindPropertyPair.put(expr, value);
			try {
				BeanUtils.populate(model, bindPropertyPair);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage(), e);
				continue;
			} catch (InvocationTargetException e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
		return model;
	}

	/**
	 * 对非表单bind字段进行校验：有一些特殊场景，无法对提交的表单字段本身进行validate，而需要结合整个model其它字段计算，
	 * 例如：需要根据id和email一起查询检查email是否重复
	 * @param event
	 * @param formClass 要进行校验的form的类型
	 * @param propertyPathToValidate 要进行校验的字段
	 * @param errorPropertyPathToReport 字段校验出错后，向上报异常的字段名称（告诉框架错误信息属于页面那个组件）
	 * @return
	 */
	public WrongValuesException validateModelProperty(BindingValidateEvent event, Class formClass,
			String propertyPathToValidate, String errorPropertyPathToReport) {
		Iterator<Component> refs = event.getReferences().iterator();
		Iterator<Binding> bindings = event.getBindings().iterator();
		Component errorPropertyRef = null;
		while (refs.hasNext() && bindings.hasNext()) {
			Component ref = refs.next();
			Binding binding = bindings.next();
			String expr = binding.getExpression();
			String propName = expr.substring(expr.indexOf('.') + 1);
			propName = propName.substring(propName.indexOf('.') + 1);
			if (propName.equals(errorPropertyPathToReport)) {
				errorPropertyRef = ref;
				break;
			}
		}
		if (errorPropertyRef == null) {
			logger.warn("errorPropertyPath[" + errorPropertyPathToReport + "]not found in formFieldMap!");
			return null;
		}
		Object formModel = populateFormModel(event, formClass);
		List<WrongValueException> wvelist = new ArrayList<WrongValueException>();
		Set<ConstraintViolation<Object>> errors = validator.validateProperty(formModel, propertyPathToValidate,
				Default.class);
		for (ConstraintViolation<Object> v : errors) {
			String violationPropertyPath = v.getPropertyPath().toString();
			if (violationPropertyPath.equals(propertyPathToValidate)) {
				logger.debug("Property[" + propertyPathToValidate + "]" + v.getMessage());
				WrongValueException wve = new WrongValueException(errorPropertyRef, v.getMessage());
				wvelist.add(wve);
			}
		}
		if (!wvelist.isEmpty()) {
			return new WrongValuesException(wvelist.toArray(new WrongValueException[0]));
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private Set validate(Class clazz, String propName, Object value) {
		return validator.validateValue(clazz, propName, value);
	}

	/**
	 * 将多个校验错误信息合并，然后一起向上抛给框架处理
	 */
	public void throwWrongValues(WrongValuesException... errorsArray) {
		List<WrongValueException> wvelist = new ArrayList<WrongValueException>();
		for (WrongValuesException errors : errorsArray) {
			if (errors != null) {
				for (WrongValueException wrongvalue : errors.getWrongValueExceptions()) {
					wvelist.add(wrongvalue);
				}
			}
		}
		if (!wvelist.isEmpty()) {
			throw new WrongValuesException(wvelist.toArray(new WrongValueException[0]));
		}
	}

	@Override
	public boolean supports(Class clazz) {
		return true;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(target);
		for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
			String propertyPath = constraintViolation.getPropertyPath().toString();
			String message = constraintViolation.getMessage();
			errors.rejectValue(propertyPath, "", message);
		}
	}
}