package me.dimitri.libertyweb.repository;

import jakarta.inject.Singleton;
import me.dimitri.libertyweb.api.LibertyWeb;

import java.util.Optional;
import java.util.UUID;

@Singleton
public class LookupRepository {
    private final LibertyWeb libertyWeb;

    public LookupRepository(LibertyWeb libertyWeb) {
        this.libertyWeb = libertyWeb;
    }

    public Optional<UUID> lookupUUID(String username) {
        return libertyWeb.getApi().getUserResolver()
                .lookupUUID(username)
                .toCompletableFuture()
                .join();
    }

    public Optional<String> lookupUsername(UUID UUID) {
        return libertyWeb.getApi().getUserResolver()
                .lookupName(UUID)
                .toCompletableFuture()
                .join();
    }
}
