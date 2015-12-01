import numpy						# For vector operations

import theano						# Deep learning library
import theano.tensor as T

import hiddenlayer as HL 			# HiddenLayer class import
import logreglayer as LL 			# LogRegLayer class import

class Net:
	
	''' Neural network class

			This class represents the whole structure of the neural network.

			Parameters:
				logreg_layer			-	The output layer, which gets its input from the last hidden layer
										   and determines a vector with probabilities of classes

				hidden_layers_params	-	List which contains (n_in, n_out) pairs, where 'n_in' represents the layer's input length
										   while 'n_out' represents the layer's output length

				hidden_layers 			-   List which contains the hidden layers of the network. Each element's type is HiddenLayer

				learning_rate			-	A float which determines the network's learning rate

				L1_reg					- 	A float which is used in L1 regulation

				L2_reg					- 	A float which is used in L2 regulation

				classes 				- 	The number of classes

				train_model				- 	A function which trains the network and updates its weight matrix based on cost

				evaluate_model 			-   A function which determine an input vector's class 

	'''
	
	# Constructor
	def __init__(self, classes, learning_rate=0.01, L1_reg=0.00, L2_reg=0.0001):
		
		self.logreg_layer			= None
		self.hidden_layers_params 	= []
		self.hidden_layers 			= []

		self.learning_rate			= learning_rate
		self.L1_reg					= L1_reg
		self.L2_reg					= L2_reg
		self.classes				= classes

		self.train_model			= None
		self.evaluate_model			= None
		self.cost					= None


	# Adding a new hidden layer with input length @n_in and output length @n_out
	# The layer's parameters will be saved, the layer will be instanced in 'compile_model' procedure
	def add_hidden_layer(self, n_in, n_out):
		self.hidden_layers_params.append((n_in, n_out))

	# A function which determines a layer's new weight vector using learning rate and the network's cost
	def sgd_step(self, param):
		return param - (self.learning_rate * T.grad(self.cost, param))

	# L1 regulation 
	def L1(self, w1, w2):
		return self.L1_reg * (abs(w1).sum() + abs(w2).sum())

	# L2 regulation
	def L2(self, w1, w2):
		return self.L2_reg * ((w1 ** 2).sum() + (w2 ** 2).sum())


	# Procedure for creating the training and evaluating model
	# It is like a compilation
	def compile_model(self):

		x = T.vector('x')  # Features
		y = T.iscalar('y') # (Gold) Label

		# Creating the first hidden layer with x symbolic vector
		n_in, n_out = self.hidden_layers_params.pop(0)
		self.hidden_layers.append(HL.HiddenLayer(x, n_in, n_out))

		# Creating the rest hidden layers
		# Each layers input is the previous layer's output
		for n_in, n_out in self.hidden_layers_params:
			self.hidden_layers.append(HL.HiddenLayer(self.hidden_layers[-1].output, n_in, n_out))

		# Creating the logistical regression layer
		self.logreg_layer = LL.LogRegLayer(self.hidden_layers[-1].output, self.hidden_layers[-1].n_out, self.classes)

		# Calculating the cost of the network
		# The cost is the negative log likelihood of gold label + L1 and L2 regressions
		self.cost = -T.log(self.logreg_layer.output)[0,y]
		for hidden in self.hidden_layers:
			self.cost += self.L1(self.logreg_layer.W, hidden.W)
			self.cost += self.L2(self.logreg_layer.W, hidden.W)

		# Creating the udate vector
		# Each layer's weight vector is changed based on the cost
		updates = [(self.logreg_layer.W, self.sgd_step(self.logreg_layer.W)), (self.logreg_layer.b, self.sgd_step(self.logreg_layer.b))]
		updates.extend([(hidden.W, self.sgd_step(hidden.W)) for hidden in self.hidden_layers])
		updates.extend([(hidden.b, self.sgd_step(hidden.b)) for hidden in self.hidden_layers])

		# Creating the training model which is a theano function
		# Inputs are a feature vector and a label
		self.train_model = theano.function(
        	inputs  = [x, y],
        	outputs = self.cost, # <-- Output depends on cost, which depends on P(y | x)
        	updates = updates,
    	)

		# Creating the evaluating model which is a theano function
		# Inputs are a feature vector and a label
		self.evaluate_model = theano.function(
        	inputs  = [x, y],
        	outputs = T.neq(y, T.argmax(self.logreg_layer.output[0]))
    	)

######################################################################################################

######################################################################################################

def load_data(dataset):
    ''' Loads the dataset
    :type dataset: string
    :param dataset: the path to the dataset (here MNIST)
    '''
    # Download the MNIST dataset if it is not present
    data_dir, data_file = os.path.split(dataset)
    if data_dir == "" and not os.path.isfile(dataset):
        # Check if dataset is in the data directory.
        data_dir = os.path.join(os.path.split(__file__)[0], "..", "data")
        if not path.exists(data_dir):
            print "No data directory to save data to. Try:"
            print "mkdir ../data"
            sys.exit(1)
        new_path = path.join(data_dir, data_file)
        if os.path.isfile(new_path) or data_file == 'mnist.pkl.gz':
            dataset = new_path

    if (not os.path.isfile(dataset)) and data_file == 'mnist.pkl.gz':
        import urllib
        url = 'http://www.iro.umontreal.ca/~lisa/deep/data/mnist/mnist.pkl.gz'
        print 'Downloading data from %s' % url
        urllib.urlretrieve(url, dataset)

    print '... loading data'

    # Load the dataset
    with gzip.open(dataset, 'rb') as f:
        train_set, valid_set, test_set = cPickle.load(f)
    return _make_array(train_set), _make_array(valid_set), _make_array(test_set)


def _make_array(xy):
    data_x, data_y = xy
    return zip(
        numpy.asarray(data_x, dtype=theano.config.floatX),
        numpy.asarray(data_y, dtype='int32'))


######################################################################################################

######################################################################################################

def main(dataset='mnist.pkl.gz'):
    train_examples, dev_examples, test_examples = load_data(dataset)
    print '... building the model'

    n    = Net(10)
    n.add_hidden_layer(28*28, 500)
    n.compile_model()
    print '... training'
    for epoch in range(1, 101):
        for x, y in train_examples:
            n.train_model(x, y)
        # compute zero-one loss on validation set
        error = numpy.mean([n.evaluate_model(x, y) for x, y in dev_examples])
        print('epoch %i, validation error %f %%' % (epoch, error * 100))


if __name__ == '__main__':
    main()