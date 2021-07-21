# JAM

**J**ira **a**bstraction with **m**arkdown.

<img src="assets/poc.png" width="700" height="auto">

Jira schema in markdown looks like:
```markdown
# [WATCH-1:Epic] Working with jira issues as a code
- [WATCH-2:Story] Prepare to do one thing
- - [WATCH-3:Sub-task] Prepare to do one thing part 1
- - [WATCH-4:Sub-task] Prepare to do one thing part 2
- [WATCH-5:Story] Actually do one thing
```

## Features

Ways of working:
- **checkout** - provide schema representation in markdown for the issue by the key
- **dependencies** - graphviz representation for issue's dependencies
- **offer** - offers changes that schema represents to the issue

### Available

- Schema representation by issue identifier (Stories, Tasks, Epics):
- - Sub-task (maybe not)
- - Story -> Sub-task
- - Epic -> Story -> Sub-task
- Graphviz representation for issue's dependencies

### Not available yet

- Creation of issue
- Changing of issue by identifier (summary, description)
- Estimation changing
- Linkage
- Type changing
- Parent changing
- Movement

### Will not be implemented

- Issue deletion - dangerous

## How to launch

### With gradle

```bash
./gradlew run --args='checkout WATCH-1 -c=jam-local.properties'
./gradlew run --args='dependencies WATCH-1 -c=jam-local.properties'
```
where `jam-local.properties` is
```properties
host = http://localhost:8081
username = admin
password = admin
```

### With binary

```bash
./jam checkout WATCH-1 -c=jam-local.properties
./jam dependencies WATCH-1 -c=jam-local.properties
```

If you don't have binary you can build it with `nativeImage` task (tested with GraalVM CE 21.1.0):
```bash
./gradlew nativeImage
```

## For testing

For some integration tests jira instance is required with default configuration: 
```properties
host = http://localhost:8081
username = admin
password = admin
```
Check `docker-compose-postgres.yml`