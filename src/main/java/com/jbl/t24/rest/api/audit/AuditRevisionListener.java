package com.jbl.t24.rest.api.audit;

import java.util.Optional;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jbl.t24.rest.api.model.MyUserAccountDetails;

public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {

		String currentUser = Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication).filter(Authentication::isAuthenticated)
				.map(Authentication::getPrincipal).map(MyUserAccountDetails.class::cast)
				.map(MyUserAccountDetails::getUsername).orElse("Demo-User");

		AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
		audit.setLogUser(currentUser);

	}

}
