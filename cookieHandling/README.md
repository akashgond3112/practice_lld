# cookie-filter

Command-line tool that reads a cookie log CSV file and prints the most active cookie(s) for a given UTC date.

## Prerequisites

- Java 25
- Maven 3.x

If you use SDKMAN:

```bash
sdk use java 25.0.4-tem
```

## Build

From the project root:

```bash
mvn clean package
```

This creates:

```text
target/cookie-filter-0.0.1-SNAPSHOT.jar
```

## Run

```bash
java -jar target/cookie-filter-0.0.1-SNAPSHOT.jar -f <path-to-csv-file> -d <date>
```

### Parameters

| Parameter | Description | Example |
|-----------|-------------|---------|
| `-f` | Path to the cookie log CSV file | `src/logs/cookie_log.csv` |
| `-d` | Date to filter by, in UTC (`yyyy-MM-dd`) | `2018-12-09` |

Both parameters are required.

### Running with any file

You can pass any valid file path to `-f`:

**Relative path** (from your current directory):

```bash
java -jar target/cookie-filter-0.0.1-SNAPSHOT.jar -f src/logs/cookie_log.csv -d 2018-12-09
```

**Absolute path**:

```bash
java -jar target/cookie-filter-0.0.1-SNAPSHOT.jar -f /home/user/data/my_cookie_log.csv -d 2018-12-09
```

**Another directory**:

```bash
java -jar target/cookie-filter-0.0.1-SNAPSHOT.jar -f ../logs/production.csv -d 2018-12-09
```

Example output:

```text
AtY0laUfhglK3lC7
```

## Expected CSV format

The file must be a comma-separated CSV with a header row:

```csv
cookie,timestamp
AtY0laUfhglK3lC7,2018-12-09T14:19:00+00:00
SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00
```

- **Column 1 (`cookie`)**: cookie identifier
- **Column 2 (`timestamp`)**: ISO-8601 timestamp with timezone offset, for example `2018-12-09T14:19:00+00:00`
- The first line is treated as a header and is skipped
- Blank lines are ignored
- Dates are evaluated in **UTC**

## Run tests

```bash
mvn test
```