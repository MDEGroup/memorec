# memorec

This folder contains the implementation of **memorec**. It allows to run the evaluation on all our [datasets](../../dataset/).

## Requirements

  - Apache Maven >= 3.0
  - Java >= 1.8
  - (optional) Bash for running all evaluations automatically

## Running the tool
To start the evaluation of **memorec** using the default `evaluation.properties` file (see below), run the following command:

```
mvn clean compile exec:java -Dexec.mainClass=it.univaq.disim.Runner
```

