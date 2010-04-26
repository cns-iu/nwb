#!/bin/bash


if [ ! -a "/home/epic_website/epic_media/core" ]; then
	mkdir /home/epic_website/epic_media/core
fi
if [ ! -a "/home/epic_website/epic_media/projects" ]; then
	mkdir /home/epic_website/epic_media/projects
fi
if [ ! -a "/home/epic_website/epic_media/search" ]; then
	mkdir /home/epic_website/epic_media/search
fi
if [ ! -a "/home/epic_website/epic_media/tags" ]; then
	mkdir /home/epic_website/epic_media/tags
fi


for f in /home/epic_website/epic_code/epic/core/media/*
do
	mv "$f" /home/epic_website/epic_media/core/
done

for f in /home/epic_website/epic_code/epic/projects/media/*
do
	mv "$f" /home/epic_website/epic_media/projects/
done

for f in /home/epic_website/epic_code/epic/search/media/*
do
	mv "$f" /home/epic_website/epic_media/search/
done

for f in /home/epic_website/epic_code/epic/tags/media/*
do
	mv "$f" /home/epic_website/epic_media/tags/
done


