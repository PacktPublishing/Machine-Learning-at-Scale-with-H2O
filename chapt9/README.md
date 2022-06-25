# Chapter 9: Production Scoring and the H2O MOJO

## BatchFileScorer

In this exercise showed how to embed the MOJO and its runtime in a Java program.  The MOJO used here was exported from the model built in Chapter 8.  (Recall the MOJO runtime is generic to all exported MOJOs).

### Contents
* ***BatchFileScorer.jar:*** jar compiled from our Java program in /src and the H2O MOJO runtime in /lib
* ***final_mojo.zip:*** MOJO scorer artifact exported from model building exercise in Chapter 8.
* ***transformed_data.csv:*** batch data set to score

### How to run
	java -jar BatchFileScorer.jar /path/to/batchdata /path=/to/model/exported/MOJO (optional) --shap
	
So the following will return batch predictions (using the directory structure here)

	java -jar BatchFileScorer.jar transformed_data.csv final_mojo.zip
	
and this will return predictions and Shapley values as described in the chapter 

	java -jar BatchFileScorer.jar transformed_data.csv final_mojo.zip --shap
	
Notes
* See _/src/BatchFileScorer.java_ for source code and Chapter 9 for explanation
* _/src/BatchFileScorer.java_ is developed as an exercise for this book and should not be confused with the enterprise-grade batch file scoring software that H2O.ai distributes(as discussed in Chapter 10).

## Inspect MOJO

In this exercise we leverage the the MOJO runtime capability of inspecting tree details of the MOJO exported from the model built in Chapter 8.

### Contents
* ***h2o-genmodel.jar:*** copy of same for BatchFileScorer (in /lib directory)
* ***final_mojo.zip:*** copy of same for BatchFileScorer

### Run
Run the following to get output in .png format for the first tree in the model

	java -cp h2o-genmodel.jar hex.genmodel.tools.PrintMojo  -i "final_mojo.zip" -o tree.png --format png --tree 0
	
Run the folowing to get the same in .json and .dot formats, respectively.  (The latter can be used to visualize using the open source Graphviz)

	java -cp h2o-genmodel.jar hex.genmodel.tools.PrintMojo  -i "final_mojo.zip" -o output/tree.json --format json --tree 0

	java -cp h2o-genmodel.jar hex.genmodel.tools.PrintMojo  -i "final_mojo.zip" -o output/tree.dot --format dot --tree 0
	
Remove the --tree flag to get output of all trees, or keep the flag and change the tree number to output specific trees