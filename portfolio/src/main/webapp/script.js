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
  console.log(slides);
  var dots = document.getElementsByClassName("dot");
  console.log(slides);
  console.log(slides.length);
  for (i = 0; i < slides.length; i++) {
    console.log(slides[i].classList[0]);
    slides[i].style.display = "none";  
    console.log(slides[i].style.display);
  }
  slideIndex++;
  console.log(slideIndex);
  if (slideIndex > slides.length) {slideIndex = 1}    
  for (i = 0; i < dots.length; i++) {
    dots[i].className = dots[i].className.replace(" active", "");
  }
  console.log(slides[0]);
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

/** Creates an <li> element containing text. */
function createCommentElement(comment) {
  const list_element = document.createElement("li");
  list_element.innerText = comment;
  return list_element;
}

function displayComments() {
    fetch('/data').then(response => response.json()).then((comments) => {
    const commentListElement = document.getElementById('comments-list');
    comments.forEach((comment) => {
      commentListElement.appendChild(createCommentElement(comment));
    })
  });
}

