# 01: Register a User

**What to build:** A person can register with a username and a password and is not logged in afterwards. The username saved at registration is the spelling other people will see. Signing up no longer asks for an email or a display name. The account remains available for a later login in the same process, and is gone after the process restarts.

**Blocked by:** None (can start immediately)

**Status:** ready-for-agent

- [x] Registering with a username of 3 to 32 English letters, digits, or underscores and a password of 8 to 72 characters succeeds and returns the username in the spelling that was submitted
- [x] The response does not grant a session
- [x] A second registration whose username differs only by letter case is rejected, and the original spelling is kept
- [x] A username shorter than 3, longer than 32, or containing any other character is rejected
- [x] A password shorter than 8 or longer than 72 characters is rejected
- [x] A missing username or password is rejected
- [x] After the process restarts, that user can no longer be found
- [x] The behavior is visible from the HTTP status and body, not from the database
