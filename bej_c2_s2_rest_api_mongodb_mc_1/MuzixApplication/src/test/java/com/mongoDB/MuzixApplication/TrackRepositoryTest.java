package com.mongoDB.MuzixApplication;

import com.mongoDB.MuzixApplication.domain.Artist;
import com.mongoDB.MuzixApplication.domain.Track;
import com.mongoDB.MuzixApplication.exception.TrackAlreadyExistsException;
import com.mongoDB.MuzixApplication.exception.TrackNotFoundException;
import com.mongoDB.MuzixApplication.repository.TrackRepository;
import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class TrackRepositoryTest {

    @Autowired
    private TrackRepository trackRepository;
    private Artist artist;
    private Track track;
    private Track track1;
    private Track track2;




    @BeforeEach
    void setUp() {
        artist = new Artist(101, "JustinBieber");
        track = new Track(201, "Play track", 5, artist);


           }

    @AfterEach
    void tearDown() {
        artist = null;
        track = null;
        trackRepository.deleteAll();
    }

    @Test
    @DisplayName("Test case for saving track object")
    void givenTrackToSaveShouldReturnSavedTrack() {
        trackRepository.save(track);
        Track track1 = trackRepository.findById(track.getTrackId()).get();
        assertNotNull(track1);
        assertEquals(track.getTrackId(), track1.getTrackId());
    }

    @Test
    @DisplayName("Test case for saving track object - Failure: Different Track ID")
    void givenTrackToSaveShouldNotReturnSavedTrack() {
        trackRepository.save(track);

        // Create a new track with a different ID
        Track differentTrack = new Track();
        differentTrack.setTrackId(451);

        Track track1 = trackRepository.findById(track.getTrackId()).orElse(null);
        assertNotNull(track1);
        assertNotEquals(differentTrack.getTrackId(), track1.getTrackId(), "Track ID mismatch");
    }




    @Test
    @DisplayName("Test case for deleting track object")
    public void givenTrackToDeleteShouldDeleteTrack() {
        trackRepository.insert(track);
        Track track1 = trackRepository.findById(track.getTrackId()).get();
        trackRepository.delete(track1);
        assertEquals(Optional.empty(), trackRepository.findById(track.getTrackId()));

    }
    @Test
    @DisplayName("Test case for deleting track object - Failure: Track not found before deletion")
    public void givenTrackToDeleteShouldNotDeleteTrack() {
        // track is not inserted intentionally
        assertEquals(Optional.empty(), trackRepository.findById(205));

        // Attempt to delete a non-existent track
        trackRepository.delete(new Track());

        // Ensure the track is still not found
        assertEquals(Optional.empty(), trackRepository.findById(205));
    }


    @Test
    @DisplayName("Test case for retrieving all the track object")
    public void givenTrackReturnAllTrackDetails() {

        trackRepository.insert(track);
        Artist artist1 = new Artist(1101, "JustinBieber");
        Track track1 = new Track(1201, "Play track", 4, artist1);
        trackRepository.insert(track1);

        List<Track> list = trackRepository.findAll();
        assertEquals(2, list.size());
        assertEquals("Play track", list.get(1).getTrackName());

    }
    @Test
    @DisplayName("Failure test case for retrieving all the track object")
    public void givenTrackReturnAllTrackDetailsFailure() {
        // Insert a track
        trackRepository.insert(track);

        // Intentionally not inserting the second track (track1)

        // Retrieve the list of tracks
        List<Track> list = trackRepository.findAll();

        // Assert that the size is not as expected
        assertNotEquals(2, list.size(), "Expected size to be different");


    }



    @Test
    @DisplayName("Test case for finding tracks by rating greater than 4")
    void givenTracksWithRatingGreaterThan4ShouldReturnTrackList() {
        // Save tracks with ratings greater than 4
        trackRepository.save(new Track(301, "Track1", 4, artist));
        trackRepository.save(track);
        trackRepository.save(new Track(401, "Track2", 5, artist)); // Rating is 5

        // Call the repository method
        List<Track> result = trackRepository.findBytrackRating( );

        // Verify the result
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(track.getTrackId(), result.get(0).getTrackId());
        assertEquals(track.getTrackName(), result.get(0).getTrackName());
        assertEquals(track.getTrackRating(), result.get(0).getTrackRating());
    }

    @Test
    @DisplayName("Test case for finding tracks by rating greater than 4 - No tracks found")
    void givenNoTracksWithRatingGreaterThan4ShouldReturnEmptyList() {
        // Save tracks with ratings less than or equal to 4
        trackRepository.save(new Track(501, "Track3", 4, artist));
        trackRepository.save(new Track(601, "Track4", 3, artist));
        trackRepository.save(new Track(701, "Track5", 2, artist));

        // Call the repository method
        List<Track> result = trackRepository.findBytrackRating();

        // Verify the result is an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    @DisplayName("Test case for finding tracks by artist name (JustinBieber)")
    void givenArtistNameJustinBieberShouldReturnTrackList() {
        track1 = new Track(201, "Track1", 4, artist);
        track2 = new Track(202, "Track2", 5, artist);

        trackRepository.save(track1);
        trackRepository.save(track2);
        // Call the repository method
        List<Track> result = trackRepository.findAllArtistJustinBieber("JustinBieber");

        // Verify the result
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        // Check if the expected tracks are present in the result
        assertTrue(result.stream().anyMatch(track -> track.getTrackId() == track1.getTrackId()));
        assertTrue(result.stream().anyMatch(track -> track.getTrackId() == track2.getTrackId()));
    }
    @Test
    @DisplayName("Failure test case for finding tracks by non-existing artist name")
    void givenNonExistingArtistNameShouldReturnEmptyList() {
        // Call the repository method with a non-existing artist name
        List<Track> result = trackRepository.findAllArtistJustinBieber("NonExistingArtist");

        // Verify the result is an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}



