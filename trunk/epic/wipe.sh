#!/bin/bash
# Wipe out the database and epic_data/ folder for the targeted EPIC_HOST.

# Determine EPIC_SETTINGS by examining EPIC_HOST
if [ "$EPIC_HOST" == "cns-epic-dev" ]; then
	EPIC_SETTINGS=dev_settings
elif [ "$EPIC_HOST" == "cns-epic-stage" ]; then
	EPIC_SETTINGS=staging_settings
elif [ "$EPIC_HOST" == "epic" ]; then
	EPIC_SETTINGS=production_settings
else
	echo "Restore stopped: unexpected EPIC_HOST '$EPIC_HOST'."
	exit
fi

echo "cd /home/epic_website/epic_code/epic"                               > remote.sh
echo "export PYTHONPATH=/home/epic_website:/home/epic_website/epic_code" >> remote.sh
echo "echo Wiping database.."                                            >> remote.sh
echo "python2.6 manage.py shell --settings=$EPIC_SETTINGS < wipeout.py"  >> remote.sh
echo "python2.6 manage.py syncdb --noinput --settings=$EPIC_SETTINGS "   >> remote.sh
echo "python2.6 manage.py migrate --settings=$EPIC_SETTINGS "            >> remote.sh
echo "echo Done."                                                        >> remote.sh
echo "echo Wiping dataset files.."                                       >> remote.sh
echo "rm -rf /home/epic_website/epic_data/*"                             >> remote.sh
echo "echo Done."                                                        >> remote.sh

scp remote.sh apache@$EPIC_HOST:/tmp/remote.sh
ssh apache@$EPIC_HOST "chmod +x /tmp/remote.sh"
ssh apache@$EPIC_HOST "/tmp/remote.sh"
