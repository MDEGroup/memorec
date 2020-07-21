# Memorec Datasets

This directory contains the meta-data of the four datasets used in the paper:

- [SH_L](./SH_L): 610 Java projects randomly selected and retrieved from GitHub via the [Software Heritage](https://www.softwareheritage.org/archive/)  archive
- [SH_S](./SH_S): the 200 smallest (in size) Java projects extracted from SH<sub>L</sub>
- [MV_L](./MV_L): 3,600 JAR files randomly selected and retrieved from the Maven Central repository
- [MV_S](./MV_S): 1,600 JAR files extracted from MV<sub>L</sub>: for every project, only one version is kepy
- [SH_L](./SH_L): 610 Java projects randomly selected and retrieved from GitHub via the [Software Heritage](https://www.softwareheritage.org/archive/)  archive
- [SH_S](./SH_S): the 200 smallest (in size) Java projects extracted from SH<sub>L</sub>
- [MV_L](./MV_L): 3,600 JAR files randomly selected and retrieved from the Maven Central repository
- [MV_S](./MV_S): 1,600 JAR files extracted from MV<sub>L</sub>: for every project, only one version is kepy

We also include the results obtained with PAM using the SH<sub>S</sub> dataset in the [PAM](./PAM) directory.

## Meta-data

Naturally, it is not possible to store in this repository the source code of every GitHub project we analyzed. Instead, the four datasets MV<sub>L</sub>, MV<sub>S</sub>, SH<sub>L</sub>, and SH<sub>S</sub> are organized as follows:

- Every dataset contains a file `List.txt` which points to the meta-data extracted from every project it contains (e.g. 200 projects in [SH_S/List.txt](./SH_S/List.txt))
