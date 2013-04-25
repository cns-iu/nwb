#!/bin/bash

echo "cd /home/epic_website/epic_code/epic" > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "pwd" >> remote.sh
echo "ls -la" >> remote.sh
#echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < get_environment_info.py" >> remote.sh
echo "python2.6 < get_environment_info.py" >> remote.sh

scp remote.sh apache@$EPIC_HOST:/tmp/remote.sh
ssh apache@$EPIC_HOST "chmod +x /tmp/remote.sh"
ssh apache@$EPIC_HOST "/tmp/remote.sh"