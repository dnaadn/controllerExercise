package com.example.capstone2.Service;

import com.example.capstone2.Model.Place;
import com.example.capstone2.Model.PlaceAccessibility;
import com.example.capstone2.Repository.PlaceAccessibilityRepository;
import com.example.capstone2.Repository.PlaceRepository;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Schema;
import com.google.genai.types.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeminiService {

    private final Client client;
    private final PlaceRepository placeRepository;
    private final PlaceAccessibilityRepository placeAccessibilityRepository;

    public String analyzeAccessibilityRequest(String userRequest) {

        List<Place> places = placeRepository.findAll();

        List<PlaceAccessibility> allFeatures = placeAccessibilityRepository.findAll();

        String placesContext = places.stream().map(p -> {

                    String featuresText = allFeatures.stream()
                            .filter(a -> a.getPlaceId().equals(p.getId()))
                            .map(PlaceAccessibility::getAccessibilityType)
                            .collect(Collectors.joining(", "));

                    return "- " + p.getName() + " (" + p.getCity() + "): "
                            + (featuresText.isEmpty()
                            ? "No accessibility info"
                            : featuresText);
                })
                .collect(Collectors.joining("\n"));

        String prompt = """
                You are an accessibility assistant for an application called Wusool.

                Here are the available places and their accessibility features:

                %s

                User request:
                %s

                Recommend ONLY places from the list above.

                A place should be recommended only if it has ALL of the
                accessibility features required by the user.

                For each matching place, return:
                - placeName
                - city
                - matchingFeatures

                If no place matches all the user's requirements,
                return an empty matches array.

                Do not invent places.
                Do not invent accessibility features.
                Only use information from the provided places list.
                """.formatted(placesContext, userRequest);

        Schema matchingPlaceSchema = Schema.builder()
                        .type(Type.Known.OBJECT)
                        .properties(
                                Map.of(
                                        "placeName",
                                        Schema.builder()
                                                .type(Type.Known.STRING)
                                                .build(),

                                        "city",
                                        Schema.builder()
                                                .type(Type.Known.STRING)
                                                .build(),

                                        "matchingFeatures",
                                        Schema.builder()
                                                .type(Type.Known.ARRAY)
                                                .items(
                                                        Schema.builder()
                                                                .type(Type.Known.STRING)
                                                                .build()
                                                )
                                                .build()
                                )
                        )
                        .required(List.of(
                                "placeName",
                                "city",
                                "matchingFeatures"
                        ))
                        .build();

        Schema responseSchema =
                Schema.builder()
                        .type(Type.Known.OBJECT)
                        .properties(
                                Map.of(
                                        "matches",
                                        Schema.builder()
                                                .type(Type.Known.ARRAY)
                                                .items(matchingPlaceSchema)
                                                .build(),

                                        "message",
                                        Schema.builder()
                                                .type(Type.Known.STRING)
                                                .build()
                                )
                        )
                        .required(List.of("matches", "message"))
                        .build();

        GenerateContentConfig config = GenerateContentConfig.builder()
                        .responseMimeType("application/json")
                        .responseSchema(responseSchema)
                        .build();

        GenerateContentResponse response = client.models.generateContent("gemini-flash-latest", prompt, config);

        return response.text();
    }
}