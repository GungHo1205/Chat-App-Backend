package controllers;

import play.*;
import play.mvc.*;
import play.libs.Json;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import views.html.*;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import play.http.DefaultHttpFilters;
import play.api.mvc.EssentialFilter;
import play.http.HttpFilters;

import javax.inject.Inject;



public class Application extends Controller {
    int state = 0;
    String name = "";
    String gender = "";
    String hobbies = "";

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result byeworld() {
        return ok(byeworld.render(""));
    }

    public Result newsurvey() {
        return ok(newsurvey.render("", "", ""));
    }
    public Result getState() {
    return ok(
            "{ \n" +
            "\"state\":"+ state + "\n" +
            "}"
            );
    }
    public Result singlePageSurvey() {

        JsonNode postInfo = request().body().asJson();
        String fieldValue = postInfo.get("message").toString().toLowerCase();
        System.out.println(fieldValue);
        fieldValue = fieldValue.replaceAll("\"", "");
        System.out.println(fieldValue);
        String serverResponse = "";
        String[] hobbiesArray = {"reading", "cooking", "sleeping"};
        boolean correctHobbies = false;
        if (state == 0) {
            //execute start survey state
            if (!(fieldValue.equals("start survey"))) {
                serverResponse =
                        "{ \n" +
                            "\"message\": \"Please input start survey\" \n" +
                        "}";

            } else {
                serverResponse =
                        "{ \n" +
                        "   \"message\": \"starting survey\", \n" +
                        "   \"type\": \"text\", \n" +
                        "   \"value\": \"What is your name?\"\n" +
                        "}";
                state++;
            }
        }else if (state == 1) {
            //execute getName

            name = fieldValue;
            serverResponse =
                    "{ \n" +
                    "   \"type\": \"radio\", \n" +
                    "   \"value\": \"What is your gender?\",\n" +
                    "   \"options\": [\"Male\", \"Female\"]\n" +
                    "}";
            state++;
        }else if (state == 2) {
            // execute getGender
            if (!(fieldValue.equals("male") || fieldValue.equals("female"))) {
                serverResponse =
                        "{ \n" +
                        "   \"message\": \"Please choose from one of the options\", \n" +
                        "   \"options\": [\"Male\", \"Female\"]\n" +
                        "}";
            } else {
                gender = fieldValue;
                serverResponse =
                        "{ \n" +
                                "   \"type\": \"checkbox\", \n" +
                                "   \"value\": \"What are your hobbies?\",\n" +
                                "   \"options\": [\"Reading\", \"Cooking\", \"Sleeping\"]\n" +
                                "}";
                state++;
            }
        } else if (state == 3) {
            // execute getHobbies
            hobbies = fieldValue;
            hobbies = hobbies.replace("[", "");
            hobbies = hobbies.replace("\"", "");
            hobbies = hobbies.replace("]", "");
            hobbies = hobbies.replaceAll(" ", "");
            hobbies = hobbies.trim();
            List<String> tokens = new ArrayList<>(Arrays.asList(hobbies.split(","))); // split user input
            boolean[] isPresent = new boolean[tokens.size()];
            for (int i = 0; i < tokens.size(); i++) {
                String arg = tokens.get(i);
                if (Arrays.asList(hobbiesArray).contains(arg.toLowerCase())) {
                    isPresent[i] = true;
                } else {
                    isPresent[i] = false;
                }
            }
            for (boolean b : isPresent) {
                if (!b) {
                    correctHobbies = false;
                } else {
                    correctHobbies = true;
                }
            }
            if (correctHobbies == false) {
                serverResponse =
                        "{ \n" +
                        "   \"message\": \"Please choose from one of the options\", \n" +
                        "   \"options\": [\"Reading\", \"Cooking\", \"Sleeping\"]\n" +
                        "}";
            } else {
                switch (tokens.size()) {
                    case (1):

                        serverResponse =
                                "{ \n" +
                                " \"message\": \"survey finished\", \n " +
                                "   \"value\" : \"A " + gender + " " + name + " who likes " +
                                tokens.get(0).toLowerCase() + "\" \n" +
                                "}";
                        break;
                    case (2):
                        serverResponse =
                                "{ \n" +
                                " \"message\": \"survey finished\", \n " +
                                "   \"value\" : \"A " + gender + " " + name + " who likes " +
                                tokens.get(0).toLowerCase() + " and " + tokens.get(1).toLowerCase() + "\" \n" +
                                "}";
                        break;
                    case (3):
                        serverResponse =
                                "{ \n" +
                                " \"message\": \"survey finished\", \n " +
                                "   \"value\" : \"A " + gender + " " + name + " who likes " +
                                tokens.get(0).toLowerCase() + ", " + tokens.get(1).toLowerCase() +
                                " and " + tokens.get(2).toLowerCase() + "\" \n" +
                                "}";
                        break;
                }
                state = 0;
            }
        }
        JsonNode json = Json.parse(serverResponse);
        return ok(json);
    }
}


