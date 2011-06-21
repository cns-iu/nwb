#!/bin/bash

echo "cd /home/epic_website/epic_code/epic" > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "python2.6 manage.py syncdb --noinput --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.core --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.categories --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.datarequests --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.projects --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.datasets --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.comments --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.djangoratings --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.tags --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.messages --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.geoloc --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate epic.search --settings=$EPIC_SETTINGS " >> remote.sh

scp remote.sh $EPIC_HOST:/tmp/remote.sh
ssh $EPIC_HOST "chmod +x /tmp/remote.sh"
ssh $EPIC_HOST "/tmp/remote.sh"