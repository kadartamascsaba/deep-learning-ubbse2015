import numpy as np                  # For vector operations

import theano                       # Deep learning library
import theano.tensor as T

class HiddenLayer(object):
    ''' Hidden layer class

            This class represents a hidden layer in the neural network.
            The layer uses the tanh activation function, because it usually yields to faster learning.

            Parameters:

                input    -  A vector wich can be either the input features or an another hidden layer's output

                n_in     -  An integer which represents the layer's input  length

                n_out    -  An integer which represents the layer's output length

                W        -  The layer's weight vector

                b        -  The layer's bias vector

                output   -  The layer's output vector after using the activation function

    '''

    def __init__(self, input_, n_in, n_out):
        
        # If @n_in is lesser than @n_out then we write out an error message and terminate the program
        # if n_in < n_out:
        #    print 'Error at LogRegLayer __init__: n_in < n_out!'
        #    exit(1)

        self.input  = input_
        self.n_in   = n_in
        self.n_out  = n_out

        self.W 	    = None
        self.b      = None
        self.output = None

        rng = np.random.RandomState()

        # Creating the weight vector
        W_values = np.asarray(
                	rng.uniform(
                    	low=-np.sqrt(6. / (n_in + n_out)),
                    	high=np.sqrt(6. / (n_in + n_out)),
                    	size=(n_in, n_out)
                	),
                	dtype=theano.config.floatX
            	   )
          
        # Creating the bias vector
        b_values = np.zeros(n_out, dtype=theano.config.floatX)
            

        self.W = theano.shared(value=W_values, name='W', borrow=True)
        self.b = theano.shared(value=b_values, name='b', borrow=True)

        # Creating the output vector using the tanh activation function
        self.output = T.tanh(T.dot(self.input, self.W) + self.b)  

    def setW(self, W, b):
        self.W = theano.shared(value=np.asarray(W, dtype=theano.config.floatX), name='W', borrow=True)
        self.b = theano.shared(value=np.asarray(b, dtype=theano.config.floatX), name='b', borrow=True)

        self.output = T.tanh(T.dot(self.input, self.W) + self.b)

    def get_W(self):
        return self.W