#!/usr/bin/env python
from subprocess import Popen, PIPE, STDOUT
import sys
import os

# you must pre-configure putty/pscp to use pub key auth
def remote_copy(server, user, src, dst, passw):
	path = os.getcwd()
    print path
    p = Popen(
        '%(path)spscp -pw %(passw)s %(src)s %(user)s@%(server)s:%(dst)s' % locals(), 
        shell=True, stdout=PIPE, stderr=STDOUT
    )
    all_output, null = p.communicate()
    return all_output

if __name__ == "__main__":
	print remote_copy("triqla.ddns.net", "pi","foo.bar","/home/pi/foo", str(sys.argv[1]))
