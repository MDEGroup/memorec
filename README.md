# memorec

This repository contains the source code implementation of Memorec and the datasets used to replicate the experimental results of our paper submitted to the MDE Intelligence 2020:

_A Recommender System based on 
Collaborative-filtering Techniques to Support the Specification of Metamodels_

Authors: Juri Di Rocco, Davide Di Ruscio, Claudio Di Sipio, Phuong T. Nguyen, Alfonso Pierantonio



## Introduction

**MMRec** is a novel approach that makes use of a collaborative filtering strategy to recommend valuable entities related to the model under construction. Our approach can provide suggestions related to both meta-classes and structured features. 



## Repository Structure

This repository is organized as follows:

* The [tools](./tools) directory contains the implementation of the different tools we developed:
	* [memorec](./tools/it.univaq.disim.memorec): The Java implementation of **memorec**
	* [dataextractor](it.univaq.disim.memorec.dataextractor): A set of tools that are used to transform metamodels into memorec-processable 
	* [demo metamodel](it.univaq.disim.memorec.demometamodel): the modeling project of the motivation example Web metamodel.
* The [dataset](./dataset) directory contains the datasets described in the paper that we use to evaluate **memorec**:
	* [METAMODELS_CURATED](./dataset/METAMODELS_CURATED): 555 metamodels extracted from the [curated dataset](https://zenodo.org/record/2585456#.XxbpuvgzbSI)
	* [METAMODELS_RAW](./dataset/METAMODELS_CURATED): 2151 metamodels mined from GitHub
	* [CLS ATTR RAW RQ1](./dataset/cls_attr_raw_RQ1)
	* [CLS ATTR RAW RQ2](./dataset/cls_attr_raw_RQ2)
	* [CLS ATTR CURATED RQ1](./dataset/cls_attr_curated_RQ1)
	* [CLS ATTR RAW RQ2](./dataset/cls_attr_curated_RQ2)
	* [PKG CLS RAW RQ1](./dataset/pkg_cls_raw_RQ1)
	* [PKG CLS RAW RQ2](./dataset/pkg_cls_raw_RQ2)
	* [PKG CLS CURATED RQ1](./dataset/pkg_cls_curated_RQ1)
	* [PKG CLS RAW RQ2](./dataset/pkg_cls_curated_RQ2)

