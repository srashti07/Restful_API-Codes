package com.mongoDB.MuzixApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongoDB.MuzixApplication.controller.TrackController;
import com.mongoDB.MuzixApplication.domain.Artist;
import com.mongoDB.MuzixApplication.domain.Track;
import com.mongoDB.MuzixApplication.exception.TrackAlreadyExistsException;
import com.mongoDB.MuzixApplication.service.TrackServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TrackControllerTest {

    private MockMvc mockMvc;
    @Mock
    private TrackServiceImpl trackService;

    @InjectMocks
    private TrackController trackController;
    private Track track1, track2;
    private Artist artist1, artist2;
    List<Track> trackList;
    @BeforeEach
    void setUp(){
        artist1 = new Artist(1101,"Justin Bieber");
        track1 = new Track(1201,"Play Track",5, artist1);
        artist2 = new Artist(1102,"Badshah");
        track2 = new Track(1202,"Hip-pop",4,artist2);
        trackList = Arrays.asList(track1,track2);

        mockMvc = MockMvcBuilders.standaloneSetup(trackController).build();
    }
    @AfterEach
    void tearDown(){
        track1 = null;
        track2 = null;
    }
    @Test
    public void givenTrackToSaveReturnSavedTrack() throws Exception{
        when(trackService.saveTrackDetail(any())).thenReturn(track1);
        mockMvc.perform(post("/api/v1/track")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonToString(track1)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).saveTrackDetail(any());
    }
    @Test
    public void givenCustomerToSaveReturnSavedTrackFailure() throws Exception {
        when(trackService.saveTrackDetail(any())).thenThrow(TrackAlreadyExistsException.class);
        mockMvc.perform(post("/api/v1/track")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(track1)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).saveTrackDetail(any());

    }
    @Test
    public void givenTrackIdDeleteTrack() throws Exception {
        when(trackService.deleteTrack(anyInt())).thenReturn(true);
        mockMvc.perform(delete("/api/v1/track/1201")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(trackService,times(1)).deleteTrack(anyInt());

    }
    @Test
    public void givenTrackIdDeleteTrackFailure() throws Exception {
        when(trackService.deleteTrack(anyInt())).thenReturn(false); // Mock to simulate deletion failure
        mockMvc.perform(delete("/api/v1/track/1201")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(trackService, times(1)).deleteTrack(anyInt());
    }

    @Test
    public void getAllTrackSuccess() throws Exception {

        List<Track> mockTracks = Arrays.asList(new Track(/*...*/), new Track(/*...*/));
        when(trackService.getAllTrackDetails()).thenReturn(mockTracks);

        mockMvc.perform(get("/api/v1/tracks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockTracks.size())) // Assuming a JSON array response
                .andDo(MockMvcResultHandlers.print());


        verify(trackService, times(1)).getAllTrackDetails();
    }
    @Test
    public void getAllTrackFailure() throws Exception {

        when(trackService.getAllTrackDetails()).thenThrow(new RuntimeException("Some error occurred"));

        mockMvc.perform(get("/api/v1/tracks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error occurred"))
                .andDo(MockMvcResultHandlers.print());


        verify(trackService, times(1)).getAllTrackDetails();
    }
    @Test
    public void getAllArtistJustinBieberSuccess() throws Exception {

        List<Track> mockTracks = Arrays.asList(new Track(/*...*/), new Track(/*...*/));
        when(trackService.getAllArtistJustinBieber(anyString())).thenReturn(mockTracks);

        mockMvc.perform(get("/api/v1/tracks/artist/JustinBieber")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockTracks.size())) // Assuming a JSON array response
                .andDo(MockMvcResultHandlers.print());


        verify(trackService, times(1)).getAllArtistJustinBieber("JustinBieber");
    }

    @Test
    public void getAllArtistJustinBieberFailure() throws Exception {

        when(trackService.getAllArtistJustinBieber(anyString())).thenThrow(new RuntimeException("Some error occurred"));

        mockMvc.perform(get("/api/v1/tracks/artist/JustinBieber")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error occurred"))
                .andDo(MockMvcResultHandlers.print());


        verify(trackService, times(1)).getAllArtistJustinBieber("JustinBieber");
    }
    @Test
    public void getTrackByRatingSuccess() throws Exception {
        List<Track> mockTracks = Arrays.asList(new Track(/*...*/), new Track(/*...*/));
        when(trackService.getTrackByRating()).thenReturn(mockTracks);

        mockMvc.perform(get("/api/v1/tracks/rating")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockTracks.size()));

        verify(trackService, times(1)).getTrackByRating();
    }
    @Test
    public void getTrackByRatingFailure() throws Exception {
        when(trackService.getTrackByRating()).thenThrow(new RuntimeException("Some error occurred"));

        mockMvc.perform(get("/api/v1/tracks/rating")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Some error occurred"));

        verify(trackService, times(1)).getTrackByRating();
    }



    private static String jsonToString(final Object ob) throws JsonProcessingException {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(ob);
            result = jsonContent;
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }

        return result;
    }




}
