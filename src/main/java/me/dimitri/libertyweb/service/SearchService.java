package me.dimitri.libertyweb.service;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.UsernameAPI;
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

    /*
    TODO: Validate if given username/UUID are of valid characters and length
     */
    public WebSearchResponse searchUser(String input) {
        Optional<UUID> userUUID = convertUUID(input);
        Optional<String> username;

        if (userUUID.isPresent()) { // Search used UUID
            username = lookupRepository.lookupUsername(userUUID.get());
        } else {
            username = Optional.of(input);
            userUUID = lookupRepository.lookupUUID(username.get());
        }

        if (userUUID.isPresent() && username.isPresent()) {
            return searchRepository.query(userUUID.get(), username.get());
        }
        return null;
    }

    private Optional<UUID> convertUUID(String input) {
        try {
            return Optional.of(UUID.fromString(input));
        } catch (Exception ignored) {}
        return Optional.empty();
    }
}
