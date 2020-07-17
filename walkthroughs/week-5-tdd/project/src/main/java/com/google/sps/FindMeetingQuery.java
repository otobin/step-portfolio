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

package com.google.sps;

import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public final class FindMeetingQuery {
  // Given the currently booked events (time ranges and attendees) and a meeting request, 
  // (duration and attendees) return a collection of the time ranges within which the meeting
  // can be booked.
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    HashSet<String> requiredAttendees = new HashSet<String>(request.getAttendees());
		HashSet<String> optionalAttendees = new HashSet<String>(request.getOptionalAttendees());
    long duration = request.getDuration();
    Collection<TimeRange> entireDay = new ArrayList<TimeRange>(Arrays.asList(TimeRange.WHOLE_DAY));
    ArrayList<TimeRange> unavailableTimes = new ArrayList<TimeRange>();

    // If the duration is over a day, then there is no time available
    if (duration > TimeRange.WHOLE_DAY.duration()) {
      return unavailableTimes;
    }

    if (requiredAttendees.isEmpty()) {
      if (optionalAttendees.isEmpty()) {
				// No attendees, so return the entire day by default
				return entireDay;
			} else {
				// No required attendees, so treat optional attendees like required attendees
				requiredAttendees = optionalAttendees;
			}
    }

		// Get the unavailable times for both the required attendees and optional attendees
		ArrayList<TimeRange> requiredUnavailableTime =  getUnavailableTimes(requiredAttendees, events);
		ArrayList<TimeRange> optionalUnavailableTime = getUnavailableTimes(optionalAttendees, events);

    // If there are no meetings for the required attendees, return the entire day
    if (requiredUnavailableTime.size() == 0) {
      return entireDay;
    }

		// Combine and consolidate the unavailable time for both optional and required attendees
		ArrayList<TimeRange> combinedTime = new ArrayList<TimeRange>();
		combinedTime.addAll(requiredUnavailableTime);
		combinedTime.addAll(optionalUnavailableTime);
		Collections.sort(combinedTime, TimeRange.ORDER_BY_START);
		combinedTime = consolidateAll(combinedTime);

		// Sort and combine the unavailable time for just the required attendees
		Collections.sort(requiredUnavailableTime, TimeRange.ORDER_BY_START);
		requiredUnavailableTime = consolidateAll(requiredUnavailableTime);

		// Find the available times for both optional and required and just required.
		ArrayList<TimeRange> combinedAvailableTime = findAvailableTimes(combinedTime, duration);
		ArrayList<TimeRange> requiredAvailableTime = findAvailableTimes(requiredUnavailableTime, duration);
		Collection<TimeRange> result = combinedAvailableTime.isEmpty() ? requiredAvailableTime : combinedAvailableTime;
		return result;
  }


    // getUnavailableTimes takes in a hashset of attendees and a list of events and returns
	// the TimeRanges where the attendees in the hash set are required to attend the event
	public ArrayList<TimeRange> getUnavailableTimes(HashSet<String> attendees, Collection<Event> events) {
    ArrayList<TimeRange> unavailableTimes = new ArrayList<TimeRange>();
    for (Event event: events) {
      HashSet<String> eventAttendees = new HashSet<String>(event.getAttendees());
      eventAttendees.retainAll(attendees);
      // ^^ eventAttendees now only has the intersection of the two lists
      if (!eventAttendees.isEmpty()) {
        unavailableTimes.add(event.getWhen());
      }
    }
		return unavailableTimes;
	}

  // Given an array list of unavailable unavailable TimeRanges ordered by start time that are 
  // guranteed not to overlap, findAvailableTimes iterates through and finds available gaps
  // that are greater than or equal to the given duration
  public ArrayList<TimeRange> findAvailableTimes(ArrayList<TimeRange> unavailableTimes, long duration) {
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();
    
		// Get the time before the first event 
    TimeRange firstEvent = unavailableTimes.get(0);
    int startDif = firstEvent.start() - TimeRange.START_OF_DAY;
    if (startDif >= duration) {
      availableTimes.add(TimeRange.fromStartEnd(TimeRange.START_OF_DAY, firstEvent.start(), false));
    }

    // Get the times in between the events 
    for (int i = 0; i < unavailableTimes.size() - 1; i++) {
      TimeRange currentEvent = unavailableTimes.get(i);
      TimeRange nextEvent = unavailableTimes.get(i + 1);
      if (nextEvent.start() - currentEvent.end() >= duration) {
        availableTimes.add(TimeRange.fromStartEnd(currentEvent.end(), nextEvent.start(), false));
      }
    }

    // Get the time after the last event 
    TimeRange lastEvent = unavailableTimes.get(unavailableTimes.size() - 1);
    int endDif = TimeRange.END_OF_DAY - lastEvent.end();
    if (endDif >= duration) {
      availableTimes.add(TimeRange.fromStartEnd(lastEvent.end(), TimeRange.END_OF_DAY, true));
    }
    
    return availableTimes;
  }

  // Consolidates two overlapping events into one. An event can overlap another event
  // but not be contained by an event, so the order of the if else statements here matters.
  public TimeRange consolidate(TimeRange timeRange1, TimeRange timeRange2) {
    if (timeRange1.contains(timeRange2)) {
      return timeRange1;
    } else if (timeRange2.contains(timeRange1)) {
      return timeRange2;
    } else {
      int start = Math.min(timeRange1.start(), timeRange2.start());
      int end = Math.max(timeRange1.end(), timeRange2.end());
      return TimeRange.fromStartEnd(start, end, false);
    }
  }

  // ConsolidateAll takes in the list of unavailable times that are possibly overlapping
  // and returns a list of unavailable times with no overlaps.
  public ArrayList<TimeRange> consolidateAll(ArrayList<TimeRange> unavailableTimes) {
    ArrayList<TimeRange> overLappingTimes = new ArrayList<TimeRange>(unavailableTimes);
    ArrayList<TimeRange> nonOverLappingTimes = new ArrayList<TimeRange>();
    TimeRange currentTime = overLappingTimes.get(0);
    for (TimeRange overLappingTime: overLappingTimes) {
      if (overLappingTime.overlaps(currentTime)) {
        currentTime = consolidate(currentTime, overLappingTime);
        continue;
      }
      nonOverLappingTimes.add(currentTime);
      currentTime = overLappingTime;
    }
    nonOverLappingTimes.add(currentTime);
    return nonOverLappingTimes;
  }
}
