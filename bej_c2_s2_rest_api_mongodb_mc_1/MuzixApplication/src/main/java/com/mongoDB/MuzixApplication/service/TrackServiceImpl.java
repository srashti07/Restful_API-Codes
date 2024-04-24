package com.mongoDB.MuzixApplication.service;

import com.mongoDB.MuzixApplication.domain.Track;
import com.mongoDB.MuzixApplication.exception.TrackAlreadyExistsException;
import com.mongoDB.MuzixApplication.exception.TrackNotFoundException;
import com.mongoDB.MuzixApplication.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrackServiceImpl implements ITrackService {

    private TrackRepository trackRepository;
    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository){
        this.trackRepository = trackRepository;
    }

    @Override
    public Track saveTrackDetail(Track track) throws TrackAlreadyExistsException {
        if(trackRepository.findById(track.getTrackId()).isPresent()){
            throw new TrackAlreadyExistsException();
        }
        return trackRepository.save(track);
    }

    @Override
    public boolean deleteTrack(int id) throws TrackNotFoundException {
        boolean flag = false;
        if(trackRepository.findById(id).isEmpty())
        {
            throw new TrackNotFoundException();
        }
        else{
            trackRepository.deleteById(id);
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Track> getAllTrackDetails() throws Exception {
        return trackRepository.findAll();
    }

    @Override
    public List<Track> getTrackByRating() throws TrackNotFoundException {
        if (trackRepository.findBytrackRating().isEmpty()){
            throw new TrackNotFoundException();
        }
        return trackRepository.findBytrackRating();
    }


    @Override
    public List<Track> getAllArtistJustinBieber(String artistName) throws TrackNotFoundException {
        if(trackRepository.findAllArtistJustinBieber(artistName).isEmpty())
        {
            throw new TrackNotFoundException();
        }
        return trackRepository.findAllArtistJustinBieber(artistName);
    }
}
