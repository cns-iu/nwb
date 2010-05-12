#!/bin/bash

echo "cd /home/epic_website/epic_code/epic" > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < wipeout.py" >> remote.sh
echo "python2.6 manage.py syncdb --noinput --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py reset sessions admin sites contenttypes auth --noinput --settings=$EPIC_SETTINGS " >> remote.sh
echo "rsync -rvz epic:/home/epic_website/epic_data/ /home/epic_website/epic_data" >> remote.sh
echo "pg_dump -h cns-dbp -U epic_dumper --data-only --schema=public epic_web | psql -h $DATABASE_HOST -U epic_appuser epic_web" >> remote.sh

scp remote.sh $EPIC_HOST:/tmp/remote.sh
ssh $EPIC_HOST "chmod +x /tmp/remote.sh"
ssh $EPIC_HOST "/tmp/remote.sh"