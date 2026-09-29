package com.example.capstone2.Controller;

import com.example.capstone2.Service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class GeminiController {

    private final GeminiService geminiService;

    @PostMapping("/ask-ai")
    public String analyzeAccessibility(@RequestBody String request){
        return geminiService.analyzeAccessibilityRequest(request);
    }
}
