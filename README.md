# JAM

**J**ira **a**bstraction with **m**arkdown.

Jira schema in markdown looks like:
```markdown
# [WATCH-1:Epic] Working with jira issues as a code
- [WATCH-2:Story] Prepare to do one thing
- - [WATCH-3:Sub-task] Prepare to do one thing part 1
- - [WATCH-4:Sub-task] Prepare to do one thing part 2
- [WATCH-5:Story] Actually do one thing
```

## How to launch

```bash
./gradlew run --args='checkout WATCH-1 -c=jam-local.properties'
```
where `jam-local.properties` is 
```properties
host = http://localhost:8081
username = admin
password = admin
```

## Features

### Available

- Schema generation by issue identifier
- Creation of issue
- Changing of issue (summary, description)

### Not available yet

- Estimation changing
- Linkage
- Type changing
- Parent changing
- Movement

### Will not be implemented for now due rest api restrictions

- Issue deletion - dangerous