#!/usr/bin/env bash

# 1) Limpia y crea bin
rm -rf bin
mkdir -p bin

# 2) Compila todos los paquetes
javac -d bin \
  --module-path "/Library/Java/JavaVirtualMachines/javafx-sdk-23.0.1/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "lib/sqlite-jdbc-3.47.1.0.jar" \
  src/app/*.java \
  src/controller/*.java \
  src/db/*.java \
  src/model/*.java \
  src/util/*.java \
  src/service/*.java \
  src/dao/*.java \
  src/view/*.java

# 3) Ejecuta la aplicación
java \
  --module-path "/Library/Java/JavaVirtualMachines/javafx-sdk-23.0.1/lib" \
  --add-modules javafx.controls,javafx.fxml \
  -Djava.library.path="/Library/Java/JavaVirtualMachines/javafx-sdk-23.0.1/lib" \
  -cp "bin:lib/sqlite-jdbc-3.47.1.0.jar" \
  app.ClienteApp
