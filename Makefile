JAVAW="/cygdrive/c/Program Files/Java/jre7/bin/javaw.exe"
#JAR="engine/target/engine-1.0-SNAPSHOT.jar"
JAR="engine/target/engine-1.0-SNAPSHOT-jar-with-dependencies.jar"
ARGS=cz.sio2.crowler.configurations.kub1x.KbxConfiguration file test_results
#cz.sio2.crowler.Runner

all:
	$(JAVAW) -jar $(JAR) $(ARGS)
