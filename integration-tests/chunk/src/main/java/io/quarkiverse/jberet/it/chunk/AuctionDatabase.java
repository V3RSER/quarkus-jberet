package io.quarkiverse.jberet.it.chunk;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AuctionDatabase {
    private final ConcurrentMap<Long, Auction> database = new ConcurrentHashMap<>();

    void put(Auction auction) {
        database.put(auction.getId(), auction);
    }

    List<Auction> get() {
        return new ArrayList<>(database.values());
    }

    Auction getById(Long id) {
        return database.get(id);
    }

    public boolean isEmpty() {
        return database.isEmpty();
    }
}
