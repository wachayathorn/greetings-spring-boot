# 04: Create a Booking

**What to build:** A logged-in user can hold one room for themselves for a stretch of time. The stretch is measured in minutes on the Thailand clock, includes the start minute, and stops before the end minute. The owner is the logged-in user.

**Blocked by:** 03: List Rooms

**Status:** done

- [x] A booking of at least 15 minutes and at most 8 hours, starting at the current Thailand minute or later, succeeds and shows the room, the start, the end, and the owner's registration spelling
- [x] A booking from 22:00 to 06:00 the next day succeeds
- [x] A booking that ends at 11:00 and another that starts at 11:00 on the same room both succeed
- [x] A second active booking that shares any minute with the same room is rejected
- [x] A second active booking that shares any minute with the same user, on any room, is rejected
- [x] A start before the current Thailand minute, a length under 15 minutes, a length over 8 hours, an end that is not after the start, or a time that is not a whole minute is rejected
- [x] An unknown room is rejected
- [x] The request cannot name a different user as the owner
- [x] A request without a usable login is rejected
- [x] The behavior is visible from the HTTP status and body, not from the database
