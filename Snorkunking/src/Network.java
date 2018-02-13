import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class Network
{
	private class Neuron
	{
	    ArrayList<Neuron> inputs = new ArrayList<Neuron>();
	    ArrayList<Float> weights = new ArrayList<Float>();

	    float output;

		float sigmoid(float x)
		{
		    return (float) (2.0f / (1.0f + Math.exp(x))  -1.0f);
		}

	    float compute()
	    {
	        float sum = 0.0f;
	        for (int i = 0 ; i < inputs.size() ; i++)
	            sum += weights.get(i) * inputs.get(i).output;

	        output = sigmoid(sum);
	        //output = tanh(sum);

	        return output;
	    }
	}

	static int inputSize = 19;
	static int outputSize = 3;
	

	ArrayList<Neuron> inputLayer;
	ArrayList<Neuron> hiddenLayer = new ArrayList<Neuron>();
	ArrayList<Neuron> outputLayer = new ArrayList<Neuron>();
	
	
	Network(String _genome)
	{
		String[] split = _genome.split("\\s+");
		
		Hashtable<Integer, Neuron> neurons = new Hashtable<Integer, Neuron>(); 
		
	    int in, out;
	    float weight;
	    boolean enabled;

	    for (int i = 0; i < split.length; i+=5)
	    {	    	
	    	enabled = (split[i+4].equals("1"));
	    	if (!enabled)
	    		continue;
	    	
	    	in = Integer.parseInt(split[i]);
	    	out = Integer.parseInt(split[i+1]);
	    	weight = Float.parseFloat(split[i+2]);
	    	
	    	Neuron nIn = neurons.get(in);
	    	if (nIn == null)
	    	{
	    		neurons.put(in, new Neuron());
	    		nIn = neurons.get(in);
	    	}

	    	Neuron nOut = neurons.get(out);
	    	if (nOut == null)
	    	{
	    		neurons.put(out, new Neuron());
		    	nOut = neurons.get(out);
	    	}
	    	
	    	nOut.inputs.add(nIn);
	    	nOut.weights.add(weight);
	    }
	    
	    int hs = neurons.size()-inputSize-outputSize;
	    
	    inputLayer = new ArrayList<Neuron>(inputSize);
	    outputLayer = new ArrayList<Neuron>(outputSize);
	    hiddenLayer = new ArrayList<Neuron>(hs);
	    
	    for (int i = 0; i < inputSize; i++)
	    	inputLayer.add(null);

	    for (int i = 0; i < outputSize; i++)
	    	outputLayer.add(null);

	    for (int i = 0; i < hs; i++)
	    	hiddenLayer.add(null);
	    
	    
        Set<Integer> keys = neurons.keySet();
        for(Integer key: keys)
	    {
	    	if (key < inputSize)
	    		inputLayer.set(key, neurons.get(key));
	    		
	    	else if (key < inputSize + outputSize)
	    		outputLayer.set(key-inputSize, neurons.get(key));

	    	else
	    		hiddenLayer.set(--hs, neurons.get(key));
	    }
	}
	

	ArrayList<Float> evaluate(ArrayList<Float> _input)
	{
		ArrayList<Float> output = new ArrayList<Float>();
		
		for (int i = 0; i < outputLayer.size(); i++)
			output.add(-1.0f);

		
	    _input.add(1.0f); // bias

	    if (_input.size() != inputLayer.size())
	    	System.out.println("input size invalid: " + _input.size() + " instead of " + inputLayer.size());
	    

	    for (int i = 0; i < inputLayer.size(); i++)
	    {
	        if (inputLayer.get(i) != null)
	            inputLayer.get(i).output = _input.get(i);
	    }

	    for (Neuron n: hiddenLayer)
	        n.compute();

	    for (int i = 0 ; i < outputLayer.size() ; i++)
	    {
	        if (outputLayer.get(i) != null)
	            output.set(i, outputLayer.get(i).compute());
	    }


	    return output;
	}
}
