package com.fitness.aiservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityAiService {
	
   private final GeminiService geminiService;
   
   
   public ActivityAiService(GeminiService geminiService) {
       this.geminiService = geminiService;
   }
	
	public Recommendation generateRecommendations(Activity activity) {
		// Placeholder for AI recommendation generation logic
		String promt = createPromtForActivity(activity);
		String aiResponse = geminiService.getRecommendationFromGemini(promt);
		log.info("Generating recommendations from AI for activity: {}", aiResponse);
		// AI logic would go here
		return processAiResponse(activity, aiResponse);
	}
	private Recommendation processAiResponse(Activity activity, String aiResponse) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(aiResponse);
			JsonNode textNode = rootNode.path("candidates")
					.get(0).path("content")
					.get("parts")
					.get(0).path("text");
			
			String jsonContent = textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n```", "")
                    .trim();
			log.info("Cleaned Response from AI for activity : {}", jsonContent);
			
			JsonNode analysisJson = mapper.readTree(jsonContent);
			JsonNode analysisNode = analysisJson.path("analysis");
			StringBuilder fullAnalysis = new StringBuilder();
			addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
			addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
			addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
			addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");
			
			List<String> improvements = extractImprovements(analysisJson.path("improvements"));
			List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
			List<String> safety = extractSafetyGuidLines(analysisJson.path("safety"));
				
			return Recommendation.builder()
					.activityId(activity.getId())
					.type(activity.getType().toString())
					.userId(activity.getUserId())
					.recommendationText(fullAnalysis.toString().trim())
					.improvementTips(improvements)
					.suggestions(suggestions)
					.safety(safety)
					.createdAt(java.time.LocalDateTime.now())
					.build();
			
		} catch (Exception e) {
			e.printStackTrace();
			return createDefaultRecommendation(activity);
		}
		
	}
      private Recommendation createDefaultRecommendation(Activity activity) {
		return Recommendation.builder()
				.activityId(activity.getId())
				.type(activity.getType().toString())
				.userId(activity.getUserId())
				.recommendationText("No detailed analysis available at the moment.")
				.improvementTips(Collections.singletonList("Continue regular exercise and maintain a balanced diet"))
				.suggestions(Collections.singletonList("Consider consulting a fitness professional for personalized advice"))
				.safety(Arrays.asList("Always warm up before exercising", 
						"Stay hydrated during workouts", 
						"Listen to your body and avoid overexertion"
						))
				.createdAt(java.time.LocalDateTime.now())
				.build();
	}

	  private List<String> extractSafetyGuidLines(JsonNode safetyNode) {
		List<String> safetyGuidelines = new ArrayList<>();
		if(safetyNode.isArray()) {
			safetyNode.forEach(guideline -> {
				safetyGuidelines.add(guideline.asText());
			});
			return safetyGuidelines.isEmpty() ? Collections.singletonList("No specific safety guidelines provided") : safetyGuidelines;
		}
		return Collections.singletonList("No specific safety guidelines provided");
	}

	  private List<String> extractSuggestions(JsonNode suggestionsNode) {
		List<String> suggestions = new ArrayList<>();
		if(suggestionsNode.isArray()) {
			suggestionsNode.forEach(suggestion -> {
				String workout = suggestion.path("workout").asText();
				String description = suggestion.path("description").asText();
				suggestions.add(String.format("%s: %s", workout, description));
			});
		}
		return suggestions.isEmpty() ? Collections.singletonList("No specific suggestions provided") : suggestions;
	}

	  private List<String> extractImprovements(JsonNode improvementsNode) {
    	  List<String> improvements = new ArrayList<>();
		if(improvementsNode.isArray()) {
			improvementsNode.forEach(improvement -> {
				String area = improvement.path("area").asText();
				String recommendation = improvement.path("recommendation").asText();
				improvements.add(String.format("%s: %s", area, recommendation));
			});
		}
		return improvements.isEmpty() ? Collections.singletonList("No specific recommendation provided") : improvements;
	}

	// Helper method to add analysis sections for Human readability
	private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
		if(!analysisNode.path(key).isMissingNode()) {
			fullAnalysis.append(prefix)
			            .append(" ")
			            .append(analysisNode.path(key).asText())
			            .append("\n\n");
		}
		
	}

	private String createPromtForActivity(Activity activity) {
		// TODO Auto-generated method stub
		 return String.format("""
			        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
			        {
			          "analysis": {
			            "overall": "Overall analysis here",
			            "pace": "Pace analysis here",
			            "heartRate": "Heart rate an	alysis here",
			            "caloriesBurned": "Calories analysis here"
			          },
			          "improvements": [
			            {
			              "area": "Area name",
			              "recommendation": "Detailed recommendation"
			            }
			          ],
			          "suggestions": [
			            {
			              "workout": "Workout name",
			              "description": "Detailed workout description"
			            }
			          ],
			          "safety": [
			            "Safety point 1",
			            "Safety point 2"
			          ]
			        }

			        Analyze this activity:
			        Activity Type: %s
			        Duration: %d minutes
			        Calories Burned: %d
			        Additional Metrics: %s
			        
			        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
			        Ensure the response follows the EXACT JSON format shown above.
			        """,
			                activity.getType(),
			                activity.getDuration(),
			                activity.getCaloriesBurned(),
			                activity.getAdditionalMetrics()
			        );
	}
	
}
