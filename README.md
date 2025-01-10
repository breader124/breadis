# Breadis

Breadis is a lightweight implementation of Redis, supporting a few operations exposed by the original implementation.
It's based on the Coding Challenge created by John Crickett: [Build Your Own Redis Server](https://codingchallenges.fyi/challenges/challenge-redis)

## Running

Run the following command to start the server:

```shell
./gradlew run
```

Run the following command to execute the tests:

```shell
./gradlew check
```

## Improvement opportunities

### Features
- [ ] Add support for an active expiration mechanism in addition to the implemented passive expiration mechanism.
- [ ] Implement the rest of initial Redis commands:
  - [ ] `INCR`
  - [ ] `DECR`
  - [ ] `SAVE`

### Maintainability

- [ ] Consider splitting Storage class into multiple classes implementing different operations sets. Otherwise, it might become unmaintainable.
- [ ] Expiration checking tends to be present in implementation of many operations. It should be checked if it can be abstracted out.