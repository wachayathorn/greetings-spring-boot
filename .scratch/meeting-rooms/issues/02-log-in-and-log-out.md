# 02: Log in and log out

**What to build:** A registered user can log in with a username and password and receive a token that stands for that login only. Each login is independent. Logging out kills only that login. There is no way to extend a token without logging in again.

**Blocked by:** 01: Register a User

**Status:** ready-for-agent

- [ ] Logging in with the registration spelling, or with different letter case, succeeds and returns a token
- [ ] A wrong password or an unknown username is rejected and returns no token
- [ ] A request that requires login is rejected when the token is missing, unknown, or logged out
- [ ] Logging in twice yields two tokens; logging out the first leaves the second usable
- [ ] Logging out the same token a second time is rejected
- [ ] A login older than 24 hours is rejected
- [ ] Nothing in the system exchanges an existing token for a new one
- [ ] The behavior is visible from the HTTP status and body, not from the database
