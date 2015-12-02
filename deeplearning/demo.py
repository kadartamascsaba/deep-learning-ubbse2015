# This program is a demo for using deep learning algorithm for OCR digit recognition
# Based on the tutorial here: 	http://deeplearning.net/tutorial/mlp.html
# A stripped down version: 		https://gist.github.com/honnibal/6a9e5ef2921c0214eeeb
#
# This version of the demo was tested on Linux

import os
import sys
import time
from os import path


import gzip
import cPickle


import neuralnetwork as nn
import numpy
import theano

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



def main(dataset='mnist.pkl.gz'):
    train_examples, dev_examples, test_examples = load_data(dataset)
    print '... building the model'

    n = nn.Net(10)						# Creating a neural network with 10 classes as output(digits from 0 to 9)
    n.add_hidden_layer(28**2, 500)		# Adding a hidden layer which input length is 28**2 and output length is 500
    n.compile_model()					# Generating the network's training and evaluating model

    # We train the network 100 times
    # Each time we evaluate the results and write out the error percentage
    for epoch in range(1, 101):
        print '... training'

        for x, y in train_examples:
            n.train_model(x, y)
            
        # compute zero-one loss on validation set
        error = numpy.mean([n.evaluate_model(x, y) for x, y in dev_examples])
        print('epoch %i, validation error %f %%' % (epoch, error * 100))



if __name__ == '__main__':
    main()