#!/bin/bash


if [ -a "/home/epic_website/epic_media/core" ]; then
	rm -rf /home/epic_website/epic_media/core
fi
if [ -a "/home/epic_website/epic_media/projects" ]; then
	rm -rf /home/epic_website/epic_media/projects
fi
if [ -a "/home/epic_website/epic_media/search" ]; then
	rm -rf /home/epic_website/epic_media/search
fi
if [ -a "/home/epic_website/epic_media/tags" ]; then
	rm -rf /home/epic_website/epic_media/tags
fi


mv /home/epic_website/epic_code/epic/core/media /home/epic_website/epic_media/core
mv /home/epic_website/epic_code/epic/projects/media /home/epic_website/epic_media/projects
mv /home/epic_website/epic_code/epic/search/media /home/epic_website/epic_media/search
mv /home/epic_website/epic_code/epic/tags/media /home/epic_website/epic_media/tags