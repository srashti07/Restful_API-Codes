package com.mongoDB.MuzixApplication.controller;

import com.mongoDB.MuzixApplication.domain.Track;
import com.mongoDB.MuzixApplication.exception.TrackAlreadyExistsException;
import com.mongoDB.MuzixApplication.exception.TrackNotFoundException;
import com.mongoDB.MuzixApplication.service.ITrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.when;

@RestController
@RequestMapping("api/v1/")
public class TrackController {

    private ResponseEntity responseEntity;
    private ITrackService iTrackService;
    @Autowired
    public TrackController(final ITrackService iTrackService){
        this.iTrackService = iTrackService;
    }
    @PostMapping("track")
    public ResponseEntity<?> saveTrack(@RequestBody Track track) throws TrackAlreadyExistsException{
        try{
            iTrackService.saveTrackDetail(track);
            responseEntity = new ResponseEntity(track, HttpStatus.CREATED);
        }catch (TrackAlreadyExistsException e){
            throw new TrackAlreadyExistsException();
        }
        catch (Exception e)
        {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @DeleteMapping("track/{trackId}")
    public ResponseEntity<?> deleteTrack(@PathVariable("trackId")int trackId)throws TrackNotFoundException {
        try {
            iTrackService.deleteTrack(trackId);
            responseEntity = new ResponseEntity("Successfully deleted!!!", HttpStatus.OK);
        } catch (Exception exception) {
            responseEntity = new ResponseEntity(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("tracks")
    public ResponseEntity<?> getAllTrack(){
        try{
            responseEntity = new ResponseEntity(iTrackService.getAllTrackDetails(),HttpStatus.OK);
        }catch (Exception exception){
            responseEntity = new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }
    @GetMapping("tracks/artist/{artistName}")
    public ResponseEntity<?> getAllArtistJustinBieber(@PathVariable String artistName){
        try{
            responseEntity = new ResponseEntity(iTrackService.getAllArtistJustinBieber(artistName),HttpStatus.OK);
        }catch (Exception exception){
            responseEntity = new ResponseEntity(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @GetMapping("tracks/rating")
    public ResponseEntity<?> getTrackByRating( ){
        try{
            responseEntity = new ResponseEntity(iTrackService.getTrackByRating(),HttpStatus.OK);
        }catch (Exception exception){
            responseEntity = new ResponseEntity<>(exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }



    }

