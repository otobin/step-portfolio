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

import java.util.*;

public final class FindMeetingQuery {
   // Given the currently booked events (time ranges and attendees) and
   // a meeting request (duration and attendees), return a collection
   // the of time ranges within which the meeting can be booked.
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
		HashSet<String> attendees = new HashSet<String>(request.getAttendees());		
		long duration = request.getDuration();
		ArrayList<TimeRange> unavailableTimes = new ArrayList<TimeRange>();
		ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();
		Collection<TimeRange> entireDay = new ArrayList<TimeRange>(Arrays.asList(TimeRange.WHOLE_DAY));

		// If the duration is over a day, then there is no time available 
		if (duration > TimeRange.WHOLE_DAY.duration()){
			return unavailableTimes;
		}

		// If there are no attendees, then the entire day is available by default 
		if (attendees.isEmpty()) {
			return entireDay;
		}

		// Get all of the TimeRanges when required attendees are in meetings 
		for (Event event: events) {
			HashSet<String> eventAttendees = new HashSet<String>(event.getAttendees());
			eventAttendees.retainAll(attendees);
			// ^^ eventAttendees now only has the intersection of the two lists 
			if (!eventAttendees.isEmpty()) {
				unavailableTimes.add(event.getWhen());
			}
		}

		// If there are no meetings for the required attendees, return entire day
		if (unavailableTimes.size() == 0) {
			return entireDay;
		}

        // Sort the times by earliest start 
        Collections.sort(unavailableTimes, TimeRange.ORDER_BY_START);

		// Call consolidateAll(unavailableTimes) to combine overlapping events into one TimeRange
		unavailableTimes = consolidateAll(unavailableTimes);

		// Get the time before the first event.
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

		// return
		return availableTimes;
  }

	// Takes in two TimeRanges that overlap and combines them into one larger one that 
	// encompasses them both
	public TimeRange consolidate(TimeRange timeRange1, TimeRange timeRange2) {
        // 2 TimeRanges can overlap but not contain each other, but 2 TimeRanges
        // cannot contain each other without overlapping, so the order on these 
        // if else statements matters.
		if (timeRange1.contains(timeRange2)) {
            return TimeRange.fromStartEnd(timeRange1.start(), timeRange1.start() + timeRange1.duration(), false);
        } else if (timeRange2.contains(timeRange1)) {
            return TimeRange.fromStartEnd(timeRange2.start(), timeRange2.start() + timeRange2.duration(), false);
        } else {
            int start = Math.min(timeRange1.start(), timeRange2.start());
            int end = Math.max(timeRange1.end(), timeRange2.end());
            int duration = end - start;
            return TimeRange.fromStartEnd(start, start + duration, false);
        }
	}

	// consolidateAll takes in the list of unavailable times that are possibly overlapping
	// and returns a list of unavailable times with no overlaps.
	public ArrayList<TimeRange> consolidateAll(ArrayList<TimeRange> unavailableTimes) {
		ArrayList<TimeRange> overLappingTimes = new ArrayList<TimeRange>(unavailableTimes);
		ArrayList<TimeRange> nonOverLappingTimes = new ArrayList<TimeRange>();
		while (!(overLappingTimes.isEmpty())) {
			// Pop the first element from the overlapping events 
			TimeRange currentTime = overLappingTimes.get(0);
			overLappingTimes.remove(0);
			// Look see if it overlaps with any existing events in the nonOverlappingEvents. 
			// Combine the two TimeRanges or add the nonOverlapping event to the end. 
			int index = 0;
			boolean isOverlapping = false;
            int size = nonOverLappingTimes.size();
			while (index < size) {
				if (nonOverLappingTimes.get(index).overlaps(currentTime)) {
					TimeRange combinedTime = consolidate(nonOverLappingTimes.get(index), currentTime);
					nonOverLappingTimes.set(index, combinedTime);
					isOverlapping = true;
				}
				index++;
			}
			if (!isOverlapping) {
				nonOverLappingTimes.add(currentTime);
			}
		}
		return nonOverLappingTimes;
	}
}