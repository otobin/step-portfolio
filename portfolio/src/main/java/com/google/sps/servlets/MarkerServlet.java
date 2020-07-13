package com.google.sps.servlets;

import com.google.gson.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.sps.data.Marker;

//Only need a get function in order to fetch the existing locations from the backend 
@WebServlet("/markers")
public class MarkerServlet extends HttpServlet {
  
  // My markers, hard coded in the backend
  public static ArrayList<Marker> getMarkers() {
    ArrayList<Marker> markers = new ArrayList<>();
    //Order : name, description, lat, long
    String philzDescription = "It simply does not get much better than philz." + 
                            "Super fast wifi. Noise level is great " +
                          "for cramming for a midterm or working on a group projet with friends." +
                          "Literally the most wholesome place in the universe.";
    String mishkasDescription = "Mishka's has a great selection of basic lattes and drinks and extremely" +
                          " delicious baked goods.They would edge out Philz if their wifi didn't have" +
                           " a 2 hour limit and they weren't constantly packed with cranky grad students." +
                           "Overall, a great place to take your parents to show them what a" +
                           " *~hip and cool*~ college town Davis is!";
    String peetsDescription = "Classic, predictable, and consistent. Peet's quality is surprisingly good for a nationwide chain.";
    String templeDescription = "If Temple was a student at Davis, they'd be an RA with a 4.0." +
                          "Not great wifi, pretty bitter coffee, and an austere modern" +
                          " layout instead of the cozy vibes from a classic coffee shop." +
                          "Also a limited selection of only pastries and no bagels.";
    String asucdDescription = "A great go to for in between classes.";
    markers.add(new Marker("Philz Coffee", philzDescription, 38.544331, -121.735481));
    markers.add(new Marker("Mishka's Cafe",mishkasDescription, 38.543150, -121.740530));
    markers.add(new Marker("Peet's Coffee",peetsDescription, 38.544110,-121.741800));
    markers.add(new Marker("Temple Coffee Roasters", templeDescription, 38.544781, -121.749454));
    markers.add(new Marker("ASUCD Coffee House",asucdDescription, 38.542335, -121.749454));
    return markers;
  }


  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    ArrayList<Marker> markers = getMarkers();
    Gson gson = new Gson();
    String json = gson.toJson(markers);

    response.getWriter().println(json);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter("user_name");
    String text = request.getParameter("user_comment");
    long timestamp = System.currentTimeMillis();

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("name", name);
    commentEntity.setProperty("text", text);
    commentEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);

    response.sendRedirect("/map.html");
  }

}