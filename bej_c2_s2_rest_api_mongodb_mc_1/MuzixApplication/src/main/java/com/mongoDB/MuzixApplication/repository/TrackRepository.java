package com.mongoDB.MuzixApplication.repository;

import com.mongoDB.MuzixApplication.domain.Track;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TrackRepository extends MongoRepository<Track,Integer> {
    @Query("{'trackRating':{$gt:4}}")
     List<Track> findBytrackRating();

    @Query("{'trackArtist.artistName': {$in : [?0]}}")
    List<Track>findAllArtistJustinBieber(String artistName);
}
