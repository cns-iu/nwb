#!/bin/bash
svn export epic epic2
scp -r epic2 $EPIC_HOST:/home/epic_website/epic_code/epic-$SVN_REVISION-$BUILD_NUMBER

echo "cd /home/epic_website/epic_code" > remote.sh
echo 'if [ -a "epic" ]; then' >> remote.sh
echo "	CURRENT_LOC=`readlink -n epic`" >> remote.sh
echo "fi" >> remote.sh

echo "ln -s -f -n /home/epic_website/epic_code/epic-$SVN_REVISION-$BUILD_NUMBER /home/epic_website/epic_code/epic" >> remote.sh
echo "cd epic-$SVN_REVISION-$BUILD_NUMBER" >> remote.sh
echo "chmod +x media_move.sh" >> remote.sh
echo "./media_move.sh" >> remote.sh
echo "touch /home/epic_website/epic.wsgi" >> remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < wipeout.py" >> remote.sh
echo "python2.6 manage.py syncdb --noinput --settings=$EPIC_SETTINGS " >> remote.sh
echo "python2.6 manage.py loaddata demo_data --settings=$EPIC_SETTINGS " >> remote.sh


echo 'if [ -n "$CURRENT_LOC" ]; then' >> remote.sh
echo '	rm -rf $CURRENT_LOC' >> remote.sh
echo "fi" >> remote.sh
echo "rm -rf /home/epic_website/epic_data/*" >> remote.sh

scp remote.sh $EPIC_HOST:/tmp/remote.sh
ssh $EPIC_HOST "chmod +x /tmp/remote.sh"
ssh $EPIC_HOST "/tmp/remote.sh"