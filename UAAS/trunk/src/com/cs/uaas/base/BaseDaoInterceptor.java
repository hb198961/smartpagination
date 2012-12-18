package com.cs.uaas.base;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import com.cs.uaas.security.AuthenticationUtil;

@SuppressWarnings("serial")
public class BaseDaoInterceptor extends EmptyInterceptor {

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		return audit(entity, state, propertyNames);
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {
		for (int i = 0; i < propertyNames.length; i++) {
			if ("lastUpdateDttm".equals(propertyNames[i])) {
				currentState[i] = new Date();
			} else if ("lastUpdateUser".equals(propertyNames[i])) {
				currentState[i] = AuthenticationUtil.getCurrentUser();
			}
		}
		return true;
	}

	private boolean audit(Object entity, Object[] state, String[] propertyNames) {
		boolean changed = false;
		if (entity instanceof BaseHibernateModelNoId) {
			for (int i = 0; i < propertyNames.length; i++) {
				String propertyName = propertyNames[i];
				if ("crtDttm".equals(propertyName)) {
					Object currState = state[i];
					if (currState == null) {
						state[i] = new Date();
						changed = true;
					}
				} else if ("lastUpdateDttm".equals(propertyName)) {
					Object currState = state[i];
					if (currState == null) {
						state[i] = new Date();
						changed = true;
					}
				} else if ("crtUser".equals(propertyName)) {
					Object currState = state[i];
					if (currState == null) {
						state[i] = AuthenticationUtil.getCurrentUser();
						changed = true;
					}
				} else if ("lastUpdateUser".equals(propertyName)) {
					Object currState = state[i];
					if (currState == null) {
						state[i] = AuthenticationUtil.getCurrentUser();
						changed = true;
					}
				}
			}
		}

		return changed;
	}
}