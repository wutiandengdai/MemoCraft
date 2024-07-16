package com.softek.memoCraft.repository;

import com.softek.memoCraft.model.TextDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextDocumentRepository extends MongoRepository<TextDocument, String> {
}
