// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.*;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  private ArrayList<String> movie_quotes;
  /* Modify DataServlet so its doGet() function returns the ArrayList as a JSON String. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      /** Create a ArrayList<String> variable containing hard coded famous movie quotes */
    movie_quotes = new ArrayList<>();
    movie_quotes.add("According to all known laws of aviation, there is no way a bee should be able to fly");
    movie_quotes.add("This conversation can serve no purpose anymore. Goodbye.");
    movie_quotes.add("It is the TITULAR role");
    movie_quotes.add("Honey? Where is my supersuit");
    String json = convertToJson(movie_quotes);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }
  
  /* Convert to JSOn using Gson */
  private String convertToJson(ArrayList<String> movie_quotes) {
    Gson gson = new Gson();
    String json = gson.toJson(movie_quotes);
    return json;
  }
}
