package com.mongoDB.MuzixApplication.service;

import com.mongoDB.MuzixApplication.domain.Track;
import com.mongoDB.MuzixApplication.exception.TrackAlreadyExistsException;
import com.mongoDB.MuzixApplication.exception.TrackNotFoundException;

import java.util.List;

public interface ITrackService {
    Track saveTrackDetail(Track track) throws TrackAlreadyExistsException;
    boolean deleteTrack(int id) throws TrackNotFoundException;
    List<Track> getAllTrackDetails() throws Exception;
//    List<Track> getAllTrackRatingGreaterThan4(int trackRating) throws TrackNotFoundException;

    List<Track> getTrackByRating() throws TrackNotFoundException ;

    List<Track> getAllArtistJustinBieber(String artistName) throws TrackNotFoundException;
}
