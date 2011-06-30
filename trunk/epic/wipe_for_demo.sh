#!/bin/bash

echo "cd /home/epic_website/epic_code/epic" > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < wipeout.py" >> remote.sh
echo "python2.6 manage.py syncdb --all --noinput --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py migrate --fake --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py loaddata demo_data_2 --settings=$EPIC_SETTINGS " >> remote.sh
echo "echo 'y' | python2.6 manage.py rebuild_index --settings=$EPIC_SETTINGS " >> remote.sh
echo "chmod -R 777 /tmp/whoosh" >> remote.sh
echo "rm -rf /home/epic_website/epic_data/*" >> remote.sh

scp remote.sh $EPIC_HOST:/tmp/remote.sh
ssh $EPIC_HOST "chmod +x /tmp/remote.sh"
ssh $EPIC_HOST "/tmp/remote.sh"
