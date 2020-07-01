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
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  
  /* data is an ArrayList made up of strings that represent comments that have been given */
  private ArrayList<String> data; 
  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
      /** Create a ArrayList<String> variable containing hard coded famous movie quotes */
    ArrayList<String> movieQuotes = new ArrayList<String>(
        Arrays.asList("According to all known laws of aviation, there is no way a bee should be able to fly",
                        "This conversation can serve no purpose anymore. Goodbye.",
                        "It is the TITULAR role",
                        "Honey? Where is my supersuit"
                    )
    );
    String json = convertToJson(movieQuotes);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("user_name");
    String comment = request.getParameter("user_comment");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("User", name);
    commentEntity.setProperty("Comment", comment);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/index.html");
  }
  
  /* Convert to JSOn using Gson */
  private String convertToJson(ArrayList<String> data) {
    Gson gson = new Gson();
    String json = gson.toJson(data);
    return json;
  }
}
