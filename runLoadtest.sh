#!/bin/sh

# output calling 'jmeter --version'
#     _    ____   _    ____ _   _ _____       _ __  __ _____ _____ _____ ____
#    / \  |  _ \ / \  / ___| | | | ____|     | |  \/  | ____|_   _| ____|  _ \
#   / _ \ | |_) / _ \| |   | |_| |  _|    _  | | |\/| |  _|   | | |  _| | |_) |
#  / ___ \|  __/ ___ \ |___|  _  | |___  | |_| | |  | | |___  | | | |___|  _ <
# /_/   \_\_| /_/   \_\____|_| |_|_____|  \___/|_|  |_|_____| |_| |_____|_| \_\ 5.6.2
#
# Copyright (c) 1999-2023 The Apache Software Foundation
JMETER_HOME='<path/to/jmeter>'

$JMETER_HOME/bin/jmeter -n -t ./src/loadTest/loadTest.jmx -l ./target/result -e -o ./target/loadtest