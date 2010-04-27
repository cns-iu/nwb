#!/bin/bash
svn export epic epic2
scp -r epic2 cns-epic-dev:/home/epic_website/epic_code/epic-$SVN_REVISION-$BUILD_NUMBER

echo 'if [ -a "/home/epic_website/epic_code/epic" ]; then' > remote.sh
echo "	CURRENT_LOC=`readlink -n /home/epic_website/epic_code/epic`" >> remote.sh
echo "fi" >> remote.sh

echo "ln -s -f -n /home/epic_website/epic_code/epic-$SVN_REVISION-$BUILD_NUMBER /home/epic_website/epic_code/epic" >> remote.sh
echo "chmod +x /home/epic_website/epic_code/epic/media_move.sh" >> remote.sh
echo "/home/epic_website/epic_code/epic/media_move.sh" >> remote.sh
echo "touch /home/epic_website/epic.wsgi" >> remote.sh
echo "python2.6 /home/epic_website/epic_code/epic/manage.py dbshell < /home/epic_website/epic_code/epic/wipeout.sql" >> remote.sh
echo "python2.6 /home/epic_website/epic_code/epic/manage.py syncdb --settings=epic.dev_settings --pythonpath=/home/epic_website/epic_code" >> remote.sh

echo 'if [ -n "$CURRENT_LOC" ]; then' >> remote.sh
echo '	rm -rf $CURRENT_LOC' >> remote.sh
echo "fi" >> remote.sh
echo "rm -rf /home/epic_website/epic_data/*" >> remote.sh

scp remote.sh cns-epic-dev:/tmp/remote.sh
ssh cns-epic-dev "chmod +x /tmp/remote.sh"
ssh cns-epic-dev "/tmp/remote.sh"