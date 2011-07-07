#!/bin/bash

echo "cd /home/epic_website/epic_code/epic" > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "python2.6 manage.py syncdb --noinput --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 2 epic.categories --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.core --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.datarequests --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.projects --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.datasets --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.comments --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.djangoratings --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.tags --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.messages --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.geoloc --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate -v 0 epic.search --settings=$EPIC_SETTINGS " >> remote.sh

scp remote.sh $EPIC_HOST:/tmp/remote.sh
ssh $EPIC_HOST "chmod +x /tmp/remote.sh"
ssh $EPIC_HOST "/tmp/remote.sh"