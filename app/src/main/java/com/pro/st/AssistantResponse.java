package com.pro.st;

import java.util.Random;

public class AssistantResponse {

    public static String simpleResponse(String string) {
        int i = 3;
        int random = new Random().nextInt(i);
        String message = string.toLowerCase();



        if (message.contains("hi") || message.contains("hello") || message.contains("hey")) {
            switch (random) {

                case 0:

                    return "hi";

                case 1:
                    return "hello";

                case 2:
                    return "hey";

                default:
                    return "error";

            }
        } else if (message.contains("what can you do for me")) {
            switch (random) {

                case 0:

                    return "I can tell you a joke";

                case 1:
                    return "I can help you to talk with someone";

                case 2:
                    return "I am your friend ";

                default:
                    return "error";

            }
        } else if (message.contains("joke")) {
            switch (random) {

                case 0:

                    return "Why was the Math book sad? Because it had so many problems.";

                case 1:
                    return "Why do  programmers mistake Halloween and Christmas? Because Oct 31 === Dec 25.";

                case 2:
                    return "Did you hear about the two antennas that got married? The ceremony was long and boring, but the reception was great!";

                default:
                    return "error";

            }
        } else if (message.contains("how are you")) {
            switch (random) {

                case 0:

                    return " doing fine, thanks!";

                case 1:
                    return "Pretty good! How about you?";

                case 2:
                    return "I'm fine";

                default:
                    return "error";

            }
        } else {
            switch (random) {
                case 0:
                    return "I don't understand...";
                case 1:
                    return "Try asking me something different";
                case 2:
                    return "Idk";
                default:
                    return "error";

            }
        }


    }


    public static String startConversation(String string ) {
        int i = 3;
        int random = new Random().nextInt(i);
        String message = string.toLowerCase();





        if (message.contains("yes") && message.contains("true") ) {

                    return "let me ask you something";

        }else if(message.contains("yes") && message.contains("false") ){

            return "startSearch";

        }else if(message.contains("no")){

            return simpleResponse(string);
        }
        else {
            switch (random) {
                case 0:
                    return "I don't understand...";
                case 1:
                    return "Try asking me something different";
                case 2:
                    return "Idk";
                default:
                    return "error";

            }
        }


    }


}



