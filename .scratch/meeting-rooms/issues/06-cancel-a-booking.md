# 06: Cancel a Booking

**What to build:** The owner can give up a booking until its end minute. The room and the user are then free for that whole stretch, including a booking that has already started. Other people stop seeing it. The owner still sees it as cancelled.

**Blocked by:** 04: Create a Booking

**Status:** ready-for-agent

- [ ] The owner can cancel a booking whose end is still in the future, including one that has already started
- [ ] After that cancel, another user can book a stretch that overlaps the old one, as long as the new start is the current Thailand minute or later
- [ ] The owner can book that stretch again too
- [ ] Other logged-in users no longer see the cancelled booking
- [ ] The owner still sees it, and can tell that it is cancelled
- [ ] Cancelling the same booking again is rejected
- [ ] A different user cannot cancel it
- [ ] A booking whose end has passed cannot be cancelled
- [ ] A request without a usable login is rejected
- [ ] The behavior is visible from the HTTP status and body, not from the database
