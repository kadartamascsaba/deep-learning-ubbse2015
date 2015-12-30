To whom it may concern:
	To integrate the evaluation with the neural network into the server do the following:

    0. make sure that you have already installed numpy, spicy and theano

	1. import the neuralnetwork.py file into your source

	2. create a neuralnetwork object

	3. use objects load(filename) method where filename contains the prepared weight matrix

	4. use neuralnetwork.evaluate(sound) to get whoever talks, where sound is a path to the .wav file


Example:

import neuralnetwork as nn

n = nn.Net()
n.load('matrix.txt')
print n.evaluate(sound)