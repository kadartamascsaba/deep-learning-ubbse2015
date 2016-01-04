#!/usr/bin/env python
from subprocess import Popen, PIPE, STDOUT
import sys

# you must pre-configure putty/pscp to use pub key auth
def remote_copy(server, user, src, dst, pass):
    p = Popen(
        'putty.exe -batch %(src)s %(user)s@%(server)s:%(dst)s -pw %(pass)s' % locals(), 
        shell=True, stdout=PIPE, stderr=STDOUT
    )
    all_output, null = p.communicate()
    return all_output

if __name__ == "__main__":
	remote_copy("triqla.ddns.net", "pi","foo.bar","/home/pi/foo.bar", sys.argv[1])
