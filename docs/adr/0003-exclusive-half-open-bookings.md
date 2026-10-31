# A booking is an exclusive half-open interval

A booking holds one room for one user from an inclusive start minute until an exclusive end minute on the Thailand clock, for 15 minutes up to 8 hours. Until it is cancelled, that interval cannot overlap another active booking of the same room or the same user. A booking that ends at 11:00 and another that starts at 11:00 do not overlap.

A room-only rule would let one person hold two rooms at the same time. We rejected that. Cancelling before the end frees the whole interval. A booking whose end has passed cannot be cancelled, and a second cancel is rejected.
