javac -d .\.build Main.java
cd %~dp0.build
jar cvfm Main.jar manifest.mf *.class LeaderboardObject PlayerObject TrackObject
java -jar Main.jar
pause