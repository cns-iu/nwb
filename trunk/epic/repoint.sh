#!/bin/bash
svn export epic epic2
scp -r epic2 cns-epic-dev:/home/epic_website/epic_code/epic-$SVN_REVISION-$BUILD_NUMBER

echo "cd /home/epic_website/epic_code" > remote.sh
echo 'if [ -a "epic" ]; then' >> remote.sh
echo "	CURRENT_LOC=`readlink -n epic`" >> remote.sh
echo "fi" >> remote.sh

echo "ln -s -f -n /home/epic_website/epic_code/epic-$SVN_REVISION-$BUILD_NUMBER epic" >> remote.sh
echo "cd epic" >> remote.sh
echo "chmod +x media_move.sh" >> remote.sh
echo "./media_move.sh" >> remote.sh
echo "touch /home/epic_website/epic.wsgi" >> remote.sh
echo "export PYTHONPATH=/home/epic_website" >> remote.sh
echo "python2.6 manage.py shell --settings=dev_settings < wipeout.py" >> remote.sh
echo "python2.6 manage.py syncdb --noinput --settings=dev_settings " >> remote.sh
echo "python2.6 manage.py loaddata demo_data --settings=dev_settings " >> remote.sh


echo 'if [ -n "$CURRENT_LOC" ]; then' >> remote.sh
echo '	rm -rf $CURRENT_LOC' >> remote.sh
echo "fi" >> remote.sh
echo "rm -rf /home/epic_website/epic_data/*" >> remote.sh

scp remote.sh cns-epic-dev:/tmp/remote.sh
ssh cns-epic-dev "chmod +x /tmp/remote.sh"
ssh cns-epic-dev "/tmp/remote.sh"