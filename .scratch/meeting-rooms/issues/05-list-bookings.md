# 05: List Bookings

**What to build:** A logged-in user can read every booking that is still a hold, including ones whose end has already passed. Each row shows the room, the start, the end, and the owner's username in the spelling saved at registration. Free gaps are not a separate query.

**Blocked by:** 04: Create a Booking

**Status:** ready-for-agent

- [ ] Any logged-in user sees every booking that has not been cancelled, including bookings that have already ended
- [ ] Each row shows the room, the inclusive start, the exclusive end, and the owner's registration spelling
- [ ] A user who registered as `Ann` and logged in as `ann` is shown as `Ann`
- [ ] There is no request that returns only the open stretches of a room
- [ ] A request without a usable login is rejected
- [ ] The behavior is visible from the HTTP status and body, not from the database
