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

function makeTransparent(img) {
    img.opacity = .5;
}

function makeOpaque(img) {
    img.opacity = 1;
}
