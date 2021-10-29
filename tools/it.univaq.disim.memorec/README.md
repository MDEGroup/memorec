# MemoRec

This folder contains the implementation of **MemoRec**. It allows to run the evaluation on all our [datasets](../../dataset/).

## Requirements

  - Apache Maven >= 3.0
  - Java >= 1.8
  - (optional) Bash for running all evaluations automatically

## Running the tool
To start the evaluation of **MemoRec** using the default `evaluation.properties` file (see below), run the following command:

```
mvn clean compile exec:java -Dexec.mainClass=it.univaq.disim.Runner
```
The command runs 5 times the evaluation process with different number of neighbors, i.e., k =  1, 5, 10, 15, 20.
Each time the ten-fold validation is executed where the input dataset is splitted into training and testing data.

Results in average (success rate, precision, recall) for different cut-off values (N) are displayed in the console directly as follows:

```
Runner:106 - Running the evaluation with k = 1
Runner:167 - 	Fold 0 time 6160 ms
Runner:167 - 	Fold 1 time 7177 ms
Runner:167 - 	Fold 2 time 6177 ms
Runner:167 - 	Fold 3 time 5175 ms
Runner:167 - 	Fold 4 time 6335 ms
Runner:167 - 	Fold 5 time 7346 ms
Runner:167 - 	Fold 6 time 6379 ms
Runner:167 - 	Fold 7 time 7269 ms
Runner:167 - 	Fold 8 time 56265 ms
Runner:167 - 	Fold 9 time 6246 ms
Runner:182 - ### 10-FOLDS RESULTS ###
Runner:183 - N, SuccessRate,Precision,Recall,k
Runner:185 - 1,8,692,0,087,0,010,1
Runner:185 - 2,11,168,0,080,0,019,1
Runner:185 - 3,13,364,0,076,0,023,1
Runner:185 - 4,14,720,0,071,0,026,1
Runner:185 - 5,15,421,0,067,0,028,1
Runner:185 - 6,16,075,0,065,0,031,1
Runner:185 - 7,16,542,0,062,0,033,1
Runner:185 - 8,17,056,0,060,0,035,1
Runner:185 - 9,17,523,0,058,0,037,1
Runner:185 - 10,17,944,0,056,0,039,1
Runner:185 - 11,18,224,0,055,0,040,1
Runner:185 - 12,18,505,0,053,0,041,1
Runner:185 - 13,18,692,0,052,0,043,1
Runner:185 - 14,18,832,0,051,0,044,1
Runner:185 - 15,18,972,0,049,0,044,1
Runner:185 - 16,19,019,0,048,0,045,1
Runner:185 - 17,19,065,0,047,0,046,1
Runner:185 - 18,19,393,0,046,0,047,1
Runner:185 - 19,19,486,0,045,0,048,1
Runner:185 - 20,19,626,0,045,0,049,1
```
The log output lists the execution time for each fold and the average metric (success rate, precision recall) scores.

Intermediate results for every fold (recommendations, groundtruth data, metamodel similarities, etc.) are stored in the `evaluation` folder of the corresponding dataset (e.g. `<dataset_folder>/evaluation/`).
## The `evaluation.properties` file
The evaluation of MemoRec is configured with a `.properties` file that specifies the dataset. For instance, the default `evaluation.properties` runs 10-fold cross-validation on the curated dataset supported by IE_S

```
# Dataset directory 
sourceDirectory=../../dataset/cls_attr_curated_RQ1/
```
