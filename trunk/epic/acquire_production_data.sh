#!/bin/bash

echo "cd /home/epic_website/epic_code/epic" > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < wipeout.py" >> remote.sh
echo "rsync -rvz --delete apache@epic:/home/epic_website/epic_data/ /home/epic_website/epic_data" >> remote.sh #NOTE: --delete should come up rarely at best . . . And  don't delete the trailing slash
echo "pg_dump -h cns-dbp -U epic_dumper --schema=public -Fc epic_web | pg_restore --no-acl --no-owner -h $DATABASE_HOST -U epic_appuser -d epic_web" >> remote.sh

scp remote.sh apache@$EPIC_HOST:/tmp/remote.sh
ssh apache@$EPIC_HOST "chmod +x /tmp/remote.sh"
ssh apache@$EPIC_HOST "/tmp/remote.sh"