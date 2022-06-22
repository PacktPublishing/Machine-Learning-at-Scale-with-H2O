import java.io.*;

import hex.genmodel.easy.RowData;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.prediction.*;
import hex.genmodel.MojoModel;

public class H2OBatchFileScorer {
	
	
	public static void main(String[] args) throws Exception{
		
		// get input parameters
	    File fileToScore = new File(args[0]);
	    String pathToMojo = args[1];
	    boolean doShapley = args.length == 3 
	    		&& args[2].equals("--shap");

	   	
		// Load the mojo (only once) and configure
	    EasyPredictModelWrapper.Config config = 
	    		new EasyPredictModelWrapper.Config();

	    config.setModel(MojoModel.load(pathToMojo));
	    if (doShapley) config.setEnableContributions(true);

	    EasyPredictModelWrapper model = new EasyPredictModelWrapper(config);
	    
	    // get each record from the file
	    BufferedReader br = new BufferedReader(new FileReader(fileToScore));
	    
		// we are skipping the first line (header line)
		br.readLine();
		String record = null;
		while ((record = br.readLine()) != null) {
			 			
		    // Convert input record to type required by mojo api
		    RowData mojoRow = convertInput(record);
		    
		    // make the prediction (p holds the model prediction, plus lots of metadata around it)
		    BinomialModelPrediction p = model.predictBinomial(mojoRow);
		    
		    // get the results you want from p and format it to your needs (in this case, csv to write to file)
		    String outputString = formatOutput(model, record, p, doShapley);
		    
		    // can write to file but printing to screen for ease of code explanation
		    System.out.println(outputString);
			
		}
	}  // Note: the main method ends here
	
    private static RowData convertInput(String record) {
    	
	    String[] featureValues = record.split(",");
	    
	    RowData row = new RowData();
	    row.put("purpose_te", featureValues[0]);
	    row.put("addr_state_te", featureValues[1]);
	    row.put("loan_amnt", featureValues[2]);
	    row.put("term", featureValues[3]);
	    row.put("installment", featureValues[4]);
	    row.put("grade", featureValues[5]);
	    row.put("emp_length", featureValues[6]);
	    row.put("annual_inc", featureValues[7]);
	    row.put("verification_status", featureValues[8]);
	    row.put("dti", featureValues[9]);
	    row.put("delinq_2yrs", featureValues[10]);
	    row.put("inq_last_6mths", featureValues[11]);
	    row.put("mths_since_last_delinq", featureValues[12]);
	    row.put("open_acc", featureValues[13]);
	    row.put("pub_rec", featureValues[14]);
	    row.put("revol_bal", featureValues[15]);
	    row.put("revol_util", featureValues[16]);
	    row.put("total_acc", featureValues[17]);
	    row.put("credit_length", featureValues[18]);
	    row.put("issue_d_year", featureValues[19]);
	    row.put("issue_d_month", featureValues[20]);
	    row.put("issue_d_dayOfWeek", featureValues[21]);
	    row.put("issue_d_weekend", featureValues[22]);
	    row.put("log_inc_per_acct", featureValues[23]);
	    row.put("home_3cat", featureValues[24]);
	    row.put("emp_length_missing", featureValues[25]);
	    
	    return row;
    }
    
    private static String formatOutput(EasyPredictModelWrapper model, String record, BinomialModelPrediction p, boolean doShapley) {
    	// start the ouput string with the record being scored
    	String outputString = record;
    	
    	// add prediction to output string
    	outputString += "   PREDICTION (good=0, bad=1): " + p.label + " " + p.classProbabilities[0];
    	
    	// add Shapley values (bar-delimited) to output string
    	if(doShapley) {
    		outputString += "  SHAP VALUES > 0.01: ";
    		
    		for (int i=0; i < p.contributions.length; i++) {
    			
    			// retrieving only Shap values over 0.01 (you can get all, or sort and return top 5 etc)
    			if (p.contributions[i] <  0.01) continue;
    			outputString += model.getContributionNames()[i] + ": " + p.contributions[i] + "|" ;
    		}
    	}
    	return outputString;	
    }

}


