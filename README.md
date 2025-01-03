# Breadis

Breadis is a lightweight implementation of Redis, supporting a few operations exposed by the original implementation.

## Improvement opportunities

- [ ] Add support for an active expiration mechanism in addition to the implemented passive expiration mechanism.
- [ ] Expiration checking tends to be present in implementation of many operations. It should be checked if it can be abstracted out.
- [ ] Consider splitting Storage class into multiple classes implementing different operations sets.
- [ ] Implement the rest of initial Redis commands:
  - [ ] `INCR`
  - [ ] `DECR`
  - [ ] `SAVE`