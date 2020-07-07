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

/**
 * Adds a random greeting to the page.
 */
var slideIndex = 0;
showSlides();

function showSlides() {
  var i;
  var slides = document.getElementsByClassName("my-slides");
  var dots = document.getElementsByClassName("dot");
  for (i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";  
  }
  slideIndex++;
  if (slideIndex > slides.length) {slideIndex = 1}    
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  slides[slideIndex-1].style.display = "block";  
  dots[slideIndex-1].className += " active";
  setTimeout(showSlides, 2000); // Change image every 2 seconds
}


function addRandomFact() {
  const facts =
      ["I want to set foot on all 7 Continents",
       "My favorite food is pesto pasta", 
       "I have a cat named Dash", 
       "I used to have a cyst behind my ear that I named 'Ear Bubble'"];

  // Pick a random fact.
  const fact = facts[Math.floor(Math.random() * facts.length)];

  // Add it to the page.
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

function openNav() {
    document.getElementById("side-navigation").style.width = "160px";
}

function closeNav() {
    document.getElementById("side-navigation").style.width = "0";
}

/** Creates an <li> element containing text that will display a comment*/
function createCommentElement(comment) {
  const commentElement = document.createElement("div");
  commentElement.className = "comment";

  const timestampElement = document.createElement("div");
  timestampElement.innerText = new Date(comment.timestamp).toString();

  const nameElement = document.createElement("div");
  nameElement.innerText = comment.name;

  const textElement = document.createElement("div");
  textElement.innerText = comment.text;

  commentElement.appendChild(timestampElement);
  commentElement.appendChild(nameElement);
  commentElement.appendChild(textElement);
  return commentElement;
}

/* Fetches data from JavaServlet and displays the comments on index.html */
function displayComments() {
    // Clear out old commments first
    const commentListElement = document.getElementById("comment-container");
    commentListElement.innerText = "";
    const maxComments = document.getElementById("num-comments").value;
    // Then fetch new comments
    fetch('/comments?numComments=' + maxComments.toString()).then(response => response.json()).then((comments) => {
    comments.forEach((comment) => {
      commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

function confirmDeletion() {
    if (confirm("Are you sure you would like to delete all comments?")) {
        deleteAllComments();
    } else {
        displayComments();
    }
}


function deleteAllComments() {
    const responsePromise = fetch('/delete-data', {method: 'POST'});
    responsePromise.then(displayComments());
}

function createMap() {

  // Initialize the map to show UC Davis and downtown Davis
  const davis_map = new google.maps.Map(
      document.getElementById('map'),
      {center: {lat: 38.5449, lng: -121.7405}, zoom: 14});
  var names = ["Philz Coffee", "Mishka's Cafe", "Peet's Coffee", "Temple Coffee Roasters", "ASUCD Coffee House"];
  var coordinates = [ {lat: 38.544331, lng: -121.735481}, 
                    {lat: 38.543150, lng: -121.740530}, 
                    {lat: 38.544110, lng: -121.741800},
                    {lat: 38.544781, lng: -121.739609}, 
                    {lat: 38.542335, lng: -121.749454}];
  for (var i = 0; i < 5; i++) {
      var marker = new google.maps.Marker({
          position: coordinates[i],
          map: davis_map,
          title: names[i]
      });
  }
}
