#!/bin/bash

mv .git/ .git_backup 2>/dev/null
sbt run
