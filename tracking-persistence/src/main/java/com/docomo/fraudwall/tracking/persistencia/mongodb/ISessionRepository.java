package com.docomo.fraudwall.tracking.persistencia.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.docomo.fraudwall.domain.SessionDomain;

public interface ISessionRepository extends MongoRepository<SessionDomain, String> {
	
}
