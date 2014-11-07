package crf;

import java.util.HashMap;
public class CRFMain {

	static int labelSize=0;
	public static void main(String[]args){
		//Use a crfReader to read in all the passage\ labels and the templates
		CRFReader crfReader = new CRFReader("data/train.utf8","data/labels.utf8","data/template.utf8");
		//Use a overall hashMap to map from the content of a certain pattern
		//to an array representing the scores of 'S,B,I,E'
		HashMap<String,int[]>hashMap =new HashMap<String,int[]>();
		labelSize = crfReader.getLabelLength();
		CRFTraining crfTrain = new CRFTraining(labelSize);
		//Initialize the hashMap from the given reader
		crfTrain.init(crfReader,hashMap);
		//Specify the percentage to stop
//		for(int i=0;i<8;i++){
//			double st = 0.999;
//			st+=(double)i/1000;
//			
//		}
		
		
		//Read in the test set to predict the desired char Array
		crfTrain.learn(crfReader, hashMap,0.9996);
		crfReader = new CRFReader("data/test.utf8","data/labels.utf8","data/template.utf8");
		crfTrain.test(crfReader, hashMap);
	}
}
