import json

import numpy                        # For vector operations

import theano                       # Deep learning library
import theano.tensor as T

import hiddenlayer as HL            # HiddenLayer class import
import logreglayer as LL            # LogRegLayer class import

import spectrogram as sg

class Net:
    
    ''' Neural network class

            This class represents the whole structure of the neural network.

            Parameters:
                logreg_layer            -   The output layer, which gets its input from the last hidden layer
                                           and determines a vector with probabilities of classes

                hidden_layers_params    -   List which contains (n_in, n_out) pairs, where 'n_in' represents the layer's input length
                                           while 'n_out' represents the layer's output length

                hidden_layers           -   List which contains the hidden layers of the network. Each element's type is HiddenLayer

                learning_rate           -   A float which determines the network's learning rate

                L1_reg                  -   A float which is used in L1 regulation

                L2_reg                  -   A float which is used in L2 regulation

                classes                 -   The number of classes

                train_model             -   A function which trains the network and updates its weight matrix based on cost

                evaluate_model          -   A function which determine an input vector's class 

    '''
    
    # Constructor
    def __init__(self, classes=None, learning_rate=0.01, L1_reg=0.00, L2_reg=0.0001):
        
        self.logreg_layer           = None
        self.hidden_layers_params   = []
        self.hidden_layers          = []

        self.learning_rate          = learning_rate
        self.L1_reg                 = L1_reg
        self.L2_reg                 = L2_reg
        self.classes                = classes

        self.train_model            = None
        self.evaluate_model         = None
        self.cost                   = None


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
    def compile_model(self, weightMatrix=None):

        x = T.vector('x')  # Features
        y = T.iscalar('y') # (Gold) Label

        params = self.hidden_layers_params[:]
        # Creating the first hidden layer with x symbolic vector
        n_in, n_out = params.pop(0)
        self.hidden_layers.append(HL.HiddenLayer(x, n_in, n_out))

        if weightMatrix:
            self.hidden_layers[0].setW(weightMatrix[0][0], weightMatrix[0][1])
            weightMatrix.pop(0)

        # Creating the rest hidden layers
        # Each layers input is the previous layer's output
        for i in xrange(len(params)):
            n_in, n_out = params[i]
            self.hidden_layers.append(HL.HiddenLayer(self.hidden_layers[-1].output, n_in, n_out))
            if weightMatrix:
                self.hidden_layers[-1].setW(weightMatrix[i][0], weightMatrix[i][1])

        # Creating the logistical regression layer
        self.logreg_layer = LL.LogRegLayer(self.hidden_layers[-1].output, self.hidden_layers[-1].n_out, len(self.classes))
        if weightMatrix:
            self.logreg_layer.setW(weightMatrix[-1][0], weightMatrix[-1][1])

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
        self.devtest_model = theano.function(
            inputs  = [x, y],
            outputs = T.neq(y, T.argmax(self.logreg_layer.output[0]))
        )

        self.evaluate_model = theano.function( 
            inputs  = [x],
            outputs = T.argmax(self.logreg_layer.output[0])
        )


    # Saves the neural network to a file in json format
    def save(self, filename='matrix.txt'):
        output = dict()

        output['classes']       = self.classes
        output['learning_rate'] = self.learning_rate
        output['L1']            = self.L1_reg
        output['L2']            = self.L2_reg
        output['hidden_params'] = self.hidden_layers_params
        output['w_matrix']      = [(hidden.W.get_value().tolist(), hidden.b.get_value().tolist()) for hidden in self.hidden_layers]
        output['w_matrix'].append((self.logreg_layer.W.get_value().tolist(), self.logreg_layer.b.get_value().tolist()))

        with open(filename, 'w') as outfile:
            json.dump(output, outfile)

    # Fills the neural network with data loaded from json formatted data
    def load(self, filename='matrix.txt'):

        with open(filename, 'r') as infile:
            data = json.load(infile)

        self.classes                = data['classes']
        self.learning_rate          = data['learning_rate']
        self.L1_reg                 = data['L1']
        self.L2_reg                 = data['L2']
        self.hidden_layers_params   = data['hidden_params']

        self.compile_model(data['w_matrix'])


    def evaluate(self, sound):

        stgrm    = sg.generate_spectrogram(sound)
        elements = [self.evaluate_model(x).tolist() for x in stgrm]
        position = max(set(elements), key=elements.count)
        return self.classes[position]