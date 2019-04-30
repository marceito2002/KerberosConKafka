package com.docomo.fraudwall.tracking.persistencia.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.docomo.fraudwall.domain.PartnerDomain;

public interface IPartnerRespository extends MongoRepository<PartnerDomain, String> {

}
