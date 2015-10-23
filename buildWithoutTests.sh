#!/bin/bash

mv .git/ .git_backup 2>/dev/null
sbt 'set test in assembly := {}' clean assembly
