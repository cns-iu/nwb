#!/bin/bash


if [ ! -a /home/epic_website/epic_media/core ]; then
	mkdir /home/epic_website/epic_media/core
fi
if [ ! -a /home/epic_website/epic_media/projects ]; then
	mkdir /home/epic_website/epic_media/projects
fi
if [ ! -a /home/epic_website/epic_media/search ]; then
	mkdir /home/epic_website/epic_media/search
fi
if [ ! -a /home/epic_website/epic_media/tags ]; then
	mkdir /home/epic_website/epic_media/tags
fi


CORE="/home/epic_website/epic_code/epic/core/media/*"
for f in "$CORE"
do
	mv "$f" /home/epic_website/epic_media/core/
done

PROJECTS="/home/epic_website/epic_code/epic/projects/media/*"
for f in "$PROJECTS"
do
	mv "$f" /home/epic_website/epic_media/projects/
done

SEARCH="/home/epic_website/epic_code/epic/search/media/*"
for f in "$SEARCH"
do
	mv "$f" /home/epic_website/epic_media/search/
done

TAGS="/home/epic_website/epic_code/epic/tags/media/*"
for f in "$CORE"
do
	mv "$f" /home/epic_website/epic_media/tags/
done


