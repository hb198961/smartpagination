package com.cs.uaas.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.uaas.base.BaseDao;
import com.cs.uaas.base.LoggerUtil;
import com.cs.uaas.model.Resource;
import com.cs.uaas.model.SubSystem;

@Service
public class ResourceService {
	Logger logger = LoggerUtil.getLogger();
	@Autowired
	BaseDao baseDao;

	@SuppressWarnings("unchecked")
	public List<SubSystem> getAllSubSystem() {
		DetachedCriteria dc = DetachedCriteria.forClass(SubSystem.class);
		dc.add(Restrictions.eq("enableFlg", true));
		return baseDao.findByCriteria(dc);
	}

	@SuppressWarnings("unchecked")
	public List<Resource> findResource(Resource resource) {
		DetachedCriteria dc = DetachedCriteria.forClass(Resource.class);
		if (resource.getSubSystem() != null && resource.getSubSystem().getId() != null) {
			dc.createAlias("subSystem", "ss");
			dc.add(Restrictions.eq("ss.id", resource.getSubSystem().getId()));
		}
		if (StringUtils.isNotEmpty(resource.getResourceName())) {
			dc.add(Restrictions.like("resourceName", "%" + resource.getResourceName() + "%"));
		}
		if (StringUtils.isNotEmpty(resource.getUrl())) {
			dc.add(Restrictions.like("url", "%" + resource.getUrl() + "%"));
		}
		dc.add(Restrictions.eq("enableFlg", true));
		List<Resource> resourceList = baseDao.findByCriteria(dc);
		List<Resource> resultList = new ArrayList<Resource>();
		for (Resource res : resourceList) {
			Resource resourceResult = new Resource();
			BeanUtils.copyProperties(res, resourceResult, Resource.class);
			baseDao.getHibernateTemplate().initialize(resourceResult.getSubSystem());
			resultList.add(resourceResult);
		}
		return resultList;
	}

	public void saveResource(Resource resource) {
		baseDao.saveOrUpdate(resource);
	}

}
