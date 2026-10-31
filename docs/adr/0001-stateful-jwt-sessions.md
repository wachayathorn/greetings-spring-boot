# Stateful JWT sessions

Login creates a session row and returns a signed JWT that only identifies that session. Each login gets its own row, the row lasts 24 hours, and there is no refresh token. Every authenticated request must find that row still present and unexpired. Logout deletes only the row for that token, so other logins stay valid.

A stateless JWT would stay valid until expiry, so logout would be a lie. A server session cookie would also be revocable, but the client is required to present a JWT.
