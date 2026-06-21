package com.ai.pova.service;

import java.util.List;

import com.ai.pova.model.RagDocument;
import com.ai.pova.model.RagSource;

public interface DocumentLoader {

    boolean supports(String type);

    List<RagDocument> load(RagSource source);
}
