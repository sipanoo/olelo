package com.pro.st;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Provides rule-based responses for the Dating Assistant chatbot.
 * Currently uses keyword matching with randomized responses.
 * 
 * Future improvement: Replace with ML Kit Smart Reply, Dialogflow, 
 * or Gemini API for more intelligent and natural conversations.
 */
public class AssistantResponse {

    private static final Random random = new Random();

    // Response pools organized by category
    private static final List<String> GREETINGS = Arrays.asList(
            "Hi there! 👋",
            "Hello! How can I help you?",
            "Hey! Nice to chat with you!"
    );

    private static final List<String> CAPABILITIES = Arrays.asList(
            "I can tell you a joke 😄",
            "I can help you start a conversation with someone!",
            "I'm your dating assistant - I can suggest topics to talk about!",
            "I can give you tips for your profile!"
    );

    private static final List<String> JOKES = Arrays.asList(
            "Why was the Math book sad? Because it had so many problems. 📚",
            "Why do programmers mistake Halloween and Christmas? Because Oct 31 === Dec 25. 🎃🎄",
            "Did you hear about the two antennas that got married? The ceremony was long and boring, but the reception was great! 📡",
            "Why do Java developers wear glasses? Because they can't C#. 👓",
            "What's a computer's favorite snack? Microchips! 💻"
    );

    private static final List<String> HOW_ARE_YOU = Arrays.asList(
            "Doing fine, thanks! How about you? 😊",
            "Pretty good! Ready to help you find a match!",
            "I'm great! Let's find you someone special!"
    );

    private static final List<String> DATING_TIPS = Arrays.asList(
            "Be yourself! Authenticity is the most attractive quality. ✨",
            "Ask open-ended questions to keep the conversation going!",
            "Show genuine interest in their hobbies and passions. 💬",
            "A good sense of humor goes a long way! 😄",
            "Don't forget to smile in your profile photos! 📸"
    );

    private static final List<String> UNKNOWN = Arrays.asList(
            "I'm not sure I understand. Could you try rephrasing?",
            "Hmm, I didn't quite get that. Try asking me for a joke or dating tips!",
            "I'm still learning! Try asking me 'what can you do for me?'"
    );

    /**
     * Generates a response based on simple keyword matching.
     * @param input The user's message text
     * @return A contextual response string
     */
    public static String simpleResponse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return pickRandom(UNKNOWN);
        }

        String message = input.toLowerCase().trim();

        if (containsAny(message, "hi", "hello", "hey", "hallo", "hola")) {
            return pickRandom(GREETINGS);
        } else if (containsAny(message, "what can you do", "help", "features", "abilities")) {
            return pickRandom(CAPABILITIES);
        } else if (containsAny(message, "joke", "funny", "laugh", "humor")) {
            return pickRandom(JOKES);
        } else if (containsAny(message, "how are you", "how r u", "how do you do")) {
            return pickRandom(HOW_ARE_YOU);
        } else if (containsAny(message, "tip", "advice", "suggest", "dating")) {
            return pickRandom(DATING_TIPS);
        } else if (containsAny(message, "thank", "thanks", "thx")) {
            return "You're welcome! Let me know if you need anything else. 😊";
        } else if (containsAny(message, "bye", "goodbye", "see you")) {
            return "Goodbye! Good luck with your matches! 💕";
        } else {
            return pickRandom(UNKNOWN);
        }
    }


    /**
     * Handles the conversation flow when helping a user start chatting with a match.
     * @param input The user's message combined with conversation state
     * @return Response or "startSearch" signal to trigger YouTube search
     */
    public static String startConversation(String input) {
        if (input == null || input.trim().isEmpty()) {
            return pickRandom(UNKNOWN);
        }

        String message = input.toLowerCase().trim();

        if (message.contains("yes") && message.contains("true")) {
            return "Great! Let me ask you something first 📝";
        } else if (message.contains("yes") && message.contains("false")) {
            return "startSearch";
        } else if (containsAny(message, "no", "nope", "nah")) {
            return simpleResponse(input);
        } else {
            return pickRandom(UNKNOWN);
        }
    }

    /**
     * Picks a random item from a list of responses.
     */
    private static String pickRandom(List<String> responses) {
        return responses.get(random.nextInt(responses.size()));
    }

    /**
     * Checks if a message contains any of the specified keywords.
     */
    private static boolean containsAny(String message, String... keywords) {
        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
