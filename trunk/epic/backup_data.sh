time=$(date "+%F-%H-%M-%S")

# Dump production database to an archive file
pg_dump -h cns-dbp -U epic_dumper --schema=public -Fc epic_web > epic_database.$time.archive

# Dump production data to an archive directory
destination_path=epic_data_backup/epic_data_$time
mkdir -p $destination_path
rsync -avz apache@epic:/home/epic_website/epic_data/ $destination_path