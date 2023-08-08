package me.dimitri.libertyweb.service;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.model.response.WebSearchResponse;
import me.dimitri.libertyweb.repository.LookupRepository;
import me.dimitri.libertyweb.repository.SearchRepository;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class SearchService {

    private final LookupRepository lookupRepository;
    private final SearchRepository searchRepository;

    public SearchService(LookupRepository lookupRepository, SearchRepository searchRepository) {
        this.lookupRepository = lookupRepository;
        this.searchRepository = searchRepository;
    }

    public WebSearchResponse searchUser(String username) {
        Optional<UUID> userUUID = lookupRepository.lookupUUID(username);
        return userUUID.map(uuid -> searchRepository.query(uuid, username)).orElse(null);
    }
}
