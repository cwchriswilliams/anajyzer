# anajyzer

Program to run analysis on target Clojure projects.

*NOTE: This is very, very pre-alpha and very prone to change*

## Installation

### deps.edn

Add the following alias to your deps.edn
**TODO: add path to github SHA**

### lein

**TODO: Add explaination**

## Usage

### deps.edn

From the root directory of your project, run the alias: **TODO: Add alias**

### lein

**TODO: Add explaination**

## Configuration

A default configuration is provided and will be used if no other provided under resources (default_config.edn).

Default configuration looks for the following directories: `src` and `test`

Default configuration runs the following analyzers:

- cwchriswilliams.analyzers.line-count-analyzer/line-count-analyzer
- cwchriswilliams.analyzers.top-level-form-count-analyzer/top-level-forms-count-analyzer
- cwchriswilliams.analyzers.reference-count-analyzer/reference-count-analyzer
- cwchriswilliams.analyzers.form-frequency-analyzer/form-frequency-analyzer

Default configuration uses the following reporters:

- cwchriswilliams.reporters.stdout-reporter/report
- cwchriswilliams.reporters.json-reporter/report

Json reporter by default outputs to `tmp/output.json` (this directory must exist)

**TODO** Allow custom configuration to be merged in

## Example output

```json
[
  {
    "file-path": "src/cwchriswilliams/config.clj",
    "line-count": 18,
    "top-level-form-count": 5,
    "reference-count": 2,
    "form-frequency": {
      "defn": 3,
      "aero/read-config": 2,
      "read-default-config": 2,
      "let": 2,
      "io/resource": 1,
      "io/file": 1,
      "ns": 1,
      "merge": 1,
      "load-config": 1,
      "defmethod": 1
    }
  },
  {
    "file-path": "src/cwchriswilliams/file_management.clj",
    "line-count": 21,
    "top-level-form-count": 6,
    "reference-count": 1,
    "form-frequency": {
      "defn": 5,
      "filter": 2,
      "and": 2,
      ".isDirectory": 2,
      "io/file": 1,
      "validate-code-paths": 1,
      "ns": 1,
      "not": 1,
      "mapcat": 1,
      "map": 1,
      "let": 1,
      "comp": 1,
      ".getName": 1,
      ".exists": 1
    }
  }
]
```

## Adding your own analyzers

**TODO: add explaination**

## Adding your own reporters

**TODO: Add explaination**

## TODO

- Allow custom configuration to be provided
- Analysis summary
- Form depth analysis
- Add (require X) and (use X) to the reference count analyzer
- Add defn length analyzer
- Add reference frequency analyzer
- Add HTML output