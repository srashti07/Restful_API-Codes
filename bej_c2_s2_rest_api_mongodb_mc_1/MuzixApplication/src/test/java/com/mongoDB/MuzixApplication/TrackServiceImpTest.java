package com.mongoDB.MuzixApplication;

import com.mongoDB.MuzixApplication.domain.Artist;
import com.mongoDB.MuzixApplication.domain.Track;
import com.mongoDB.MuzixApplication.exception.TrackAlreadyExistsException;
import com.mongoDB.MuzixApplication.exception.TrackNotFoundException;
import com.mongoDB.MuzixApplication.repository.TrackRepository;
import com.mongoDB.MuzixApplication.service.TrackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackServiceImpTest {
    @Mock
    private TrackRepository trackRepository;
    @InjectMocks
    private TrackServiceImpl trackService;

    private Track track1, track2;
    List<Track> trackList;
    Artist artist1,artist2;
    @BeforeEach
    void setUp(){
        artist1 = new Artist(1001,"Justin Bieber");
        track1 = new Track(2001,"Play Track",5,artist1);
        artist2 = new Artist(1002,"Badshah");
        track2 = new Track(2002,"Sufi",4,artist2);
        trackList = Arrays.asList(track1,track2);
    }
    @AfterEach
   void tearDown(){
        track1 = null;
        track2 = null;
}
    @Test
    public void givenTrackToSaveReturnSavedTrackSuccess() throws TrackAlreadyExistsException{
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(null));
        when(trackRepository.save(any())).thenReturn(track1);
        assertEquals(track1,trackService.saveTrackDetail(track1));
        verify(trackRepository,times(1)).save(any());
        verify(trackRepository,times(1)).findById(any());
}
    @Test
    public void givenTrackToSaveReturnSavedTrackFailure(){
      when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(track1));
      assertThrows(TrackAlreadyExistsException.class,()->trackService.saveTrackDetail(track1));
      verify(trackRepository,times(0)).save(any());
      verify(trackRepository,times(1)).findById(any());
    }
    @Test
    public void givenTrackToDeleteShouldDeleteSuccess() throws TrackNotFoundException{
        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.ofNullable(track1));
        boolean flag = trackService.deleteTrack(track1.getTrackId());
        assertEquals(true,flag);

        verify(trackRepository,times(1)).deleteById(any());
        verify(trackRepository,times(1)).findById(any());
    }
    @Test
    public void givenNonExistentTrackToDeleteFailure() throws TrackNotFoundException {

        when(trackRepository.findById(track1.getTrackId())).thenReturn(Optional.empty());

        // Try to delete the track and expect a TrackNotFoundException
        assertThrows(TrackNotFoundException.class, () -> {
            trackService.deleteTrack(track1.getTrackId());
        });

        // Ensuring that deleteById and findById were not called
        verify(trackRepository, never()).deleteById(any());
        verify(trackRepository, times(1)).findById(any());
    }

    @Test
    public void getAllTrackDetailsSuccess() throws Exception {
        // Simulating the scenario where tracks are present in the repository
        List<Track> tracks = Arrays.asList(track1, track2);
        when(trackRepository.findAll()).thenReturn(tracks);

        List<Track> result = trackService.getAllTrackDetails();

        // Verifying that the findAll method is called once
        verify(trackRepository, times(1)).findAll();

        // Verify that the result matches the expected list of tracks
        assertEquals(tracks, result);
    }

    @Test
    public void getAllTrackDetailsFailure() throws Exception {
        // Simulate the scenario where an exception occurs while retrieving tracks
        when(trackRepository.findAll()).thenThrow(new RuntimeException("Error fetching tracks"));

        // Trying to get all track details and expect an exception
        assertThrows(Exception.class, () -> {
            trackService.getAllTrackDetails();
        });

        // Verify that the findAll method is called once
        verify(trackRepository, times(1)).findAll();
    }

    @Test
    public void getTrackByRatingSuccess() throws TrackNotFoundException {
        // Simulate the scenario where tracks with ratings are present in the repository
        List<Track> tracks = Arrays.asList(track1, track2);
        when(trackRepository.findBytrackRating()).thenReturn(tracks);

        List<Track> result = trackService.getTrackByRating();

        // Verify that the findBytrackRating method is called once
        verify(trackRepository, times(2)).findBytrackRating();

        // Verify that the result matches the expected list of tracks
        assertEquals(tracks, result);
    }

    @Test
    public void getTrackByRatingFailure() throws TrackNotFoundException {
        // Simulate the scenario where no tracks with ratings are found
        when(trackRepository.findBytrackRating()).thenReturn(Collections.emptyList());

        // Try to get tracks by rating and expect a TrackNotFoundException
        assertThrows(TrackNotFoundException.class, () -> {
            trackService.getTrackByRating();
        });

        // Verify that the findBytrackRating method is called once
        verify(trackRepository, times(1)).findBytrackRating();
    }

    @Test
    public void getAllArtistJustinBieberSuccess() throws TrackNotFoundException {
        // Simulate the scenario where tracks by Justin Bieber are present in the repository
        List<Track> justinBieberTracks = Arrays.asList(track1, track2);
        String artistName = "Justin Bieber";

        when(trackRepository.findAllArtistJustinBieber(artistName)).thenReturn(justinBieberTracks);

        List<Track> result = trackService.getAllArtistJustinBieber(artistName);

        // Verify that the findAllArtistJustinBieber method is called once
        verify(trackRepository, times(2)).findAllArtistJustinBieber(artistName);

        // Verify that the result matches the expected list of tracks by Justin Bieber
        assertEquals(justinBieberTracks, result);
    }

    @Test
    public void getAllArtistJustinBieberFailure() throws TrackNotFoundException {
        // Simulate the scenario where no tracks by Justin Bieber are found
        String artistName = "Justin Bieber";

        when(trackRepository.findAllArtistJustinBieber(artistName)).thenReturn(Collections.emptyList());

        // Try to get tracks by Justin Bieber and expect a TrackNotFoundException
        assertThrows(TrackNotFoundException.class, () -> {
            trackService.getAllArtistJustinBieber(artistName);
        });

        // Verify that the findAllArtistJustinBieber method is called once
        verify(trackRepository, times(1)).findAllArtistJustinBieber(artistName);
    }









}
