#!/bin/sh

TEST_START_TIME=$(date +%s)

java -jar ../NW2PNG-cli.jar \
--graaldir "~/Application Support/Graal" \
--tileset classic_collaboration/collaboration_tileset.png \
--input classic_collaboration/classic_collaboration.gmap \
--output output.png

TEST_END_TIME=$(date +%s)
TEST_TIME_TAKEN=$(($TEST_END_TIME - $TEST_START_TIME))

echo "====================================="
echo "Test completed."
echo "Time taken: $TEST_TIME_TAKEN seconds."
